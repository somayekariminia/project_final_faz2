package ir.maktab.project_final_faz2.service.impl;

import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import ir.maktab.project_final_faz2.exception.Insufficient;
import ir.maktab.project_final_faz2.exception.NotAcceptedException;
import ir.maktab.project_final_faz2.exception.TimeOutException;
import ir.maktab.project_final_faz2.exception.ValidationException;
import ir.maktab.project_final_faz2.data.model.repository.CreditRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Slf4j
public class CreditServiceImpl {

    private final CreditRepository creditRepository;
    private final ExpertServiceImpl expertService;
    private final OfferServiceImpl offerService;
    private final OrderCustomerServiceImpl orderCustomerService;

    public CreditServiceImpl(CreditRepository creditRepository, ExpertServiceImpl expertService, OfferServiceImpl offerService, OrderCustomerServiceImpl orderCustomerService) {
        this.creditRepository = creditRepository;

        this.expertService = expertService;
        this.offerService = offerService;
        this.orderCustomerService = orderCustomerService;
    }

    @Transactional
    public void checkCredit(LocalDate expiredDate, OrderCustomer orderCustomer) {
        if(!orderCustomer.getOrderStatus().equals(OrderStatus.DoItsBeen))
            throw new ValidationException(String.format("this order  %s  already isNot end",orderCustomer.getSubJob()));
        if (expiredDate.isBefore((LocalDate.now())))
            throw new TimeOutException("expiredDate is Expired");
        orderCustomer.setOrderStatus(OrderStatus.Paid);
        orderCustomerService.updateOrder(orderCustomer);
        log.debug("continue method checkCredit");
        Expert expert = offerService.findOffersIsAccept(orderCustomer).getExpert();
        expertService.withdrawToCreditExpert(orderCustomer.getOfferPrice(), expert);

    }

    @Transactional
    public void payOfCredit(OrderCustomer orderCustomer) {
        Customer customer=orderCustomer.getCustomer();
        if(!orderCustomer.getOrderStatus().equals(OrderStatus.DoItsBeen))
            throw new NotAcceptedException("you cant payment because orderCustomer dont done");
        if (orderCustomer.getOfferPrice().compareTo(customer.getCredit().getBalance()) > 0)
            throw new Insufficient("Your balance is insufficient");
        orderCustomer.setOrderStatus(OrderStatus.Paid);
        orderCustomerService.updateOrder(orderCustomer);
        BigDecimal balance = customer.getCredit().getBalance();
        balance = balance.subtract(orderCustomer.getOfferPrice());
        customer.getCredit().setBalance(balance);
        creditRepository.save(customer.getCredit());
        Expert expert = offerService.findOffersIsAccept(orderCustomer).getExpert();
        expertService.withdrawToCreditExpert(orderCustomer.getOfferPrice(), expert);
    }

}
