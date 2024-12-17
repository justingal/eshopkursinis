package com.coursework.eshop.fxController.MainShop.Util.ProductProcessors;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.model.ProductType;

public class ProductProcessorFactory {
    public static ProductProcessor getProcessor(ProductType type, CustomHib customHib) {
        return switch (type) {
            case BOARD_GAME -> new BoardGameProcessor(customHib);
            case PUZZLE -> new PuzzleProcessor(customHib);
            case DICE -> new DiceProcessor(customHib);
            default -> throw new IllegalArgumentException("Unknown product type: " + type);
        };
    }
}
