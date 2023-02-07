package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.enums.exception.Insufficient;
import ir.maktab.project_final_faz2.data.model.enums.exception.TimeOutException;
import ir.maktab.project_final_faz2.data.model.repository.CreditRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service

public class CreditServiceImpl {

    private final CreditRepository creditRepository;
    private final ExpertServiceImpl expertService;
    private final OfferServiceImpl offerService;

    public CreditServiceImpl(CreditRepository creditRepository, ExpertServiceImpl expertService, OfferServiceImpl offerService) {
        this.creditRepository = creditRepository;

        this.expertService = expertService;
        this.offerService = offerService;
    }

    @Transactional
    public boolean checkCredit(LocalDate expiredDate, OrderCustomer orderCustomer) {
        if (expiredDate.isBefore((LocalDate.now())))
            throw new TimeOutException("expiredDate is Expired");
        Expert expert = offerService.findOffersIsAccept(orderCustomer).getExpert();
        expertService.withdrawToCreditExpert(orderCustomer.getOfferPrice(), expert);
        return true;
    }

    @Transactional
    public void payOfCredit(OrderCustomer orderCustomer) {
        Customer customer=orderCustomer.getCustomer();
        if (orderCustomer.getOfferPrice().compareTo(customer.getCredit().getBalance()) < 0)
            throw new Insufficient("Your balance is insufficient");
        BigDecimal balance = customer.getCredit().getBalance();
        balance = balance.subtract(orderCustomer.getOfferPrice());
        customer.getCredit().setBalance(balance);
        creditRepository.save(customer.getCredit());
        Expert expert = offerService.findOffersIsAccept(orderCustomer).getExpert();
        expertService.withdrawToCreditExpert(orderCustomer.getOfferPrice(), expert);
    }


}
