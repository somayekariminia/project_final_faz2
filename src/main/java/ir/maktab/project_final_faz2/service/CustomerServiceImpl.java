package ir.maktab.project_final_faz2.service;

import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.repository.CustomerRepository;
import ir.maktab.project_final_faz2.util.util.ValidationInput;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl {
    private final CustomerRepository customerRepository;

    public Customer save(Customer customer) {
        validateInfoPerson(customer);
        return customerRepository.save(customer);
    }
    private void validateInfoPerson(Customer person) {
        ValidationInput.validateName(person.getFirstName());
        ValidationInput.validateName(person.getLastName());
        ValidationInput.validateEmail(person.getEmail());
        ValidationInput.validatePassword(person.getPassword());
    }
}
