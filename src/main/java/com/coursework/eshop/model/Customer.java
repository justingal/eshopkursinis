package com.coursework.eshop.model;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
public class Customer extends User {
    public Customer() {

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

    public CustomerOrder getUserCustomerOrder() {
        return userCustomerOrder;
    }

    public void setUserCustomerOrder(CustomerOrder userCustomerOrder) {
        this.userCustomerOrder = userCustomerOrder;
    }

    public Customer(String login, String password, LocalDate birthDate, String name, String surname) {
        super(login, password, birthDate, name, surname);
    }

    public Customer(String login, String password, LocalDate birthDate, String name, String surname, String address, String cardNo, CustomerOrder userCustomerOrder) {
        super(login, password, birthDate, name, surname);
        this.address = address;
        this.cardNo = cardNo;
        this.userCustomerOrder = userCustomerOrder;
    }

    private String address;
    private String cardNo;
    @OneToOne
    @JoinColumn(name = "user_order_id")
    CustomerOrder userCustomerOrder;

    public Customer(String login, String password, LocalDate birthDate, String name, String surname, String address, String cardNo) {
        super(login, password, birthDate, name, surname);
        this.address = address;
        this.cardNo = cardNo;
    }



}