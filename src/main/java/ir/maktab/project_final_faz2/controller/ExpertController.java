package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.request.AccountDto;
import ir.maktab.project_final_faz2.data.model.dto.request.ChangePasswordDto;
import ir.maktab.project_final_faz2.data.model.dto.request.ExpertAndFileDto;
import ir.maktab.project_final_faz2.data.model.dto.request.OfferRegistryDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.ExpertDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.OrderCustomerDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.ResponseDTO;
import ir.maktab.project_final_faz2.data.model.entity.*;
import ir.maktab.project_final_faz2.mapper.MapperOffer;
import ir.maktab.project_final_faz2.mapper.MapperOrder;
import ir.maktab.project_final_faz2.mapper.MapperUsers;
import ir.maktab.project_final_faz2.service.serviceImpl.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/expert")
@Validated
public class ExpertController {

    private final ExpertServiceImpl expertService;

    private final OfferServiceImpl offerService;

    private final OrderCustomerServiceImpl orderCustomerService;

    private final SubJobServiceImpl subJobService;

    private final ReviewServiceImpl reviewService;

    public ExpertController(ExpertServiceImpl expertService, OfferServiceImpl offerService, OrderCustomerServiceImpl orderCustomerService, SubJobServiceImpl subJobService, ReviewServiceImpl reviewService) {
        this.expertService = expertService;
        this.offerService = offerService;
        this.orderCustomerService = orderCustomerService;
        this.subJobService = subJobService;
        this.reviewService = reviewService;
    }


    @PostMapping("/save_expert")
    public ResponseEntity<String> saveExpert(@RequestBody ExpertAndFileDto expertAndFileDto) {
        Expert expert = expertService.save(MapperUsers.INSTANCE.expertDtoToExpert(expertAndFileDto.getExpertDto()), new File(expertAndFileDto.getPath()));
        return ResponseEntity.ok().body("save " + expert.getEmail() + " ok");
    }

    @PostMapping("/register_offer")
    public ResponseEntity<String> saveOffer(@RequestBody OfferRegistryDto offerRegistryDto) {
        Offers offers = MapperOffer.INSTANCE.offerDtoToOffer(offerRegistryDto.getOffersDto());
        Expert expert = expertService.findByUserName(offerRegistryDto.getUserName());
        OrderCustomer orderCustomer = orderCustomerService.findById(offerRegistryDto.getId());
        offers.setExpert(expert);
        offers.setOrderCustomer(orderCustomer);
        Offers offerSave = offerService.save(offers, offerRegistryDto.getId());
        return ResponseEntity.ok().body("save " + offerSave.getId());
    }

    @GetMapping("/view_orders_subJob")
    public ResponseEntity<List<OrderCustomerDto>> findAllOrder(@RequestParam("nameSubService") String nameSubService) {
        SubJob subJob = subJobService.findSubJobByName(nameSubService);
        List<OrderCustomer> orderCustomers = orderCustomerService.findAllOrdersBySubJob(subJob);
        return ResponseEntity.ok().body(MapperOrder.INSTANCE.listOrderCustomerTOrderCustomerDto(orderCustomers));
    }

    @GetMapping("/view_credit")
    public ResponseEntity<String> viewCreditExpert(@RequestParam String userName) {
        Expert expert = expertService.findByUserName(userName);
        return ResponseEntity.ok().body("your credit is " + expert.getCredit().getBalance());
    }

    @GetMapping("/view_rating")
    public ResponseEntity<List<Integer>> viewRating(@RequestParam String userName) {
        Expert expert = expertService.findByUserName(userName);
        return ResponseEntity.ok().body(reviewService.findAllReviewForExpert(expert).stream().map(Review::getRating).collect(Collectors.toList()));
    }

    @GetMapping("/view_comments")
    public ResponseEntity<List<String>> viewComments(@RequestParam String userName) {
        Expert expert = expertService.findByUserName(userName);
        return ResponseEntity.ok().body(reviewService.findAllReviewForExpert(expert).stream().map(Review::getComment).collect(Collectors.toList()));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<ExpertDto>> login(@Valid @RequestBody AccountDto accountDto) {
        Expert expert = expertService.login(accountDto.getUserName(), accountDto.getPassword());
        ResponseDTO<ExpertDto> responseDTO = new ResponseDTO<>();
        responseDTO.setInfo(MapperUsers.INSTANCE.expertToExpertDto(expert));
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/changing_password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        Expert expert = expertService.changePassword(changePasswordDto.getAccountDto().getUserName(), changePasswordDto.getAccountDto().getPassword(), changePasswordDto.getNewPassword());
        return ResponseEntity.ok().body("Successfully change password "+expert.getEmail());
    }

    @GetMapping("/view_performance")
    public ResponseEntity<String> viewPerformance(@RequestParam String userName) {
        Expert expert = expertService.findByUserName(userName);
        return ResponseEntity.ok().body("performance " + expert.getPerformance());
    }

    @GetMapping("/all-Orders-for-expert")
    public ResponseEntity<List<OrderCustomerDto>> viewAllOrdersForAnExpert(@RequestParam String userName) {
        Expert expert = expertService.findByUserName(userName);
        List<OrderCustomer> orderCustomers = orderCustomerService.viewAllOrder(expert);
        return ResponseEntity.ok().body(MapperOrder.INSTANCE.listOrderCustomerTOrderCustomerDto(orderCustomers));
    }

    @GetMapping("/view_image")
    public ResponseEntity<File> viewImage(@RequestParam String userName) {
        File file = new File("C:\\Users\\Lenovo\\Desktop\\OIF.jpg");
        File file1 = expertService.viewImage(userName, file);
        return ResponseEntity.ok().body(file1);
    }

}

