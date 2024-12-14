package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import mockit.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTabControllerTest {

    @Mocked
    private CustomHib customHib;

    private ProductTabController controller;

    @BeforeAll
    static void initToolkit() {
        if (!Platform.isFxApplicationThread()) {
            Platform.startup(() -> {});
        }
    }

    @BeforeEach
    void setup() {
        controller = new ProductTabController();

        controller.productTitleField = new TextField();
        controller.descriptionField = new TextArea();
        controller.authorField = new TextField();
        controller.productType = new ComboBox<>(FXCollections.observableArrayList(ProductType.values()));
        controller.warehouseComboBox = new ComboBox<>(FXCollections.observableArrayList());
        controller.priceField = new TextField();
        controller.playersQuantityField = new TextField();
        controller.gameDurationFIeld = new TextField();
        controller.piecesQuantityField = new TextField();
        controller.puzzleMaterialField = new TextField();
        controller.puzzleSizeField = new TextField();
        controller.diceNumberField = new TextField();
        controller.productListManager = new ListView<>();

        controller.setData(customHib);

        new MockUp<JavaFxCustomsUtils>() {
            @Mock
            public void generateAlert(Alert.AlertType alertType, String title, String header, String content) {
            }
        };
    }

    @Test
    void testAddNewProduct_MissingFields() {
        new Expectations() {{
            controller.productTitleField.getText(); result = " ";
            controller.descriptionField.getText(); result = "Description";
            controller.authorField.getText(); result = "Author";
            controller.warehouseComboBox.getSelectionModel().getSelectedItem(); result = null;
            controller.priceField.getText(); result = "25.00";
        }};

        controller.addNewProduct();

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Missing Information", "Please fill all required fields correctly.");
            times = 1;
        }};
    }

    @Test
    void testAddNewProduct_InvalidPriceFormat() {
        new Expectations() {{
            controller.productTitleField.getText(); result = "Product Title";
            controller.descriptionField.getText(); result = "Description";
            controller.authorField.getText(); result = "Author";
            controller.warehouseComboBox.getSelectionModel().getSelectedItem(); result = new Warehouse();
            controller.priceField.getText(); result = "InvalidPrice";
        }};

        controller.addNewProduct();

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Invalid Format", "Please enter valid numbers for price, quantities or other numeric fields.");
            times = 1;
        }};
    }

    @Test
    void testAddNewProduct_BoardGameCreation() {
        Warehouse warehouse = new Warehouse();
        new Expectations() {{
            controller.productTitleField.getText(); result = "Board Game";
            controller.descriptionField.getText(); result = "A fun board game";
            controller.authorField.getText(); result = "Game Author";
            controller.warehouseComboBox.getSelectionModel().getSelectedItem(); result = warehouse;
            controller.priceField.getText(); result = "49.99";
            controller.productType.getSelectionModel().getSelectedItem(); result = ProductType.BOARD_GAME;
            controller.playersQuantityField.getText(); result = "4";
            controller.gameDurationFIeld.getText(); result = "60";
            customHib.getEntityById(Warehouse.class, warehouse.getId()); result = warehouse;
        }};

        controller.addNewProduct();

        new Verifications() {{
            customHib.create((BoardGame) any);
            times = 1;
        }};
    }

    @Test
    void testAddNewProduct_PuzzleCreation() {
        Warehouse warehouse = new Warehouse();

        new Expectations() {{
            controller.productTitleField.getText(); result = "Puzzle";
            controller.descriptionField.getText(); result = "A challenging puzzle";
            controller.authorField.getText(); result = "Puzzle Creator";
            controller.priceField.getText(); result = "29.99";
            controller.piecesQuantityField.getText(); result = "500";
            controller.puzzleMaterialField.getText(); result = "Cardboard";
            controller.puzzleSizeField.getText(); result = "20x30";
            controller.productType.getSelectionModel().getSelectedItem(); result = ProductType.PUZZLE;
            controller.warehouseComboBox.getSelectionModel().getSelectedItem(); result = warehouse;
            customHib.getEntityById(Warehouse.class, warehouse.getId()); result = warehouse;
        }};

        controller.addNewProduct();

        new Verifications() {{
            customHib.create(withInstanceOf(Puzzle.class));
            times = 1;
        }};
    }

    @Test
    void testAddNewProduct_DiceCreation() {
        Warehouse warehouse = new Warehouse();
        new Expectations() {{
            controller.productTitleField.getText(); result = "Dice Set";
            controller.descriptionField.getText(); result = "A set of dice";
            controller.authorField.getText(); result = "Dice Maker";
            controller.warehouseComboBox.getSelectionModel().getSelectedItem(); result = warehouse;
            controller.priceField.getText(); result = "19.99";
            controller.productType.getSelectionModel().getSelectedItem(); result = ProductType.DICE;
            controller.diceNumberField.getText(); result = "10";
            customHib.getEntityById(Warehouse.class, warehouse.getId()); result = warehouse;
        }};

        controller.addNewProduct();

        new Verifications() {{
            customHib.create((Dice) any);
            times = 1;
        }};
    }
}