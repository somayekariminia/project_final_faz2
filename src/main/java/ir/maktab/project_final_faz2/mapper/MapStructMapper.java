package ir.maktab.project_final_faz2.mapper;

import ir.maktab.project_final_faz2.data.model.dto.*;
import ir.maktab.project_final_faz2.data.model.entity.*;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MapStructMapper {
    MapStructMapper INSTANCE = Mappers.getMapper(MapStructMapper.class);
    List<SubJobDto> subJobListToSubJobDto(List<SubJob> subJobList);
    List<BasicJobDto> ListBasicJobToBasicJobDto(List<BasicJob> list);
    BasicJobDto basicJobToBasicJobDto(BasicJob basicJob);

    BasicJob basicJobDtoToBasicJob(BasicJobDto basicJobDto);

    SubJobDto subJubToSubJobDto(SubJob subJob);

    SubJob subJobDtoToSubJob(SubJobDto subJobDto);
    @Mapping(source="startTime",target = "startTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    OffersDto offerToOfferDto(Offers offers);
    @Mapping(source="startTime",target = "startTime",dateFormat = "yyyy-MM-dd HH:mm:ss")
    Offers offerDtoToOffer(OffersDto offersDto);

    ExpertDto expertToExpertDto(Expert expert);

    Expert expertDtoToExpert(ExpertDto expertDto);
    CustomerDto customerToCustomerDto(Customer customer);

    Customer customerDtoToCustomer(CustomerDto customerDto);
    @Mapping(source = "startDateDoWork",target = "startDateDoWork",dateFormat = "yyyy.MM.dd HH:mm:ss")
    OrderCustomerDto orderCustomerToOrderCustomerDto(OrderCustomer orderCustomer);
    @Mapping(source="startDateDoWork",target = "startDateDoWork",dateFormat = "yyyy-MM-dd HH:mm:ss")
    OrderCustomer orderCustomerDtoToOrderCustomer(OrderCustomerDto orderCustomerDto);

    ReviewDto reviewToReviewDto(Review review);

    Review reviewDtoToReview(ReviewDto reviewDto);
    List<ExpertDto> listExpertToExpertDto(List<Expert> listExpert);


}
