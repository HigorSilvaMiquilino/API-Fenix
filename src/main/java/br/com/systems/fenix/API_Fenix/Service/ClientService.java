package br.com.systems.fenix.API_Fenix.Service;

import br.com.systems.fenix.API_Fenix.Model.Client;
import br.com.systems.fenix.API_Fenix.Repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client findById(Long id) {
        Optional<Client> client = this.clientRepository.findById(id);
        return client
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found with " + id));
    }

    public Client findByName(String name) {
        return this.clientRepository.findByFirstName(name);
    }

    public Client findByEmail(String email) {
        return this.clientRepository.findByEmail(email);
    }

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    @Transactional
    public Client save(Client client) {
        Client clientBuilt = Client.builder()
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .age(client.getAge())
                .telephone(client.getTelephone())
                .email(client.getEmail())
                .password(client.getPassword())
                .imageUrl(client.getImageUrl())
                .promotions(client.getPromotions())
                .build();
        this.clientRepository.save(client);
        return clientBuilt;
    }

    @Transactional
    public void save(List<Client> clients) {
        for (Client client : clients) {
            Client ClientsBuilt = Client.builder()
                    .firstName(client.getFirstName())
                    .lastName(client.getLastName())
                    .age(client.getAge())
                    .telephone(client.getTelephone())
                    .email(client.getEmail())
                    .password(client.getPassword())
                    .imageUrl(client.getImageUrl())
                    .promotions(client.getPromotions())
                    .build();
            this.clientRepository.save(ClientsBuilt);
        }
    }

    @Transactional
    public Optional<Client> update(Client client) {
        try {
            Optional<Client> clientToUpdate = clientRepository.findById(client.getId());
            if (clientToUpdate.isPresent()) {
                Client existingClient = clientToUpdate.get();
                existingClient.setFirstName(client.getFirstName());
                existingClient.setLastName(client.getLastName());
                existingClient.setAge(client.getAge());
                existingClient.setTelephone(client.getTelephone());
                existingClient.setEmail(client.getEmail());
                existingClient.setPassword(client.getPassword());
                existingClient.setEmail(client.getEmail());
                Client clientUpdated = clientRepository.save(existingClient);
                return Optional.of(clientUpdated);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update client: " + e.getMessage());
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            clientRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete client: " + e.getMessage());
        }
    }

    public boolean validateClient(String email, String password) {
        Client client = clientRepository.findByEmail(email);
        return client != null && client.getPassword().equals(password);
    }
}
