package br.com.systems.fenix.API_Fenix.Repository;

import br.com.systems.fenix.API_Fenix.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByFirstName(String name);

}
