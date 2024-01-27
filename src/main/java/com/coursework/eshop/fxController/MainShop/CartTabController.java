package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.model.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;

public class CartTabController {


    private CustomHib customHib;

    public void setData(CustomHib customHib) {
        this.customHib = customHib;
    }

}
