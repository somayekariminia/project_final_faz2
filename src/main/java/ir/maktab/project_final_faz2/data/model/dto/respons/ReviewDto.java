package ir.maktab.project_final_faz2.data.model.dto.respons;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class ReviewDto {

    private Long id;
   @Range(min = 1 ,max =5 ,message = "please enter a number between 1 to 5  ")
    private int rating;

    private String comment;
}
