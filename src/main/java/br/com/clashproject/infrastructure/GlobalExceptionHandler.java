package br.com.clashproject.infrastructure;

import br.com.clashproject.core.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // Default status is 500

        // Check the exception code and map it to appropriate status
        if (ex.getExceptionCode() != null) {
            status = HttpStatus.valueOf(ex.getExceptionCode().getHttpStatus());
        }

        // Return the response with the appropriate status and error message
        return ResponseEntity.status(status)
                .body(new ErrorResponse(ex.getExceptionCode().getCode(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("GENERAL_ERROR", "An unexpected error occurred."));
    }

    static class ErrorResponse {
        private final String code;
        private final String message;

        public ErrorResponse(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}