package com.coursework.eshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Manager extends User {

    private String employeeId;
    private String medCertificate;
    private LocalDate employmentDate;

    public Manager(String login, String password, LocalDate birthDate, String name, String surname, String employeeId, String medCertificate, LocalDate employmentDate) {
        super(login, password, birthDate, name, surname);
        this.employeeId = employeeId;
        this.medCertificate = medCertificate;
        this.employmentDate = employmentDate;
    }

}