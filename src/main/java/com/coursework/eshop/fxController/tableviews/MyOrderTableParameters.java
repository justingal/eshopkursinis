package com.coursework.eshop.fxController.tableviews;

import com.coursework.eshop.model.OrderStatus;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class MyOrderTableParameters {
    private final SimpleIntegerProperty id;
    private final SimpleObjectProperty<LocalDate> dateCreated;
    private final SimpleStringProperty customerName;
    private final SimpleObjectProperty<OrderStatus> orderStatus;

    public MyOrderTableParameters(int id, LocalDate dateCreated, String customerName, OrderStatus orderStatus) {
        this.id = new SimpleIntegerProperty(id);
        this.dateCreated = new SimpleObjectProperty<>(dateCreated);
        this.customerName = new SimpleStringProperty(customerName);
        this.orderStatus = new SimpleObjectProperty<>(orderStatus);
    }

    public int getId() {
        return id.get();
    }

    public LocalDate getDateCreated() {
        return dateCreated.get();
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public OrderStatus getOrderStatus() {
        return orderStatus.get();
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus.set(orderStatus);
    }
}
