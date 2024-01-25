package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.List;

public class ProductTabController {
    @FXML
    public ListView<Product> productListManager;
    @FXML
    public TextField productTitleField;
    @FXML
    public TextArea descriptionField;
    @FXML
    public TextField authorField;
    @FXML
    public ComboBox<ProductType> productType;
    @FXML
    public ComboBox <Warehouse>  warehouseComboBox;
    @FXML
    public TextField gameDurationFIeld;
    @FXML
    public TextField playersQuantityField;
    @FXML
    public TextField piecesQuantityField;
    @FXML
    public TextField puzzleMaterialField;
    @FXML
    public TextField puzzleSizeField;
    @FXML
    public TextField diceNumberField;

    private CustomHib customHib;

    public void setData(CustomHib customHib) {
        productType.getItems().addAll(ProductType.values());
        loadProductListManager();
        warehouseComboBox.getItems().clear();
        warehouseComboBox.getItems().addAll(customHib.getAllRecords(Warehouse.class));
    }

    private void loadProductListManager() {
        productListManager.getItems().clear();
        List<BoardGame> boardGames = customHib.getAllRecords(BoardGame.class);
        productListManager.getItems().addAll(boardGames);
        List<Puzzle> puzzles = customHib.getAllRecords(Puzzle.class);
        productListManager.getItems().addAll(puzzles);
        List<Dice> dices = customHib.getAllRecords(Dice.class);
        productListManager.getItems().addAll(dices);
    }

    public void addNewProduct() {
        if(productType.getSelectionModel().getSelectedItem() == ProductType.BOARD_GAME){
            customHib.create(new BoardGame(productTitleField.getText(),descriptionField.getText(), authorField.getText(), customHib.getEntityById(Warehouse.class, warehouseComboBox.getSelectionModel().getSelectedItem().getId()), playersQuantityField.getText(), gameDurationFIeld.getText()));
        }
        else if( productType.getSelectionModel().getSelectedItem() == ProductType.PUZZLE){
            customHib.create(new Puzzle(productTitleField.getText(), descriptionField.getText(), authorField.getText(), customHib.getEntityById(Warehouse.class, warehouseComboBox.getSelectionModel().getSelectedItem().getId()), Integer.parseInt(piecesQuantityField.getText()) , puzzleMaterialField.getText(), puzzleSizeField.getText()));
        }
        else if( productType.getSelectionModel().getSelectedItem() == ProductType.DICE){
            customHib.create(new Dice(productTitleField.getText(), descriptionField.getText(), authorField.getText(), customHib.getEntityById(Warehouse.class, warehouseComboBox.getSelectionModel().getSelectedItem().getId()), Integer.parseInt(diceNumberField.getText())));
        }
        loadProductListManager();
    }

    public void updateProduct() {
        Product selectedProduct = productListManager.getSelectionModel().getSelectedItem();
        Product product = customHib.getEntityById(selectedProduct.getClass(), selectedProduct.getId());

        product.setTitle(productTitleField.getText());
        product.setDescription(descriptionField.getText());
        product.setAuthor(authorField.getText());
        product.setWarehouse(customHib.getEntityById(Warehouse.class, warehouseComboBox.getSelectionModel().getSelectedItem().getId()));

        if (productType.getSelectionModel().getSelectedItem() == ProductType.BOARD_GAME) {
            BoardGame boardGame = (BoardGame) product;
            boardGame.setPlayersQuantity(playersQuantityField.getText());
            boardGame.setGameDuration(gameDurationFIeld.getText());
        } else if (productType.getSelectionModel().getSelectedItem() == ProductType.PUZZLE) {
            Puzzle puzzle = (Puzzle) product;
            puzzle.setPiecesQuantity(Integer.parseInt(piecesQuantityField.getText()));
            puzzle.setPuzzleMaterial(puzzleMaterialField.getText());
            puzzle.setPuzzleSize(puzzleSizeField.getText());
        }
        customHib.update(product);
        loadProductListManager();
    }

    public void deleteProduct() {
        Product selectedProduct = productListManager.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            customHib.deleteProduct(selectedProduct.getId(), productType.getSelectionModel().getSelectedItem());
            loadProductListManager();
        }
    }

    public void loadProductManagerData() {
        Product selectedProduct = productListManager.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            productTitleField.setText(selectedProduct.getTitle());
            descriptionField.setText(selectedProduct.getDescription());
            authorField.setText(selectedProduct.getAuthor());
            warehouseComboBox.getSelectionModel().select(selectedProduct.getWarehouse());

            if (selectedProduct instanceof BoardGame) {
                BoardGame boardGame = (BoardGame) selectedProduct;
                productType.getSelectionModel().select(ProductType.BOARD_GAME);
                playersQuantityField.setText(boardGame.getPlayersQuantity());
                gameDurationFIeld.setText(boardGame.getGameDuration());
                warehouseComboBox.getSelectionModel().select(selectedProduct.getWarehouse());
            } else if (selectedProduct instanceof Puzzle) {
                Puzzle puzzle = (Puzzle) selectedProduct;
                productType.getSelectionModel().select(ProductType.PUZZLE);
                piecesQuantityField.setText(String.valueOf(puzzle.getPiecesQuantity()));
                puzzleMaterialField.setText(puzzle.getPuzzleMaterial());
                puzzleSizeField.setText(puzzle.getPuzzleSize());
                warehouseComboBox.getSelectionModel().select(selectedProduct.getWarehouse());
            } else {
                productType.getSelectionModel().select(ProductType.DICE);
                Dice dice = (Dice) selectedProduct;
                productType.getSelectionModel().select(ProductType.DICE);
                warehouseComboBox.getSelectionModel().select(selectedProduct.getWarehouse());
                diceNumberField.setText(String.valueOf(dice.getDiceNumber()));

            }
        }
    }

    public void enableProductField(ActionEvent actionEvent) {
        if(productType.getSelectionModel().getSelectedItem() == ProductType.BOARD_GAME){
            gameDurationFIeld.setDisable(false);
            playersQuantityField.setDisable(false);
            piecesQuantityField.setDisable(true);
            puzzleMaterialField.setDisable(true);
            puzzleSizeField.setDisable(true);
        }
        else if(productType.getSelectionModel().getSelectedItem() == ProductType.PUZZLE){
            gameDurationFIeld.setDisable(true);
            playersQuantityField.setDisable(true);
            piecesQuantityField.setDisable(false);
            puzzleMaterialField.setDisable(false);
            puzzleSizeField.setDisable(false);
        }
        else if(productType.getSelectionModel().getSelectedItem() == ProductType.DICE){
            gameDurationFIeld.setDisable(true);
            playersQuantityField.setDisable(true);
            piecesQuantityField.setDisable(true);
            puzzleMaterialField.setDisable(true);
            puzzleSizeField.setDisable(true);
        }
    }
}
