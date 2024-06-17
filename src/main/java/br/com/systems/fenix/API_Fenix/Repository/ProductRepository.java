package br.com.systems.fenix.API_Fenix.Repository;

import br.com.systems.fenix.API_Fenix.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByProductName(String name);

    Product save(List<Product> products);
}
