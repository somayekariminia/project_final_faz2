package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.AccountDto;
import ir.maktab.project_final_faz2.data.model.dto.CustomerDto;
import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.mapper.MapStructMapper;
import ir.maktab.project_final_faz2.service.CustomerServiceImpl;
import ir.maktab.project_final_faz2.service.SubJobServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerController {
    @Autowired
    private CustomerServiceImpl customerService;
    @Autowired
    private SubJobServiceImpl subJobService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody CustomerDto customerDto) {
        Customer customer = MapStructMapper.INSTANCE.customerDtoToCustomer(customerDto);
        customerService.save(customer);
        return ResponseEntity.ok().body("customer save ok");
    }

    @PutMapping("/changing_password")
    public ResponseEntity<CustomerDto> changePassword(@RequestParam("userName") String userName,
                                                      @RequestParam("oldPassword") String oldPassword,
                                                      @RequestParam("newPassword") String newPassword) {
        Customer customer = customerService.changePassword(userName, oldPassword, newPassword);
        return ResponseEntity.ok().body(MapStructMapper.INSTANCE.customerToCustomerDto(customer));
    }

    @GetMapping("/login_customer")
    public ResponseEntity<CustomerDto> findCustomerBy(@RequestBody AccountDto accountDto) {
        Customer customer = customerService.login(accountDto.getUserName(), accountDto.getPassword());
        return ResponseEntity.ok().body(MapStructMapper.INSTANCE.customerToCustomerDto(customer));
    }

}
