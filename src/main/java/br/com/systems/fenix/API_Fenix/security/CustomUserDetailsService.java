package br.com.systems.fenix.API_Fenix.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.com.systems.fenix.API_Fenix.Model.enuns.ProfileEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
@Getter
public class CustomUserDetailsService implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean isEnabled;

    public CustomUserDetailsService(Long id, String username, String password, boolean isEnabled,
            Set<ProfileEnum> profileEnums) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = profileEnums
                .stream()
                .map(enumeration -> new SimpleGrantedAuthority(enumeration.getDescription()))
                .collect(Collectors.toList());
        this.isEnabled = isEnabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean hasRole(ProfileEnum profileEnum) {
        return getAuthorities().contains(new SimpleGrantedAuthority(profileEnum.getDescription()));
    }
}
