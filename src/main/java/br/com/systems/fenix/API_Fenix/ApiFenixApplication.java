package br.com.systems.fenix.API_Fenix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync(proxyTargetClass = true)
public class ApiFenixApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiFenixApplication.class, args);
	}

}