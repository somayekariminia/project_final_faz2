package ir.maktab.project_final_faz2.service.serviceInterface;

import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.Review;

import java.util.List;

public interface ReviewService {
    Review save(Review review);

    void giveScoreToExpert(Long orderCustomerId, Review review);

    List<Review> findAllReviewForExpert(Expert expert);
}
