package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData (ClientRepo clientrepo) {
		return(args -> {
			Client c1 = new Client(1, "melba@mindhub.com", "Melba", "MOREL");
			Client c2 = new Client(2, "mmdohmen@hotmail.com", "Mario Maximo", "DOHMEN");
			// persisto los objetos
			clientrepo.save(c1);
			clientrepo.save(c2);
		});
	}
}
