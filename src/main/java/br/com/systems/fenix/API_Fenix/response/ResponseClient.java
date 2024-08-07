package br.com.systems.fenix.API_Fenix.response;

import java.util.Map;

import org.springframework.http.HttpStatus;

import br.com.systems.fenix.API_Fenix.Model.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseClient {

    private String timeStamp;
    private int statusCode;
    private HttpStatus status;
    private String message;
    private String developerMessage;
    private Client client;
    private String path;
    private String requestMethod;
    private Map<?, ?> data;
}