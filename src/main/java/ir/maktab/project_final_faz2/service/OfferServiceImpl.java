package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.Offers;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import ir.maktab.project_final_faz2.data.model.enums.SpecialtyStatus;
import ir.maktab.project_final_faz2.data.model.repository.OfferRepository;
import ir.maktab.project_final_faz2.exception.NotAccept;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.TimeOutException;
import ir.maktab.project_final_faz2.exception.ValidationException;
import ir.maktab.project_final_faz2.service.interfaces.OfferService;
import ir.maktab.project_final_faz2.util.util.UtilDate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {
    private final OfferRepository offerRepository;
    private final ExpertServiceImpl expertService;
    private final OrderCustomerServiceImpl orderCustomerService;

    @Override
    public List<OrderCustomer> findAllOrdersForAExpert(Expert expert) {
        Expert expertDb = expertService.findByUserName(expert.getEmail());
        if (!expertDb.getSpecialtyStatus().equals(SpecialtyStatus.Confirmed))
            throw new ValidationException(String.format("Expert %s is not Confirm !!! ", expertDb.getEmail()));
        List<OrderCustomer> list = new ArrayList<>();
        for (SubJob subJob : expertDb.getServicesList()) {
            List<OrderCustomer> listSub = orderCustomerService.findAllOrdersBySubJob(subJob);
            listSub.parallelStream().collect(Collectors.toCollection(() -> list));
        }
        if (list.isEmpty())
            throw new NotFoundException(String.format("No Order for this Expert %s", expertDb.getEmail()));
        return list;
    }

    @Override
    public List<OrderCustomer> findAllOrdersForAnSubJobOfExpert(Expert expert, SubJob subJob) {
        Expert expertDb = expertService.findByUserName(expert.getEmail());
        if (!expertDb.getSpecialtyStatus().equals(SpecialtyStatus.Confirmed))
            throw new ValidationException(String.format("Expert %s is not Confirm !!! ", expertDb.getEmail()));
        if (expertDb.getServicesList().stream().noneMatch(subJob1 -> subJob1.getId().equals(subJob.getId())))
            throw new NotFoundException(String.format("this %s is Not Exist", subJob.getSubJobName()));
        List<OrderCustomer> list = orderCustomerService.findAllOrdersBySubJob(subJob);
        if (list.isEmpty())
            throw new NotFoundException(String.format("No Order for this Expert %s", expertDb.getEmail()));
        return list;

    }

    @Override
    @Transactional
    public Offers save(Offers offers, String codeOrder) {
        Date today = UtilDate.changeLocalDateToDate(LocalDate.now());
        OrderCustomer orderCustomer = orderCustomerService.findByCode(codeOrder);
        if (offers.getOfferPriceByExpert().compareTo(orderCustomer.getSubJob().getPrice()) < 0)
            throw new ValidationException(String.format("The offer price for this sub-service %s is lower than the original price",orderCustomer.getSubJob().getSubJobName()));
        if (UtilDate.compareTwoDate(offers.getStartTime(), today) < 0)
            throw new TimeOutException("The current date is less than the proposed date");
        orderCustomer.setOrderStatus(OrderStatus.WaitingSelectTheExpert);
        orderCustomerService.updateOrder(orderCustomer);
        offers.setAccept(false);
        offers.setOrderCustomer(orderCustomer);
        return offerRepository.save(offers);
    }

    @Override
    public List<Offers> viewAllOffersOrdersByCustomerOrderByPrice(String orderCode) {
        OrderCustomer orderCustomer = orderCustomerService.findByCode(orderCode);
        List<Offers> allOffersAOrder = offerRepository.findAllByOrderCustomerOrderByPriceOrder(orderCustomer);
        if (allOffersAOrder.isEmpty())
            throw new NotFoundException(String.format("Not Found offer for this order %s !!",orderCode));
        return allOffersAOrder;
    }

    @Override
    public List<Offers> viewAllOffersOrdersByCustomerOrderByPerformanceExpert(String orderCode) {
        OrderCustomer orderCustomer = orderCustomerService.findByCode(orderCode);
        List<Offers> allOffersAOrder = offerRepository.findAllOffersAnOrderOrderByScoreExpert(orderCustomer);
        if (allOffersAOrder.isEmpty())
            throw new NotFoundException(String.format("Not Found offer for this order %s !!",orderCode));
        return allOffersAOrder;
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
            throw new NotAccept("offers is not accept because the offer isn't its orderCustomer ");
        if (!(orderCustomerDb.getOrderStatus().equals(OrderStatus.WaitingForOfferTheExperts) || orderCustomerDb.getOrderStatus().equals(OrderStatus.WaitingSelectTheExpert)))
            throw new NotAccept("Order an offer has already accepted ");
        orderCustomerDb.setOrderStatus(OrderStatus.WaitingForTheExpertToComeToYourLocation);
        orderCustomerService.updateOrder(orderCustomerDb);
        offers.setAccept(true);
        return offerRepository.save(offers);
    }

    @Override
    public void changeOrderToStartByCustomer(Offers offers, OrderCustomer orderCustomer) {
        OrderCustomer orderCustomerDb = getOrderCustomerById(orderCustomer.getId());
        Offers offersDb = findById(offers.getId());
        Date today = UtilDate.changeLocalDateToDate(LocalDate.now());
        if (UtilDate.compareTwoDate(offersDb.getStartTime(), today) < 0)
            throw new TimeOutException("The current date is less than the proposed date!!!");
        if (!offers.isAccept())
            throw new NotAccept("The selected offer has not been accepted !!!!");
        orderCustomerDb.setOrderStatus(OrderStatus.Started);
        orderCustomerService.updateOrder(orderCustomerDb);
    }

    @Override
    public void endDoWork(OrderCustomer orderCustomer, LocalDateTime endDoWork) {
        OrderCustomer orderCustomerDb = getOrderCustomerById(orderCustomer.getId());
        orderCustomerDb.setEndDate(UtilDate.changeLocalDateToDate(LocalDate.from(endDoWork)));
        orderCustomerDb.setOrderStatus(OrderStatus.DoItsBeen);
        orderCustomerService.updateOrder(orderCustomerDb);
    }

    @Override
    public Offers findById(Long id) {
        return offerRepository.findById(id).orElseThrow(() -> new NotFoundException((String.format("Not Fount Offer %d",id))));
    }

    @Override
    public Offers findOffersIsAccept(OrderCustomer orderCustomer) {
        return offerRepository.findOffersIsAccept(orderCustomer).orElseThrow(() -> new NotAccept(String.format("No offers isAccept for OrderCustomer %s",orderCustomer.getCodeOrder())));
    }

}
