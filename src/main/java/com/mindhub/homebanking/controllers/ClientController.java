package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {

    // propiedades
    @Autowired
    private ClientRepo clientRepo;

    // metodos
    @GetMapping("/clients")
    public List<ClientDTO> getClients () {
        //return clientRepo.findAll();
        return clientRepo.findAll().stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient (@PathVariable int id) {
        return clientRepo.findById(id).map(ClientDTO::new).orElse(null);
    }

}
