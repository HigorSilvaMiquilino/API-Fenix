package br.com.systems.fenix.API_Fenix.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.systems.fenix.API_Fenix.DTO.CouponDTO;
import br.com.systems.fenix.API_Fenix.Model.Coupon;
import br.com.systems.fenix.API_Fenix.Repository.CouponRepository;
import br.com.systems.fenix.API_Fenix.exception.CouponCNPJNotFoundException;
import br.com.systems.fenix.API_Fenix.exception.CouponClientNotFoundException;
import br.com.systems.fenix.API_Fenix.exception.CouponIdNotFoundException;
import br.com.systems.fenix.API_Fenix.exception.CouponNotFoundException;
import br.com.systems.fenix.API_Fenix.exception.CouponNumberNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    public Coupon findById(Long id) {
        Optional<Coupon> cOptional = this.couponRepository.findById(id);
        return cOptional.orElseThrow(() -> new CouponIdNotFoundException("Coupon not found with id:", id));
    }

    public Coupon findByCNPJ(String CNPJ) {
        Coupon byCNPJ = this.couponRepository.findByCNPJ(CNPJ);
        if (byCNPJ == null) {
            throw new CouponCNPJNotFoundException("Coupon not found with CNPJ:", CNPJ);
        } else {
            return byCNPJ;
        }
    }

    public Coupon findByCouponNumber(String coupomNumbeString) {
        Coupon byCouponNumber = this.couponRepository.findByCouponNumber(coupomNumbeString);
        if (coupomNumbeString == null) {
            throw new CouponNumberNotFoundException("Coupon number not found: ", coupomNumbeString);
        } else {
            return byCouponNumber;
        }

    }

    public List<Coupon> findAllByClientID(Long id) {
        List<Coupon> byClient = this.couponRepository.findAllByClientID(id);
        if (byClient == null) {
            throw new CouponClientNotFoundException("Coupon's client not found: ", id);
        } else {
            return byClient;
        }
    }

    public List<Coupon> findAllCoupons() {
        List<Coupon> allCoupons = this.couponRepository.findAll();
        if (allCoupons.isEmpty()) {
            throw new CouponNotFoundException("There is no coupon");
        } else {
            return allCoupons;
        }
    }

    @Transactional
    public Coupon save(CouponDTO coupon) {
        System.out.println("I want to see if the cnpj get here" + coupon.getCNPJ());
        Coupon couponBuilt = Coupon.builder()
                .CNPJ(coupon.getCNPJ())
                .couponNumber(coupon.getCouponNumber())
                .localDate(LocalDate.parse(coupon.getLocalDate().toString()))
                .clientID(coupon.getClientId())
                .promotionId(coupon.getPromotionId())
                .build();
        System.out.println(couponBuilt);
        this.couponRepository.save(couponBuilt);
        return couponBuilt;
    }

}
