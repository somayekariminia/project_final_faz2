package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.Offers;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import ir.maktab.project_final_faz2.data.model.repository.OfferRepository;
import ir.maktab.project_final_faz2.data.model.repository.OrderCustomerRepository;
import ir.maktab.project_final_faz2.exception.NotAccept;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.ValidationException;
import ir.maktab.project_final_faz2.util.util.UtilDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
        return orderCustomerRepository.findAllBySubJobForAExpert(expertDb.getId());
    }

    public Offers save(Offers offers, String codeOrder) {
        Date today = UtilDate.changeLocalDateToDate(LocalDate.now());
        OrderCustomer orderCustomer = orderCustomerService.findByCode(codeOrder);
        if (offers.getOfferPriceByExpert().compareTo(orderCustomer.getSubJob().getPrice()) < 0)
            throw new ValidationException("offerPrice lower of basePrice");
        if (UtilDate.compareTwoDate(offers.getStartTime(), today) < 0) throw new ValidationException("");
        offers.setAccept(false);
        Offers offerSave = offerRepository.save(offers);
        updateOrder(orderCustomer, offerSave);
        return offers;
    }

    private OrderCustomer updateOrder(OrderCustomer orderCustomer, Offers offerSave) {
        OrderCustomer orderCustomerDb = orderCustomerRepository.findById(orderCustomer.getId()).orElseThrow(() -> new NotFoundException(""));
        orderCustomerDb.getOffersList().add(offerSave);
        orderCustomerDb.setOrderStatus(OrderStatus.WaitingSelectTheExpert);
        return orderCustomerRepository.save(orderCustomerDb);
    }

    public List<Offers> viewAllOffersOrdersByCustomer(String orderCode) {
        OrderCustomer orderCustomer = orderCustomerService.findByCode(orderCode);
        return orderCustomer.getOffersList().stream().sorted(Comparator.comparing(Offers::getOfferPriceByExpert)).collect(Collectors.toList());
    }

    private OrderCustomer getOrderCustomerById(OrderCustomer orderCustomer) {
        return orderCustomerRepository.findById(orderCustomer.getId()).orElseThrow(() -> new NotFoundException("is not find"));
    }

    public Offers selectAnOfferByCustomer(Offers offers, OrderCustomer orderCustomer) {
        OrderCustomer orderCustomerDb = getOrderCustomerById(orderCustomer);
        if (orderCustomerDb.getOffersList().stream().noneMatch(offers1 -> offers1.getId() == offers.getId()))
            throw new NotFoundException("there arent the offer for this order");
        orderCustomerDb.setOrderStatus(OrderStatus.WaitingForTheExpertToComeToYourLocation);
        orderCustomerRepository.save(orderCustomerDb);
        offers.setAccept(true);
        return offerRepository.save(offers);
    }

    public void changeOrderToStartByCustomer(Offers offers, OrderCustomer orderCustomer) {
        OrderCustomer orderCustomerDb = getOrderCustomerById(orderCustomer);
        if (orderCustomerDb.getOffersList().stream().noneMatch(offers1 -> offers1.getId() == offers.getId()))
            throw new NotFoundException("there arent the offer for this order");
        Date today = UtilDate.changeLocalDateToDate(LocalDate.now());
        if (UtilDate.compareTwoDate(offers.getStartTime(), today) < 0)
            throw new ValidationException("time bigger of time offer");
        if (!offers.isAccept())
            throw new NotAccept("the offer isnt accept");
        orderCustomerDb.setOrderStatus(OrderStatus.Started);
        orderCustomerRepository.save(orderCustomerDb);
    }

    public void endDoWork(OrderCustomer orderCustomer, LocalDateTime endDoWork) {
        OrderCustomer orderCustomerDb = getOrderCustomerById(orderCustomer);
        orderCustomerDb.setEndDate(UtilDate.changeLocalDateToDate(LocalDate.from(endDoWork)));
        orderCustomerDb.setOrderStatus(OrderStatus.DoItsBeen);
        orderCustomerRepository.save(orderCustomerDb);
    }

    public Offers findById(Long id) {
        return offerRepository.findById(id).orElseThrow(() -> new NotFoundException("is not found"));
    }

}
