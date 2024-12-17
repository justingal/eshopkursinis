package com.coursework.eshop.fxController.MainShop.Util.ProductProcessors;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.model.BoardGame;
import com.coursework.eshop.model.CartItem;
import com.coursework.eshop.model.CustomerOrder;

public class BoardGameProcessor implements ProductProcessor {
    private final CustomHib customHib;

    public BoardGameProcessor(CustomHib customHib) {
        this.customHib = customHib;
    }

    @Override
    public double process(CartItem item, CustomerOrder order) {
        BoardGame boardGame = customHib.getEntityById(BoardGame.class, item.getProductId());
        if (boardGame != null) {
            order.getInOrderBoardGames().add(boardGame);
            return boardGame.getPrice();
        }
        return 0.0;
    }
}

