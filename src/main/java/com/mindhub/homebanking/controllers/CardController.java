package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepo;
import com.mindhub.homebanking.repositories.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class CardController {

    // atributos
    @Autowired
    ClientRepo clientRepo;
    @Autowired
    CardRepo cardRepo;

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> newCard (Authentication authentication, CardColor color, CardType type) {
        // cliente autenticado
        Client client = clientRepo.findByEmail(authentication.getName());
        // verifico cantidad de tarjetas del cliente autenticado
        Set<Card> cards = client.getCards();
        List<String> creditCards = new ArrayList<>();
        List<String> debitCards = new ArrayList<>();
        for (Card card : cards) {
            if (card.getType().equals(CardType.CREDIT)) { creditCards.add(card.getColor().toString()); }
            if (card.getType().equals(CardType.DEBIT)) { debitCards.add(card.getColor().toString()); }
        }
        if (type.equals(CardType.DEBIT) && debitCards.contains(color.toString())) {
            return new ResponseEntity<>("you've already got a " + color + " debit card", HttpStatus.FORBIDDEN);
        }
        if (type.equals(CardType.DEBIT) && debitCards.size() >= 3) {
            return new ResponseEntity<>("NO MORE THAN 3 DEBIT CARDS ...", HttpStatus.FORBIDDEN);
        }
        if (type.equals(CardType.CREDIT) && creditCards.contains(color.toString())) {
            return new ResponseEntity<>("you've already got a " + color + " credit card", HttpStatus.FORBIDDEN);
        }
        if (type.equals(CardType.CREDIT) && creditCards.size() >= 3)  {
            return new ResponseEntity<>("NO MORE THAN 3 CREDIT CARDS ...", HttpStatus.FORBIDDEN);
        }

        // creacion y persistencia de la tarjeta nueva
        String cardHolder = client.getFirstName() + " " + client.getLastName();

        List<Card> allCards = cardRepo.findAll();
        boolean exists = false;
        Integer random;
        String number = "";
        do {
            for (int i = 0; i < 4; i++) {
                random = (int) (Math.random() * 10000);
                number = number + random.toString() + "-";
            }
            number = number.substring(0, number.length() - 1);
            // verifico que el numero de tarjeta NO exista
            for (Card card : allCards) {
                if (number.equals(card.getNumber())) { exists = true; }
            }
        } while (exists == true);
        //                                                     *** verficar que el numero de tarjeta NO EXISTA ***

        int cvv = (int) (Math.random() * 1000);

        Card card = new Card(cardHolder, type, color, number, (short) cvv, LocalDate.now(), LocalDate.now().plusYears(5));
        // asigno la tarjeta al cliente
        client.addCard(card);
        cardRepo.save(card);
        return new ResponseEntity<>("Card CREATED ...", HttpStatus.CREATED);
    }

}
