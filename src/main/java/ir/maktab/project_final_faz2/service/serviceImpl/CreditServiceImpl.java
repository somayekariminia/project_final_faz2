package ir.maktab.project_final_faz2.service.serviceImpl;

import ir.maktab.project_final_faz2.config.MessageSourceConfiguration;
import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import ir.maktab.project_final_faz2.data.model.repository.CreditRepository;
import ir.maktab.project_final_faz2.exception.Insufficient;
import ir.maktab.project_final_faz2.exception.NotAcceptedException;
import ir.maktab.project_final_faz2.exception.TimeOutException;
import ir.maktab.project_final_faz2.exception.ValidationException;
import ir.maktab.project_final_faz2.service.serviceInterface.CreditService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {
    private final CreditRepository creditRepository;
    private final ExpertServiceImpl expertService;
    private final OfferServiceImpl offerService;
    private final OrderCustomerServiceImpl orderCustomerService;
    private final MessageSourceConfiguration messageSource;


    @Override
    @Transactional
    public void checkCredit(LocalDate expiredDate, OrderCustomer orderCustomer) {
        log.debug("start method CheckCredit");
        if (!orderCustomer.getOrderStatus().equals(OrderStatus.DoItsBeen)) {
            log.error("this order  %s  already isNot end");
            throw new ValidationException(messageSource.getMessage("errors.message.order_isn't_done"));
        }
        if (expiredDate.isBefore((LocalDate.now()))) {
            log.error("expiredDate is Expired");
            throw new TimeOutException(messageSource.getMessage("errors.message.expired_date"));
        }
        orderCustomer.setOrderStatus(OrderStatus.Paid);
        orderCustomerService.updateOrder(orderCustomer);
        Expert expert = offerService.findOffersIsAccept(orderCustomer).getExpert();
        expertService.withdrawToCreditExpert(orderCustomer.getOfferPrice(), expert);
        log.debug("end method");
    }

    @Override
    @Transactional
    public void payOfCredit(OrderCustomer orderCustomer) {
        Customer customer = orderCustomer.getCustomer();
        if (!orderCustomer.getOrderStatus().equals(OrderStatus.DoItsBeen)) {
            log.error("errors.message.done_work");
            throw new NotAcceptedException(messageSource.getMessage("errors.message.done_work"));
        }

        if (orderCustomer.getOfferPrice().compareTo(customer.getCredit().getBalance()) > 0) {
            log.error("errors.message.dont_insufficient");
            throw new Insufficient(messageSource.getMessage("errors.message.dont_insufficient"));
        }

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
