package ir.maktab.project_final_faz2.service.serviceImpl;

import ir.maktab.project_final_faz2.config.MessageSourceConfiguration;
import ir.maktab.project_final_faz2.data.model.entity.Expert;
import ir.maktab.project_final_faz2.data.model.entity.OrderCustomer;
import ir.maktab.project_final_faz2.data.model.entity.Review;
import ir.maktab.project_final_faz2.data.model.enums.OrderStatus;
import ir.maktab.project_final_faz2.data.model.repository.ReviewRepository;
import ir.maktab.project_final_faz2.exception.NullObjects;
import ir.maktab.project_final_faz2.exception.TimeOutException;
import ir.maktab.project_final_faz2.exception.ValidationException;
import ir.maktab.project_final_faz2.service.serviceInterface.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ExpertServiceImpl expertService;
    private final OrderCustomerServiceImpl orderCustomerService;
    private final ReviewRepository reviewRepository;
    private final OfferServiceImpl offerService;
    private final MessageSourceConfiguration messageSource;
    @Override
    public Review save(Review review) {
        if (Objects.isNull(review))
            throw new NullObjects(messageSource.getMessage("errors.message.null-object"));
        return reviewRepository.save(review);
    }

    @Override
    @Transactional
    public void giveScoreToExpert(OrderCustomer orderCustomer, Review review) {
        OrderCustomer orderCustomerDb = orderCustomerService.findById(orderCustomer.getId());
        if (!orderCustomerDb.getOrderStatus().equals(OrderStatus.DoItsBeen))
            throw new TimeOutException(messageSource.getMessage("errors.message.order_isn't_done"));
        Expert expert = offerService.findOffersIsAccept(orderCustomerDb).getExpert();
        double performance = review.getRating() + expert.getPerformance() / 2;
        expert.setPerformance(performance);
        review.setExpert(expert);
        expertService.updateExpert(expert);
        reviewRepository.save(review);
    }

    @Override
    public List<Review> findAllReviewForExpert(Expert expert) {
        if (reviewRepository.findAllByExpert(expert).isEmpty())
            throw new ValidationException(String.format(messageSource.getMessage("errors.message.list_isEmpty")));
        return reviewRepository.findAllByExpert(expert);

    }
}
