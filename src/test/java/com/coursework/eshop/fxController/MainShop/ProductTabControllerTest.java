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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ProductTabControllerTest {

    @Mocked
    private CustomHib customHib;

    @Injectable
    private Warehouse warehouse;

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

        warehouse = new Warehouse();

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
    void testLoadProductListManager_WithMockedData() {
        Puzzle mockedPuzzle = new Puzzle("Mock Puzzle", "Mock Description", "Mock Author", warehouse, 20.00, 500, "Cardboard", "20x30");

        new Expectations() {{
            customHib.getAllRecords(Puzzle.class); result = List.of(mockedPuzzle);
        }};

        controller.loadProductListManager();

        assertEquals(1, controller.productListManager.getItems().size(), "The product list should contain one item.");
        assertInstanceOf(Puzzle.class, controller.productListManager.getItems().getFirst(), "The item should be a Puzzle.");

        Puzzle actualPuzzle = (Puzzle) controller.productListManager.getItems().getFirst();
        assertEquals("Mock Puzzle", actualPuzzle.getTitle(), "Puzzle title should match.");
        assertEquals("Mock Description", actualPuzzle.getDescription(), "Puzzle description should match.");
    }

    @Test
    void testAddNewProduct_BoardGameCreation() {
        Warehouse mockWarehouse = new Warehouse(1, "Mock Warehouse", "Mock Address", null, null, null);
        new Expectations() {{
            customHib.getEntityById(Warehouse.class, 1);
            result = mockWarehouse;

            customHib.getAllRecords(BoardGame.class); result = new ArrayList<BoardGame>();
            customHib.getAllRecords(Puzzle.class); result = new ArrayList<Puzzle>();
            customHib.getAllRecords(Dice.class); result = new ArrayList<Dice>();
        }};

        controller.authorField.setText("Board Game Author");
        controller.productTitleField.setText("New Board Game");
        controller.descriptionField.setText("New Description");
        controller.priceField.setText("30.00");
        controller.playersQuantityField.setText("2-4");
        controller.gameDurationFIeld.setText("30-60");
        controller.productType.getSelectionModel().select(ProductType.BOARD_GAME);

        controller.warehouseComboBox.getItems().add(mockWarehouse);
        controller.warehouseComboBox.getSelectionModel().select(mockWarehouse);

        controller.addNewProduct();

        new Verifications() {{
            BoardGame createdBoardGame;
            customHib.create(createdBoardGame = withCapture());

            assertNotNull(createdBoardGame, "Board Game should not be null");
            assertEquals("New Board Game", createdBoardGame.getTitle(), "Board Game title mismatch");
            assertEquals("New Description", createdBoardGame.getDescription(), "Board Game description mismatch");
            assertEquals("Board Game Author", createdBoardGame.getAuthor(), "Board Game author mismatch");
            assertEquals(30.00, createdBoardGame.getPrice(), 0.01, "Board Game price mismatch");
            assertEquals("2-4", createdBoardGame.getPlayersQuantity(), "Board Game players quantity mismatch");
            assertEquals("30-60", createdBoardGame.getGameDuration(), "Board Game game duration mismatch");
            assertEquals(mockWarehouse, createdBoardGame.getWarehouse(), "Warehouse mismatch");
        }};
    }

    @Test
    void testAddNewProduct_CreatesPuzzle() {
        Warehouse mockWarehouse = new Warehouse(1, "Mock Warehouse", "Mock Address", null, null, null);
        new Expectations() {{


            customHib.getEntityById(Warehouse.class, 1);
            result = mockWarehouse;

            customHib.getAllRecords(BoardGame.class); result = new ArrayList<BoardGame>();
            customHib.getAllRecords(Puzzle.class); result = new ArrayList<Puzzle>();
            customHib.getAllRecords(Dice.class); result = new ArrayList<Dice>();

        }};

        controller.authorField.setText("Puzzle Author");
        controller.productTitleField.setText("New Puzzle");
        controller.descriptionField.setText("New Description");
        controller.priceField.setText("20.00");
        controller.piecesQuantityField.setText("500");
        controller.puzzleMaterialField.setText("Cardboard");
        controller.puzzleSizeField.setText("30x40");
        controller.productType.getSelectionModel().select(ProductType.PUZZLE);

        controller.warehouseComboBox.getItems().add(mockWarehouse);
        controller.warehouseComboBox.getSelectionModel().select(mockWarehouse);

        System.out.println("Warehouse Mock Debug:");
        System.out.println("Warehouse ID: " + mockWarehouse.getId());
        System.out.println("lmao" + controller.authorField.getText());
        System.out.println("Warehouse from ComboBox: " + controller.warehouseComboBox.getSelectionModel().getSelectedItem());
        System.out.println("Warehouse retrieved by ID: " + customHib.getEntityById(Warehouse.class, 1));



        controller.addNewProduct();

        new Verifications() {{
            Puzzle createdPuzzle;
            customHib.create(createdPuzzle = withCapture());

            assertNotNull(createdPuzzle, "Puzzle should not be null");
            assertEquals("New Puzzle", createdPuzzle.getTitle(), "Puzzle title mismatch");
            assertEquals("New Description", createdPuzzle.getDescription(), "Puzzle description mismatch");
            assertEquals("Puzzle Author", createdPuzzle.getAuthor(), "Puzzle author mismatch");
            assertEquals(20.00, createdPuzzle.getPrice(), 0.01, "Puzzle price mismatch");
            assertEquals(500, createdPuzzle.getPiecesQuantity(), "Puzzle pieces quantity mismatch");
            assertEquals("Cardboard", createdPuzzle.getPuzzleMaterial(), "Puzzle material mismatch");
            assertEquals("30x40", createdPuzzle.getPuzzleSize(), "Puzzle size mismatch");
            assertEquals(mockWarehouse, createdPuzzle.getWarehouse(), "Warehouse mismatch");
        }};
    }



    @Test
    void testAddNewProduct_DiceCreation() {
        Warehouse mockWarehouse = new Warehouse(1, "Mock Warehouse", "Mock Address", null, null, null);
        new Expectations() {{
            customHib.getEntityById(Warehouse.class, 1);
            result = mockWarehouse;

            customHib.getAllRecords(BoardGame.class); result = new ArrayList<BoardGame>();
            customHib.getAllRecords(Puzzle.class); result = new ArrayList<Puzzle>();
            customHib.getAllRecords(Dice.class); result = new ArrayList<Dice>();
        }};

        controller.authorField.setText("Dice Author");
        controller.productTitleField.setText("New Dice");
        controller.descriptionField.setText("New Description");
        controller.priceField.setText("5.00");
        controller.diceNumberField.setText("6");
        controller.productType.getSelectionModel().select(ProductType.DICE);

        controller.warehouseComboBox.getItems().add(mockWarehouse);
        controller.warehouseComboBox.getSelectionModel().select(mockWarehouse);

        controller.addNewProduct();

        new Verifications() {{
            Dice createdDice;
            customHib.create(createdDice = withCapture());

            assertNotNull(createdDice, "Dice should not be null");
            assertEquals("New Dice", createdDice.getTitle(), "Dice title mismatch");
            assertEquals("New Description", createdDice.getDescription(), "Dice description mismatch");
            assertEquals("Dice Author", createdDice.getAuthor(), "Dice author mismatch");
            assertEquals(5.00, createdDice.getPrice(), 0.01, "Dice price mismatch");
            assertEquals(6, createdDice.getDiceNumber(), "Dice number mismatch");
            assertEquals(mockWarehouse, createdDice.getWarehouse(), "Warehouse mismatch");
        }};
    }

    @Test
    void testAddNewProduct_MissingPuzzleSpecificFields() {
        Warehouse mockWarehouse = new Warehouse(1, "Mock Warehouse", "Mock Address", null, null, null);
        new Expectations() {{
            customHib.getEntityById(Warehouse.class, 1);
            result = mockWarehouse;
        }};

        controller.productType.getSelectionModel().select(ProductType.PUZZLE);
        controller.authorField.setText("Puzzle Author");
        controller.productTitleField.setText("Puzzle Title");
        controller.descriptionField.setText("Puzzle Description");
        controller.priceField.setText("20.00");
        controller.piecesQuantityField.setText("500");
        controller.puzzleMaterialField.setText("");
        controller.puzzleSizeField.setText("");
        controller.warehouseComboBox.getItems().add(mockWarehouse);
        controller.warehouseComboBox.getSelectionModel().select(mockWarehouse);

        controller.addNewProduct();

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Puzzle specific fields are empty.", "Please complete all fields properly.");
            times = 1;
        }};
    }

    @Test
    void testAddNewProduct_MissingBoardGameSpecificFields() {
        Warehouse mockWarehouse = new Warehouse(1, "Mock Warehouse", "Mock Address", null, null, null);
        new Expectations() {{
            customHib.getEntityById(Warehouse.class, 1);
            result = mockWarehouse;
        }};

        controller.productType.getSelectionModel().select(ProductType.BOARD_GAME);
        controller.authorField.setText("Board Game Author");
        controller.productTitleField.setText("Board Game Title");
        controller.descriptionField.setText("Board Game Description");
        controller.priceField.setText("40.00");
        controller.playersQuantityField.setText("");
        controller.gameDurationFIeld.setText("");
        controller.warehouseComboBox.getItems().add(mockWarehouse);
        controller.warehouseComboBox.getSelectionModel().select(mockWarehouse);

        controller.addNewProduct();

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Board game specific fields are empty.", "Please complete all fields properly.");
            times = 1;
        }};
    }

    @Test
    void testAddNewProduct_InvalidNumberFormat() {
        Warehouse mockWarehouse = new Warehouse(1, "Mock Warehouse", "Mock Address", null, null, null);
        new Expectations() {{
            customHib.getEntityById(Warehouse.class, 1);
            result = mockWarehouse;

            customHib.getAllRecords(BoardGame.class); result = new ArrayList<BoardGame>();
            customHib.getAllRecords(Puzzle.class); result = new ArrayList<Puzzle>();
            customHib.getAllRecords(Dice.class); result = new ArrayList<Dice>();
        }};

        controller.productType.getSelectionModel().select(ProductType.DICE);
        controller.authorField.setText("Dice Author");
        controller.productTitleField.setText("Dice Title");
        controller.descriptionField.setText("Dice Description");
        controller.priceField.setText("InvalidNumber");
        controller.diceNumberField.setText("6");
        controller.warehouseComboBox.getItems().add(mockWarehouse);
        controller.warehouseComboBox.getSelectionModel().select(mockWarehouse);

        controller.addNewProduct();

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Invalid Format", "Please enter valid numbers for price, quantities or other numeric fields.");
            times = 1;

            customHib.getEntityById(Warehouse.class, anyInt);
            times = 1;
        }};
    }

    @Test
    void testAddNewProduct_GenericIllegalArgumentException() {
        Warehouse mockWarehouse = new Warehouse(1, "Mock Warehouse", "Mock Address", null, null, null);
        new Expectations() {{
            customHib.getEntityById(Warehouse.class, 1);
            result = mockWarehouse;
        }};

        controller.productType.getSelectionModel().select(ProductType.BOARD_GAME);
        controller.authorField.setText("Board Game Author");
        controller.productTitleField.setText("");
        controller.descriptionField.setText("Board Game Description");
        controller.priceField.setText("50.00");
        controller.playersQuantityField.setText("2-4");
        controller.gameDurationFIeld.setText("30-60");
        controller.warehouseComboBox.getItems().add(mockWarehouse);
        controller.warehouseComboBox.getSelectionModel().select(mockWarehouse);

        controller.addNewProduct();

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Missing Information", "Please fill all required fields correctly.");
            times = 1;

            customHib.getEntityById(Warehouse.class, anyInt);
            times = 0;
        }};
    }

    @Test
    void testUpdateProduct_NoSelection() {
        controller.productListManager.getSelectionModel().clearSelection();

        controller.updateProduct();

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "No Selection", "No product selected", "Please select a product to update.");
            times = 1;
        }};
    }

    @Test
    void testUpdateProduct_MissingFields() {
        Product mockProduct = new BoardGame("Existing Title", "Existing Description", "Existing Author", warehouse, 50.00, "2-4", "30-60");
        controller.productListManager.getItems().add(mockProduct);
        controller.productListManager.getSelectionModel().select(mockProduct);

        controller.productTitleField.setText("");
        controller.descriptionField.setText("Updated Description");
        controller.authorField.setText("Updated Author");
        controller.warehouseComboBox.getSelectionModel().clearSelection();
        controller.priceField.setText("40.00");

        controller.updateProduct();

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Missing Information", "Please fill all required fields.");
            times = 1;
        }};
    }

    @Test
    void testUpdateProduct_MissingPuzzleSpecificFields() {
        Product mockProduct = new Puzzle("Existing Title", "Existing Description", "Existing Author", warehouse, 20.00, 500, "Cardboard", "30x40");
        controller.productListManager.getItems().add(mockProduct);
        controller.productListManager.getSelectionModel().select(mockProduct);

        controller.productType.getSelectionModel().select(ProductType.PUZZLE);
        controller.productTitleField.setText("Updated Title");
        controller.descriptionField.setText("Updated Description");
        controller.authorField.setText("Updated Author");
        controller.priceField.setText("20.00");
        controller.piecesQuantityField.setText("1000");
        controller.puzzleMaterialField.setText(""); // Empty
        controller.puzzleSizeField.setText("");     // Empty
        controller.warehouseComboBox.getItems().add(warehouse);
        controller.warehouseComboBox.getSelectionModel().select(warehouse);

        controller.updateProduct();

        new Verifications() {{
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Error", "Puzzle specific fields cannot be empty.");
            times = 1;
        }};
    }

    @Test
    void testUpdateProduct_SuccessfulBoardGameUpdate() {
        BoardGame mockBoardGame = new BoardGame("Old Title", "Old Description", "Old Author", warehouse, 50.00, "2-4", "30-60");
        controller.productListManager.getItems().add(mockBoardGame);
        controller.productListManager.getSelectionModel().select(mockBoardGame);

        new Expectations() {{
            customHib.getEntityById(BoardGame.class, mockBoardGame.getId());
            result = mockBoardGame;
        }};

        controller.productType.getSelectionModel().select(ProductType.BOARD_GAME);
        controller.productTitleField.setText("Updated Title");
        controller.descriptionField.setText("Updated Description");
        controller.authorField.setText("Updated Author");
        controller.priceField.setText("60.00");
        controller.playersQuantityField.setText("3-5");
        controller.gameDurationFIeld.setText("40-80");
        controller.warehouseComboBox.getItems().add(warehouse);
        controller.warehouseComboBox.getSelectionModel().select(warehouse);

        controller.updateProduct();

        new Verifications() {{
            customHib.update(mockBoardGame);
            times = 1;

            assertEquals("Updated Title", mockBoardGame.getTitle());
            assertEquals("Updated Description", mockBoardGame.getDescription());
            assertEquals("Updated Author", mockBoardGame.getAuthor());
            assertEquals(60.00, mockBoardGame.getPrice(), 0.01);
            assertEquals("3-5", mockBoardGame.getPlayersQuantity());
            assertEquals("40-80", mockBoardGame.getGameDuration());
        }};
    }



    @Test
    void testUpdateProduct_SuccessfulPuzzleUpdate() {
        Puzzle mockPuzzle = new Puzzle("Old Title", "Old Description", "Old Author", warehouse, 30.00, 500, "Cardboard", "30x40");
        controller.productListManager.getItems().add(mockPuzzle);
        controller.productListManager.getSelectionModel().select(mockPuzzle);

        new Expectations() {{
            customHib.getEntityById(Puzzle.class, mockPuzzle.getId());
            result = mockPuzzle;
        }};

        controller.productType.getSelectionModel().select(ProductType.PUZZLE);
        controller.productTitleField.setText("Updated Title");
        controller.descriptionField.setText("Updated Description");
        controller.authorField.setText("Updated Author");
        controller.priceField.setText("35.00");
        controller.piecesQuantityField.setText("1000");
        controller.puzzleMaterialField.setText("Plastic");
        controller.puzzleSizeField.setText("40x60");
        controller.warehouseComboBox.getItems().add(warehouse);
        controller.warehouseComboBox.getSelectionModel().select(warehouse);

        controller.updateProduct();

        new Verifications() {{
            customHib.update(mockPuzzle);
            times = 1;

            assertEquals("Updated Title", mockPuzzle.getTitle());
            assertEquals("Updated Description", mockPuzzle.getDescription());
            assertEquals("Updated Author", mockPuzzle.getAuthor());
            assertEquals(35.00, mockPuzzle.getPrice(), 0.01);
            assertEquals(1000, mockPuzzle.getPiecesQuantity());
            assertEquals("Plastic", mockPuzzle.getPuzzleMaterial());
            assertEquals("40x60", mockPuzzle.getPuzzleSize());
        }};
    }




}