package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.ExpertAndFile;
import ir.maktab.project_final_faz2.data.model.dto.OfferRegistry;
import ir.maktab.project_final_faz2.data.model.dto.OrderCustomerDto;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.Offers;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.mapper.MapStructMapper;
import ir.maktab.project_final_faz2.mapper.MapperOffer;
import ir.maktab.project_final_faz2.mapper.MapperOrder;
import ir.maktab.project_final_faz2.mapper.MapperUsers;
import ir.maktab.project_final_faz2.service.ExpertServiceImpl;
import ir.maktab.project_final_faz2.service.OfferServiceImpl;
import ir.maktab.project_final_faz2.service.OrderCustomerServiceImpl;
import ir.maktab.project_final_faz2.service.SubJobServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.time.Duration;
import java.util.List;

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


    @PostMapping("/save_expert")
    public ResponseEntity<String> saveExpert(@RequestBody ExpertAndFile expertAndFile) {
        Expert expert = expertService.save(MapperUsers.INSTANCE.expertDtoToExpert(expertAndFile.getExpertDto()), new File(expertAndFile.getPath()));
        return ResponseEntity.ok().body("save " + expert.getEmail() + " ok");
    }

    @PostMapping("/regist_offer")
    public ResponseEntity<String> saveOffer(@RequestBody OfferRegistry offerRegistry) {
        Offers offers= MapperOffer.INSTANCE.offerDtoToOffer(offerRegistry.getOffersDto());
        Expert expert = expertService.findByUserName(offerRegistry.getUserName());
        OrderCustomer orderCustomer = orderCustomerService.findById(offerRegistry.getId());
        offers.setExpert(expert);
        offers.setOrderCustomer(orderCustomer);
        Offers offerSave = offerService.save(offers, offerRegistry.getId());
        return ResponseEntity.ok().body("save " + offerSave.getId());
    }
    @GetMapping("/view_orders ")
    public ResponseEntity<List<OrderCustomerDto>> findAllOrder(@Param("nameSubService") String nameSubService){
        SubJob subJob=subJobService.findSubJobByName(nameSubService);
        List<OrderCustomer>orderCustomers=orderCustomerService.findAllOrdersBySubJob(subJob);
        return ResponseEntity.ok().body(MapperOrder.INSTANCE.listOrderCustomerTOrderCustomerDto(orderCustomers));
    }

}

