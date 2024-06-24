package br.com.systems.fenix.API_Fenix.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import br.com.systems.fenix.API_Fenix.response.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

  private ErrorResponse creatErrorResponse(HttpStatus status, String message, String parameter, WebRequest request,
      Exception ex) {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    String stackTrace = sw.toString();

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(status.value())
        .error(status.getReasonPhrase())
        .message(ex.getMessage() + parameter)
        .path(request.getDescription(false))
        .exception(ex.getClass().getName())
        .trace(stackTrace)
        .build();

    return errorResponse;
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    String stackTrace = sw.toString();

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .message(ex.getMessage())
        .path(request.getDescription(true))
        .trace(stackTrace)
        .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ClientIdNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleClientIdNotFoundException(ClientIdNotFoundException ex,
      WebRequest request) {

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    String stackTrace = sw.toString();

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.NOT_FOUND.value())
        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
        .message(ex.getMessage() + "Id: " + ex.getId())
        .path(request.getDescription(false))
        .exception(ex.getClass().getName())
        .trace(stackTrace)
        .build();
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ClientNameNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleClientNameNotFoundException(ClientNameNotFoundException ex,
      WebRequest request) {
    ErrorResponse errorResponse = creatErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getName(), request, ex);
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ClientEmailNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleClientEmailNotFoundException(ClientEmailNotFoundException ex,
      WebRequest request) {
    ErrorResponse errorResponse = creatErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getEmail(), request,
        ex);
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

  }

  @ExceptionHandler(ClientsNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleClientsNotFoundException(ClientsNotFoundException ex, WebRequest request) {
    ErrorResponse errorResponse = creatErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "", request, ex);
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }
}
