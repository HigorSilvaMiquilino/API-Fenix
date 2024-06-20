package br.com.systems.fenix.API_Fenix.Controller;

import br.com.systems.fenix.API_Fenix.Model.Client;
import br.com.systems.fenix.API_Fenix.Service.ClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

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
    public ResponseEntity<Map<String, Object>> crete(@RequestBody Client client) {
        this.clientService.save(client);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(client.getId()).toUri();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Client created successfully");
        response.put("client", client);
        return ResponseEntity.created(uri).body(response);
    }

    @PostMapping("/all")
    @Validated
    public ResponseEntity<Void> createAll(@RequestBody List<Client> clients) {
        this.clientService.save(clients);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Client client) {
        client.setId(id);
        this.clientService.update(client);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Client updated successfully");
        response.put("client", client);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        this.clientService.delete(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Client deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
