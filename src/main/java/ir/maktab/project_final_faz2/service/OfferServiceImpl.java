package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.Offers;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import ir.maktab.project_final_faz2.data.model.repository.OfferRepository;
import ir.maktab.project_final_faz2.data.model.repository.OrderCustomerRepository;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.ValidationException;
import ir.maktab.project_final_faz2.util.util.UtilDate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl {
    private final OfferRepository offerRepository;
    private final OrderCustomerRepository orderCustomerRepository;

    public OfferServiceImpl(OfferRepository offerRepository, OrderCustomerRepository orderCustomerRepository) {
        this.offerRepository = offerRepository;
        this.orderCustomerRepository = orderCustomerRepository;
    }

    public List<OrderCustomer> findAllOrdersForAExpert(Expert expert) {
        //chek expert null nabashad
        List<OrderCustomer> allBySubJobForAExpert = orderCustomerRepository.findAllBySubJobForAExpert(expert.getId());
        if (allBySubJobForAExpert.isEmpty()) throw new NotFoundException("There is no specialty for this specialist");
        return allBySubJobForAExpert.stream().filter(orderCustomer -> orderCustomer.getOrderStatus().equals(OrderStatus.WaitingForOfferTheExperts) || orderCustomer.getOrderStatus().equals(OrderStatus.WaitingSelectTheExpert)).collect(Collectors.toList());
    }

    public Offers save(Offers offers, OrderCustomer orderCustomer) {
        Date today = UtilDate.changeLocalDateToDate(LocalDate.now());
        if (offers.getOfferPriceByExpert().compareTo(orderCustomer.getSubJob().getPrice()) < 0)
            throw new ValidationException("offerPrice lower of basePrice");
        if (UtilDate.compareTwoDate(offers.getStartTime(), today) < 0) throw new ValidationException("");
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

    public List<Offers> viewAllOffersOrdersByCustomer(OrderCustomer orderCustomer) {
        OrderCustomer orderCustomer1 = getOrderCustomerById(orderCustomer);
        return orderCustomer1.getOffersList().stream().sorted(Comparator.comparing(Offers::getOfferPriceByExpert)).collect(Collectors.toList());
    }

    private OrderCustomer getOrderCustomerById(OrderCustomer orderCustomer) {
        return orderCustomerRepository.findById(orderCustomer.getId()).orElseThrow(() -> new NotFoundException("is not find"));
    }

    public void selectAnOfferByCustomer(Offers offers, OrderCustomer orderCustomer) {
        OrderCustomer orderCustomerDb = getOrderCustomerById(orderCustomer);
        if (orderCustomerDb.getOffersList().stream().noneMatch(offers1 -> offers1.getId() == offers.getId()))
            throw new NotFoundException("there arent the offer for this order");
        orderCustomerDb.setOrderStatus(OrderStatus.WaitingForTheExpertToComeToYourLocation);
        orderCustomerDb.setExpert(offers.getExpert());
        orderCustomerRepository.save(orderCustomerDb);
    }

    public void updateStatusOrder(OrderCustomer orderCustomer) {
        if (Objects.isNull(orderCustomer))
            throw new NotFoundException("there arent orderCustomer to id" + orderCustomer.getId());
        orderCustomerRepository.update(String.valueOf(orderCustomer.getOrderStatus()), orderCustomer.getId());
    }

    public void changeOrderToStartByCustomer(Offers offers, OrderCustomer orderCustomer) {
        OrderCustomer orderCustomerDb = getOrderCustomerById(orderCustomer);
        if (orderCustomerDb.getOffersList().stream().noneMatch(offers1 -> offers1.getId() == offers.getId()))
            throw new NotFoundException("there arent the offer for this order");
        Date today = UtilDate.changeLocalDateToDate(LocalDate.now());
        if (UtilDate.compareTwoDate(offers.getStartTime(), today) < 0)
            throw new ValidationException("time bigger of time offer");
        orderCustomerDb.setOrderStatus(OrderStatus.Started);
        updateStatusOrder(orderCustomerDb);
    }
    public void endDoWork(OrderCustomer orderCustomer){

    }

}
