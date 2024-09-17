package br.com.systems.fenix.API_Fenix.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.systems.fenix.API_Fenix.Model.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Coupon findByCNPJ(String CNPJ);

    Coupon findByCouponNumber(String coupnNumber);

    List<Coupon> findAllByClientID(Long id);

    int deleteByClientID(Long id);

}
