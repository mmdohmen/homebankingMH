package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepo;
import com.mindhub.homebanking.repositories.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    // propiedades
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private ClientRepo clientRepo;

    // metodos
    @GetMapping("/accounts")
    public Set<AccountDTO> getAccounts() {
        //return accountRepo.findAll()
        //return clientRepo.findAll().stream().map(ClientDTO::new).collect(Collectors.toList());
        return accountRepo.findAll().stream().map(AccountDTO::new).collect(Collectors.toSet());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount (@PathVariable long id) {   // *** cambiar por AccountDTO ***
        //return accountRepo.findById(id);
        // return clientRepo.findById(id).map(ClientDTO::new).orElse(null);
        return accountRepo.findById(id).map(AccountDTO::new).orElse(null);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> newAccount (Authentication authentication) {
        // cliente autenticado
        Client client = clientRepo.findByEmail(authentication.getName());
        // verifico cantidad de cuentas del cliente autenticado
        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("NO puede tener MAS de 3 CUENTAS ...", HttpStatus.FORBIDDEN);
        }
        // creacion y persistencia de cuenta nueva
        List<Account> accounts = accountRepo.findAll();
        boolean exists = false;
        String number;
        do {
            Integer random = (int) (Math.random() * 100000000);
            number = "VIN-" + random.toString();
            // verifico que el numero de cuenta NO EXISTA
            for (Account account : accounts) {
                if (number.equals(account.getNumber())) { exists = true; }
            }
        } while (exists == true);

        Account a = new Account(number, LocalDate.now(), 0);
        // asignacion de la cuenta al cliente
        client.addAccount(a);
        a.setCliente(client);
        accountRepo.save(a);
        return new ResponseEntity<>("cuenta CREADA...", HttpStatus.CREATED);
    }


}
