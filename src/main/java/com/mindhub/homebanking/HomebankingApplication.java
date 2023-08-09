package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepo;
import com.mindhub.homebanking.repositories.ClientRepo;
import com.mindhub.homebanking.repositories.TransactionRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData (ClientRepo clientrepo, AccountRepo accountrepo, TransactionRepo transactionRepo) {
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


			// instancio TRANSACCIONES
			Transaction t1 = new Transaction(LocalDateTime.now(),"deposito",1000, TransactionType.CREDITO);
			a1.addTransaction(t1);
			transactionRepo.save(t1);

			Transaction t2 = new Transaction(LocalDateTime.now(), "deposito 2", 3000, TransactionType.CREDITO);
			a1.addTransaction(t2);
			transactionRepo.save(t2);

			Transaction t3 = new Transaction(LocalDateTime.now(), "extraccion", -900, TransactionType.DEBITO);
			a2.addTransaction(t3);
			transactionRepo.save(t3);

			Transaction t4 = new Transaction(LocalDateTime.now(), "transferencia", -7000, TransactionType.DEBITO);
			a3.addTransaction(t4);
			transactionRepo.save(t4);


		});
	}
}
