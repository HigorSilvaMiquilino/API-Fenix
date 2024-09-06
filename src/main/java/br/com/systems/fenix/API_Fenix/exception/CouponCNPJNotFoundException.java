package br.com.systems.fenix.API_Fenix.exception;

public class CouponCNPJNotFoundException extends RuntimeException {

    String CNPJ;

    public CouponCNPJNotFoundException(String message, String CNPJ) {
        super(message);
        this.CNPJ = CNPJ;
    }

    public String getCNPJ() {
        return this.CNPJ;
    }
}
