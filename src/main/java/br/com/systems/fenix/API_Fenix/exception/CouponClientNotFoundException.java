package br.com.systems.fenix.API_Fenix.exception;

public class CouponClientNotFoundException extends RuntimeException {

    private Long id;

    public CouponClientNotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return super.toString() + "[ Coupon Id: " + id + "]";
    }

}
