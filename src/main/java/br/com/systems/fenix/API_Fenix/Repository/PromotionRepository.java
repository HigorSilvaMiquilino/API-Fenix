package br.com.systems.fenix.API_Fenix.Repository;

import br.com.systems.fenix.API_Fenix.Model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    Promotion findByPromotionName(String name);

    Promotion save(List<Promotion> promotions);
}
