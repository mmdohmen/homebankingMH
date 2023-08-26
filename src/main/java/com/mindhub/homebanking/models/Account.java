package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {

    // atributos
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDate creationDate;
    private double balance;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_ID")
    private Client cliente;
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();


    // constructores
    public Account() {}

    public Account(LocalDate creationDate, double balance) {
        Integer random = (int) (Math.random() * 100000000);
        //                                                    *** agregar VERIFICACION de NO REPETIR nro de cuenta
        this.number = "VIN-" + random.toString();
        this.creationDate = creationDate;
        this.balance = balance;
    }
    /*
    public Account(String number, LocalDate creationDate, double balance) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
    } */


    // getter & setter
    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    /*
    public void setNumber(String number) {
        this.number = number;
    }
    */

    public LocalDate getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }

    @JsonIgnore
    public Client getCliente() {
        return cliente;
    }
    public void setCliente(Client cliente) {
        this.cliente = cliente;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }
    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }


    // metodos
    public void addTransaction (Transaction transaction) {
        transaction.setAccount(this);
        transactions.add(transaction);
    }


}
