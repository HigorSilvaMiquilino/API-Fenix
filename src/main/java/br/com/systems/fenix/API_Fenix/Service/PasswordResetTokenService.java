package br.com.systems.fenix.API_Fenix.Service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.systems.fenix.API_Fenix.Model.Client;
import br.com.systems.fenix.API_Fenix.Model.PasswordResetToken;
import br.com.systems.fenix.API_Fenix.Repository.PasswordResetTokenRepository;

@Service
public class PasswordResetTokenService {

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                        : null;
    }

    public Client getClientByPasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        Client client = passwordResetToken.getClient();
        return client;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToke) {
        final Calendar cal = Calendar.getInstance();
        return passToke.getExpiryDate().before(cal.getTime());
    }

    public void deleteTokenByClientId(Long clientId) {
        passwordResetTokenRepository.deleteByClientId(clientId);
    }

}
