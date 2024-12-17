package com.coursework.eshop.fxController.MainShop.Util.ProductProcessors;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.model.CartItem;
import com.coursework.eshop.model.CustomerOrder;
import com.coursework.eshop.model.Dice;

public class DiceProcessor implements ProductProcessor {
    private final CustomHib customHib;

    public DiceProcessor(CustomHib customHib) {
        this.customHib = customHib;
    }

    @Override
    public double process(CartItem item, CustomerOrder order) {
        Dice dice = customHib.getEntityById(Dice.class, item.getProductId());
        if (dice != null) {
            order.getInOrderDices().add(dice);
            return dice.getPrice();
        }
        return 0.0;
    }
}

