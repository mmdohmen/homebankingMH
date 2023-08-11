package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {

    // atributos
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;
    private double amount;
    private int payments;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_ID")
    private Client client;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_ID")
    private Loan loan;


    // constructores
    public ClientLoan() {}
    public ClientLoan(double amount, int payments) {
        this.amount = amount;
        this.payments = payments;
    }

    // getter & setter
    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }
    public void setPayments(int payments) {
        this.payments = payments;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }

    @JsonIgnore
    public Loan getLoan() {
        return loan;
    }
    public void setLoan(Loan loan) {
        this.loan = loan;
    }


}
