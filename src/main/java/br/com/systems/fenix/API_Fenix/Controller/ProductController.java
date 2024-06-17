package br.com.systems.fenix.API_Fenix.Controller;

import br.com.systems.fenix.API_Fenix.Model.Product;
import br.com.systems.fenix.API_Fenix.Service.ProductService;
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
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        Product product = this.productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("name/{name}")
    public ResponseEntity<Product> findByName(@PathVariable String name) {
        Product byProductName = this.productService.findByName(name);
        return ResponseEntity.ok(byProductName);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> findAll() {
        List<Product> allProducts = this.productService.findAllProducts();
        return ResponseEntity.ok(allProducts);
    }

    @PostMapping
    @Validated
    public ResponseEntity<Void> create(@RequestBody Product product) {
        this.productService.save(product);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(product.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PostMapping("/all")
    @Validated
    public ResponseEntity<Void> createAll(@RequestBody List<Product> products) {
        this.productService.save(products);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        this.productService.update(product);
        return ResponseEntity.ok("Product updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        this.productService.delete(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
