package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.*;
import ir.maktab.project_final_faz2.data.model.entity.BasicJob;
import ir.maktab.project_final_faz2.data.model.entity.Customer;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.mapper.MapStructMapper;
import ir.maktab.project_final_faz2.service.BasicJubServiceImpl;
import ir.maktab.project_final_faz2.service.CustomerServiceImpl;
import ir.maktab.project_final_faz2.service.OrderCustomerServiceImpl;
import ir.maktab.project_final_faz2.service.SubJobServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {
    @Autowired
    private CustomerServiceImpl customerService;
    @Autowired
    private SubJobServiceImpl subJobService;
    @Autowired
    private BasicJubServiceImpl basicJubService;
    @Autowired
    private OrderCustomerServiceImpl orderCustomerService;

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

    @GetMapping("/view_basicJob")
    public ResponseEntity<List<BasicJobDto>> findAllBAsicJob() {
        List<BasicJob> listBasicJobs = basicJubService.findAllBasicJobs();
        return ResponseEntity.ok().body(MapStructMapper.INSTANCE.ListBasicJobToBasicJobDto(listBasicJobs));
    }

    @GetMapping("/view_subServices")
    public ResponseEntity<List<SubJobDto>> findAllSubJobs() {
        List<SubJob> listSubJob = subJobService.findAllSubJob();
        return ResponseEntity.ok().body(MapStructMapper.INSTANCE.subJobListToSubJobDto(listSubJob));
    }
    @GetMapping("/view_subServices_basic")
    public ResponseEntity<List<SubJobDto>> findSubJobABasic(@Param("nameBasic") String nameBasic){
        List<SubJob> basicJobList=basicJubService.findAllSubJobsABasicJob(nameBasic);
        return ResponseEntity.ok().body(MapStructMapper.INSTANCE.subJobListToSubJobDto(basicJobList));
    }

    @PostMapping("/register_order")
    public ResponseEntity<OrderCustomerDto> registerOrder(@RequestBody OrderRegistry orderRegistry) {
        Customer customer = customerService.login(orderRegistry.getAccountDto().getUserName(), orderRegistry.getAccountDto().getPassword());
        SubJob subJob = subJobService.findSubJobByName(orderRegistry.getNameSubJob());
        OrderCustomer orderCustomer = MapStructMapper.INSTANCE.orderCustomerDtoToOrderCustomer(orderRegistry.getOrderCustomerDto());
        orderCustomer.setSubJob(subJob);
        orderCustomer.setCustomer(customer);
        OrderCustomer orderCustomer1 = orderCustomerService.saveOrder(orderCustomer);
        return ResponseEntity.ok().body(MapStructMapper.INSTANCE.orderCustomerToOrderCustomerDto(orderCustomer1));
    }

}
