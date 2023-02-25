package ir.maktab.project_final_faz2.mapper;

import ir.maktab.project_final_faz2.data.model.dto.respons.OffersDto;
import ir.maktab.project_final_faz2.data.model.dto.respons.OffersResponseDto;
import ir.maktab.project_final_faz2.data.model.entity.Offers;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MapperOffer {
    MapperOffer INSTANCE = Mappers.getMapper(MapperOffer.class);

    @Mapping(source = "startTime", target = "startTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    OffersDto offerToOfferDto(Offers offers);

    @Mapping(source = "startTime", target = "startTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Offers offerDtoToOffer(OffersDto offersDto);

    List<OffersDto> listOfferToOfferDto(List<Offers> list);

    OffersResponseDto offersToOffersResponseDto(Offers offers);
}
