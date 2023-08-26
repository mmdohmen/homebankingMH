package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepo;
import com.mindhub.homebanking.repositories.ClientRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {

    // propiedades
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepo accountRepo;


    // metodos
    @GetMapping("/clients")
    public List<ClientDTO> getClients () {
        //return clientRepo.findAll();
        return clientRepo.findAll().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient (@PathVariable long id) {
        return clientRepo.findById(id).map(ClientDTO::new).orElse(null);
    }

    @GetMapping ("/clients/current")
    public Client getClient (Authentication authentication) {
        return clientRepo.findByEmail(authentication.getName());
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register (@RequestParam String email, @RequestParam String firstName,
                                            @RequestParam String lastName, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("ALL fields are REQUIRED", HttpStatus.FORBIDDEN);
        }

        if (clientRepo.findByEmail(email) != null) {
            return new ResponseEntity<>("Email already IN USE", HttpStatus.FORBIDDEN);
        }

        // creo el CLIENTE y su CUENTA
        Client c = new Client(email, firstName, lastName, passwordEncoder.encode(password));
        Account a = new Account(LocalDate.now(), 0);
        c.addAccount(a);
        // PERSISTO a ambos
        clientRepo.save(c);
        accountRepo.save(a);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }



}
