package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.fxController.MainShop.Util.ProductInputFields;
import com.coursework.eshop.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

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
    public ComboBox<Warehouse> warehouseComboBox;
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
    @FXML
    public AnchorPane productTabAnchor;
    public TextField priceField;

    private CustomHib customHib;

    public void setData(CustomHib customHib) {
        this.customHib = customHib;
        loadProductListManager();
        warehouseComboBox.getItems().clear();
        productType.getItems().clear();
        productType.getItems().addAll(ProductType.values());
        warehouseComboBox.getItems().addAll(customHib.getAllRecords(Warehouse.class));
    }

    public void loadProductListManager() {
        System.out.println("Loading product list...");
        productListManager.getItems().clear();
        List<BoardGame> boardGames = customHib.getAllRecords(BoardGame.class);
        productListManager.getItems().addAll(boardGames);
        List<Puzzle> puzzles = customHib.getAllRecords(Puzzle.class);
        System.out.println("Puzzles retrieved: " + puzzles.size());
        productListManager.getItems().addAll(puzzles);
        List<Dice> dices = customHib.getAllRecords(Dice.class);
        productListManager.getItems().addAll(dices);
    }

    public void addNewProduct() {
        if (isMissingRequiredFields()) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Missing Information", "Please fill all required fields correctly.");
            return;
        }

        try {
            System.out.println("Creating product...");
            double price = Double.parseDouble(priceField.getText().trim());
            Warehouse selectedWarehouse = getSelectedWarehouse();

            ProductInputFields fields = new ProductInputFields(
                    productTitleField.getText().trim(),
                    descriptionField.getText().trim(),
                    authorField.getText().trim(),
                    playersQuantityField.getText().trim(),
                    gameDurationFIeld.getText().trim(),
                    puzzleMaterialField.getText().trim(),
                    puzzleSizeField.getText().trim(),
                    piecesQuantityField.getText().trim(),
                    diceNumberField.getText().trim()
            );

            ProductType selectedProductType = productType.getSelectionModel().getSelectedItem();
            selectedProductType.createProduct(customHib, fields, selectedWarehouse, price);

            loadProductListManager();

        } catch (NumberFormatException e) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Invalid Format", "Please enter valid numbers for price, quantities, or other numeric fields.");
        } catch (IllegalArgumentException e) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", e.getMessage(), "Please complete all fields properly.");
        }
    }

    private boolean isMissingRequiredFields() {
        return productTitleField.getText().trim().isEmpty() ||
                descriptionField.getText().trim().isEmpty() ||
                authorField.getText().trim().isEmpty() ||
                warehouseComboBox.getSelectionModel().getSelectedItem() == null ||
                priceField.getText().trim().isEmpty();
    }

    private Warehouse getSelectedWarehouse() {
        Warehouse warehouse = customHib.getEntityById(Warehouse.class, warehouseComboBox.getSelectionModel().getSelectedItem().getId());
        System.out.println("Warehouse from ComboBox: " + warehouseComboBox.getSelectionModel().getSelectedItem());
        System.out.println("Warehouse ID in addNewProduct: " + warehouse.getId());
        return warehouse;
    }


    public void updateProduct() {
        Product selectedProduct = productListManager.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "No Selection", "No product selected", "Please select a product to update.");
            return;
        }

        try {
            ProductInputFields fields = collectInputFields();
            validateCommonFields(fields);

            Product product = customHib.getEntityById(selectedProduct.getClass(), selectedProduct.getId());
            updateCommonFields(product, fields);

            ProductType selectedType = productType.getSelectionModel().getSelectedItem();
            selectedType.updateProduct(product, fields);

            customHib.update(product);
            System.out.println("Calling loadProductListManager...");
            loadProductListManager();

        } catch (NumberFormatException e) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Invalid Input", "Please enter valid numbers for numeric fields.");
        } catch (IllegalArgumentException e) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", e.getMessage(), "Please complete all fields properly.");
        }
    }

    private ProductInputFields collectInputFields() {
        return new ProductInputFields(
                productTitleField.getText().trim(),
                descriptionField.getText().trim(),
                authorField.getText().trim(),
                playersQuantityField.getText().trim(),
                gameDurationFIeld.getText().trim(),
                puzzleMaterialField.getText().trim(),
                puzzleSizeField.getText().trim(),
                piecesQuantityField.getText().trim(),
                diceNumberField.getText().trim()
        );
    }

    private void validateCommonFields(ProductInputFields fields) {
        if (fields.productTitle().isEmpty() || fields.description().isEmpty() || fields.author().isEmpty() ||
                warehouseComboBox.getSelectionModel().getSelectedItem() == null) {
            throw new IllegalArgumentException("All common fields must be filled.");
        }
    }

    private void updateCommonFields(Product product, ProductInputFields fields) {
        product.setTitle(fields.productTitle());
        product.setDescription(fields.description());
        product.setAuthor(fields.author());
        product.setPrice(Double.parseDouble(priceField.getText().trim()));
        Warehouse selectedWarehouse = warehouseComboBox.getSelectionModel().getSelectedItem();
        product.setWarehouse(customHib.getEntityById(Warehouse.class, selectedWarehouse.getId()));
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
            priceField.setText(String.valueOf(selectedProduct.getPrice()));

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
        if (productType.getSelectionModel().getSelectedItem() == ProductType.BOARD_GAME) {
            gameDurationFIeld.setDisable(false);
            playersQuantityField.setDisable(false);
            piecesQuantityField.setDisable(true);
            puzzleMaterialField.setDisable(true);
            puzzleSizeField.setDisable(true);
            diceNumberField.setDisable(true);
        } else if (productType.getSelectionModel().getSelectedItem() == ProductType.PUZZLE) {
            gameDurationFIeld.setDisable(true);
            playersQuantityField.setDisable(true);
            piecesQuantityField.setDisable(false);
            puzzleMaterialField.setDisable(false);
            puzzleSizeField.setDisable(false);
            diceNumberField.setDisable(true);
        } else if (productType.getSelectionModel().getSelectedItem() == ProductType.DICE) {
            gameDurationFIeld.setDisable(true);
            playersQuantityField.setDisable(true);
            piecesQuantityField.setDisable(true);
            puzzleMaterialField.setDisable(true);
            puzzleSizeField.setDisable(true);
            diceNumberField.setDisable(false);
        }
    }
}
