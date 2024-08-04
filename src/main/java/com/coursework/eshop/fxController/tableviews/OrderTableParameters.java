package com.coursework.eshop.fxController.tableviews;

import com.coursework.eshop.model.Manager;
import com.coursework.eshop.model.OrderStatus;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import net.bytebuddy.asm.Advice;

import java.time.LocalDate;

public class OrderTableParameters {
    private final SimpleIntegerProperty id;
    private final SimpleObjectProperty<LocalDate> dateCreated;
    private final SimpleStringProperty customerName;
    private final SimpleObjectProperty<OrderStatus> orderStatus;
    private final SimpleObjectProperty<Manager> manager;

    public OrderTableParameters(int id, LocalDate dateCreated, String customerName, OrderStatus orderStatus, Manager manager) {
        this.id = new SimpleIntegerProperty(id);
        this.dateCreated = new SimpleObjectProperty<>(dateCreated);
        this.customerName = new SimpleStringProperty(customerName);
        this.orderStatus = new SimpleObjectProperty<>(orderStatus);
        this.manager = new SimpleObjectProperty<>(manager);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public LocalDate getDateCreated() {
        return dateCreated.get();
    }

    public SimpleObjectProperty<LocalDate> dateCreatedProperty() {
        return dateCreated;
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public SimpleStringProperty customerNameProperty() {
        return customerName;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus.get();
    }

    public SimpleObjectProperty<OrderStatus> orderStatusProperty() {
        return orderStatus;
    }

    public Manager getManager() {
        return manager.get();
    }

    public SimpleObjectProperty<Manager> managerProperty() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager.set(manager);
    }

    public void setOrderStatus(OrderStatus orderStatus){
        this.orderStatus.set(orderStatus);
    }
}
