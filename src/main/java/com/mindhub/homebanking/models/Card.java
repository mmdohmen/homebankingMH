package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {

    // atributos
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String cardholder;
    private CardType type;
    private CardColor color;
    private String number;
    private short cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_ID")
    private Client client;


    // constructores
    public Card() {}
    public Card(String cardholder, CardType type, CardColor color, String number, short cvv, LocalDate fromDate, LocalDate thruDate) {
        this.cardholder = cardholder;
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
    }


    // getter & setter
    public long getId() {
        return id;
    }

    public String getCardholder() {
        return cardholder;
    }
    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public CardType getType() {
        return type;
    }
    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }
    public void setColor(CardColor color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public short getCvv() {
        return cvv;
    }
    public void setCvv(short cvv) {
        this.cvv = cvv;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }
    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }
    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
}