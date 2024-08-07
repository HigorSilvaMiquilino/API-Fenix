package br.com.systems.fenix.API_Fenix.utils;

public class EmailUtils {

    public static String getEmailMessage(String name, String host, String token) {
        return "Hello " + name
                + ",\n\nYour new account has been created. Please click the link below to verify your account. \n\n"
                + getVerificationUrl(host, token) + "\n\n The support Team";

    }

    public static String getVerificationUrl(String host, String token) {
        return host + "email/emailToken?token=" + token;

    }

    public static String getEmailMessageResetPassword(String name, String host, String token) {
        return "Hello " + name
                + ",\n\nYou have asked to reset your password. Please click the link below to reset your password. \n\n"
                + getVerificationUrlResetPassword(host, token) + "\n\n The support Team";

    }

    public static String getVerificationUrlResetPassword(String host, String token) {
        return "<a href=" + host + "reset/client/changePassword?token=" + token + ">" + "Reset" + "</a>";

    }
}
