package ir.maktab.project_final_faz2.exception.global;

import org.springframework.http.HttpStatus;

public record CustomException(HttpStatus httpStatus, String message) {
}
