package com.coursework.eshop.fxController.MainShop;


import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.fxController.tableviews.CustomerTableParameters;
import com.coursework.eshop.fxController.tableviews.ManagerTableParameters;
import com.coursework.eshop.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import java.util.List;

public class MainShopController  {


    public ListView<Product> productList;
@FXML
    public ListView<Cart> currentOrder;
    @FXML
    public Tab primaryTab;
    @FXML
    public Tab usersTab;
    @FXML
    public Tab warehousesTab;
    @FXML
    public Tab ordersTab;

    @FXML
    public TableView customerTable;
    @FXML
    public TableView managerTable;
    @FXML
    public TabPane tabPane;
    @FXML
    public Tab productsTab;
    public ListView<Product> productListManager;
    @FXML
    public TextField productTitleField;
    @FXML
    public TextArea descriptionField;
    @FXML
    public TextField authorField;
    @FXML
    public ComboBox <ProductType> productType;
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
    public TableColumn<CustomerTableParameters, Integer> idTableCol;
    @FXML
    public TableColumn<CustomerTableParameters, String> loginTableCol;
    @FXML
    public TableColumn<CustomerTableParameters, String> passwordTableCol;
    @FXML
    public TableColumn<CustomerTableParameters, String> addressTableCol;
    @FXML
    public TableColumn< CustomerTableParameters, Void> dummyCol;
    @FXML
    public TableColumn< ManagerTableParameters, Integer> idManagerTableCol;
    @FXML
    public TableColumn<ManagerTableParameters,String> loginManagerTableCol;
    @FXML
    public TableColumn <ManagerTableParameters,String> passwordManagerTableCol;
    @FXML
    public TableColumn <ManagerTableParameters,String> employeeIdManagerTableCol;
    @FXML
    public TableColumn dummyManagerCol;
    @FXML
    public TextField diceNumberField;
    @FXML
    public Tab commentsTab;

    @FXML
    private WarehouseTabController warehouseTabController;

    @FXML
    private CommentTabController commentTabController;

    private ObservableList<ManagerTableParameters> dataManager = FXCollections.observableArrayList();
    private ObservableList<CustomerTableParameters> data = FXCollections.observableArrayList();
    private EntityManagerFactory entityManagerFactory;
    private User currentUser;
    private CustomHib customHib;



    public void initialize() {
        customHib = new CustomHib(entityManagerFactory);
        productType.getItems().addAll(ProductType.values());
        // ---------------------set tableview settings---------------------
        // ---------------------CUSTOMER---------------------
        customerTable.setEditable(true);
        idTableCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        loginTableCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        loginTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        loginTableCol.setOnEditCommit( event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setLogin(event.getNewValue());
            Customer customer = customHib.getEntityById(Customer.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            customer.setLogin(event.getNewValue());
            customHib.update(customer);
        });
        passwordTableCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordTableCol.setOnEditCommit( event -> {
           event.getTableView().getItems().get(event.getTablePosition().getRow()).setPassword(event.getNewValue());
           Customer customer = customHib.getEntityById(Customer.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
              customer.setPassword(event.getNewValue());
                customHib.update(customer);
        });
        addressTableCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addressTableCol.setOnEditCommit( event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setAddress(event.getNewValue());
            Customer customer = customHib.getEntityById(Customer.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            customer.setAddress(event.getNewValue());
            customHib.update(customer);
        });
        // ---------------------DELETE BUTTON---------------------
        Callback<TableColumn<CustomerTableParameters, Void>, TableCell<CustomerTableParameters, Void>> customerDeleteCallback = param -> {
            final TableCell<CustomerTableParameters, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(event -> {
                        CustomerTableParameters row = getTableView().getItems().get(getIndex());
                        customHib.delete(Customer.class, row.getId());
                        customerTable.getItems().clear();
                        loadUserTables();
                    });
                }
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
            return cell;
        };

        dummyCol.setCellFactory(customerDeleteCallback);


        // ---------------------MANAGER---------------------
        managerTable.setEditable(true);
        idManagerTableCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        loginManagerTableCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        loginManagerTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        loginManagerTableCol.setOnEditCommit( event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setLogin(event.getNewValue());
            Manager manager = customHib.getEntityById(Manager.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            manager.setLogin(event.getNewValue());
            customHib.update(manager);
        });
        passwordManagerTableCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordManagerTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordManagerTableCol.setOnEditCommit( event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPassword(event.getNewValue());
            Manager manager = customHib.getEntityById(Manager.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            manager.setPassword(event.getNewValue());
            customHib.update(manager);
        });
        employeeIdManagerTableCol.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        employeeIdManagerTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        employeeIdManagerTableCol.setOnEditCommit( event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setEmployeeId(event.getNewValue());
            Manager manager = customHib.getEntityById(Manager.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            manager.setEmployeeId(event.getNewValue());
            customHib.update(manager);
        });
        // ---------------------DELETE BUTTON---------------------
        Callback<TableColumn<ManagerTableParameters, Void>, TableCell<ManagerTableParameters, Void>> managerDeleteCallback = param -> {
            final TableCell<ManagerTableParameters, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(event -> {
                        ManagerTableParameters row = getTableView().getItems().get(getIndex());
                        customHib.delete(Customer.class, row.getId());
                        managerTable.getItems().clear();
                        loadUserTables();
                    });
                }
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
            return cell;
        };

