package br.com.systems.fenix.API_Fenix.Controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@Validated
@RequestMapping("/")
public class IndexController {

    @GetMapping()
    public String showLogin() {
        return "redirect:/html/login.html";
    }
    
}
