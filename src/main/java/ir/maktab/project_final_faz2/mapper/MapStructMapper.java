package ir.maktab.project_final_faz2.mapper;

import ir.maktab.project_final_faz2.data.model.dto.respons.ReviewDto;
import ir.maktab.project_final_faz2.data.model.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapStructMapper {
    MapStructMapper INSTANCE = Mappers.getMapper(MapStructMapper.class);

    ReviewDto reviewToReviewDto(Review review);

    Review reviewDtoToReview(ReviewDto reviewDto);

}
