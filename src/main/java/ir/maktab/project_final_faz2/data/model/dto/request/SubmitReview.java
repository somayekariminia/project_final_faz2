package ir.maktab.project_final_faz2.data.model.dto.request;

import ir.maktab.project_final_faz2.data.model.dto.respons.ReviewDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SubmitReview {
    private Long orderId;

    private ReviewDto reviewDto;
}
