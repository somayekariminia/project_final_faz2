package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Credit;
import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.enums.exception.Insufficient;
import ir.maktab.project_final_faz2.data.model.enums.exception.TimeOutException;
import ir.maktab.project_final_faz2.data.model.enums.exception.ValidationException;
import ir.maktab.project_final_faz2.data.model.repository.CreditRepository;
import ir.maktab.project_final_faz2.util.util.UtilDate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class CreditServiceImpl {

    private final CreditRepository creditRepository;

    public CreditServiceImpl(CreditRepository creditRepository) {
        this.creditRepository = creditRepository;
    }

    public boolean checkCredit(Credit credit, Customer customer) {
        if (credit.getNumberCard().equals(customer.getCredit().getNumberCard()))
            throw new ValidationException("number Card is error");
        if (credit.getCvv2().equals(customer.getCredit().getCvv2()))
            throw new ValidationException("cvv2 is error");
        if (credit.getExpiredDate().after(UtilDate.changeLocalDateToDate(LocalDate.now())))
            throw new TimeOutException("expiredDate is Expired");
        return true;
    }

    public Credit payOfCredit(BigDecimal bigDecimal, Customer customer) {
        if (bigDecimal.compareTo(customer.getCredit().getBalance()) < 0)
            throw new Insufficient("Your balance is insufficient");
        BigDecimal balance = customer.getCredit().getBalance();
        balance = balance.subtract(bigDecimal);
        customer.getCredit().setBalance(balance);
        return creditRepository.save(customer.getCredit());
    }
}
