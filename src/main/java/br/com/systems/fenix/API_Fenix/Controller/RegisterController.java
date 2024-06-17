package br.com.systems.fenix.API_Fenix.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterController {

  @GetMapping("/")
  public String register() {
    return "redirect: /html/register";
  }

}
