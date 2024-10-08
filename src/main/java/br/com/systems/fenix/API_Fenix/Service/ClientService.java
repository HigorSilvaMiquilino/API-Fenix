package br.com.systems.fenix.API_Fenix.Service;

import br.com.systems.fenix.API_Fenix.Model.Client;
import br.com.systems.fenix.API_Fenix.Model.PasswordResetToken;
import br.com.systems.fenix.API_Fenix.Model.ValidationToken;
import br.com.systems.fenix.API_Fenix.Model.enuns.ProfileEnum;
import br.com.systems.fenix.API_Fenix.Repository.ClientRepository;
import br.com.systems.fenix.API_Fenix.Repository.CouponRepository;
import br.com.systems.fenix.API_Fenix.Repository.PasswordResetTokenRepository;
import br.com.systems.fenix.API_Fenix.Repository.ValidationTokenRepository;
import br.com.systems.fenix.API_Fenix.Service.impl.EmailServiceImpl;
import br.com.systems.fenix.API_Fenix.exception.AuthorizationException;
import br.com.systems.fenix.API_Fenix.exception.ClientEmailNotFoundException;
import br.com.systems.fenix.API_Fenix.exception.ClientIdNotFoundException;
import br.com.systems.fenix.API_Fenix.exception.ClientNameNotFoundException;
import br.com.systems.fenix.API_Fenix.exception.ClientsNotFoundException;
import br.com.systems.fenix.API_Fenix.security.CustomUserDetailsService;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ClientService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmailServiceImpl emailServiceImpl;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ValidationTokenRepository validationTokenRepository;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    public Client findById(Long id) {
        CustomUserDetailsService userDetailsService = authenticated();
        if (userDetailsService == null
                || !userDetailsService.hasRole(ProfileEnum.ADMIN) && !id.equals(userDetailsService.getId()))
            throw new AuthorizationException("Access denied");

        Optional<Client> client = this.clientRepository.findById(id);
        return client
                .orElseThrow(() -> new ClientIdNotFoundException("Client not found with id:", id));
    }

    public Client findByName(String name) {
        Client client = this.clientRepository.findByFirstName(name);
        if (client == null) {
            throw new ClientNameNotFoundException("Client name not found: ", name);
        } else {
            return client;
        }

    }

    public Client findByEmail(String email) {
        Client client = this.clientRepository.findByEmail(email);
        if (client == null) {
            throw new ClientEmailNotFoundException("Client email not found: ", email);
        } else {
            return client;
        }
    }

    public List<Client> findAllClients() {
        List<Client> clients = this.clientRepository.findAll();
        if (clients.isEmpty()) {
            throw new ClientsNotFoundException("There is no one in the database");
        }
        return clients;
    }

    @Transactional
    public Client save(Client client) {
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new RuntimeException("email already existis");
        } else {
            Client clientBuilt = Client.builder()
                    .firstName(client.getFirstName())
                    .lastName(client.getLastName())
                    .age(client.getAge())
                    .telephone(client.getTelephone())
                    .email(client.getEmail())
                    .password(this.passwordEncoder.encode(client.getPassword()))
                    .profiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()))
                    .imageURL(client.getImageURL())
                    .isEnabled(false)
                    .build();
            this.clientRepository.save(clientBuilt);

            ValidationToken validationToken = new ValidationToken(clientBuilt);
            validationTokenRepository.save(validationToken);

            // emailServiceImpl.sendHtmlEmail(client.getFirstName(), client.getEmail(),
            // validationToken.getToken());

            emailServiceImpl.sendHtmlEmailWithEmbeddedFiles(client.getFirstName(), client.getEmail(),
                    validationToken.getToken());
            return clientBuilt;
        }
    }

    public Boolean verirfyToken(String token) {
        ValidationToken validation = validationTokenRepository.findByToken(token);
        Client client = clientRepository.findByEmail(validation.getClient().getEmail());
        client.setEnabled(true);
        clientRepository.save(client);
        return Boolean.TRUE;

    }

    public void changeClientPassword(Client client, String password) {
        client.setPassword(this.passwordEncoder.encode(password));
        this.clientRepository.save(client);
    }

    public Cookie creatCookie(Client client, String authorization) {

        Map<Object, Object> keyValuePairs = new HashMap<>();
        keyValuePairs.put("userEmail", client.getEmail());
        keyValuePairs.put("Authorization", authorization);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonValue;
        Cookie theCokie;
        try {
            jsonValue = objectMapper.writeValueAsString(keyValuePairs);
            String safeValue = URLEncoder.encode(jsonValue, StandardCharsets.UTF_8.toString());
            theCokie = new Cookie("userInfo", safeValue);
            theCokie.setMaxAge(60 * 60 * 24);
            return theCokie;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
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
                    .password(this.passwordEncoder.encode(client.getPassword()))
                    .profiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()))
                    .imageURL(client.getImageURL())
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
    public Optional<Client> updateProfile(Client client) {
        try {
            Optional<Client> clientToUpdate = clientRepository.findById(client.getId());
            if (clientToUpdate.isPresent()) {
                Client existingClient = clientToUpdate.get();
                existingClient.setImageURL(client.getImageURL());
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
    public String delete(Long id) {
        Client byId = findById(id);
        ;

        try {
            couponRepository.deleteByClientID(id);
            passwordResetTokenRepository.deleteByClientId(id);
            validationTokenRepository.deleteByClientId(id);
            clientRepository.deleteById(id);
            return byId.getFirstName();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete client: " + e.getMessage());
        }
    }

    public boolean validateClient(String email, String password) {
        Client client = clientRepository.findByEmail(email);
        return client != null && client.getPassword().equals(password);
    }

    public static CustomUserDetailsService authenticated() {
        try {
            return (CustomUserDetailsService) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    public void createPasswordResetTokenForClient(Client client, String token) {
        PasswordResetToken myTOToken = new PasswordResetToken(token, client);
        passwordResetTokenRepository.save(myTOToken);
    }

}