        dummyManagerCol.setCellFactory(managerDeleteCallback);

    }

    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = user;
        limitAccess();
        loadData();

    }


    private void loadData() {
        customHib = new CustomHib(entityManagerFactory);
        productList.getItems().addAll(customHib.getAllRecords(Product.class));

    }

    private void limitAccess() {
        if(currentUser.getClass()== Manager.class){
            Manager manager = (Manager) currentUser;
            if(!manager.isAdmin()) {
                managerTable.setDisable(true);
            }
        }else if(currentUser.getClass()== Customer.class){
                tabPane.getTabs().remove(usersTab);
                tabPane.getTabs().remove(warehousesTab);
                tabPane.getTabs().remove(productsTab);
        }
    }

    public void leaveComment(ActionEvent actionEvent) {
    }

    public void addToCart(ActionEvent actionEvent) {
    }

    public void loadTabValues() {
        if (productsTab.isSelected()) {
            loadProductListManager();
            warehouseComboBox.getItems().clear();
            warehouseComboBox.getItems().addAll(customHib.getAllRecords(Warehouse.class));
        }else if( warehousesTab.isSelected()){
            warehouseTabController.setData(customHib);
        }else if( usersTab.isSelected()){
            loadUserTables();
        }else if( commentsTab.isSelected()){
            commentTabController.setData(customHib, currentUser);
        }
    }

    private void loadUserTables() {
        customerTable.getItems().clear();
        List <Customer> customerList = customHib.getAllRecords(Customer.class);
        for (Customer c : customerList) {
            CustomerTableParameters customerTableParameters = new CustomerTableParameters();
            customerTableParameters.setId(c.getId());
            customerTableParameters.setLogin(c.getLogin());
            customerTableParameters.setPassword(c.getPassword());
            customerTableParameters.setAddress(c.getAddress());
            data.add(customerTableParameters);
        }
        customerTable.setItems(data);

        managerTable.getItems().clear();
        List <Manager> managerList = customHib.getAllRecords(Manager.class);
        for (Manager m : managerList) {
            ManagerTableParameters managerTableParameters = new ManagerTableParameters();
            managerTableParameters.setId(m.getId());
            managerTableParameters.setLogin(m.getLogin());
            managerTableParameters.setPassword(m.getPassword());
            managerTableParameters.setEmployeeId(m.getEmployeeId());
            dataManager.add(managerTableParameters);
        }
        managerTable.setItems(dataManager);
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
    //---------------------------------Product-----------------------------------

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

}