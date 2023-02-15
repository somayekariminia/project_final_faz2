package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.request.AccountDto;
import ir.maktab.project_final_faz2.data.model.dto.request.BasicJobDto;
import ir.maktab.project_final_faz2.data.model.dto.request.OrderRegistry;
import ir.maktab.project_final_faz2.data.model.dto.request.SubJobDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.CustomerDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.OffersDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.OrderCustomerDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.ReviewDto;
import ir.maktab.project_final_faz2.data.model.entity.*;
import ir.maktab.project_final_faz2.data.model.enums.StatusSort;
import ir.maktab.project_final_faz2.mapper.*;
import ir.maktab.project_final_faz2.service.serviceImpl.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerServiceImpl customerService;

    private final SubJobServiceImpl subJobService;

    private final BasicJubServiceImpl basicJubService;

    private final OrderCustomerServiceImpl orderCustomerService;

    private final OfferServiceImpl offerService;

    private final CreditServiceImpl creditService;

    private final ReviewServiceImpl reviewService;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody CustomerDto customerDto) {
        Customer customer = MapperUsers.INSTANCE.customerDtoToCustomer(customerDto);
        customerService.save(customer);
        return ResponseEntity.ok().body("customer save ok");
    }

    @PutMapping("/changing_password")
    public ResponseEntity<CustomerDto> changePassword(@Valid @RequestParam("userName") String userName,
                                                      @RequestParam("oldPassword") String oldPassword,
                                                      @RequestParam("newPassword") String newPassword) {
        Customer customer = customerService.changePassword(userName, oldPassword, newPassword);
        return ResponseEntity.ok().body(MapperUsers.INSTANCE.customerToCustomerDto(customer));
    }

    @PostMapping("/login_customer")
    public ResponseEntity<CustomerDto> findCustomerBy(@Valid @RequestBody AccountDto accountDto) {
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
    public ResponseEntity<List<SubJobDto>> findSubJobABasic(@RequestParam("nameBasic") String nameBasic) {
        List<SubJob> basicJobList = basicJubService.findAllSubJobsABasicJob(nameBasic);
        return ResponseEntity.ok().body(MapperServices.INSTANCE.subJobListToSubJobDto(basicJobList));
    }

    @PostMapping("/register_order")
    public ResponseEntity<String> saveOrder(@Valid @RequestBody OrderRegistry orderRegistry) {
        Customer customer = customerService.findByUserName(orderRegistry.getUserName());
        SubJob subJob = subJobService.findSubJobByName(orderRegistry.getNameSubJob());
        OrderCustomer orderCustomer = MapperOrder.INSTANCE.orderCustomerDtoToOrderCustomer(orderRegistry.getOrderCustomerDto());
        orderCustomer.setSubJob(subJob);
        orderCustomer.setCustomer(customer);
        OrderCustomer orderCustomer1 = orderCustomerService.saveOrder(orderCustomer);
        return ResponseEntity.ok().body("save order id : " + orderCustomer1.getId() + " ok");
    }

    @GetMapping("/view_Offers")
    public ResponseEntity<List<OffersDto>> findAllOffers(@RequestParam("orderId") Long orderId, @RequestParam("numberSelect") int numberSelect) {
        List<Offers> offers = new ArrayList<>();
        StatusSort statusSort = StatusSort.values()[numberSelect];
        switch (statusSort) {
            case PRICE_ASC -> offers = offerService.viewAllOffersOrderByPriceAsc(orderId);

            case PRICE_DESC -> offers = offerService.viewAllOffersOrderByPriceDesc(orderId);

            case SCORE_ASC -> offers = offerService.viewAllOrdersOrderByScoreExpertAsc(orderId);

            case SCORE_DESC -> offers = offerService.viewAllOrdersOrderByScoreExpertDesc(orderId);
        }
        return ResponseEntity.ok().body(MapperOffer.INSTANCE.listOfferToOfferDto(offers));
    }

    @GetMapping("/select_offer")
    public ResponseEntity<String> SelectOffer(@RequestParam Long orderId, @RequestParam Long offerId) {
        Offers offer = offerService.findById(offerId);
        OrderCustomer order = orderCustomerService.findById(orderId);
        Offers offers = offerService.selectAnOfferByCustomer(offer, order);
        return ResponseEntity.ok().body("select " + offers.getId());
    }

    @PutMapping("/change_state_start")
    public ResponseEntity<String> ChangeState(@RequestParam("offerId") Long offerId, @RequestParam("orderId") Long orderId) {
        Offers offer = offerService.findById(offerId);
        OrderCustomer order = orderCustomerService.findById(orderId);
        OrderCustomer orderCustomer = offerService.changeOrderToStartByCustomer(offer, order);
        return ResponseEntity.ok().body("order " + orderCustomer.getId() + " change state started");
    }

    @PutMapping("/endWork")
    public ResponseEntity<String> finalDoWork(@RequestParam("orderId") Long orderId) {
        OrderCustomer order = orderCustomerService.findById(orderId);
        LocalDateTime today = LocalDateTime.now();
        offerService.endDoWork(order, today);
        return ResponseEntity.ok().body("order in date  " + order.getEndDateDoWork() + "  end");
    }


    @GetMapping("/paymentOfCredit")
    public ResponseEntity<String> paymentOfCredit(@RequestParam Long orderId) {
        OrderCustomer orderCustomer = orderCustomerService.findById(orderId);
        creditService.payOfCredit(orderCustomer);
        return ResponseEntity.ok().body("payment of your credit");
    }

    @GetMapping("/submit_comment")
    public ResponseEntity<String> giveScore(@RequestParam Long orderId, @Valid @RequestBody ReviewDto reviewDto) {
        OrderCustomer order = orderCustomerService.findById(orderId);
        reviewService.giveScoreToExpert(order, MapStructMapper.INSTANCE.reviewDtoToReview(reviewDto));
        return ResponseEntity.ok().body("your comment submit for desired expert");
    }
    @GetMapping("/view-all-order-customer")
    public ResponseEntity<List<OrderCustomerDto>> viewAllOrder(@RequestParam String userName){
       Customer customer=customerService.findByUserName(userName);
       List<OrderCustomer> orderCustomers=orderCustomerService.findOrdersCustomer(customer);
       return ResponseEntity.ok().body(MapperOrder.INSTANCE.listOrderCustomerTOrderCustomerDto(orderCustomers));
    }
}