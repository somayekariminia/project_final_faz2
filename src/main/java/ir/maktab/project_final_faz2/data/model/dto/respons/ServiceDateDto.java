package ir.maktab.project_final_faz2.data.model.dto.respons;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ServiceDateDto {

    private SubJobDtoRes subJob;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private OffersResponseDto offersResponseDto;
}
