package ir.maktab.project_final_faz2.data.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto {

    private Long id;

    private int rating;

    private String comment;
}
