package com.coursework.eshop.fxController.tableviews;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class UserTableParameters {
    SimpleIntegerProperty id = new SimpleIntegerProperty();
    SimpleStringProperty login = new SimpleStringProperty();
    SimpleStringProperty password = new SimpleStringProperty();

    public UserTableParameters(SimpleIntegerProperty id, SimpleStringProperty login, SimpleStringProperty password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public UserTableParameters() {
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public String getLogin() {
        return login.get();
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public SimpleStringProperty loginProperty() {
        return login;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }
}