package ir.maktab.project_final_faz2.data.model.enums.exception;

import org.springframework.http.HttpStatus;

public record CustomException(HttpStatus httpStatus, String message) {
}
