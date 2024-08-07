package br.com.systems.fenix.API_Fenix.Controller;

import br.com.systems.fenix.API_Fenix.Model.Client;
import br.com.systems.fenix.API_Fenix.Service.ClientService;
import br.com.systems.fenix.API_Fenix.response.ResponseClient;
import br.com.systems.fenix.API_Fenix.security.JWTUtilities;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    JWTUtilities jwtUtilities;

    @GetMapping("/{id}")
    public ResponseEntity<Client> findById(@PathVariable Long id) {
        Client client = clientService.findById(id);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Client> findByFirstName(@PathVariable String name) {
        Client client = clientService.findByName(name);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Client> findByEmail(@PathVariable String email) {
        Client client = clientService.findByEmail(email);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Client>> findAll() {
        List<Client> allClients = clientService.findAllClients();
        return ResponseEntity.ok(allClients);
    }

    @PostMapping
    @Validated
    public ResponseEntity<ResponseClient> crete(@RequestBody Client client, HttpServletRequest request, Errors errors) {
        this.clientService.save(client);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(client.getId()).toUri();
        ResponseClient response = ResponseClient.builder()
                .timeStamp(LocalDateTime.now().toString())
                .statusCode(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .message("Client created successfully, wellcome " + client.getFirstName())
                .client(client)
                .build();

        String token = "Bearer " + this.jwtUtilities.generateToken(client.getEmail());

        return ResponseEntity.created(uri).header("Authorization", token).body(response);
    }

    @PostMapping("/all")
    @Validated
    public ResponseEntity<Void> createAll(@RequestBody List<Client> clients) {
        this.clientService.save(clients);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<ResponseClient> update(@PathVariable Long id, @RequestBody Client client) {
        client.setId(id);
        this.clientService.update(client);
        ResponseClient response = ResponseClient.builder()
                .timeStamp(LocalDateTime.now().toString())
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("Client updated successfully")
                .client(client)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<ResponseClient> updateProfile(
            @PathVariable Long id,
            @RequestParam(value = "imageURL") MultipartFile imageURL) throws IOException {
        Optional<Client> clientOptional = Optional.ofNullable(clientService.findById(id));

        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();

            if (!imageURL.isEmpty()) {
                String folder = "src/main/resources/static/images/";
                byte[] bytes = imageURL.getBytes();
                Path path = Paths.get(folder + imageURL.getOriginalFilename());
                Files.write(path, bytes);

                client.setImageURL("http://localhost:5500/" + path.toString());
                clientService.updateProfile(client);

                ResponseClient response = ResponseClient.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .statusCode(HttpStatus.OK.value())
                        .status(HttpStatus.OK)
                        .message("Profile picture updated successfully")
                        .client(client)
                        .build();

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(ResponseClient.builder().message("File is empty").build());
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseClient.builder().message("Client not found").build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseClient> delete(@PathVariable Long id) {
        this.clientService.delete(id);

        ResponseClient response = ResponseClient.builder()
                .timeStamp(LocalDateTime.now().toString())
                .statusCode(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .message("Client deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
