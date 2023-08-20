package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.repositories.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    // propiedades
    @Autowired
    private AccountRepo accountRepo;


    // metodos
    @GetMapping("/accounts")
    public Set<AccountDTO> getAccounts() {                   // *** cambiar por AccountDTO ***
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


}
