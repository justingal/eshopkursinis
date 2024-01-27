package com.coursework.eshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Customer extends User {
    private String address;
    private String cardNo;
    @OneToOne
    @JoinColumn(name = "myCart_id")
    Cart myCart;

    public Customer(String login, String password, LocalDate birthDate, String name, String surname, String address, String cardNo) {
        super(login, password, birthDate, name, surname);
        this.address = address;
        this.cardNo = cardNo;
    }



}