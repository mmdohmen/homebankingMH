package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {

    // atributos
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    @OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();


    // constructores
    public Client() {}

    public Client(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    // getter & setter

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }
    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    // metodos
    public void addAccount (Account account) {
        account.setCliente(this);
        accounts.add(account);
    }
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }


}
