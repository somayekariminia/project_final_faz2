package ir.maktab.project_final_faz2.service.serviceInterface;

import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;

import java.time.LocalDate;

public interface CreditService {
    void checkCredit(LocalDate expiredDate, OrderCustomer orderCustomer);

    void payOfCredit(Long orderCustomer);

}
