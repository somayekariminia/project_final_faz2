package ir.maktab.project_final_faz2.service.serviceImpl;

import ir.maktab.project_final_faz2.config.MessageSourceConfiguration;
import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.enums.Role;
import ir.maktab.project_final_faz2.data.model.repository.CustomerRepository;
import ir.maktab.project_final_faz2.exception.DuplicateException;
import ir.maktab.project_final_faz2.exception.NotFoundException;
import ir.maktab.project_final_faz2.exception.NullObjects;
import ir.maktab.project_final_faz2.exception.ValidationException;
import ir.maktab.project_final_faz2.service.serviceInterface.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final MessageSourceConfiguration messageSource;

    @Transactional
    @Override
    public Customer save(Customer customer) {
        if (Objects.isNull(customer))
            throw new NullObjects(messageSource.getMessage("errors.message.null-object"));
        if (customerRepository.findByEmail(customer.getEmail()).isPresent())
            throw new DuplicateException(messageSource.getMessage("errors.message.duplicate-object"));
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setRole(Role.CUSTOMER);
        return customerRepository.save(customer);
    }

    @Override
    public Customer login(String userName, String password) {
        Customer customer = customerRepository.findByEmail(userName).orElseThrow(() -> new NotFoundException(messageSource.getMessage("errors.message.notFound-object")));
        if (customer.getPassword().equals(password))
            return customer;
        else
            throw new ValidationException(messageSource.getMessage("errors.message.invalid_password"));
    }

    @Override
    public Customer changePassword(String userName, String passwordOld, String newPassword) {
        if (passwordOld.equals(newPassword))
            throw new ValidationException(messageSource.getMessage("errors.message.duplicate_password"));
        Customer customer = login(userName, passwordOld);
        customer.setPassword(newPassword);
        customerRepository.save(customer);
        Customer newCustomer = findByUserName(userName);
        if (!newCustomer.getPassword().equals(newPassword))
            throw new NotFoundException(messageSource.getMessage("errors.message.invalid_password"));
        return newCustomer;
    }

    @Override
    public Customer findByUserName(String userName) {
        return customerRepository.findByEmail(userName).orElseThrow(() -> new NotFoundException(messageSource.getMessage("errors.message.notFound-object")));
    }

    public void updateCustomer(Customer customer) {
        if (Objects.isNull(customer))
            throw new NullObjects(messageSource.getMessage("errors.message.null-object"));
        customerRepository.save(customer);
    }

}
