package ir.maktab.project_final_faz2.service.serviceImpl;

import com.google.common.collect.Lists;
import ir.maktab.project_final_faz2.config.MessageSourceConfiguration;
import ir.maktab.project_final_faz2.data.model.dto.request.OfferRegistryDto;
import ir.maktab.project_final_faz2.data.model.entity.*;
import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import ir.maktab.project_final_faz2.data.model.enums.SpecialtyStatus;
import ir.maktab.project_final_faz2.data.model.repository.OfferRepository;
import ir.maktab.project_final_faz2.exception.*;
import ir.maktab.project_final_faz2.mapper.MapperOffer;
import ir.maktab.project_final_faz2.service.serviceInterface.OfferService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final ExpertServiceImpl expertService;
    private final OrderCustomerServiceImpl orderCustomerService;
    private final MessageSourceConfiguration messageSource;

    @Override
    public List<SubJob> findAllSubJubExpert(Expert expert) {
        Expert expertDb = expertService.findById(expert.getId());
        if (expertDb.getServicesList().isEmpty())
            throw new NotFoundException(String.format("No subJob for this Expert %s", expertDb.getEmail()));
        return expertDb.getServicesList();
    }

    @Override
    public List<OrderCustomer> findAllOrdersForAnSubJobOfExpert(Expert expert, SubJob subJob) {
        Expert expertDb = expertService.findByUserName(expert.getEmail());
        if (!expertDb.getSpecialtyStatus().equals(SpecialtyStatus.Confirmed))
            throw new ValidationException(messageSource.getMessage("the desired Expert isNot confirm "));
        if (expertDb.getServicesList().stream().noneMatch(subJob1 -> subJob1.getSubJobName().equals(subJob.getSubJobName())))
            throw new NotFoundException(messageSource.getMessage("errors.message.notFound-subJob"));
        List<OrderCustomer> list = orderCustomerService.findAllOrdersBySubJob(subJob);
        if (list.isEmpty())
            throw new NotFoundException(messageSource.getMessage("errors.message.notFound_order"));
        return list;
    }

    @Override
    @Transactional
    public Offers save(OfferRegistryDto offerRegistryDto) {
        Offers offers = MapperOffer.INSTANCE.offerDtoToOffer(offerRegistryDto.getOffersDto());
        Expert expert = expertService.findByUserName(offerRegistryDto.getUserName());
        OrderCustomer orderCustomer = orderCustomerService.findById(offerRegistryDto.getId());
        offers.setExpert(expert);
        offers.setOrderCustomer(orderCustomer);
        if (Objects.isNull(offers))
            throw new NullObjects(messageSource.getMessage("errors.message.null-object"));
        if (!offers.getExpert().getSpecialtyStatus().equals(SpecialtyStatus.Confirmed))
            throw new NotAcceptedException(messageSource.getMessage("errors.message.isn't_confirm"));
        if (offers.getExpert().getServicesList().stream().noneMatch(subJob -> subJob.getSubJobName().equals(orderCustomer.getSubJob().getSubJobName())))
            throw new NotFoundException(messageSource.getMessage("errors.message.notFound-subJob"));
        if (offers.getOfferPriceByExpert().compareTo(orderCustomer.getSubJob().getPrice()) < 0)
            throw new ValidationException(messageSource.getMessage("errors.message.low_price"));
        if (offers.getStartTime().isBefore(LocalDateTime.now()))
            throw new TimeOutException(messageSource.getMessage("errors.message.isBefore_date_now"));
        orderCustomer.setOrderStatus(OrderStatus.WaitingSelectTheExpert);
        orderCustomerService.updateOrder(orderCustomer);
        offers.setAccept(false);
        offers.setOrderCustomer(orderCustomer);
        return offerRepository.save(offers);
    }

    @Override
    public List<Offers> viewAllOffersOrderByPriceAsc(Long id) {
        OrderCustomer orderCustomer = orderCustomerService.findById(id);
        List<Offers> allOffersAOrder = offerRepository.findAllByOrderCustomerOrderByPriceOrder(orderCustomer.getId());
        if (allOffersAOrder.isEmpty())
            throw new NotFoundException(messageSource.getMessage("errors.message.list_isEmpty"));
        return allOffersAOrder;
    }

    @Override
    public List<Offers> viewAllOffersOrderByPriceDesc(Long id) {
        List<Offers> offersAsc = viewAllOffersOrderByPriceAsc(id);
        return Lists.reverse(offersAsc);
    }

    @Override
    public List<Offers> viewAllOrdersOrderByScoreExpertAsc(Long id) {
        List<Offers> allOffersAOrder = viewAllOffersOrderByPriceAsc(id);
        List<Offers> orderByScore = allOffersAOrder.stream().sorted(Comparator.comparing(offers -> offers.getExpert().getPerformance())).toList();
        if (orderByScore.isEmpty())
            throw new NotFoundException(messageSource.getMessage("errors.message.notFound_offer_for_order"));
        return orderByScore;
    }

    @Override
    public List<Offers> viewAllOrdersOrderByScoreExpertDesc(Long id) {
        List<Offers> offersAsc = viewAllOrdersOrderByScoreExpertAsc(id);
        return Lists.reverse(offersAsc);
    }

    private OrderCustomer getOrderCustomerById(Long id) {
        return orderCustomerService.findById(id);
    }

    @Override
    @Transactional
    public Offers selectAnOfferByCustomer(Long offersId, Long orderCustomerId) {
        OrderCustomer orderCustomerDb = getOrderCustomerById(orderCustomerId);
        Offers offersDb = findById(offersId);
        if (!Objects.equals(offersDb.getOrderCustomer().getId(), orderCustomerDb.getId()))
            throw new NotAcceptedException(messageSource.getMessage("errors.message.is_not_exist_offer_for_order"));
        if (!(orderCustomerDb.getOrderStatus().equals(OrderStatus.WaitingForOfferTheExperts) || orderCustomerDb.getOrderStatus().equals(OrderStatus.WaitingSelectTheExpert)))
            throw new NotAcceptedException(messageSource.getMessage("errors.message.exist_accept_offer_order"));
        orderCustomerDb.setOrderStatus(OrderStatus.WaitingForTheExpertToComeToYourLocation);
        orderCustomerService.updateOrder(orderCustomerDb);
        offersDb.setAccept(true);
        offersDb.getExpert().setNumberOrderDone(offersDb.getExpert().getNumberOrderDone()+1);
        expertService.updateExpert(offersDb.getExpert());
        return offerRepository.save(offersDb);
    }

    @Override
    public OrderCustomer changeOrderToStartByCustomer(Long offersId, Long orderCustomerId) {
        OrderCustomer orderCustomerDb = getOrderCustomerById(orderCustomerId);
        Offers offers=findById(offersId);
        if (!orderCustomerDb.getOrderStatus().equals(OrderStatus.WaitingForTheExpertToComeToYourLocation))
            throw new ValidationException(messageSource.getMessage("errors.message.state_order_must_waiting_come_expert"));
        Offers offersDb = findById(offers.getId());
        if (offersDb.getStartTime().isBefore(LocalDateTime.now()))
            throw new TimeOutException(messageSource.getMessage("errors.message.isBefore_date_now"));
        if (!offers.isAccept())
            throw new NotAcceptedException(messageSource.getMessage("errors.message.offer_isn't_accept"));
        orderCustomerDb.setOrderStatus(OrderStatus.Started);
        return orderCustomerService.updateOrder(orderCustomerDb);
    }

    @Override
    public void endDoWork(Long orderCustomerId) {
        OrderCustomer orderCustomer=orderCustomerService.findById(orderCustomerId);
        if (!orderCustomer.getOrderStatus().equals(OrderStatus.Started))
            throw new ValidationException(messageSource.getMessage("errors.message.order_state_start"));
        OrderCustomer orderCustomerDb = getOrderCustomerById(orderCustomer.getId());
        if(LocalDateTime.now().isBefore(orderCustomerDb.getStartDateDoWork()))
             throw new TimeOutException("date end before start time and order and offer");
        orderCustomerDb.setEndDateDoWork(LocalDateTime.now());
        orderCustomerDb.setOrderStatus(OrderStatus.Done);
        orderCustomerService.updateOrder(orderCustomerDb);
        subtractOfScore(orderCustomerDb);
    }

    @Override
    public Offers findById(Long id) {
        return offerRepository.findById(id).orElseThrow(() -> new NotFoundException((String.format("Not Fount Offer %d", id))));
    }

    @Override
    public Offers findOffersIsAccept(OrderCustomer orderCustomer) {
        return offerRepository.findOffersIsAccept(orderCustomer.getId()).orElseThrow(() -> new NotAcceptedException(String.format("No offers isAccept for OrderCustomer %s", orderCustomer.getId())));
    }

    public void updateOffer(Offers offers) {
        offerRepository.save(offers);
    }

    @Override
    public void subtractOfScore(OrderCustomer orderCustomer) {
        OrderCustomer orderCustomerDb = orderCustomerService.findById(orderCustomer.getId());
        if (!orderCustomerDb.getOrderStatus().equals(OrderStatus.Done))
            throw new TimeOutException(messageSource.getMessage("errors.message.order_isn't_done"));
        Offers offers = findOffersIsAccept(orderCustomerDb);
        LocalDateTime timeEndExpert = offers.getStartTime().plus(offers.getDurationWork());
        if (orderCustomerDb.getEndDateDoWork().isAfter(timeEndExpert)) {
            int diffHours = (int) Duration.between(orderCustomerDb.getEndDateDoWork(), offers.getStartTime()).plus(offers.getDurationWork()).toHours();
            offers.getExpert().setPerformance((offers.getExpert().getPerformance() - Math.abs(diffHours)));
        }
        if (offers.getExpert().getPerformance() < 0)
            disableExpert(offers.getExpert());
        updateOffer(offers);
    }

    private void disableExpert(Expert expert) {
        if (expert.getPerformance() >= 0)
            throw new ValidationException(messageSource.getMessage("error.message.active_expert"));
        expert.setSpecialtyStatus(SpecialtyStatus.WaitingForConfirm);
        expertService.updateExpert(expert);
    }
    @Override
    public List<OrderCustomer> findAllOrderDoneExpert(Expert expert){
        List<Offers> list=offerRepository.findOfferIsAcceptAExpert(expert);
        if(list.isEmpty())
            throw new NotFoundException(messageSource.getMessage("errors.message.list_isEmpty"));
        return list.stream().map(Offers::getOrderCustomer).collect(Collectors.toList());
    }
    @Override
    public List<Offers> findAllOffersIsAcceptedAExpert(Expert expert){
        if(offerRepository.findOfferIsAcceptAExpert(expert).isEmpty())
            throw new NotFoundException(messageSource.getMessage("errors.message.list_isEmpty"));
        return offerRepository.findOfferIsAcceptAExpert(expert);

    }
    @Override
    public List<Offers> findAllOffersIsAcceptedACustomer(Customer customer){
        if(offerRepository.findAllOffersIsAcceptedCustomer(customer).isEmpty())
            throw new NotFoundException(messageSource.getMessage("errors.message.list_isEmpty"));
        return offerRepository.findAllOffersIsAcceptedCustomer(customer);

    }

}
