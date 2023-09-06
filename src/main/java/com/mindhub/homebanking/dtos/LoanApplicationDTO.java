package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {

    // atributos
    private long loanId;
    private double amount;
    private int payments;
    private String toAccountNumber;


    // constructor
    public LoanApplicationDTO() {}


    // getter

    public long getLoanId() {
        return loanId;
    }

    public double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }


}
