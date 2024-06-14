package org.sii.performance;

import org.sii.performance.exception.ActionNotFoundException;
import org.sii.performance.exception.BilanNotFoundException;
import org.sii.performance.exception.ObjectifNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(BilanNotFoundException.class)
    public ResponseEntity<?> handleBilanNotFoundException(RuntimeException bilanNotFound){
        return new ResponseEntity<>(bilanNotFound.getMessage(), HttpStatus.NOT_FOUND) ;
    }

    @ExceptionHandler(ObjectifNotFoundException.class)
    public ResponseEntity<?> handleObjectifNotFoundException(RuntimeException objectifNotFound){
        return  new ResponseEntity<>(objectifNotFound.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ActionNotFoundException.class)
    public ResponseEntity<?> handleActionNotFoundException(RuntimeException actionNotFound){
        return  new ResponseEntity<>(actionNotFound.getMessage(), HttpStatus.NOT_FOUND);
    }
}
