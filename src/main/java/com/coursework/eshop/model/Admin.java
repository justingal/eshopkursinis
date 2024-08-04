package com.coursework.eshop.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;


@Entity
@DiscriminatorValue("Admin")
public class Admin extends Manager {
    public Admin(String employeeId, String medCertificate, LocalDate employmentDate) {
        super(employeeId, medCertificate, employmentDate);
    }

    public Admin(String login, String password, LocalDate birthDate, String name, String surname) {
        super(login, password, birthDate, name, surname);
    }

    public Admin(String login, String password, LocalDate birthDate, String name, String surname, String employeeId, String medCertificate, LocalDate employmentDate) {
        super(login, password, birthDate, name, surname, employeeId, medCertificate, employmentDate);

    }

    public Admin() {
        super();
    }
}