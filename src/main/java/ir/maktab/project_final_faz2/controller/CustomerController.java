package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.*;
import ir.maktab.project_final_faz2.data.model.entity.*;
import ir.maktab.project_final_faz2.data.model.enums.StatusSort;
import ir.maktab.project_final_faz2.mapper.MapperOffer;
import ir.maktab.project_final_faz2.mapper.MapperOrder;
import ir.maktab.project_final_faz2.mapper.MapperServices;
import ir.maktab.project_final_faz2.mapper.MapperUsers;
import ir.maktab.project_final_faz2.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @Autowired
    private OfferServiceImpl offerService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody CustomerDto customerDto) {
        Customer customer = MapperUsers.INSTANCE.customerDtoToCustomer(customerDto);
        customerService.save(customer);
        return ResponseEntity.ok().body("customer save ok");
    }

    @PutMapping("/changing_password")
    public ResponseEntity<CustomerDto> changePassword(@RequestParam("userName") String userName,
                                                      @RequestParam("oldPassword") String oldPassword,
                                                      @RequestParam("newPassword") String newPassword) {
        Customer customer = customerService.changePassword(userName, oldPassword, newPassword);
        return ResponseEntity.ok().body(MapperUsers.INSTANCE.customerToCustomerDto(customer));
    }

    @GetMapping("/login_customer")
    public ResponseEntity<CustomerDto> findCustomerBy(@RequestBody AccountDto accountDto) {
        Customer customer = customerService.login(accountDto.getUserName(), accountDto.getPassword());
        return ResponseEntity.ok().body(MapperUsers.INSTANCE.customerToCustomerDto(customer));
    }

    @GetMapping("/view_basicJob")
    public ResponseEntity<List<BasicJobDto>> findAllBAsicJob() {
        List<BasicJob> listBasicJobs = basicJubService.findAllBasicJobs();
        return ResponseEntity.ok().body(MapperServices.INSTANCE.ListBasicJobToBasicJobDto(listBasicJobs));
    }

    @GetMapping("/view_subServices")
    public ResponseEntity<List<SubJobDto>> findAllSubJobs() {
        List<SubJob> listSubJob = subJobService.findAllSubJob();
        return ResponseEntity.ok().body(MapperServices.INSTANCE.subJobListToSubJobDto(listSubJob));
    }

    @GetMapping("/view_subServices_basic")
    public ResponseEntity<List<SubJobDto>> findSubJobABasic(@Valid @RequestParam("nameBasic") String nameBasic) {
        List<SubJob> basicJobList = basicJubService.findAllSubJobsABasicJob(nameBasic);
        return ResponseEntity.ok().body(MapperServices.INSTANCE.subJobListToSubJobDto(basicJobList));
    }

    @PostMapping("/register_order")
    public ResponseEntity<String> saveOrder(@Valid @RequestBody OrderRegistry orderRegistry) {
        Customer customer = customerService.login(orderRegistry.getAccountDto().getUserName(), orderRegistry.getAccountDto().getPassword());
        SubJob subJob = subJobService.findSubJobByName(orderRegistry.getNameSubJob());
        OrderCustomer orderCustomer = MapperOrder.INSTANCE.orderCustomerDtoToOrderCustomer(orderRegistry.getOrderCustomerDto());
        orderCustomer.setSubJob(subJob);
        orderCustomer.setCustomer(customer);
        OrderCustomer orderCustomer1 = orderCustomerService.saveOrder(orderCustomer);
        return ResponseEntity.ok().body("save order id : " + orderCustomer1.getId() + "ok");
    }

    @GetMapping("/view_Offers")
    public ResponseEntity<List<OffersDto>> findAllOffers(@RequestParam("orderId") Long id, @RequestParam("numberSelect") int numberSelect) {
        List<Offers> offers = new ArrayList<>();
        StatusSort statusSort = StatusSort.values()[numberSelect];
        switch (statusSort) {
            case PRICE_ASC -> {
                offers = offerService.viewAllOffersOrderByPriceAsc(id);
            }

            case PRICE_DESC -> {
                offers = offerService.viewAllOffersOrderByPriceDesc(id);
            }

            case SCORE_ASC -> {
                offers = offerService.viewAllOrdersOrderByScoreExpertAsc(id);
            }

            case SCORE_DESC -> {
                offers = offerService.viewAllOrdersOrderByScoreExpertDesc(id);
            }

        }
        return ResponseEntity.ok().body(MapperOffer.INSTANCE.listOfferToOfferDto(offers));
    }

    @GetMapping("/select_offer")
    public ResponseEntity<String> SelectOffer(@RequestParam Long orderId, @RequestParam Long offerId) {
        Offers offer = offerService.findById(offerId);
        OrderCustomer order = orderCustomerService.findById(orderId);
        Offers offers = offerService.selectAnOfferByCustomer(offer, order);
        return ResponseEntity.ok().body("select "+ offers.getId());
    }

    @PutMapping("/change_state")
    public ResponseEntity<String> ChangeState(@RequestParam("offerId") Long offerId, @RequestParam("orderId") Long orderId) {
        Offers offer = offerService.findById(offerId);
        OrderCustomer order = orderCustomerService.findById(orderId);
        Offers offers = offerService.selectAnOfferByCustomer(offer, order);
        return ResponseEntity.ok().body("order " + offers.getId() + " change state");
    }

    @PutMapping("/endWork")
    public ResponseEntity<String> finalDoWork(@RequestParam("orderId") Long orderId, @RequestParam("dateDoWord") String dateDoWord) {
        OrderCustomer order = orderCustomerService.findById(orderId);
        LocalDateTime parse = LocalDateTime.parse(dateDoWord);
        offerService.endDoWork(order, parse);
        return ResponseEntity.ok().body("order in date  " + order.getEndDateDoWork() + "  end");
    }

}
