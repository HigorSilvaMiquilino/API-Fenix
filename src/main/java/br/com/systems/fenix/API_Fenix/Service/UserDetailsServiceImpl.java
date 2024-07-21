package br.com.systems.fenix.API_Fenix.Service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.systems.fenix.API_Fenix.Model.Client;
import br.com.systems.fenix.API_Fenix.Repository.ClientRepository;
import br.com.systems.fenix.API_Fenix.security.CustomUserDetailsService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = this.clientRepository.findByEmail(email);

        if (Objects.isNull(client))
            throw new UsernameNotFoundException("Username not found: " + email);

        return new CustomUserDetailsService(client.getId(), client.getEmail(), client.getPassword(),
                client.getProfile());
    }

}
