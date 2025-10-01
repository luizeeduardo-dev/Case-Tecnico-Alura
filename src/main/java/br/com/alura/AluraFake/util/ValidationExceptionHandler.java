package br.com.alura.AluraFake.util;

import br.com.alura.AluraFake.infra.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class ValidationExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<List<ErrorItemDTO>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    List<ErrorItemDTO> errors =
        ex.getBindingResult().getFieldErrors().stream().map(ErrorItemDTO::new).toList();
    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorItemDTO> handleBusinessValidationExceptions(ValidationException ex) {
    ErrorItemDTO error = new ErrorItemDTO(null, ex.getMessage());
    return ResponseEntity.badRequest().body(error);
  }
}
