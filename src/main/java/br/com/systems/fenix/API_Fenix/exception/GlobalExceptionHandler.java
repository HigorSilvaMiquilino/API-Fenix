package br.com.systems.fenix.API_Fenix.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import br.com.systems.fenix.API_Fenix.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler implements AuthenticationFailureHandler {

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

  @ExceptionHandler(AuthorizationException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseEntity<Object> handleAuthorizationException(AuthenticationException ex,
      AccessDeniedException accessDeniedException, WebRequest request) {

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    String stackTrace = sw.toString();

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.FORBIDDEN.value())
        .error(HttpStatus.FORBIDDEN.getReasonPhrase())
        .message(ex.getMessage())
        .path(request.getDescription(false))
        .exception(ex.getClass().getName())
        .trace(stackTrace)
        .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
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

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    Integer status = HttpStatus.UNAUTHORIZED.value();
    response.setStatus(status);
    response.setContentType("application/json");
    ErrorResponse errorResponse = ErrorResponse.builder().status(status).message("Invalid e-mail or password").build();
    response.getWriter().append(errorResponse.toJson());
  }
}
