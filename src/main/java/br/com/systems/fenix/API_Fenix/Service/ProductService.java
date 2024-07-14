package br.com.systems.fenix.API_Fenix.Service;

import br.com.systems.fenix.API_Fenix.Model.Product;
import br.com.systems.fenix.API_Fenix.Repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product findById(Long id) {
        Optional<Product> product = this.productRepository.findById(id);
        return product.orElseThrow(() -> new EntityNotFoundException(
                "Product not found with " + id));
    }

    public Product findByName(String name) {
        return this.productRepository.findByProductName(name);
    }

    public List<Product> findAllProducts() {
        return this.productRepository.findAll();
    }

    @Transactional
    public Product save(Product product) {
        Product productBuilt = Product.builder()
                .productName(product.getProductName())
                .price(product.getPrice())
                .promotion(product.getPromotion())
                .build();
        this.productRepository.save(productBuilt);
        return product;
    }

    @Transactional
    public void save(List<Product> products) {
        for (Product product : products) {
            Product promotionBuilt = Product.builder()
                    .productName(product.getProductName())
                    .price(product.getPrice())
                    .promotion(product.getPromotion())
                    .build();
            this.productRepository.save(promotionBuilt);
        }
    }

    @Transactional
    public Optional<Product> update(Product product) {
        try {
            Optional<Product> productToUpdate = productRepository.findById(product.getId());
            if (productToUpdate.isPresent()) {
                Product existingProduct = productToUpdate.get();
                existingProduct.setProductName(product.getProductName());
                existingProduct.setPrice(product.getPrice());
                existingProduct.setPromotion(product.getPromotion());
                Product productUpdated = productRepository.save(existingProduct);
                return Optional.of(productUpdated);
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
            productRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete product: " + e.getMessage());
        }
    }

}
