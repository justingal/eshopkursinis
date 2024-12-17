package com.coursework.eshop.fxController.MainShop.Util.ProductProcessors;

import com.coursework.eshop.model.CartItem;
import com.coursework.eshop.model.CustomerOrder;

public interface ProductProcessor {
    double process(CartItem item, CustomerOrder order);
}

