package br.com.systems.fenix.API_Fenix.exception;

public class CouponNotFoundException extends RuntimeException {

    public CouponNotFoundException(String message) {
        super(message);
    }
}
