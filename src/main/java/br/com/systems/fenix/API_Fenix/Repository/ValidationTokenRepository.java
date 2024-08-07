package br.com.systems.fenix.API_Fenix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.systems.fenix.API_Fenix.Model.ValidationToken;

@Repository
public interface ValidationTokenRepository extends JpaRepository<ValidationToken, Long> {
    ValidationToken findByToken(String token);
}
