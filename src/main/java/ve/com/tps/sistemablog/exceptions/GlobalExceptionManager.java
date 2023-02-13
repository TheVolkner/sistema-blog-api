package ve.com.tps.sistemablog.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ve.com.tps.sistemablog.dto.ErrorDetalles;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
//ESTA CLASE NOS SERVIRÁ PARA CONTROLAR LAS EXCEPCIONES QUE SE DISPAREN EN LA APLICACIÓN, Y ENVIAR UNA RESPUESTA JSON AL CLIENTE.
public class GlobalExceptionManager extends ResponseEntityExceptionHandler {

    //CONTRLAMOS RESOURCE NOT FOUND EXCEPTION
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetalles> manejarResourceNotFoundException(ResourceNotFoundException resource, WebRequest webRequest){

        ErrorDetalles detalles = new ErrorDetalles(LocalDateTime.now(),resource.getMessage(),webRequest.getDescription(false));

        return new ResponseEntity<>(detalles, HttpStatus.NOT_FOUND);
    }

    //ESTA LA COLOCAREMOS DE ULTIMO, Y SERÁ PARA CONTROLAR CUALQUIER EXCEPCIÓN QUE SE DISPARE PERO NO ESPECIFICANDO
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetalles> manejarGlobalException(Exception resource, WebRequest webRequest){

        ErrorDetalles detalles = new ErrorDetalles(LocalDateTime.now(),resource.getMessage(),webRequest.getDescription(false));


        return new ResponseEntity<>(detalles, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String,String> errores = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String nombreError = ((FieldError) error).getField();
            String mensajeError = error.getDefaultMessage();

            errores.put(nombreError,mensajeError);
        });

        return new ResponseEntity<>(errores,HttpStatus.BAD_REQUEST);
    }
}
