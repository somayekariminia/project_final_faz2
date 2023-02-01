package ir.maktab.project_final_faz2.controller;

import ir.maktab.project_final_faz2.service.SubJobServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @Autowired
    private SubJobServiceImpl subJobService;
}
