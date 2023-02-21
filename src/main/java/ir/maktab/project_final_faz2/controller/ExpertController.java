package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.request.AccountDto;
import ir.maktab.project_final_faz2.data.model.dto.request.ChangePasswordDto;
import ir.maktab.project_final_faz2.data.model.dto.request.OfferRegistryDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.ExpertDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.OrderCustomerDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.ResponseDTO;
import ir.maktab.project_final_faz2.data.model.dto.respons.ResponseListDto;
import ir.maktab.project_final_faz2.data.model.entity.*;
import ir.maktab.project_final_faz2.data.model.repository.ExpertRepository;
import ir.maktab.project_final_faz2.mapper.MapperOffer;
import ir.maktab.project_final_faz2.mapper.MapperOrder;
import ir.maktab.project_final_faz2.mapper.MapperUsers;
import ir.maktab.project_final_faz2.service.serviceImpl.*;
import ir.maktab.project_final_faz2.util.util.UtilImage;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartResolver;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/expert")
@Validated
@Slf4j
public class ExpertController {
    private final ExpertRepository expertRepository;

    private final ExpertServiceImpl expertService;

    private final OfferServiceImpl offerService;

    private final OrderCustomerServiceImpl orderCustomerService;

    private final SubJobServiceImpl subJobService;

    private final ReviewServiceImpl reviewService;

    public ExpertController(MultipartResolver multipartResolver, ExpertServiceImpl expertService, OfferServiceImpl offerService, OrderCustomerServiceImpl orderCustomerService, SubJobServiceImpl subJobService, ReviewServiceImpl reviewService,
                            ExpertRepository expertRepository) {
        this.expertService = expertService;
        this.offerService = offerService;
        this.orderCustomerService = orderCustomerService;
        this.subJobService = subJobService;
        this.reviewService = reviewService;
        this.expertRepository = expertRepository;
    }
    @PostMapping(value = "/save_expert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String saveExpert(@ModelAttribute ExpertDto expertDto) throws IOException {
        log.info("log_expert_pathFile ");
        System.out.println(expertDto);
        Expert expert = MapperUsers.INSTANCE.expertDtoToExpert(expertDto);
        expert.setExpertImage(UtilImage.validationImage(expertDto.getMultipartFile().getBytes()));
        expert.setCredit(new Credit());
        expertService.save(expert);
        return "ok";
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
    public ResponseEntity<?> findAllOrder(@RequestParam("nameSubService") String nameSubService) {
        SubJob subJob = subJobService.findSubJobByName(nameSubService);
        List<OrderCustomer> orderCustomers = orderCustomerService.findAllOrdersBySubJob(subJob);
        ResponseListDto<OrderCustomerDto> response = new ResponseListDto<>();
        response.setData(MapperOrder.INSTANCE.listOrderCustomerTOrderCustomerDto(orderCustomers));
        return ResponseEntity.ok(response);
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
        return ResponseEntity.ok().body("Successfully change password " + expert.getEmail());
    }

    @GetMapping("/view_performance")
    public ResponseEntity<String> viewPerformance(@RequestParam String userName) {
        Expert expert = expertService.findByUserName(userName);
        return ResponseEntity.ok().body("performance " + expert.getPerformance());
    }

    @GetMapping("/all-Orders-for-expert")
    public ResponseEntity<?> viewAllOrdersForAnExpert(@RequestParam String userName) {
        Expert expert = expertService.findByUserName(userName);
        List<OrderCustomer> orderCustomers = orderCustomerService.viewAllOrder(expert);
        ResponseListDto<OrderCustomerDto> response = new ResponseListDto<>();
        response.setData(MapperOrder.INSTANCE.listOrderCustomerTOrderCustomerDto(orderCustomers));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/view_image")
    public ResponseEntity<File> viewImage(@AuthenticationPrincipal Expert expert) {
        System.out.println(expert.getEmail());
        File file = new File("C:\\Users\\Lenovo\\Desktop\\OIF.jpg");
        File file1 = expertService.viewImage(expert.getEmail(), file);
        return ResponseEntity.ok().body(file1);
    }

}

