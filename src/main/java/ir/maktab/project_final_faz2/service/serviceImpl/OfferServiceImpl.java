package ir.maktab.project_final_faz2.service.serviceImpl;

import com.google.common.collect.Lists;
import ir.maktab.project_final_faz2.config.MessageSourceConfiguration;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.Offers;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import ir.maktab.project_final_faz2.data.model.enums.SpecialtyStatus;
import ir.maktab.project_final_faz2.data.model.repository.OfferRepository;
import ir.maktab.project_final_faz2.exception.*;
import ir.maktab.project_final_faz2.service.serviceInterface.OfferService;
import ir.maktab.project_final_faz2.util.util.UtilDate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final ExpertServiceImpl expertService;
    private final OrderCustomerServiceImpl orderCustomerService;
    @Autowired
    MessageSourceConfiguration messageSource;

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
    public Offers save(Offers offers, Long id) {
        if (Objects.isNull(offers))
            throw new NullObjects(messageSource.getMessage("errors.message.null-object"));
        if (!offers.getExpert().getSpecialtyStatus().equals(SpecialtyStatus.Confirmed))
            throw new NotAcceptedException(messageSource.getMessage("errors.message.isn't_confirm"));
        Date today = UtilDate.changeLocalDateToDate(LocalDate.now());
        OrderCustomer orderCustomer = orderCustomerService.findById(id);
        if (offers.getExpert().getServicesList().stream().noneMatch(subJob -> subJob.getSubJobName().equals(orderCustomer.getSubJob().getSubJobName())))
            throw new NotFoundException(messageSource.getMessage("errors.message.notFound-subJob"));
        if (offers.getOfferPriceByExpert().compareTo(orderCustomer.getSubJob().getPrice()) < 0)
            throw new ValidationException(messageSource.getMessage("errors.message.low_price"));
        if (UtilDate.compareTwoDate(offers.getStartTime(), today) < 0)
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
    public Offers selectAnOfferByCustomer(Offers offers, OrderCustomer orderCustomer) {
        OrderCustomer orderCustomerDb = getOrderCustomerById(orderCustomer.getId());
        Offers offersDb = findById(offers.getId());
        if (!Objects.equals(offersDb.getOrderCustomer().getId(), orderCustomerDb.getId()))
            throw new NotAcceptedException(messageSource.getMessage("errors.message.is_not_exist_offer_for_order"));
        if (!(orderCustomerDb.getOrderStatus().equals(OrderStatus.WaitingForOfferTheExperts) || orderCustomerDb.getOrderStatus().equals(OrderStatus.WaitingSelectTheExpert)))
            throw new NotAcceptedException(messageSource.getMessage("errors.message.exist_accept_offer_order"));
        orderCustomerDb.setOrderStatus(OrderStatus.WaitingForTheExpertToComeToYourLocation);
        orderCustomerService.updateOrder(orderCustomerDb);
        offers.setAccept(true);
        return offerRepository.save(offers);
    }

    @Override
    public OrderCustomer changeOrderToStartByCustomer(Offers offers, OrderCustomer orderCustomer) {
        OrderCustomer orderCustomerDb = getOrderCustomerById(orderCustomer.getId());
        if (!orderCustomerDb.getOrderStatus().equals(OrderStatus.WaitingForTheExpertToComeToYourLocation))
            throw new ValidationException(messageSource.getMessage("errors.message.state_order_must_waiting_come_expert"));
        Offers offersDb = findById(offers.getId());
        Date today = UtilDate.changeLocalDateToDate(LocalDate.now());
        if (UtilDate.compareTwoDate(offersDb.getStartTime(), today) < 0)
            throw new TimeOutException(messageSource.getMessage("errors.message.isBefore_date_now"));
        if (!offers.isAccept())
            throw new NotAcceptedException(messageSource.getMessage("errors.message.offer_isn't_accept"));
        orderCustomerDb.setOrderStatus(OrderStatus.Started);
        return orderCustomerService.updateOrder(orderCustomerDb);
    }

    @Override
    public void endDoWork(OrderCustomer orderCustomer, LocalDateTime endDoWork) {
        if (!orderCustomer.getOrderStatus().equals(OrderStatus.Started))
            throw new ValidationException(messageSource.getMessage("errors.message.order_state_start"));
        OrderCustomer orderCustomerDb = getOrderCustomerById(orderCustomer.getId());
        orderCustomerDb.setEndDateDoWork(UtilDate.changeLocalDateToDate(LocalDate.from(endDoWork)));
        orderCustomerDb.setOrderStatus(OrderStatus.DoItsBeen);
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
        if (!orderCustomerDb.getOrderStatus().equals(OrderStatus.DoItsBeen))
            throw new TimeOutException(messageSource.getMessage("errors.message.order_isn't_done"));
        Offers offers = findOffersIsAccept(orderCustomerDb);
        LocalDateTime timeEndExpert = UtilDate.getLocalDateTime(offers.getStartTime()).plus(offers.getDurationWork());
        if (UtilDate.getLocalDateTime(orderCustomerDb.getEndDateDoWork()).compareTo(timeEndExpert) > 0) {
            int diffHours = (int) Duration.between(UtilDate.getLocalDateTime(orderCustomerDb.getEndDateDoWork()), UtilDate.getLocalDateTime(offers.getStartTime()).plus(offers.getDurationWork())).toHours();
            offers.getExpert().setPerformance((offers.getExpert().getPerformance() - Math.abs(diffHours)));
        }
        if (offers.getExpert().getPerformance() < 0)
            disableExpert(offers.getExpert());
        updateOffer(offers);
    }

    private void disableExpert(Expert expert) {
        if (expert.getPerformance() >= 0)
            throw new ValidationException(messageSource.getMessage("error.message.active_expert"));
        expert.setSpecialtyStatus(SpecialtyStatus.NewState);
        expertService.updateExpert(expert);
    }

}
