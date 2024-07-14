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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = this.clientRepository.findByFirstName(username);

        if (Objects.isNull(client)) {
            throw new UsernameNotFoundException("Username not found: " + username);
        }

        return new CustomUserDetailsService(client.getId(), client.getFirstName(), client.getPassword(),
                client.getProfile());
    }

}
