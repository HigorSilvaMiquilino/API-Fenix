package br.com.systems.fenix.API_Fenix.exception;

public class CouponNumberNotFoundException extends RuntimeException {

    private String couponNumber;

    public CouponNumberNotFoundException(String message, String couponNumber) {
        super(message);
        this.couponNumber = couponNumber;
    }

    public String getCouponNumber() {
        return this.couponNumber;
    }

}
