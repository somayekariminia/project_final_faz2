package ir.maktab.project_final_faz2.mapper;

import ir.maktab.project_final_faz2.data.model.dto.*;
import ir.maktab.project_final_faz2.data.model.entity.*;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.Duration;
import java.util.List;

@Mapper
public interface MapStructMapper {
    MapStructMapper INSTANCE = Mappers.getMapper(MapStructMapper.class);


    ReviewDto reviewToReviewDto(Review review);

    Review reviewDtoToReview(ReviewDto reviewDto);
    Credit creditDtoToCredit(CreditDto creditDto);



}
