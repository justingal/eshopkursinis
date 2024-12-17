package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.model.*;
import javafx.application.Platform;
import javafx.scene.control.*;
import mockit.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CartTabControllerTest {

    @Mocked
    private CustomHib customHib;

    private CartTabController controller;

    @Injectable
    private ShoppingCart cart;

    @BeforeAll
    static void initToolkit() {
        if (!Platform.isFxApplicationThread()) {
            Platform.startup(() -> {});
        }
    }

    @BeforeEach
    void setup() {
        controller = new CartTabController();

        cart = new ShoppingCart();
        cart.setItems(new ArrayList<>());
        controller.cartItemList = new ListView<>();
        controller.priceLabel = new Label();
        controller.setData(customHib, cart);

        StartGui.currentUser = null;

        new MockUp<JavaFxCustomsUtils>() {
            @Mock
            public void generateAlert(Alert.AlertType alertType, String title, String header, String content) {
            }
        };
    }

    @Test
    void testMoveCartToOrder_InvalidUser() {
        StartGui.currentUser = new Manager();

        controller.moveCartToOrder(null);

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "User Error", "Current user is not a customer.");
            times = 1;
        }};
    }

    @Test
    void testMoveCartToOrder_EmptyCart() {
        StartGui.currentUser = new Customer();

        controller.moveCartToOrder(null);

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Success", "Order Created", "Your order has been successfully created.");
            times = 0;
        }};
    }

    @Test
    void testMoveCartToOrder_NullCart() {
        StartGui.currentUser = new Customer();
        controller.setData(customHib, null);

        controller.moveCartToOrder(null);

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Shopping Cart Error", "Shopping cart is not available.");
            times = 1;
        }};
    }

    @Test
    void testMoveCartToOrder_BoardGameOnly() {
        StartGui.currentUser = new Customer();
        BoardGame boardGame = new BoardGame("Game Title", "Description", "Author", new Warehouse(), 30.00, "2-4", "60");
        cart.getItems().add(new CartItem(boardGame.getId(), "", 30.00, ProductType.BOARD_GAME));

        new Expectations() {{
            customHib.getEntityById(BoardGame.class, boardGame.getId());
            result = boardGame;

            customHib.create((CustomerOrder) any);
        }};

        controller.moveCartToOrder(null);

        new Verifications() {{
            customHib.create((CustomerOrder) any);
            times = 1;

            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Success", "Order Created", "Your order has been successfully created.");
            times = 1;

            assertEquals(0, cart.getItems().size());
        }};
    }

    @Test
    void testMoveCartToOrder_MixedCartItems() {
        StartGui.currentUser = new Customer();

        Warehouse mockWarehouse = new Warehouse(1, "Mock Warehouse", "Mock Address", null, null, null);

        BoardGame boardGame = new BoardGame("Board Game", "Description", "Author", mockWarehouse, 30.00, "2-4", "60");
        boardGame.setId(1);

        Puzzle puzzle = new Puzzle("Puzzle", "Description", "Author", mockWarehouse, 20.00, 500, "Cardboard", "30x40");
        puzzle.setId(2);

        Dice dice = new Dice("Dice", "Description", "Author", mockWarehouse, 10.00, 6);
        dice.setId(3);

        cart.getItems().add(new CartItem(1, "Board Game", 30.00, ProductType.BOARD_GAME));
        cart.getItems().add(new CartItem(2, "Puzzle", 20.00, ProductType.PUZZLE));
        cart.getItems().add(new CartItem(3, "Dice", 10.00, ProductType.DICE));

        new Expectations() {{
            customHib.getEntityById(BoardGame.class, 1);
            result = boardGame;

            customHib.getEntityById(Puzzle.class, 2);
            result = puzzle;

            customHib.getEntityById(Dice.class, 3);
            result = dice;

            customHib.create((CustomerOrder) any);
        }};

        controller.moveCartToOrder(null);

        new Verifications() {{
            CustomerOrder createdOrder;
            customHib.create(createdOrder = withCapture());

            assertNotNull(createdOrder, "Order should not be null");
            assertEquals(1, createdOrder.getInOrderBoardGames().size(), "Should have 1 board game");
            assertEquals(1, createdOrder.getInOrderPuzzles().size(), "Should have 1 puzzle");
            assertEquals(1, createdOrder.getInOrderDices().size(), "Should have 1 dice");

            assertEquals(60.00, createdOrder.getTotalPrice(), 0.01, "Total price mismatch");

            assertEquals(0, cart.getItems().size(), "Cart should be empty");

            JavaFxCustomsUtils.generateAlert(Alert.AlertType.INFORMATION, "Success", "Order Created", "Your order has been successfully created.");
            times = 1;
        }};
    }

    @Test
    void testMoveCartToOrder_ExceptionDuringOrderCreation() {
        StartGui.currentUser = new Customer();

        BoardGame boardGame = new BoardGame("Board Game", "Description", "Author", new Warehouse(), 30.00, "2-4", "60");
        boardGame.setId(1);

        cart.getItems().add(new CartItem(1, "Board Game", 30.00, ProductType.BOARD_GAME));

        new Expectations() {{
            customHib.getEntityById(BoardGame.class, 1);
            result = boardGame;

            customHib.create((CustomerOrder) any);
            result = new RuntimeException("Database connection failure");
        }};

        controller.moveCartToOrder(null);

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Order Creation Error",
                    "There was an error creating the order: Database connection failure"
            );
            times = 1;
        }};
    }



}