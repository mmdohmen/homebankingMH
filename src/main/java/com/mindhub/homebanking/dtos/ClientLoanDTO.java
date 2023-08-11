package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {

    // atributos
    private int id;
    private int loanId;
    private String name;
    private double amount;
    private int payments;


    // constructores
    public ClientLoanDTO() {}
    public ClientLoanDTO (ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();

    }


    // getter
    public int getId() {
        return id;
    }

    public int getLoanId() {
        return loanId;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayment() {
        return payments;
    }
}
