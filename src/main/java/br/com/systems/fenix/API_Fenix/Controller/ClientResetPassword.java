package br.com.systems.fenix.API_Fenix.Controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.systems.fenix.API_Fenix.DTO.PasswordDto;
import br.com.systems.fenix.API_Fenix.DTO.StringRequest;
import br.com.systems.fenix.API_Fenix.Model.Client;
import br.com.systems.fenix.API_Fenix.Service.ClientService;
import br.com.systems.fenix.API_Fenix.Service.PasswordResetTokenService;
import br.com.systems.fenix.API_Fenix.Service.impl.EmailServiceImpl;
import br.com.systems.fenix.API_Fenix.response.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@Validated
@RequestMapping("/reset")
public class ClientResetPassword {

        @Autowired
        ClientService clientService;

        @Autowired
        PasswordResetTokenService passwordResetTokenService;

        @Autowired
        EmailServiceImpl emailServiceImpl;

        @Autowired
        MessageSource messages;

        @PostMapping("/client/resetPassword")
        @Validated
        public ResponseEntity<HttpResponse> resetPassword(HttpServletRequest request,
                        @RequestBody StringRequest stringRequest) {
                Client client = clientService.findByEmail(stringRequest.getContent());

                String token = UUID.randomUUID().toString();
                clientService.createPasswordResetTokenForClient(client, token);
                emailServiceImpl.sendSimpleMailMessageResetPassword(client.getFirstName(), client.getEmail(), token,
                                "Reset Password");

                HttpResponse response = HttpResponse.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .statusCode(HttpStatus.OK.value())
                                .status(HttpStatus.OK)
                                .message("We already identify you, " + client.getFirstName()
                                                + ". You can reset now, check your e-mail")
                                .build();

                return ResponseEntity.ok().body(response);
        }

        @GetMapping("/client/changePassword")
        @Validated
        public String showChangePasswordPage(Model model, @RequestParam("token") String token) {
                String result = passwordResetTokenService.validatePasswordResetToken(token);

                if (result != null) {
                        model.addAttribute("result", result);

                        if (result.equals("expired")) {
                                model.addAttribute("message", "Your password reset token has expired");
                        } else {
                                model.addAttribute("message", "The password reset token is invalid");

                        }
                        return "login";
                } else {
                        model.addAttribute("token", token);
                        return "verifyResetPasswordToken";
                }

        };

        @PostMapping("/client/savePassword")
        @Validated
        public ResponseEntity<HttpResponse> savePassword(Model model, @RequestBody PasswordDto passwordDto) {
                String result = passwordResetTokenService.validatePasswordResetToken(passwordDto.getToken());

                if (result != null) {
                        model.addAttribute("result", result);

                        if (result.equals("expired")) {
                                model.addAttribute("message", "Your password reset token has expired");
                        } else {
                                model.addAttribute("message", "The password reset token is invalid");

                        }
                }

                Client client = passwordResetTokenService
                                .getClientByPasswordResetToken(passwordDto.getToken());
                clientService.changeClientPassword(client, passwordDto.getPassword());

                HttpResponse response = HttpResponse.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .statusCode(HttpStatus.OK.value())
                                .status(HttpStatus.OK)
                                .message("Password Updated Successfully, " + client.getFirstName()
                                                + ". You can log in now")
                                .build();

                return ResponseEntity.ok().body(response);
        }

}
