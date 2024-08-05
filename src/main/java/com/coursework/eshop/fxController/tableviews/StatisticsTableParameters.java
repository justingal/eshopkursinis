package com.coursework.eshop.fxController.tableviews;

import com.coursework.eshop.model.Customer;
import com.coursework.eshop.model.Manager;
import com.coursework.eshop.model.OrderStatus;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class StatisticsTableParameters {

    private final SimpleIntegerProperty orderId;
    private final SimpleObjectProperty<Manager> manager;
    private final SimpleObjectProperty<Customer> customer;
    private final SimpleObjectProperty<LocalDate> orderDate;
    private final SimpleDoubleProperty orderValue;
    private final SimpleObjectProperty<OrderStatus> orderStatus;


    public StatisticsTableParameters(int orderId, Manager manager, Customer customer, LocalDate orderDate, double orderValue, OrderStatus orderStatus) {
        this.orderId = new SimpleIntegerProperty(orderId);
        this.manager = new SimpleObjectProperty<>(manager);
        this.customer = new SimpleObjectProperty<>(customer);
        this.orderDate = new SimpleObjectProperty<>(orderDate);
        this.orderValue = new SimpleDoubleProperty(orderValue);
        this.orderStatus = new SimpleObjectProperty<>(orderStatus);
    }

    public int getOrderId() {
        return orderId.get();
    }

    public SimpleIntegerProperty orderIdProperty() {
        return orderId;
    }


    public OrderStatus getOrderStatus() {
        return orderStatus.get();
    }

    public SimpleObjectProperty<OrderStatus> orderStatusProperty() {
        return orderStatus;
    }

    public LocalDate getOrderDate() {
        return orderDate.get();
    }

    public SimpleObjectProperty<LocalDate> orderDateProperty() {
        return orderDate;
    }

    public double getOrderValue() {
        return orderValue.get();
    }

    public SimpleDoubleProperty orderValueProperty() {
        return orderValue;
    }

    public Manager getManager() {
        return manager.get();
    }

    public SimpleObjectProperty<Manager> managerProperty() {
        return manager;
    }

    public Customer getCustomer() {
        return customer.get();
    }

    public SimpleObjectProperty<Customer> customerProperty() {
        return customer;
    }
}
