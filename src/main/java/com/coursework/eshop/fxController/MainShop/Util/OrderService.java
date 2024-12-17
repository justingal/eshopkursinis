package com.coursework.eshop.fxController.MainShop.Util;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.fxController.MainShop.Util.ProductProcessors.ProductProcessor;
import com.coursework.eshop.fxController.MainShop.Util.ProductProcessors.ProductProcessorFactory;
import com.coursework.eshop.model.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class OrderService {
    private final CustomHib customHib;
    private final ShoppingCart cart;

    public OrderService(CustomHib customHib, ShoppingCart cart) {
        this.customHib = customHib;
        this.cart = cart;
    }

    public static boolean isValidUser(User user) {
        return user instanceof Customer || user instanceof Admin;
    }

    public CustomerOrder createOrder(Customer customer) {
        CustomerOrder customerOrder = initializeOrder(customer);

        double totalPrice = 0.0;
        for (CartItem item : cart.getItems()) {
            ProductProcessor processor = ProductProcessorFactory.getProcessor(item.getProductType(), customHib);
            totalPrice += processor.process(item, customerOrder);
        }

        customerOrder.setTotalPrice(totalPrice);
        customHib.create(customerOrder);
        return customerOrder;
    }

    private CustomerOrder initializeOrder(Customer customer) {
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setCustomer(customer);
        customerOrder.setDateCreated(LocalDate.now());
        customerOrder.setOrderStatus(OrderStatus.PENDING);
        customerOrder.setInOrderBoardGames(new ArrayList<>());
        customerOrder.setInOrderPuzzles(new ArrayList<>());
        customerOrder.setInOrderDices(new ArrayList<>());
        return customerOrder;
    }
}
