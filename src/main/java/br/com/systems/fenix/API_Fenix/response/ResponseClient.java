package br.com.systems.fenix.API_Fenix.response;

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

    private int status;
    private String message;
    private Client client;

}