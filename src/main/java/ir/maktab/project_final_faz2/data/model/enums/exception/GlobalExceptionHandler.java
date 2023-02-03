package ir.maktab.project_final_faz2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundExceptionHandler(NotFoundException e) {
        CustomException exception = new CustomException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }
    @ExceptionHandler(NotAcceptedException.class)
    public ResponseEntity<?> notAcceptExceptionHandler(NotAcceptedException e) {
        CustomException exception = new CustomException(HttpStatus.NOT_ACCEPTABLE, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }
    @ExceptionHandler(PhotoValidationException.class)
    public ResponseEntity<?> photoValidationExceptionHandler(PhotoValidationException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> validationExceptionHandler(ValidationException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_GATEWAY, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<?> duplicateExceptionHandler(DuplicateException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_GATEWAY, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }
    @ExceptionHandler(TimeOutException.class)
    public ResponseEntity<?> timeOutExceptionHandler(TimeOutException e) {
        CustomException exception = new CustomException(HttpStatus.REQUEST_TIMEOUT, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }
}

