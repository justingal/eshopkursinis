package com.coursework.eshop.HibernateControllers;

import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.model.*;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import mockit.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomHibTest {

    @Mocked
    private CustomHib customHib;

    @Injectable
    private EntityManager entityManager;

    @Injectable
    private EntityTransaction transaction;

    @BeforeAll
    static void initToolkit() {
        if (!Platform.isFxApplicationThread()) {
            Platform.startup(() -> {});
        }
    }

    @BeforeEach
    void setUp() {
        // Mocking entity manager and transaction
        new Expectations(customHib) {{
            customHib.getEntityManager();
            result = entityManager; // Mock the entity manager

            entityManager.getTransaction();
            result = transaction; // Mock the transaction
        }};

        // Mock transaction methods explicitly
        new Expectations() {{
            transaction.begin(); // Mock transaction begin
            transaction.commit(); // Mock transaction commit
        }};

        // Mock JavaFX alerts to avoid FX thread issues
        new MockUp<JavaFxCustomsUtils>() {
            @Mock
            public void generateAlert(Alert.AlertType alertType, String title, String header, String content) {
                // Prevent JavaFX thread issues during alert generation
            }
        };
    }

    @Test
    void testDeleteProduct_DiceWithoutWarehouse() {
        // Prepare test data
        Dice dice = new Dice();
        dice.setId(3);

        // Set up expectations
        new Expectations() {{
            // Expect finding the dice by ID
            entityManager.find(Dice.class, 3);
            result = dice;
        }};

        // Perform the delete operation
        customHib.deleteProduct(3, ProductType.DICE);

        // Verify interactions
        new Verifications() {{
            // Verify transaction management
            transaction.begin();
            transaction.commit();

            // Verify entity removal without warehouse interaction
            entityManager.remove(dice);
        }};
    }

    @Test
    void testDeleteProduct_PuzzleWithWarehouse() {
        // Prepare test data
        Puzzle puzzle = new Puzzle();
        puzzle.setId(2);

        Warehouse warehouse = new Warehouse();
        warehouse.setId(4);

        // Create a list of puzzles and set it to the warehouse
        List<Puzzle> inStockPuzzles = new ArrayList<>(Collections.singletonList(puzzle));
        warehouse.setInStockPuzzles(inStockPuzzles);

        // Set the warehouse for the puzzle
        puzzle.setWarehouse(warehouse);

        // Set up expectations
        new Expectations() {{
            // Expect finding the puzzle by ID
            entityManager.find(Puzzle.class, 2);
            result = puzzle;

            // Expect merge operation on warehouse
            entityManager.merge(warehouse);
        }};

        // Perform the delete operation
        customHib.deleteProduct(2, ProductType.PUZZLE);

        // Verify interactions
        new Verifications() {{
            // Verify transaction management
            transaction.begin();
            transaction.commit();

            // Verify puzzle removal from warehouse
            warehouse.getInStockPuzzles().remove(puzzle);

            // Verify entity manager operations
            entityManager.merge(warehouse);
            times = 1; // Ensure merge is called exactly once

            entityManager.remove(puzzle);
        }};
    }

    @Test
    void testDeleteProduct_BoardGameWithoutWarehouse() {
        // Create a mock BoardGame without a warehouse
        BoardGame boardGame = new BoardGame();
        boardGame.setId(1);

        new Expectations() {{
            entityManager.find(BoardGame.class, 1);
            result = boardGame; // Return the mock BoardGame
        }};

        // Execute the deleteProduct method
        customHib.deleteProduct(1, ProductType.BOARD_GAME);

        // Verify the correct operations were performed
        new Verifications() {{
            transaction.begin();
            entityManager.remove(boardGame);
            transaction.commit();
        }};
    }

    @Test
    void testDeleteProduct_BoardGameWithWarehouse() {
        // Prepare test data
        BoardGame boardGame = new BoardGame();
        boardGame.setId(1);

        Warehouse warehouse = new Warehouse();
        warehouse.setId(2);

        // Create a list of board games and set it to the warehouse
        List<BoardGame> inStockBoardGames = new ArrayList<>(Collections.singletonList(boardGame));
        warehouse.setInStockBoardGames(inStockBoardGames);

        // Set the warehouse for the board game
        boardGame.setWarehouse(warehouse);

        // Set up expectations
        new Expectations() {{
            // Expect finding the board game by ID
            entityManager.find(BoardGame.class, 1);
            result = boardGame;

            // Expect merge operation on warehouse
            entityManager.merge(warehouse);
        }};

        // Perform the delete operation
        customHib.deleteProduct(1, ProductType.BOARD_GAME);

        // Verify interactions
        new Verifications() {{
            // Verify transaction management
            transaction.begin();
            transaction.commit();

            // Verify board game removal from warehouse
            warehouse.getInStockBoardGames().remove(boardGame);

            // Verify entity manager operations
            entityManager.merge(warehouse);
            times = 1; // Ensure merge is called exactly once

            entityManager.remove(boardGame);
        }};
    }

    @Test
    void testDeleteProduct_PuzzleWithoutWarehouse() {
        // Create a mock Puzzle without a warehouse
        Puzzle puzzle = new Puzzle();
        puzzle.setId(2);

        new Expectations() {{
            entityManager.find(Puzzle.class, 2);
            result = puzzle;
        }};

        // Execute the deleteProduct method
        customHib.deleteProduct(2, ProductType.PUZZLE);

        // Verify correct removal
        new Verifications() {{
            transaction.begin();
            entityManager.remove(puzzle);
            transaction.commit();
        }};
    }

    @Test
    void testDeleteProduct_DiceWithWarehouse() {
        // Prepare test data
        Dice dice = new Dice();
        dice.setId(3);

        Warehouse warehouse = new Warehouse();
        warehouse.setId(4);

        // Create a list of dices and set it to the warehouse
        List<Dice> inStockDices = new ArrayList<>(Collections.singletonList(dice));
        warehouse.setInStockDices(inStockDices);

        // Set the warehouse for the dice
        dice.setWarehouse(warehouse);

        // Set up expectations
        new Expectations() {{
            // Expect finding the dice by ID
            entityManager.find(Dice.class, 3);
            result = dice;

            // Expect merge operation on warehouse
            entityManager.merge(warehouse);
        }};

        // Perform the delete operation
        customHib.deleteProduct(3, ProductType.DICE);

        // Verify interactions
        new Verifications() {{
            // Verify transaction management
            transaction.begin();
            transaction.commit();

            // Verify dice removal from warehouse
            warehouse.getInStockDices().remove(dice);

            // Verify entity manager operations
            entityManager.merge(warehouse);
            times = 1; // Ensure merge is called exactly once

            entityManager.remove(dice);
        }};
    }
    /*@Test
    void testDeleteProduct_ThrowsException() {
        // Prepare test data
        BoardGame boardGame = new BoardGame();
        boardGame.setId(1);

        // Set up expectations to simulate a database error
        new Expectations() {{
            // Explicitly expect transaction begin
            transaction.begin();

            // Simulate finding the entity throws an exception
            entityManager.find(BoardGame.class, 1);
            result = new RuntimeException("Database error");

            // Ensure transaction is active when rollback is called
            transaction.isActive();
            result = true;

            // Expect transaction rollback when error occurs
            transaction.rollback();

            // Mock the JavaFX alert generation
            JavaFxCustomsUtils.generateAlert(
                    javafx.scene.control.Alert.AlertType.ERROR,
                    "Error",
                    "Error",
                    "Error while deleting product"
            );
        }};

        // Execute and ensure no exceptions propagate
        assertDoesNotThrow(() -> customHib.deleteProduct(1, ProductType.BOARD_GAME));

        // Verify interactions
        new Verifications() {{
            // Verify transaction begin
            transaction.begin();
            times = 1;

            // Verify transaction rollback
            transaction.rollback();
            times = 1;

            // Verify alert generation
            JavaFxCustomsUtils.generateAlert(
                    javafx.scene.control.Alert.AlertType.ERROR,
                    "Error",
                    "Error",
                    "Error while deleting product"
            );
            times = 1;
        }};
    }

    @Test
    void testDeleteProduct_TransactionError() {
        // Prepare test data
        BoardGame boardGame = new BoardGame();
        boardGame.setId(1);

        // Set up expectations to simulate a database error
        new Expectations() {{
            // Explicitly expect transaction begin
            transaction.begin();

            // Simulate finding the entity throws an exception
            entityManager.find(BoardGame.class, 1);
            result = boardGame; // First, return a valid entity

            // Simulate an error during some operation
            entityManager.remove(boardGame);
            result = new RuntimeException("Simulated database error");

            // Ensure transaction is active when rollback is called
            transaction.isActive();
            result = true;

            // Expect transaction rollback when error occurs
            transaction.rollback();

            // Mock the JavaFX alert generation
            JavaFxCustomsUtils.generateAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error",
                    "Error while deleting product"
            );
        }};

        // Execute the method
        customHib.deleteProduct(1, ProductType.BOARD_GAME);

        // Verify interactions
        new Verifications() {{
            // Verify transaction begin
            transaction.begin();
            times = 1;

            // Verify transaction rollback
            transaction.rollback();
            times = 1;

            // Verify alert generation
            JavaFxCustomsUtils.generateAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error",
                    "Error while deleting product"
            );
            times = 1;
        }};
    }

    @Test
    void testDeleteProduct_NonExistentProduct() {
        new Expectations() {{
            entityManager.find(BoardGame.class, 99);
            result = null; // Produktas nerastas
        }};

        customHib.deleteProduct(99, ProductType.BOARD_GAME);

        new Verifications() {{
            transaction.begin();
            entityManager.remove((BoardGame) any);
            times = 0; // Produktas neturi būti pašalintas
            transaction.rollback();
            JavaFxCustomsUtils.generateAlert(
                    Alert.AlertType.ERROR, "Error", "Error", "Error while deleting product");
        }};
    }*/

}