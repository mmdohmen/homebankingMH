package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData (ClientRepo clientrepo, AccountRepo accountrepo, TransactionRepo transactionRepo,
									   LoanRepo loanRepo, ClientLoanRepo clientLoanRepo, CardRepo cardRepo) {
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


			// instancio tipos de PRESTAMOS
			Loan l1 = new Loan ("mortgage", 500000, List.of(12, 24, 36, 48, 60));
			loanRepo.save(l1);
			Loan l2 = new Loan ("personal", 100000, List.of(6, 12, 24));
			loanRepo.save(l2);
			Loan l3 = new Loan ("automotive", 300000, List.of(6, 12, 24, 36));
			loanRepo.save(l3);


			// instancio PRESTAMOS otorgados a clientes
			// a MELBA - cliente c1
			ClientLoan cl1 = new ClientLoan(400000,60);   // detalles del prestamo
			c1.addClientLoan(cl1);                                        // cliente q recibe el prestamo
			l1.addClientLoan(cl1);                                        // prestamo HIPOTECARIO
			clientLoanRepo.save(cl1);                                     // persistencia

			ClientLoan cl2 = new ClientLoan(50000, 12);
			c1.addClientLoan(cl2);
			l2.addClientLoan(cl2);
			clientLoanRepo.save(cl2);

			// a MMDohmen - cliente 2
			ClientLoan cl3 = new ClientLoan(100000, 24);
			c2.addClientLoan(cl3);
			l2.addClientLoan(cl3);
			clientLoanRepo.save(cl3);

			ClientLoan cl4 = new ClientLoan(200000, 36);
			c2.addClientLoan(cl4);
			l3.addClientLoan(cl4);
			clientLoanRepo.save(cl4);


			// instancio TARJETAS para
			// MELBA - cliente c1
			Card card1 = new Card((c1.getLastName() + " " + c1.getFirstName()), CardType.DEBIT, CardColor.GOLD, "0000-0000-0000-0001",
					              (short) 1, LocalDate.now(), LocalDate.now().plusYears(5));
			c1.addCard(card1);
			cardRepo.save(card1);

			Card card2 = new Card((c1.getLastName() + " " + c1.getFirstName()), CardType.CREDIT, CardColor.TITANIUM, "0000-0000-0000-0002",
					(short) 2, LocalDate.now(), LocalDate.now().plusYears(5));
			c1.addCard(card2);
			cardRepo.save(card2);


			// MMDohmen - cliente 2
			Card card3 = new Card((c2.getLastName() + " " + c2.getFirstName()), CardType.CREDIT, CardColor.SILVER, "0000-0000-0000-0003",
					(short) 3, LocalDate.now(), LocalDate.now().plusYears(5));
			c2.addCard(card3);
			cardRepo.save(card3);


		});
	}
}
