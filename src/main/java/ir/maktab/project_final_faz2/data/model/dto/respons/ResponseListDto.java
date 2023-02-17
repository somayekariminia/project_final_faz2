package ir.maktab.project_final_faz2.data.model.dto.respons;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ResponseListDto<T> {

    private String message = "Success";
    private String status = "true";
    private List<T> data = new ArrayList<>();
}
