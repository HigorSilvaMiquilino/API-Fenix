package br.com.systems.fenix.API_Fenix.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.systems.fenix.API_Fenix.Service.UserDetailsServiceImpl;
import br.com.systems.fenix.API_Fenix.security.JWTAuthenticationFilterChain;
import br.com.systems.fenix.API_Fenix.security.JWTAuthorizationFilter;
import br.com.systems.fenix.API_Fenix.security.JWTUtilities;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private AuthenticationManager authenticationManager;

        @Autowired
        private UserDetailsServiceImpl userDetailsServiceImpl;

        @Autowired
        private JWTUtilities jwtUtil;

        public static final String[] PUBLIC_MATCHERS_POST = {
                        "/client",
                        "/login",
                        "/promotion",
                        "/promotion/all",
                        "/reset/client/resetPassword",
                        "/reset/client/savePassword",

        };

        public static final String[] PUBLIC_MATCHERS_GET = {
                        "/**",
                        "/email/emailToken",
                        "/reset/client/changePassword"
        };

        public static final String[] STATIC_RESOURCES = {
                        "/css/**",
                        "/js/**",
                        "/images/**"
        };

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder.userDetailsService(this.userDetailsServiceImpl)
                                .passwordEncoder(passwordEncoder());
                this.authenticationManager = authenticationManagerBuilder.build();

                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(withDefaults())
                                .authenticationManager(authenticationManager)
                                .sessionManagement(sessionManagement -> sessionManagement
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                                                .requestMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
                                                .requestMatchers(STATIC_RESOURCES).permitAll()
                                                .anyRequest().authenticated())
                                .httpBasic(withDefaults())
                                .logout(logout -> logout
                                                .logoutUrl("/logout")
                                                .logoutSuccessUrl("/")
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true))
                                .addFilter(new JWTAuthenticationFilterChain(authenticationManager, this.jwtUtil))
                                .addFilter(new JWTAuthorizationFilter(authenticationManager, this.jwtUtil,
                                                this.userDetailsServiceImpl));
                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500"));
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "User-Agent"));
                config.setExposedHeaders(Arrays.asList("Authorization"));
                source.registerCorsConfiguration("/**", config);
                return source;
        }

}
