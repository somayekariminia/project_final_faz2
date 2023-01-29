package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.Offers;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import ir.maktab.project_final_faz2.data.model.repository.OfferRepository;
import ir.maktab.project_final_faz2.data.model.repository.OrderCustomerRepository;
import ir.maktab.project_final_faz2.exception.NotAccept;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.ValidationException;
import ir.maktab.project_final_faz2.util.util.UtilDate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl {
    private final OfferRepository offerRepository;
    private final OrderCustomerRepository orderCustomerRepository;
    private final ExpertServiceImpl expertService;
    private final OrderCustomerServiceImpl orderCustomerService;

    public List<OrderCustomer> findAllOrdersForAExpert(Expert expert) {
        Expert expertDb = expertService.findByUserName(expert.getEmail());
        List<OrderCustomer>list=new ArrayList<>();
        for (SubJob subJob :expertDb.getServicesList()) {
            List<OrderCustomer> listSub=orderCustomerService.findAllOrdersBySubJob(subJob);
            listSub.parallelStream().collect(Collectors.toCollection(()->list));
        }
        if(list.isEmpty())
            throw new NotFoundException("list orders is null");
        return list;
    }
    @Transactional
    public Offers save(Offers offers, String codeOrder) {
        Date today = UtilDate.changeLocalDateToDate(LocalDate.now());
        OrderCustomer orderCustomer = orderCustomerService.findByCode(codeOrder);
        if (offers.getOfferPriceByExpert().compareTo(orderCustomer.getSubJob().getPrice()) < 0)
            throw new ValidationException("offerPrice lower of basePrice");
        if (UtilDate.compareTwoDate(offers.getStartTime(), today) < 0)
            throw new ValidationException("Time Enter is Expired");
        orderCustomer.setOrderStatus(OrderStatus.WaitingSelectTheExpert);
        orderCustomerService.updateOrder(orderCustomer);
        offers.setAccept(false);
        offers.setOrderCustomer(orderCustomer);
        return offerRepository.save(offers);
    }


    public List<Offers> viewAllOffersOrdersByCustomerOrderByPrice(String orderCode) {
        OrderCustomer orderCustomer = orderCustomerService.findByCode(orderCode);
        List<Offers> allOffersAOrder = offerRepository.findAllByOrderCustomerOrderByPriceOrder(orderCustomer);
        if (allOffersAOrder.isEmpty())
            throw new NotFoundException("not found");
        return allOffersAOrder;
    }
    public List<Offers> viewAllOffersOrdersByCustomerOrderByPerformanceExpert(String orderCode) {
        OrderCustomer orderCustomer = orderCustomerService.findByCode(orderCode);
        List<Offers> allOffersAOrder = offerRepository.findAllOffersAnOrderOrderByScoreExpert(orderCustomer);
        if (allOffersAOrder.isEmpty())
            throw new NotFoundException("not found");
        return allOffersAOrder;
    }

    private OrderCustomer getOrderCustomerById(Long id) {
        return orderCustomerService.findById(id);
    }

    @Transactional
    public Offers selectAnOfferByCustomer(Offers offers, OrderCustomer orderCustomer) {
        OrderCustomer orderCustomerDb = getOrderCustomerById(orderCustomer.getId());
        Offers offersDb = findById(offers.getId());
        if (offersDb.getOrderCustomer().getId() != orderCustomerDb.getId())
            throw new NotAccept("offers is not accept because the offer isn't its orderCustomer ");
        if (!(orderCustomerDb.getOrderStatus().equals(OrderStatus.WaitingForOfferTheExperts) || orderCustomerDb.getOrderStatus().equals(OrderStatus.WaitingSelectTheExpert)))
            throw new NotAccept("Order an offer has already accepted ");
        orderCustomerDb.setOrderStatus(OrderStatus.WaitingForTheExpertToComeToYourLocation);
        orderCustomerRepository.save(orderCustomerDb);
        offers.setAccept(true);
        return offerRepository.save(offers);
    }

    public void changeOrderToStartByCustomer(Offers offers, OrderCustomer orderCustomer) {
        OrderCustomer orderCustomerDb = getOrderCustomerById(orderCustomer.getId());
        Offers offersDb = findById(offers.getId());
        Date today = UtilDate.changeLocalDateToDate(LocalDate.now());
        if (UtilDate.compareTwoDate(offersDb.getStartTime(), today) < 0)
            throw new ValidationException("time bigger of time offer");
        if (!offers.isAccept())
            throw new NotAccept("the offer isnt accept");
        orderCustomerDb.setOrderStatus(OrderStatus.Started);
        orderCustomerRepository.save(orderCustomerDb);
    }

    public void endDoWork(OrderCustomer orderCustomer, LocalDateTime endDoWork) {
        OrderCustomer orderCustomerDb = getOrderCustomerById(orderCustomer.getId());
        orderCustomerDb.setEndDate(UtilDate.changeLocalDateToDate(LocalDate.from(endDoWork)));
        orderCustomerDb.setOrderStatus(OrderStatus.DoItsBeen);
        orderCustomerRepository.save(orderCustomerDb);
    }

    public Offers findById(Long id) {
        return offerRepository.findById(id).orElseThrow(() -> new NotFoundException("is not found"));
    }

    public Offers findOffersIsAccept(OrderCustomer orderCustomer) {
        return offerRepository.findOffersIsAccept(orderCustomer).orElseThrow(() -> new NotAccept("is not Offer accept"));
    }

}
