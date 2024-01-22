package com.coursework.eshop.fxController;


import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.HibernateControllers.GenericHib;
import com.coursework.eshop.fxController.tableviews.CustomerTableParameters;
import com.coursework.eshop.fxController.tableviews.ManagerTableParameters;
import com.coursework.eshop.model.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainShopController  {


    public ListView<Product> productList;
@FXML
    public ListView<Cart> currentOrder;
    @FXML
    public Tab primaryTab;
    @FXML
    public Tab usersTab;
    @FXML
    public Tab warehouseTab;
    @FXML
    public Tab ordersTab;
    @FXML
    public ListView<Warehouse>warehouseList;
    @FXML
    public TextField addressWarehouseField;
    @FXML
    public TextField titleWarehouseField;
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
    public TextField commentTitleField;
    public TextArea commentTextArea;
    public ListView<Comment> commentListView;
    public Tab commentTab;


    private ObservableList<ManagerTableParameters> dataManager = FXCollections.observableArrayList();
    private ObservableList<CustomerTableParameters> data = FXCollections.observableArrayList();
    private EntityManagerFactory entityManagerFactory;
    User currentUser;
    private GenericHib genericHib;

    public void initialize() {

        productType.getItems().addAll(ProductType.values());
        commentListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadCommentData();
            }
        });
        // ---------------------set tableview settings---------------------
        // ---------------------CUSTOMER---------------------
        customerTable.setEditable(true);
        idTableCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        loginTableCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        loginTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        loginTableCol.setOnEditCommit( event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setLogin(event.getNewValue());
            Customer customer = genericHib.getEntityById(Customer.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            customer.setLogin(event.getNewValue());
            genericHib.update(customer);
        });
        passwordTableCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordTableCol.setOnEditCommit( event -> {
           event.getTableView().getItems().get(event.getTablePosition().getRow()).setPassword(event.getNewValue());
           Customer customer = genericHib.getEntityById(Customer.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
              customer.setPassword(event.getNewValue());
                genericHib.update(customer);
        });
        addressTableCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addressTableCol.setOnEditCommit( event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setAddress(event.getNewValue());
            Customer customer = genericHib.getEntityById(Customer.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            customer.setAddress(event.getNewValue());
            genericHib.update(customer);
        });
        // ---------------------DELETE BUTTON---------------------
        /*Callback<TableColumn<CustomerTableParameters, Void>, TableCell<CustomerTableParameters, Void>> callback = param -> {
            final TableCell<CustomerTableParameters, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(event -> {
                        CustomerTableParameters row = getTableView().getItems().get(getIndex());
                        GenericHib.delete(Customer.class, row.getId());
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

        dummyCol.setCellFactory(callback);*/

        // ---------------------MANAGER---------------------
        managerTable.setEditable(true);
        idManagerTableCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        loginManagerTableCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        loginManagerTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        loginManagerTableCol.setOnEditCommit( event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setLogin(event.getNewValue());
            Manager manager = genericHib.getEntityById(Manager.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            manager.setLogin(event.getNewValue());
            genericHib.update(manager);
        });
        passwordManagerTableCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        passwordManagerTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordManagerTableCol.setOnEditCommit( event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPassword(event.getNewValue());
            Manager manager = genericHib.getEntityById(Manager.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            manager.setPassword(event.getNewValue());
            genericHib.update(manager);
        });
        employeeIdManagerTableCol.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        employeeIdManagerTableCol.setCellFactory(TextFieldTableCell.forTableColumn());
        employeeIdManagerTableCol.setOnEditCommit( event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setEmployeeId(event.getNewValue());
            Manager manager = genericHib.getEntityById(Manager.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            manager.setEmployeeId(event.getNewValue());
            genericHib.update(manager);
        });

    }

    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = user;
        limitAccess();
        loadData();
    }

    private void loadData() {
        genericHib = new GenericHib(entityManagerFactory);
        productList.getItems().addAll(genericHib.getAllRecords(Product.class));
        commentListView.getItems().addAll(genericHib.getAllRecords(Comment.class));
    }

    private void limitAccess() {
        if(currentUser.getClass()== Manager.class){
            Manager manager = (Manager) currentUser;
            if(!manager.isAdmin()) {
                managerTable.setDisable(true);
            }
        }else if(currentUser.getClass()== Customer.class){
                tabPane.getTabs().remove(usersTab);
                tabPane.getTabs().remove(warehouseTab);
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
            warehouseComboBox.getItems().addAll(genericHib.getAllRecords(Warehouse.class));
        }else if( warehouseTab.isSelected()){
            loadWarehouseList();
            warehouseList.getSelectionModel().select(0);
            Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
            if (selectedWarehouse != null) {
                titleWarehouseField.setText(selectedWarehouse.getTitle());
                addressWarehouseField.setText(selectedWarehouse.getAddress());
            }
        }else if( usersTab.isSelected()){
            loadUserTables();
        }else if( commentTab.isSelected()){
            loadCommentList();

        }
    }

    private void loadUserTables() {
        customerTable.getItems().clear();
        List <Customer> customerList = genericHib.getAllRecords(Customer.class);
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
        List <Manager> managerList = genericHib.getAllRecords(Manager.class);
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
        else if(productType.getSelectionModel().getSelectedItem() == ProductType.OTHER){
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
        List<BoardGame> boardGames = genericHib.getAllRecords(BoardGame.class);
        productListManager.getItems().addAll(boardGames);
        List<Puzzle> puzzles = genericHib.getAllRecords(Puzzle.class);
        productListManager.getItems().addAll(puzzles);
        List<Other> others = genericHib.getAllRecords(Other.class);
        productListManager.getItems().addAll(others);
    }

    public void addNewProduct() {
        if(productType.getSelectionModel().getSelectedItem() == ProductType.BOARD_GAME){
            genericHib.create(new BoardGame(productTitleField.getText(),descriptionField.getText(), authorField.getText(), genericHib.getEntityById(Warehouse.class, warehouseComboBox.getSelectionModel().getSelectedItem().getId()), playersQuantityField.getText(), gameDurationFIeld.getText()));
        }
        else if( productType.getSelectionModel().getSelectedItem() == ProductType.PUZZLE){
            genericHib.create(new Puzzle(productTitleField.getText(), descriptionField.getText(), authorField.getText(), genericHib.getEntityById(Warehouse.class, warehouseComboBox.getSelectionModel().getSelectedItem().getId()), Integer.parseInt(piecesQuantityField.getText()) , puzzleMaterialField.getText(), puzzleSizeField.getText()));
        }
        else if( productType.getSelectionModel().getSelectedItem() == ProductType.OTHER){
            genericHib.create(new Product(productTitleField.getText(), descriptionField.getText(), authorField.getText(), genericHib.getEntityById(Warehouse.class, warehouseComboBox.getSelectionModel().getSelectedItem().getId())));
        }
        loadProductListManager();
    }

    public void updateProduct() {
        Product selectedProduct = productListManager.getSelectionModel().getSelectedItem();
        Product product = genericHib.getEntityById(selectedProduct.getClass(), selectedProduct.getId());

        product.setTitle(productTitleField.getText());
        product.setDescription(descriptionField.getText());
        product.setAuthor(authorField.getText());
        product.setWarehouse(genericHib.getEntityById(Warehouse.class, warehouseComboBox.getSelectionModel().getSelectedItem().getId()));

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
        genericHib.update(product);
        loadProductListManager();
    }

    public void deleteProduct() {
        Product selectedProduct = productListManager.getSelectionModel().getSelectedItem();
        CustomHib.deleteProduct(selectedProduct.getId());
        loadProductListManager();
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
                productType.getSelectionModel().select(ProductType.OTHER);
            }
        }
    }

    //---------------------------------Warehouse---------------------------------
    private void loadWarehouseList() {
        warehouseList.getItems().clear();
        warehouseList.getItems().addAll(genericHib.getAllRecords(Warehouse.class));
    }

    public void addNewWarehouse(ActionEvent actionEvent) {
        genericHib.create(new Warehouse(titleWarehouseField.getText(), addressWarehouseField.getText()));
        loadWarehouseList();
    }

    public void updateWarehouse(ActionEvent actionEvent) {
        Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
        Warehouse warehouse = genericHib.getEntityById(Warehouse.class, selectedWarehouse.getId());
        warehouse.setTitle(titleWarehouseField.getText());
        warehouse.setAddress(addressWarehouseField.getText());
        genericHib.update(warehouse);
        loadWarehouseList();
    }

    public void removeWarehouse(ActionEvent actionEvent) {
        Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
        Warehouse warehouse = genericHib.getEntityById(Warehouse.class, selectedWarehouse.getId());
        genericHib.delete(selectedWarehouse.getClass(), selectedWarehouse.getId());
        loadWarehouseList();
    }

    public void loadWarehouseData() {
        Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
        titleWarehouseField.setText(selectedWarehouse.getTitle());
        addressWarehouseField.setText(selectedWarehouse.getAddress());
    }
//---------------------------------Comments---------------------------------

    public void addNewComment(ActionEvent actionEvent) {
        Comment comment = new Comment(commentTitleField.getText(), commentTextArea.getText(), currentUser);
        genericHib.create(comment);
        loadCommentList();

    }

    private void loadCommentList() {
        commentListView.getItems().clear();
        commentListView.getItems().addAll(genericHib.getAllRecords(Comment.class));

    }

    public void updateComment(ActionEvent actionEvent) {
        Comment selectedComment = commentListView.getSelectionModel().getSelectedItem();
        Comment comment = genericHib.getEntityById(Comment.class, selectedComment.getId());
        comment.setTitle(commentTitleField.getText());
        comment.setCommentBody(commentTextArea.getText());
        genericHib.update(comment);
        loadCommentList();

    }
    public void loadCommentData() {
        Comment selectedComment = commentListView.getSelectionModel().getSelectedItem();
        if (selectedComment != null) {
            commentTitleField.setText(selectedComment.getTitle());
            commentTextArea.setText(selectedComment.getCommentBody());
        }
    }


    public void deleteComment(ActionEvent actionEvent) {
    }
}