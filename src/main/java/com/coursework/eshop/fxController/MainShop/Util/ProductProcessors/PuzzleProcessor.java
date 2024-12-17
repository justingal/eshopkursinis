package com.coursework.eshop.fxController.MainShop.Util.ProductProcessors;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.model.CartItem;
import com.coursework.eshop.model.CustomerOrder;
import com.coursework.eshop.model.Puzzle;

public class PuzzleProcessor implements ProductProcessor {
    private final CustomHib customHib;

    public PuzzleProcessor(CustomHib customHib) {
        this.customHib = customHib;
    }

    @Override
    public double process(CartItem item, CustomerOrder order) {
        Puzzle puzzle = customHib.getEntityById(Puzzle.class, item.getProductId());
        if (puzzle != null) {
            order.getInOrderPuzzles().add(puzzle);
            return puzzle.getPrice();
        }
        return 0.0;
    }
}
