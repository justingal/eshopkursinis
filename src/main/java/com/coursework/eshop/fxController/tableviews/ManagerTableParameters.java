package com.coursework.eshop.fxController.tableviews;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ManagerTableParameters extends UserTableParameters {

    private SimpleStringProperty employeeId = new SimpleStringProperty();

    public ManagerTableParameters(SimpleIntegerProperty id, SimpleStringProperty login, SimpleStringProperty password, SimpleStringProperty employeeId) {
        super(id, login, password);
        this.employeeId = employeeId;
    }

    public ManagerTableParameters() {
    }

    public String getEmployeeId() {
        return employeeId.get();
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId.set(employeeId);
    }

    public SimpleStringProperty employeeIdProperty() {
        return employeeId;
    }
}
