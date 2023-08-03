package com.mindhub.homebanking.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Client {

    // atributos
    @Id
    private int id;
    private String email;
    private String firstName;
    private String lastName;


    // constructores
    public Client() {}

    public Client(int id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    // getter & setter
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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


}
