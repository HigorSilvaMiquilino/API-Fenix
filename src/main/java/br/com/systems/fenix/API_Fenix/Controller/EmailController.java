package br.com.systems.fenix.API_Fenix.Controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.systems.fenix.API_Fenix.Service.ClientService;

@Controller
@Validated
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/emailToken")
    @Validated
    public String confirmClientAccount(Model model, @RequestParam("token") String token) {
        Boolean isSuccess = this.clientService.verirfyToken(token);

        model.addAttribute("timeStamp", LocalDateTime.now());
        model.addAttribute("Success", isSuccess);
        model.addAttribute("message", "Account verified");
        return "verifyEmail";
    }
}
