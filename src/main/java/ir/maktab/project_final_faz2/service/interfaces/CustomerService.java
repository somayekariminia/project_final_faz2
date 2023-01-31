package ir.maktab.project_final_faz2.service.interfaces;

import ir.maktab.project_final_faz2.data.model.entity.Customer;

public interface CustomerService {
    Customer save(Customer customer);

    Customer login(String userName, String password);

    Customer changePassword(String userName, String passwordOld, String newPassword);

    Customer findByUserName(String userName);
}
