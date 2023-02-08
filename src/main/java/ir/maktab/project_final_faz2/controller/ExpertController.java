package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.ExpertAndFile;
import ir.maktab.project_final_faz2.data.model.dto.OfferRegistry;
import ir.maktab.project_final_faz2.data.model.dto.OrderCustomerDto;
import ir.maktab.project_final_faz2.data.model.entity.*;
import ir.maktab.project_final_faz2.mapper.MapperOffer;
import ir.maktab.project_final_faz2.mapper.MapperOrder;
import ir.maktab.project_final_faz2.mapper.MapperUsers;
import ir.maktab.project_final_faz2.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ExpertController {
    @Autowired
    private ExpertServiceImpl expertService;
    @Autowired
    private OfferServiceImpl offerService;
    @Autowired
    private OrderCustomerServiceImpl orderCustomerService;
    @Autowired
    private SubJobServiceImpl subJobService;
    @Autowired
    private ReviewServiceImpl reviewService;


    @PostMapping("/save_expert")
    public ResponseEntity<String> saveExpert(@RequestBody ExpertAndFile expertAndFile) {
        Expert expert = expertService.save(MapperUsers.INSTANCE.expertDtoToExpert(expertAndFile.getExpertDto()), new File(expertAndFile.getPath()));
        return ResponseEntity.ok().body("save " + expert.getEmail() + " ok");
    }

    @PostMapping("/regist_offer")
    public ResponseEntity<String> saveOffer(@RequestBody OfferRegistry offerRegistry) {
        Offers offers = MapperOffer.INSTANCE.offerDtoToOffer(offerRegistry.getOffersDto());
        Expert expert = expertService.findByUserName(offerRegistry.getUserName());
        OrderCustomer orderCustomer = orderCustomerService.findById(offerRegistry.getId());
        offers.setExpert(expert);
        offers.setOrderCustomer(orderCustomer);
        Offers offerSave = offerService.save(offers, offerRegistry.getId());
        return ResponseEntity.ok().body("save " + offerSave.getId());
    }

    @GetMapping("/view_orders ")
    public ResponseEntity<List<OrderCustomerDto>> findAllOrder(@Param("nameSubService") String nameSubService) {
        SubJob subJob = subJobService.findSubJobByName(nameSubService);
        List<OrderCustomer> orderCustomers = orderCustomerService.findAllOrdersBySubJob(subJob);
        return ResponseEntity.ok().body(MapperOrder.INSTANCE.listOrderCustomerTOrderCustomerDto(orderCustomers));
    }

    @GetMapping("/view_credit")
    public ResponseEntity<String> viewCreditExpert(@RequestParam String userName) {
        Expert expert = expertService.findByUserName(userName);
        return ResponseEntity.ok().body("your credit is " + expert.getCredit().getBalance());
    }

    @GetMapping("/view_comments")
    public ResponseEntity<List<Integer>> viewRating(@RequestParam String userName) {
        Expert expert = expertService.findByUserName(userName);
        return ResponseEntity.ok().body(reviewService.findAllReviewForExpert(expert).stream().map(Review::getRating).collect(Collectors.toList()));
    }

    @GetMapping("/view_performance")
    public ResponseEntity<String> viewPerformance(@RequestParam String userName) {
        Expert expert = expertService.findByUserName(userName);
        return ResponseEntity.ok().body("performance " + expert.getPerformance());
    }

    @GetMapping("/transfor")
    public void WithDraw(@RequestParam Long orderId) {
        OrderCustomer order = orderCustomerService.findById(orderId);
        Offers offer = offerService.findOffersIsAccept(order);
        expertService.withdrawToCreditExpert(order.getOfferPrice(), offer.getExpert());
    }
    @GetMapping("/score_subtract")
    public void subtractScore(@RequestParam Long orderId) {
        OrderCustomer order = orderCustomerService.findById(orderId);
        Offers offers = offerService.subtractOfScore(order);
        System.out.println(offers.getExpert().getPerformance());
    }

}

