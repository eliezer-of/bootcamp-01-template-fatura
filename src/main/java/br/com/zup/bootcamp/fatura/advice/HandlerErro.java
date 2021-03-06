package br.com.zup.bootcamp.fatura.advice;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestControllerAdvice
public class HandlerErro{
    private final Logger log = LoggerFactory.getLogger(HandlerErro.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroPadronizado> handle(MethodArgumentNotValidException methodArgumentNotValidException) {

        Collection<String> mensagens = new ArrayList<>();
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        fieldErrors.forEach(fieldError -> {
            String message = String.format("Campo '%s': %s", fieldError.getField(), fieldError.getDefaultMessage());
            mensagens.add(message);
        });

        ErroPadronizado erroPadronizado = new ErroPadronizado(mensagens);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroPadronizado);
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<ErroPadronizado> handleConstraintViolation(ConstraintViolationException ex) {
        Collection<String> mensagens = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String message = "O valor informado para " + violation.getPropertyPath() + ": " + violation.getMessage();
            mensagens.add(message);
        }

        ErroPadronizado erroPadronizado = new ErroPadronizado(mensagens);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroPadronizado);
    }

}

