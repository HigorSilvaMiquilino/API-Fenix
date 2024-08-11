package br.com.systems.fenix.API_Fenix.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping()
    public String showLogin() {
        return "login";
    }

    @GetMapping("register")
    public String showRegister() {
        return "register";
    }

    @GetMapping("forgetPassword")
    public String showForgetPassword() {
        return "forgetPassword";
    }

    @GetMapping("home")
    public String showHome() {
        return "home";
    }

    @GetMapping("greeting")
    public String showGreeting() {
        return "greeting";
    }

    @GetMapping("updatePassword")
    public String showUpdatePassword() {
        return "updatePassword";
    }

    @GetMapping("updateProfile")
    public String showUpdateProfile() {
        return "updateProfile";
    }

    @GetMapping("updateProfilePicture")
    public String showUpdateProfilePicture() {
        return "updateProfilePicture";
    }
}