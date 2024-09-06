package br.com.systems.fenix.API_Fenix.response;

import java.util.Map;

import org.springframework.http.HttpStatus;

import br.com.systems.fenix.API_Fenix.Model.Coupon;
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
public class ResponseCoupon {

    private String timeStamp;
    private int statusCode;
    private HttpStatus status;
    private String message;
    private String developerMessage;
    private Coupon coupon;
    private String path;
    private String requestMethod;
    private Map<?, ?> data;
}
