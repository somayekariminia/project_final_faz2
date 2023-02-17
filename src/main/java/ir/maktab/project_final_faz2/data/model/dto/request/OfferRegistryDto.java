package ir.maktab.project_final_faz2.data.model.dto.request;

import ir.maktab.project_final_faz2.data.model.dto.respons.OffersDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OfferRegistryDto {
    private OffersDto offersDto;

    private Long id;

    private String userName;
}
