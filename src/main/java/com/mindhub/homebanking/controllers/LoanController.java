package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoanController {

    // atributos
    @Autowired
    LoanRepo loanRepo;
    @Autowired
    AccountRepo accountRepo;
    @Autowired
    ClientRepo clientRepo;
    @Autowired
    ClientLoanRepo clientLoanRepo;
    @Autowired
    TransactionRepo transactionRepo;


    // metodos
    @GetMapping("/loans")
    public List<Loan> getLoans() {
        return loanRepo.findAll();
        //return (List<LoanDTO>) loanRepo.findAll().stream().map(LoanDTO::new).collect(Collectors.toSet());
    }

    @PostMapping("/loans")
    @Transactional
    public ResponseEntity<Object> newLoan (Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {

        if (loanApplicationDTO.getLoanId() == 0 || loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() <=0 ||
                loanApplicationDTO.getToAccountNumber().isEmpty() ) {
            return new ResponseEntity<>("ALL FIELDS are REQUIRED ...", HttpStatus.FORBIDDEN);
        }

        List<Loan> loans = loanRepo.findAll();
        boolean exists = false;
        for (Loan loan : loans) {
            if (loan.getId() == loanApplicationDTO.getLoanId()) { exists = true; }
        }
        if (exists == false) {
            return new ResponseEntity<>("the request LOAN does NOT EXIST ...", HttpStatus.FORBIDDEN);
        }

        Loan loan = loanRepo.findById(loanApplicationDTO.getLoanId()).get();
        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("the AMOUNT EXCEEDS the MAXIMUN ALLOWED ...", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("PAYMENTS NOT ADMITTED ...", HttpStatus.FORBIDDEN);
        }

        // verificacion cuenta destino
        Account toAccount = accountRepo.findByNumber(loanApplicationDTO.getToAccountNumber());
        if (toAccount == null) {
            return new ResponseEntity<>("DESTINATION ACCOUNT DOESN'T EXIST ...", HttpStatus.FORBIDDEN);
        }

        // cliente autenticado
        Client client = clientRepo.findByEmail(authentication.getName());
        Set<Account> clientAccounts = client.getAccounts();
        exists = false;
        for (Account account : clientAccounts) {
            if (account.getNumber().equals(loanApplicationDTO.getToAccountNumber())) { exists = true; }
        }
        if (exists == false) {
                return new ResponseEntity<>("DESTINATION ACCOUNT DOESN'T BELONG TO THE CURRENT CLIENT ...", HttpStatus.FORBIDDEN);
        }


        // instancio el prestamo solicitado
        ClientLoan cl = new ClientLoan(loanApplicationDTO.getAmount() * 1.2, loanApplicationDTO.getPayments());
        client.addClientLoan(cl);
        loan.addClientLoan(cl);
        clientLoanRepo.save(cl);

        // CREDIT transaction en cuenta destino
        Transaction credit = new Transaction(LocalDateTime.now(), loan.getName() + " loan approved", loanApplicationDTO.getAmount(),
                                             TransactionType.CREDIT);
        toAccount.addTransaction(credit);
        toAccount.setBalance(toAccount.getBalance() + loanApplicationDTO.getAmount());
        accountRepo.save(toAccount);
        transactionRepo.save(credit);


        return new ResponseEntity<>("Loan GRANTED ...", HttpStatus.CREATED);

    }

}
