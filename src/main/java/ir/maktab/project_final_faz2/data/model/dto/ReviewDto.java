package ir.maktab.project_final_faz2.data.model.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
public class ReviewDto {

    private Long id;

    private int rating;

    private String comment;
}
