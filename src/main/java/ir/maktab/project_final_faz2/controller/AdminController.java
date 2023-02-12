package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.AdminRequestDto;
import ir.maktab.project_final_faz2.data.model.dto.BasicJobDto;
import ir.maktab.project_final_faz2.data.model.dto.ExpertDto;
import ir.maktab.project_final_faz2.data.model.dto.SubJobDto;
import ir.maktab.project_final_faz2.data.model.entity.BasicJob;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.Person;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.mapper.MapperServices;
import ir.maktab.project_final_faz2.mapper.MapperUsers;
import ir.maktab.project_final_faz2.service.AdminServiceImpl;
import ir.maktab.project_final_faz2.service.BasicJubServiceImpl;
import ir.maktab.project_final_faz2.service.ExpertServiceImpl;
import ir.maktab.project_final_faz2.service.SubJobServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {

    private final SubJobServiceImpl subJobService;

    private final BasicJubServiceImpl basicJubService;

    private final ExpertServiceImpl expertService;

    private final AdminServiceImpl adminService;

    public AdminController(SubJobServiceImpl subJobService, BasicJubServiceImpl basicJubService, ExpertServiceImpl expertService, AdminServiceImpl adminService) {
        this.subJobService = subJobService;
        this.basicJubService = basicJubService;
        this.expertService = expertService;
        this.adminService = adminService;
    }

    @PostMapping("/save_subJobServices")
    public ResponseEntity<String> saveSubJobs(@Valid  @RequestBody SubJobDto subJobDto) {
        SubJob subJob = subJobService.saveSubJob(MapperServices.INSTANCE.subJobDtoToSubJob(subJobDto));
        return ResponseEntity.ok().body("save " + subJob.getSubJobName() + " ok");
    }

    @PostMapping("/save_basicJob")
    public ResponseEntity<String> saveBasicJob(@Valid @RequestBody BasicJobDto basicJobDto) {
        BasicJob basicJob = basicJubService.save(MapperServices.INSTANCE.basicJobDtoToBasicJob(basicJobDto));
        return ResponseEntity.ok().body("save " + basicJob.getNameBase() + " ok");
    }
    @PutMapping("/add_expert_to_subservice")
    public ResponseEntity<String> addExpertToSubService(@RequestParam String userName, @RequestParam String subJobName){
        Expert expert=expertService.findByUserName(userName);
        SubJob subJob=subJobService.findSubJobByName(subJobName);
        adminService.addExpertToSubJob(expert,subJob);
        return ResponseEntity.ok().body("Successfully added");
    }
    @PutMapping("/delete_expert_to_subservice")
    public ResponseEntity<String> deleteExpertToSubService(@RequestParam String userName, @RequestParam String subJobName){
        Expert expert=expertService.findByUserName(userName);
        SubJob subJob=subJobService.findSubJobByName(subJobName);
        adminService.deleteExpertOfSubJob(expert,subJob);
        return ResponseEntity.ok().body("Successfully delete");
    }
    @GetMapping("/view_all_unapproved_specialists")
    public ResponseEntity<List<ExpertDto>> findAllUnapprovedExpert(){
        List<Expert> expertUnapproved=expertService.findAllExpertsIsNotConfirm();
        return ResponseEntity.ok().body(MapperUsers.INSTANCE.listExpertToExpertDto(expertUnapproved));
    }
    @GetMapping("/view_all_approved_specialists")
    public ResponseEntity<List<ExpertDto>> findAllApprovedExpert(){
        List<Expert> expertList=expertService.findAllExpertsApproved();
        List<ExpertDto> expertDto = MapperUsers.INSTANCE.listExpertToExpertDto(expertList);

        return ResponseEntity.ok().body(expertDto);
    }
    @PutMapping("/confirm_Expert")
    public ResponseEntity<String> confirmExpert(@RequestParam String userName){
        adminService.isConfirmExpertByAdmin(userName);
        return ResponseEntity.ok().body("Successfully confirm expert "+userName+"!!!" );
    }
    @GetMapping("/find-person")
    public String findPerson(@RequestBody AdminRequestDto requestAdmin){
         adminService.findAllPerson(requestAdmin);
         return "ok";
    }


}


