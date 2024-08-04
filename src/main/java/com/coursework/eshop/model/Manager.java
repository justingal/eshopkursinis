package com.coursework.eshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;


@Entity
public class Manager extends User {
    @OneToMany
    private List<CustomerOrder> customerOrders;
    private String employeeId;
    private String medCertificate;
    private LocalDate employmentDate;

    public Manager() {

    }

    public Manager(String login, String password, LocalDate birthDate, String name, String surname) {
        super(login, password, birthDate, name, surname);
    }

    public Manager(String employeeId, String medCertificate, LocalDate employmentDate) {
        this.employeeId = employeeId;
        this.medCertificate = medCertificate;
        this.employmentDate = employmentDate;
    }

    public Manager(String login, String password, LocalDate birthDate, String name, String surname, String employeeId, String medCertificate, LocalDate employmentDate) {
        super(login, password, birthDate, name, surname);
        this.employeeId = employeeId;
        this.medCertificate = medCertificate;
        this.employmentDate = employmentDate;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getMedCertificate() {
        return medCertificate;
    }

    public void setMedCertificate(String medCertificate) {
        this.medCertificate = medCertificate;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate = employmentDate;
    }

}