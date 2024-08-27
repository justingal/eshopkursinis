package com.coursework.eshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Customer extends User {

    private String address;
    private String cardNo;
    @OneToMany(mappedBy = "customer")
    private List<CustomerOrder> userCustomerOrder;

    public Customer() {

    }


    public Customer(String login, String password, LocalDate birthDate, String name, String surname) {
        super(login, password, birthDate, name, surname);
    }

    public Customer(String login, String password, LocalDate birthDate, String name, String surname, String address, String cardNo, List<CustomerOrder> userCustomerOrder) {
        super(login, password, birthDate, name, surname);
        this.address = address;
        this.cardNo = cardNo;
        this.userCustomerOrder = userCustomerOrder;
    }


    public Customer(String login, String password, LocalDate birthDate, String name, String surname, String address, String cardNo) {
        super(login, password, birthDate, name, surname);
        this.address = address;
        this.cardNo = cardNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public List<CustomerOrder> getUserCustomerOrder() {
        return userCustomerOrder;
    }

    public void setUserCustomerOrder(List<CustomerOrder> userCustomerOrder) {
        this.userCustomerOrder = userCustomerOrder;
    }

}