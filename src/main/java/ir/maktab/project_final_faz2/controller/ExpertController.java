package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.ExpertAndFile;
import ir.maktab.project_final_faz2.data.model.dto.OfferRegistry;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.Offers;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.mapper.MapStructMapper;
import ir.maktab.project_final_faz2.service.ExpertServiceImpl;
import ir.maktab.project_final_faz2.service.OfferServiceImpl;
import ir.maktab.project_final_faz2.service.OrderCustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class ExpertController {
    @Autowired
    private ExpertServiceImpl expertService;
    @Autowired
    private OfferServiceImpl offerService;
    @Autowired
    private OrderCustomerServiceImpl orderCustomerService;

    @PostMapping("/save_expert")
    public ResponseEntity<String> saveExpert(@RequestBody ExpertAndFile expertAndFile) {
        Expert expert = expertService.save(MapStructMapper.INSTANCE.expertDtoToExpert(expertAndFile.getExpertDto()), new File(expertAndFile.getPath()));
        return ResponseEntity.ok().body("save " + expert.getEmail() + " ok");
    }

    @PostMapping("/regist_offer")
    public ResponseEntity<String> saveOffer(@RequestBody OfferRegistry offerRegistry) {
        Offers offerSave = offerService.save(MapStructMapper.INSTANCE.offerDtoToOffer(offerRegistry.getOffersDto()), offerRegistry.getId());
        Expert expert = expertService.findByUserName(offerRegistry.getUserName());
        OrderCustomer orderCustomer = orderCustomerService.findById(offerRegistry.getId());
        offerSave.setExpert(expert);
        offerSave.setOrderCustomer(orderCustomer);
        return ResponseEntity.ok().body("save " + offerSave.getId());
    }
}

