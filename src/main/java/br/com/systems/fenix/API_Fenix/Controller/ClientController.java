package br.com.systems.fenix.API_Fenix.Controller;


import br.com.systems.fenix.API_Fenix.Model.Client;
import br.com.systems.fenix.API_Fenix.Service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    @GetMapping("/{name}")
    public ResponseEntity<Client> findByFirstName(@PathVariable String name) {
        Client client = clientService.findByName(name);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/")
    public ResponseEntity<List<Client>> findAll() {
        List<Client> allClients = clientService.findAllClients();
        return ResponseEntity.ok(allClients);
    }

    @PostMapping
    @Validated
    public ResponseEntity<Void> crete(@RequestBody Client client) {
        this.clientService.save(client);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(client.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody Client client) {
        client.setId(id);
        this.clientService.update(client);
        return ResponseEntity.ok("Client updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        this.clientService.delete(id);
        return ResponseEntity.ok("Client deleted successfully");
    }

}
