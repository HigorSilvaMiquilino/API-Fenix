package br.com.systems.fenix.API_Fenix.Service;

import br.com.systems.fenix.API_Fenix.Model.Promotion;
import br.com.systems.fenix.API_Fenix.Repository.PromotionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    public Promotion findById(Long id) {
        Optional<Promotion> promotion = this.promotionRepository.findById(id);
        return promotion
                .orElseThrow(() -> new EntityNotFoundException(
                        "Failed to fetch product by ID: " + id
                ));
    }

    public Promotion findByPromotionName(String name) {
        return this.promotionRepository.findByPromotionName(name);
    }

    public List<Promotion> findAllPromotions() {
        return this.promotionRepository.findAll();
    }

    @Transactional
    public Promotion save(Promotion promotion) {
        Promotion promotionBuilt = Promotion.builder()
                .promotionName(promotion.getPromotionName())
                .description(promotion.getDescription())
                .prize(promotion.getPrize())
                .client(promotion.getClient())
                .build();
        this.promotionRepository.save(promotionBuilt);
        return promotionBuilt;
    }


    @Transactional
    public Optional<Promotion> update(Promotion promotion) {
        try {
            Optional<Promotion> promotionToUpdate = promotionRepository.findById(promotion.getId());
            if (promotionToUpdate.isPresent()) {
                Promotion existingPromotion = promotionToUpdate.get();
                existingPromotion.setPromotionName(promotion.getPromotionName());
                existingPromotion.setDescription(promotion.getDescription());
                existingPromotion.setPrize(promotion.getPrize());
                Promotion promotionUpdated = promotionRepository.save(existingPromotion);
                return Optional.of(promotionUpdated);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update product: " + e.getMessage());
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            this.promotionRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete product: " + e.getMessage());
        }
    }
}
