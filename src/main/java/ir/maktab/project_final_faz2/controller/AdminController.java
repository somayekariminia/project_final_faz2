package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.data.model.dto.request.AdminRequestDto;
import ir.maktab.project_final_faz2.data.model.dto.request.SubJobUpdateDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.*;
import ir.maktab.project_final_faz2.data.model.entity.BasicJob;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.mapper.MapperServices;
import ir.maktab.project_final_faz2.mapper.MapperUsers;
import ir.maktab.project_final_faz2.service.serviceImpl.AdminServiceImpl;
import ir.maktab.project_final_faz2.service.serviceImpl.BasicJubServiceImpl;
import ir.maktab.project_final_faz2.service.serviceImpl.ExpertServiceImpl;
import ir.maktab.project_final_faz2.service.serviceImpl.SubJobServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Admin")
@Validated
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
    public ResponseEntity<String> saveSubJobs(@Valid @RequestBody SubJobDto subJobDto) {
        SubJob subJob = subJobService.saveSubJob(MapperServices.INSTANCE.subJobDtoToSubJob(subJobDto));
        return ResponseEntity.ok().body("save " + subJob.getSubJobName() + " ok");
    }

    @PostMapping("/save_basicJob")
    public ResponseEntity<String> saveBasicJob(@Valid @RequestBody BasicJobDto basicJobDto) {
        BasicJob basicJob = basicJubService.save(MapperServices.INSTANCE.basicJobDtoToBasicJob(basicJobDto));
        ResponseDTO<BasicJobDto> responseDto = new ResponseDTO<>();
        responseDto.setInfo(MapperServices.INSTANCE.basicJobToBasicJobDto(basicJob));
        return ResponseEntity.ok().body("save " + basicJob.getNameBase() + " ok");
    }

    @PostMapping("/update_subService")
    public ResponseEntity<ResponseDTO<SubJobDto>> updateSubJob(@RequestBody SubJobUpdateDto subJobDto) {
        SubJob subJob = subJobService.updateSubJob(MapperServices.INSTANCE.subJobUpdateDtoToSubJob(subJobDto));
        ResponseDTO<SubJobDto> responseDto = new ResponseDTO<>();
        responseDto.setInfo(MapperServices.INSTANCE.subJubToSubJobDto(subJob));
        return ResponseEntity.ok().body(responseDto);
    }

    @PutMapping("/add_expert_to_subService")
    public ResponseEntity<String> addExpertToSubService(@RequestParam String userName, @RequestParam String subJobName) {
        Expert expert = expertService.findByUserName(userName);
        SubJob subJob = subJobService.findSubJobByName(subJobName);
        adminService.addExpertToSubJob(expert, subJob);
        return ResponseEntity.ok().body(subJobName + " Successfully added" + " to " + userName);
    }

    @PutMapping("/delete_expert_Of_subService")
    public ResponseEntity<String> deleteExpertToSubService(@RequestParam String userName, @RequestParam String subJobName) {
        Expert expert = expertService.findByUserName(userName);
        SubJob subJob = subJobService.findSubJobByName(subJobName);
        adminService.deleteExpertOfSubJob(expert, subJob);
        return ResponseEntity.ok().body(subJobName + " Successfully delete" + " of " + userName);
    }

    @GetMapping("/view_all_unapproved_specialists")
    public ResponseEntity<?> findAllUnapprovedExpert() {
        List<Expert> expertUnapproved = expertService.findAllExpertsIsNotConfirm();
        ResponseListDto<ExpertDto> response = new ResponseListDto<>();
        response.setData(MapperUsers.INSTANCE.listExpertToExpertDto(expertUnapproved));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/view_all_approved_specialists")
    public ResponseEntity<?> findAllApprovedExpert() {
        List<Expert> expertList = expertService.findAllExpertsApproved();
        ResponseListDto<ExpertDto> response = new ResponseListDto<>();
        response.setData(MapperUsers.INSTANCE.listExpertToExpertDto(expertList));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/view_basicJob")
    public ResponseEntity<?> findAllBAsicJob() {
        List<BasicJob> listBasicJobs = basicJubService.findAllBasicJobs();
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

    @PutMapping("/confirm_Expert")
    public ResponseEntity<String> confirmExpert(@RequestParam String userName) {
        adminService.isConfirmExpertByAdmin(userName);
        return ResponseEntity.ok().body("ok.Successfully confirm expert " + userName + "!!!");
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@Valid @RequestBody AdminRequestDto requestAdmin) {
        List<PersonDto> allPerson = adminService.search(requestAdmin);
        ResponseListDto<PersonDto> responseListDto = new ResponseListDto<>();
        responseListDto.setData(allPerson);
        return ResponseEntity.ok(responseListDto);
    }

}


