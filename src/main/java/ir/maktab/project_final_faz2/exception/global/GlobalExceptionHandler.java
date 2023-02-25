package ir.maktab.project_final_faz2.exception.global;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import ir.maktab.project_final_faz2.config.MessageSourceConfiguration;
import ir.maktab.project_final_faz2.exception.*;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.format.DateTimeParseException;


@ControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    MessageSourceConfiguration messageSource;

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundExceptionHandler(NotFoundException e) {
        CustomException exception = new CustomException(HttpStatus.NOT_FOUND, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(NotAcceptedException.class)
    public ResponseEntity<?> notAcceptExceptionHandler(NotAcceptedException e) {
        CustomException exception = new CustomException(HttpStatus.FOUND, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(PhotoValidationException.class)
    public ResponseEntity<?> photoValidationExceptionHandler(PhotoValidationException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> validationExceptionHandler(ValidationException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<?> duplicateExceptionHandler(DuplicateException e) {
        CustomException exception = new CustomException(HttpStatus.FOUND, e.getMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(TimeOutException.class)
    public ResponseEntity<?> timeOutExceptionHandler(TimeOutException e) {
        CustomException exception = new CustomException(HttpStatus.REQUEST_TIMEOUT, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(Insufficient.class)
    public ResponseEntity<?> insufficientExceptionHandler(Insufficient e) {
        CustomException exception = new CustomException(HttpStatus.REQUEST_TIMEOUT, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(CaptchaException.class)
    public ResponseEntity<?> captchaExceptionHandler(CaptchaException e) {
        CustomException exception = new CustomException(HttpStatus.REQUEST_TIMEOUT, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> boundedExceptionHandler(MethodArgumentNotValidException e) {
        String[] error = e.getMessage().split(";");
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, error[error.length - 1]);
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> NullExceptionHandler() {
        CustomException exception = new CustomException(HttpStatus.NOT_FOUND, "object is null");
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(NullObjects.class)
    public ResponseEntity<?> NullObjectExceptionHandler(NullObjects e) {
        CustomException exception = new CustomException(HttpStatus.NOT_FOUND, e.getMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<?> handleException(DataIntegrityViolationException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(UnexpectedRollbackException.class)
    ResponseEntity<?> handleException(UnexpectedRollbackException e) {
        CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(DateTimeParseException.class)
    ResponseEntity<?> dateParsHandleException(DateTimeParseException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(StringIndexOutOfBoundsException.class)
    ResponseEntity<?> handleException(StringIndexOutOfBoundsException e) {
        CustomException exception = new CustomException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, e.getMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(MismatchedInputException.class)
    ResponseEntity<?> handleException(MismatchedInputException e) {
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    ResponseEntity<?> handleException(HttpRequestMethodNotSupportedException e) {
        CustomException exception = new CustomException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    // if media is not json, for example it is xml or text
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    ResponseEntity<?> handleException(HttpMediaTypeNotSupportedException e) {
        CustomException exception = new CustomException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    ResponseEntity<?> handleExceptionFileSize(FileSizeLimitExceededException e) {
        CustomException exception = new CustomException(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, e.getMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<?> handleException(HttpMessageNotReadableException e) {
        CustomException exception = new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    ResponseEntity<?> handleException(NoHandlerFoundException e) {
        CustomException exception = new CustomException(HttpStatus.EXPECTATION_FAILED, e.getLocalizedMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    // miss RequestAttribute
    @ExceptionHandler(ServletRequestBindingException.class)
    ResponseEntity<?> handleException(ServletRequestBindingException e) {
        CustomException exception = new CustomException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, e.getMessage());
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

    @ExceptionHandler(BindException.class)
    ResponseEntity<?> handleException(BindException e) {
        String[] error = e.getMessage().split(";");
        CustomException exception = new CustomException(HttpStatus.BAD_REQUEST, error[error.length - 1]);
        return new ResponseEntity<>(exception, exception.httpStatus());
    }

}

