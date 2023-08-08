package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepo;
import com.mindhub.homebanking.repositories.ClientRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData (ClientRepo clientrepo, AccountRepo accountrepo) {
		return(args -> {
			// instancio un cliente y sus cuentas
			Client c1 = new Client ("melba@mindhub.com", "Melba", "MOREL");
			Account a1 = new Account("VIN001", LocalDate.now(), 5000);
			Account a2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500);
			// agrego las cuentas al cliente
			c1.addAccount(a1);
			c1.addAccount(a2);

			Client c2 = new Client("mmdohmen@hotmail.com", "Mario Maximo", "DOHMEN");
			Account a3 = new Account("VIN003", LocalDate.now(), 25000);
			c2.addAccount(a3);

			// persisto los objetos empezando por los CLIENTES p/ q se generen sus 'id'
			// (los cuales son necesarios para crear la relacion (clave foranea) y poder persistir las CUENTAS
			clientrepo.save(c1);
			clientrepo.save(c2);
			accountrepo.save(a1);
			accountrepo.save(a2);
			accountrepo.save(a3);

		});
	}
}
