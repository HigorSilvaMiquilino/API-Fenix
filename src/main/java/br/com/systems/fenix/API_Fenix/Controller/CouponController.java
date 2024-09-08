package br.com.systems.fenix.API_Fenix.Controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.systems.fenix.API_Fenix.DTO.CouponDTO;
import br.com.systems.fenix.API_Fenix.Model.Coupon;
import br.com.systems.fenix.API_Fenix.Service.CouponService;
import br.com.systems.fenix.API_Fenix.response.ResponseCoupon;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@Validated
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    CouponService couponService;

    @GetMapping("/{id}")
    public ResponseEntity<Coupon> findById(@PathVariable Long id) {
        Coupon byId = this.couponService.findById(id);
        return ResponseEntity.ok(byId);

    }

    @GetMapping("/CNPJ/{CNPJ}")
    public ResponseEntity<Coupon> findByCNPJ(@PathVariable String CNPJ) {
        Coupon byId = this.couponService.findByCNPJ(CNPJ);
        return ResponseEntity.ok(byId);

    }

    @GetMapping("/coupomNumbeString/{coupomNumbeString}")
    public ResponseEntity<Coupon> findByCoupomNumbeString(@PathVariable String coupomNumbeString) {
        Coupon byCoupon = this.couponService.findByCouponNumber(coupomNumbeString);
        return ResponseEntity.ok(byCoupon);

    }

    @GetMapping("/CouponClient/{id}")
    public ResponseEntity<List<Coupon>> findByCoupomClient(@PathVariable Long id) {
        List<Coupon> byClient = this.couponService.findAllByClientID(id);
        return ResponseEntity.ok(byClient);

    }

    @GetMapping("/all")
    public ResponseEntity<List<Coupon>> findAll() {
        List<Coupon> allCoupons = this.couponService.findAllCoupons();
        return ResponseEntity.ok(allCoupons);
    }

    @PostMapping
    @Validated
    public ResponseEntity<ResponseCoupon> save(@RequestBody CouponDTO coupon) {
        System.out.println("testing here in the controller " + coupon.getCNPJ());
        Coupon couponSaved = this.couponService.save(coupon);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(coupon.getClientId())
                .toUri();
        ResponseCoupon responseCoupon = ResponseCoupon.builder()
                .timeStamp(LocalDateTime.now().toString())
                .statusCode(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .message("Coupon saved successfully")
                .coupon(couponSaved)
                .build();
        return ResponseEntity.created(uri).body(responseCoupon);

    }

}
