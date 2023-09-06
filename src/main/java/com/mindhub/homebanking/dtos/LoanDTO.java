package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LoanDTO {

    // atributos
    private long id;
    private String name;
    private double maxAmount;
    private List<Integer> payments;


    // constructores
    public LoanDTO() {}

    public LoanDTO (Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = (List<Integer>) loan.getPayments().stream().collect(Collectors.toSet());
    }


    // getter
    public long getId() { return id; }
    public String getName() { return name; }
    public double getMaxAmount() { return maxAmount; }
    public List<Integer> getPayments() { return payments; }


}
