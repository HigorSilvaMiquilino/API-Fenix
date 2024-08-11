package br.com.systems.fenix.API_Fenix.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOriginPatterns("http://127.0.0.1:5500")
        .allowedHeaders("*")
        .allowedMethods("GET", "POST", "PUT", "DELETE")
        .allowCredentials(true);
  }

  @Override
  public void addResourceHandlers(@SuppressWarnings("null") ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/css/**")
        .addResourceLocations("classpath:/static/css/");

    registry.addResourceHandler("/js/**")
        .addResourceLocations("classpath:/static/js/");

    registry.addResourceHandler("/images/**")
        .addResourceLocations("classpath:/static/images/");

  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    JavaTimeModule module = new JavaTimeModule();
    mapper.registerModule(module);
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }
}
