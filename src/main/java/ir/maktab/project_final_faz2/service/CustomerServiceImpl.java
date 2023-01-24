package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.repository.CustomerRepository;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.RepeatException;
import ir.maktab.project_final_faz2.exception.ValidationException;
import ir.maktab.project_final_faz2.util.util.ValidationInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl {
    private final CustomerRepository customerRepository;

    public Customer save(Customer customer) {
        if(customerRepository.findByEmail(customer.getEmail()).isPresent())
            throw new RepeatException("exist the user to the " +customer.getEmail());
        validateInfoPerson(customer);
        return customerRepository.save(customer);
    }

    private void validateInfoPerson(Customer person) {
        ValidationInput.validateName(person.getFirstName());
        ValidationInput.validateName(person.getLastName());
        ValidationInput.validateEmail(person.getEmail());
        ValidationInput.validatePassword(person.getPassword());
    }

    public Customer login(String userName, String password) {
        Customer customer = customerRepository.findByEmail(userName).orElseThrow(() -> new NotFoundException("customer not found with this userName"));
        if (customer.getPassword().equals(password))
            return customer;
        else
            throw new ValidationException("Your password is incorrect");
    }

    public Customer changePassword(String userName, String passwordOld, String newPassword) {
        Customer customer = login(userName, passwordOld);
        customer.setPassword(newPassword);
        customerRepository.save(customer);
        return login(userName, newPassword);
    }
    public Customer findByUserName(String userName) {
        return customerRepository.findByEmail(userName).orElseThrow(() -> new NotFoundException("customer not found with this userName"));
    }

}
