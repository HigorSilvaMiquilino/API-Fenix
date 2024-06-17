package br.com.systems.fenix.API_Fenix.Controller;

import br.com.systems.fenix.API_Fenix.Model.Promotion;
import br.com.systems.fenix.API_Fenix.Service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Validated
@RequestMapping("/promotion")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @GetMapping("/{id}")
    public ResponseEntity<Promotion> findById(@PathVariable Long id) {
        Promotion promotion = this.promotionService.findById(id);
        return ResponseEntity.ok(promotion);
    }

    @GetMapping("name/{name}")
    public ResponseEntity<Promotion> findByName(@PathVariable String name) {
        Promotion byPromotionName = this.promotionService.findByPromotionName(name);
        return ResponseEntity.ok(byPromotionName);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Promotion>> find() {
        List<Promotion> allPromotions = this.promotionService.findAllPromotions();
        return ResponseEntity.ok(allPromotions);
    }

    @PostMapping
    @Validated
    public ResponseEntity<Void> create(@RequestBody Promotion promotion) {
        this.promotionService.save(promotion);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(promotion.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/all")
    @Validated
    public ResponseEntity<Void> createAll(@RequestBody List<Promotion> promotions) {
        this.promotionService.save(promotions);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Promotion promotion) {
        promotion.setId(id);
        this.promotionService.update(promotion);
        return ResponseEntity.ok("Promotion updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        this.promotionService.delete(id);
        return ResponseEntity.ok("Promotion deleted successfully");
    }
}
