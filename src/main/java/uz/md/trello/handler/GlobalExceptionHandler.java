package uz.md.trello.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.md.trello.exceptions.GenericNotFoundException;
import uz.md.trello.exceptions.NotAllowedException;
import uz.md.trello.exceptions.NotDeletedException;
import uz.md.trello.response.ApiErrorResponse;
import uz.md.trello.response.ApiResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author "Elmurodov Javohir"
 * @since 19/08/22/17:14 (Friday)
 * spring-boot-features/IntelliJ IDEA
 */

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(GenericNotFoundException.class)
    public ApiResponse<ApiErrorResponse> handle404(GenericNotFoundException e, HttpServletRequest request) {
        return new ApiResponse<>(ApiErrorResponse.builder()
                .friendlyMessage(e.getMessage())
                .developerMessage(Arrays.toString(e.getStackTrace()))
                .requestPath(request.getRequestURL().toString())
                .build(),
                e.getStatusCode());
    }

    @ExceptionHandler(NotAllowedException.class)
    public ApiResponse<ApiErrorResponse> handle405(NotAllowedException e, HttpServletRequest request) {
        return new ApiResponse<>(ApiErrorResponse.builder()
                .friendlyMessage(e.getMessage())
                .developerMessage(Arrays.toString(e.getStackTrace()))
                .requestPath(request.getRequestURL().toString())
                .build(),
                e.getStatusCode());
    }

    @ExceptionHandler(NotDeletedException.class)
    public ApiResponse<ApiErrorResponse> notDeleted(NotDeletedException e, HttpServletRequest request) {
        return new ApiResponse<>(ApiErrorResponse.builder()
                .friendlyMessage(e.getMessage())
                .developerMessage(Arrays.toString(e.getStackTrace()))
                .requestPath(request.getRequestURL().toString())
                .build(),
                e.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, Object> errors = new HashMap<>();
        BindingResult bindingResult = ex.getBindingResult();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        String requestURI = request.getRequestURI();
        return new ResponseEntity<>(ApiErrorResponse
                .builder()
                .friendlyMessage("Invalid Params Provided")
                .errorFields(errors)
                .requestPath(requestURI)
                .build(), HttpStatus.BAD_REQUEST);
    }
}
