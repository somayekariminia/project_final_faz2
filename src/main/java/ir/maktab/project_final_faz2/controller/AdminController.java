package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.BasicJobDto;
import ir.maktab.project_final_faz2.data.model.dto.ExpertDto;
import ir.maktab.project_final_faz2.data.model.dto.SubJobDto;
import ir.maktab.project_final_faz2.data.model.entity.BasicJob;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.mapper.MapStructMapper;
import ir.maktab.project_final_faz2.service.AdminServiceImpl;
import ir.maktab.project_final_faz2.service.BasicJubServiceImpl;
import ir.maktab.project_final_faz2.service.ExpertServiceImpl;
import ir.maktab.project_final_faz2.service.SubJobServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminController {
    @Autowired
    private SubJobServiceImpl subJobService;
    @Autowired
    private BasicJubServiceImpl basicJubService;
    @Autowired
    private ExpertServiceImpl expertService;
    @Autowired
    private AdminServiceImpl adminService;

    @PostMapping("/save_subJobServices")
    public ResponseEntity<String> saveSubJobs(@RequestBody SubJobDto subJobDto) {
        SubJob subJob = subJobService.saveSubJob(MapStructMapper.INSTANCE.subJobDtoToSubJob(subJobDto));
        return ResponseEntity.ok().body("save " + subJob.getSubJobName() + " ok");
    }

    @PostMapping("/save_basicJob")
    public ResponseEntity<String> saveBasicJob(@RequestBody BasicJobDto basicJobDto) {
        BasicJob basicJob = basicJubService.save(MapStructMapper.INSTANCE.basicJobDtoToBasicJob(basicJobDto));
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
        return ResponseEntity.ok().body(MapStructMapper.INSTANCE.listExpertToExpertDto(expertUnapproved));
    }
    @GetMapping("/view_all_approved_specialists")
    public ResponseEntity<List<ExpertDto>> findAllApprovedExpert(){
        List<Expert> expertList=expertService.findAllExpertsApproved();
        List<ExpertDto> expertDto = MapStructMapper.INSTANCE.listExpertToExpertDto(expertList);

        return ResponseEntity.ok().body(expertDto);
    }
    @PutMapping("/confirm_Expert")
    public ResponseEntity<String> confirmExpert(@RequestParam String userName){
        adminService.isConfirmExpertByAdmin(userName);
        return ResponseEntity.ok().body("Successfully confirm expert "+userName+"!!!" );
    }

}


