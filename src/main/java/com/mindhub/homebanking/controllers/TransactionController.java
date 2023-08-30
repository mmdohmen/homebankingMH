package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepo;
import com.mindhub.homebanking.repositories.ClientRepo;
import com.mindhub.homebanking.repositories.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class TransactionController {

    // atributos
    @Autowired
    ClientRepo clientRepo;
    @Autowired
    AccountRepo accountRepo;
    @Autowired
    TransactionRepo transactionRepo;

    @PostMapping("/transactions")
    @Transactional
    public ResponseEntity<Object> newTransaction (@RequestParam double amount, @RequestParam String description,
                                                  @RequestParam String fromAccountNumber, @RequestParam String toAccountNumber,
                                                  Authentication authentication) {

        if (amount <= 0 || description.isEmpty() || fromAccountNumber.isEmpty() || toAccountNumber.isEmpty()) {
            return new ResponseEntity<>("ALL FIELDS are REQUIRED ...", HttpStatus.FORBIDDEN);
        }

        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("ACCOUNT NUMBERS MUST BE DIFFERENT ...", HttpStatus.FORBIDDEN);
        }

        // cliente autenticado, cuentas propias y saldos
        Client client = clientRepo.findByEmail(authentication.getName());
        Account fromAccount;
        Set<Account> clientAccounts = client.getAccounts();
        System.out.println(clientAccounts.toString());
        boolean exists = false;
        for (Account account : clientAccounts) {
            if (account.getNumber().equals(fromAccountNumber)) { exists = true; }
        }
        if (exists == true) {
            fromAccount = accountRepo.findByNumber(fromAccountNumber);
        } else {
            return new ResponseEntity<>("the ORIGIN ACCOUNT does NOT BELONG to the CLIENT", HttpStatus.FORBIDDEN);
        }

        // verificacion cuenta destino
        Account toAccount = accountRepo.findByNumber(toAccountNumber);
        if (toAccount == null) {
            return new ResponseEntity<>("DESTINATION ACCOUNT DOESN'T EXIST ...", HttpStatus.FORBIDDEN);
        }

        // verificacion saldo cuenta origen
        if (fromAccount.getBalance() < amount) {
            return new ResponseEntity<>("INSUFFICIENT balance ...", HttpStatus.FORBIDDEN);
        }

        // DEBIT transaction en cuenta de origen
        Transaction debit = new Transaction(LocalDateTime.now(), description + " to " + toAccountNumber, -amount, TransactionType.DEBIT);
        fromAccount.addTransaction(debit);
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        accountRepo.save(fromAccount);
        transactionRepo.save(debit);

        // CREDIT transaction en cuenta destino
        Transaction credit = new Transaction(LocalDateTime.now(), description + " from " + fromAccountNumber, amount, TransactionType.CREDIT);
        toAccount.addTransaction(credit);
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepo.save(toAccount);
        transactionRepo.save(credit);


        return new ResponseEntity<>("SUCCESSFUL transaction ...", HttpStatus.CREATED);

    }

}
