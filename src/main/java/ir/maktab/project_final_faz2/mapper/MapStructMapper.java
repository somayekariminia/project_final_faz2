package ir.maktab.project_final_faz2.mapper;

import ir.maktab.project_final_faz2.data.model.dto.*;
import ir.maktab.project_final_faz2.data.model.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapStructMapper {
    MapStructMapper INSTANCE = Mappers.getMapper(MapStructMapper.class);

    BasicJobDto basicJobToBasicJobDto(BasicJob basicJob);

    BasicJob basicJobDtoToBasicJob(BasicJobDto basicJobDto);

    SubJobDto subJubToSubJobDto(SubJob subJob);

    SubJob subJobDtoToSubJob(SubJobDto subJobDto);

    OffersDto offerToOfferDto(Offers offers);

    Offers offerDtoToOffer(OffersDto offersDto);

    ExpertDto expertToExpertDto(Expert expert);

    Expert expertDtoToExpert(ExpertDto expertDto);

    CustomerDto customerToCustomerDto(Customer customer);

    Customer customerDtoToCustomer(CustomerDto customerDto);

    OrderCustomerDto orderCustomerToOrderCustomerDto(OrderCustomer orderCustomer);

    OrderCustomer orderCustomerDtoToOrderCustomer(OrderCustomerDto orderCustomerDto);

    ReviewDto reviewToReviewDto(Review review);

    Review reviewDtoToReview(ReviewDto reviewDto);

    PersonDto personToPersonDto(Person person);

    PersonDto personDtoToPerson(PersonDto personDto);

}
