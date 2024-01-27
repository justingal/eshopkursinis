package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.model.Cart;
import com.coursework.eshop.model.Product;
import com.coursework.eshop.model.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;

import java.util.List;

public class CartTabController {


    public ListView <Cart> cartList;
    public Label priceLabel;
    private CustomHib customHib;
    public List<Product> productCart;
    public void setData(CustomHib customHib) {
        this.customHib = customHib;
    }



}
