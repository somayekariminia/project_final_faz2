package ir.maktab.project_final_faz2.data.model.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InfoCardDto {
    @NotNull
    @Pattern(regexp = "[0-9]{16}", message = "numberCard must number and length 16 !!!")
    private String numberCard;

    @NotNull
    @Pattern(regexp = "[0-9]{3,4}", message = "numberCard must number and length 3 or 4 !!!")
    private String cvv2;

    @Pattern(regexp = "[0-9]{4}/[0-9]{2}", message = "date enter to form yyyy/MM")
    private String dateExpired;

    private String captcha;

    private String orderId;
}
