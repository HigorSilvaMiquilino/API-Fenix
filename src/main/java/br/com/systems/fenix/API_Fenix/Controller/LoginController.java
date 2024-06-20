package br.com.systems.fenix.API_Fenix.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.systems.fenix.API_Fenix.Service.ClientService;

@RestController
@Validated
@RequestMapping("/api")
public class LoginController {

  @Autowired
  private ClientService clientService;

  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> checkCredetion(@RequestBody Map<String, String> credentials) {
    String email = credentials.get("email");
    String password = credentials.get("password");

    boolean isValid = this.clientService.validateClient(email, password);

    Map<String, Object> response = new HashMap<>();
    if (isValid) {
      response.put("success", true);
      response.put("emailClient", email);
      return ResponseEntity.ok(response);
    } else {
      response.put("success", false);
      response.put("message", "Invalid email or password");

      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

  }
}
