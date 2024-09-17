package br.com.systems.fenix.API_Fenix.Controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping()
    public String showLogin(Model model,
            @CookieValue(value = "userInfo", required = false, defaultValue = "") String userInfoCookie) {
        try {
            if (!userInfoCookie.isEmpty()) {
                String decodeValue = URLDecoder.decode(userInfoCookie, StandardCharsets.UTF_8.toString());
                ObjectMapper objectMapper = new ObjectMapper();
                @SuppressWarnings("unchecked")
                Map<String, String> userInfoMap = objectMapper.readValue(decodeValue, Map.class);
                String userEmail = userInfoMap.get("email");
                model.addAttribute("userEmail", userEmail != null ? userEmail : "Guest");

            } else {
                model.addAttribute("userEmail", "Guest");
            }
            return "login";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("userEmail", "Guest");

        }

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

    @GetMapping("promotioncupom")
    public String showPromotionCupom() {
        return "promotioncupom";
    }

    @GetMapping("changePassword")
    public String showChangePassword() {
        return "changePassword";
    }

}