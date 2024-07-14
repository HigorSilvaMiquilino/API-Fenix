package br.com.systems.fenix.API_Fenix.security;

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

import br.com.systems.fenix.API_Fenix.Service.JWTUtil;
import br.com.systems.fenix.API_Fenix.Service.UserDetailsServiceImpl;

import static org.springframework.security.config.Customizer.withDefaults;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private JwtAuthEntryPoint authEntryPoint;

        @Autowired
        private UserDetailsServiceImpl userDetailsServiceImpl;

        @Autowired
        private JWTUtil jwtUtil;

        public SecurityConfig(JWTUtil jwtUtil, UserDetailsServiceImpl userDetailsServiceImpl) {
                this.jwtUtil = jwtUtil;
                this.userDetailsServiceImpl = userDetailsServiceImpl;
        }

        public static final String[] PUBLIC_MATCHERS_POST = {
                        "/client",
                        "/promotion",
                        "/product"
        };

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                AuthenticationManager authManager = authenticationManager(
                                http.getSharedObject(AuthenticationConfiguration.class));
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authenticationManager(authenticationManager(http))
                                .exceptionHandling(exceptionHandling -> exceptionHandling
                                                .authenticationEntryPoint(authEntryPoint))
                                .sessionManagement(sessionManagement -> sessionManagement
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                                                .anyRequest().authenticated())
                                .httpBasic(withDefaults())
                                .addFilter(new JWTAuthenticationFilterChain(authManager, this.jwtUtil))
                                .addFilter(new JWTAuthorizationFilter(authManager, this.jwtUtil,
                                                this.userDetailsServiceImpl));
                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        private AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder.userDetailsService(this.userDetailsServiceImpl)
                                .passwordEncoder(passwordEncoder());
                return authenticationManagerBuilder.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public JWTAuthenticationFilter jwtAuthenticationFilter() {
                return new JWTAuthenticationFilter();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(Arrays.asList("*"));
                config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
                source.registerCorsConfiguration("/**", config);
                return source;
        }

}
