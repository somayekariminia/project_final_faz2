package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.request.ChangePasswordDto;
import ir.maktab.project_final_faz2.data.model.dto.request.OrderCustomerDto;
import ir.maktab.project_final_faz2.data.model.dto.request.OrderRegistryDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.*;
import ir.maktab.project_final_faz2.data.model.entity.*;
import ir.maktab.project_final_faz2.data.model.enums.StatusSort;
import ir.maktab.project_final_faz2.mapper.*;
import ir.maktab.project_final_faz2.service.serviceImpl.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CustomerServiceImpl customerService;

    private final SubJobServiceImpl subJobService;

    private final BasicJubServiceImpl basicJobService;

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


    @PostMapping("/changing_password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        Customer customer = customerService.changePassword(changePasswordDto.getAccountDto().getUserName(), changePasswordDto.getAccountDto().getPassword(), changePasswordDto.getNewPassword());
        return ResponseEntity.ok().body("Successfully change password " + customer.getEmail());
    }


    @GetMapping("/view_basicJob")
    public ResponseEntity<?> findAllBAsicJob() {
        List<BasicJob> listBasicJobs = basicJobService.findAllBasicJobs();
        ResponseListDto<BasicJobDto> response = new ResponseListDto<>();
        response.setData(MapperServices.INSTANCE.ListBasicJobToBasicJobDto(listBasicJobs));
        return ResponseEntity.ok(response);

    }

    @GetMapping("/view_subServices")
    public ResponseEntity<?> findAllSubJobs() {
        List<SubJob> listSubJob = subJobService.findAllSubJob();
        ResponseListDto<SubJobDto> response = new ResponseListDto<>();
        response.setData(MapperServices.INSTANCE.subJobListToSubJobDto(listSubJob));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/view_subJob_a_basicJob")
    public ResponseEntity<?> findSubJobABasic(@RequestParam("nameBasic") String nameBasic) {
        List<SubJob> subJobList = basicJobService.findAllSubJobsABasicJob(nameBasic);
        ResponseListDto<SubJobDto> response = new ResponseListDto<>();
        response.setData(MapperServices.INSTANCE.subJobListToSubJobDto(subJobList));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register_order")
    public ResponseEntity<String> saveOrder(@Valid @RequestBody OrderRegistryDto orderRegistryDto, Principal principal) {
        orderRegistryDto.setUserName(principal.getName());
        orderCustomerService.saveOrder(orderRegistryDto);
        return ResponseEntity.ok().body("save order ok");
    }

    @GetMapping("/view_Offers")
    public ResponseEntity<?> findAllOffers(@RequestParam("orderId") Long orderId, @RequestParam("numberSelect") int numberSelect) {
        List<Offers> offers = new ArrayList<>();
        StatusSort statusSort = StatusSort.values()[numberSelect];
        switch (statusSort) {
            case PRICE_ASC -> offers = offerService.viewAllOffersOrderByPriceAsc(orderId);

            case PRICE_DESC -> offers = offerService.viewAllOffersOrderByPriceDesc(orderId);

            case SCORE_ASC -> offers = offerService.viewAllOrdersOrderByScoreExpertAsc(orderId);

            case SCORE_DESC -> offers = offerService.viewAllOrdersOrderByScoreExpertDesc(orderId);
        }
        ResponseListDto<OffersDto> response = new ResponseListDto<>();
        response.setData(MapperOffer.INSTANCE.listOfferToOfferDto(offers));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/select_offer")
    public ResponseEntity<String> SelectOffer(@RequestParam Long orderId, @RequestParam Long offerId) {
        Offers offers = offerService.selectAnOfferByCustomer(offerId, orderId);
        return ResponseEntity.ok().body("select " + offers.getId());
    }

    @PutMapping("/change_state_start")
    public ResponseEntity<String> ChangeState(@RequestParam("offerId") Long offerId, @RequestParam("orderId") Long orderId) {
        OrderCustomer orderCustomer = offerService.changeOrderToStartByCustomer(offerId, orderId);
        return ResponseEntity.ok().body("order " + orderCustomer.getId() + " change state started");
    }

    @PutMapping("/endWork")
    public ResponseEntity<String> finalDoWork(@RequestParam("orderId") Long orderId) {
        offerService.endDoWork(orderId);
        return ResponseEntity.ok().body("order in date  " + LocalDateTime.now() + "  end");
    }


    @GetMapping("/paymentOfCredit")
    public ResponseEntity<String> paymentOfCredit(@RequestParam Long orderId) {
        creditService.payOfCredit(orderId);
        return ResponseEntity.ok().body("payment of your credit");
    }

    @PostMapping("/submit_comment")
    public ResponseEntity<String> giveScore(@RequestParam Long orderId, @Valid @RequestBody ReviewDto reviewDto) {
        reviewService.giveScoreToExpert(orderId, MapStructMapper.INSTANCE.reviewDtoToReview(reviewDto));
        return ResponseEntity.ok().body("your comment submit for desired expert");
    }

    @GetMapping("/view_all_order_customer")
    public ResponseEntity<?> viewAllOrder(@AuthenticationPrincipal Customer customer) {
        List<OrderCustomer> orderCustomers = orderCustomerService.findOrdersCustomer(customer);
        ResponseListDto<OrderCustomerResponseDto> response = new ResponseListDto<>();
        response.setData(MapperOrder.INSTANCE.listOrderCustomerToOrderCustomerResponseDto(orderCustomers));
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/view_credit")
    public ResponseEntity<BigDecimal> viewCredit(@AuthenticationPrincipal Customer customer) {
        return ResponseEntity.ok().body(customer.getCredit().getBalance());
    }
}