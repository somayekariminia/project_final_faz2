package ir.maktab.project_final_faz2.data.model.dto;


import ir.maktab.project_final_faz2.data.model.entity.Review;
import ir.maktab.project_final_faz2.data.model.entity.SubJob;
import ir.maktab.project_final_faz2.data.model.enums.SpecialtyStatus;

import java.util.ArrayList;
import java.util.List;

public class ExpertDto extends PersonDto {

    List<SubJob> servicesList = new ArrayList<>();

    List<Review> listComment = new ArrayList<>();

    private double performance;

    private SpecialtyStatus specialtyStatus;

    private byte[] expertImage;

}
