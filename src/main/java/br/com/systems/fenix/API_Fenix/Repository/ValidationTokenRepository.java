package br.com.systems.fenix.API_Fenix.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.systems.fenix.API_Fenix.Model.ValidationToken;
import jakarta.transaction.Transactional;

@Repository
public interface ValidationTokenRepository extends JpaRepository<ValidationToken, Long> {
    ValidationToken findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM ValidationToken v WHERE v.Client.id = :clientId")
    void deleteByClientId(Long clientId);
}
