package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.model.Warehouse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class WarehouseTabController {
    @FXML
    public ListView<Warehouse> warehouseList;
    @FXML
    public TextField addressWarehouseField;
    @FXML
    public TextField titleWarehouseField;
    @FXML
    public AnchorPane warehouseTabAnchor;

    private CustomHib customHib;

    public void loadWarehouseData() {
        Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
        titleWarehouseField.setText(selectedWarehouse.getTitle());
        addressWarehouseField.setText(selectedWarehouse.getAddress());
    }

    private void loadWarehouseList() {
        warehouseList.getItems().clear();
        warehouseList.getItems().addAll(customHib.getAllRecords(Warehouse.class));
    }

    public void addNewWarehouse(ActionEvent actionEvent) {
        String title = titleWarehouseField.getText().trim();
        String address = addressWarehouseField.getText().trim();

        if (title.isEmpty() || address.isEmpty()) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Missing Information", "Both title and address must be filled out.");
            return;
        }

        customHib.create(new Warehouse(title, address));
        loadWarehouseList();
    }

    public void updateWarehouse(ActionEvent actionEvent) {
        Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
        if (selectedWarehouse == null) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "No Selection", "No warehouse selected", "Please select a warehouse to update.");
            return;
        }

        String title = titleWarehouseField.getText().trim();
        String address = addressWarehouseField.getText().trim();

        if (title.isEmpty() || address.isEmpty()) {
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "ERROR", "Missing Information", "Both title and address must be filled out.");
            return;
        }

        Warehouse warehouse = customHib.getEntityById(Warehouse.class, selectedWarehouse.getId());
        warehouse.setTitle(title);
        warehouse.setAddress(address);
        customHib.update(warehouse);
        loadWarehouseList();
    }

    public void removeWarehouse(ActionEvent actionEvent) {
        Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
        customHib.delete(selectedWarehouse.getClass(), selectedWarehouse.getId());
        loadWarehouseList();
    }

    public void setData(CustomHib customHib) {
        this.customHib = customHib;
        loadWarehouseList();
        warehouseList.getSelectionModel().select(0);
        Warehouse selectedWarehouse = warehouseList.getSelectionModel().getSelectedItem();
        if (selectedWarehouse != null) {
            titleWarehouseField.setText(selectedWarehouse.getTitle());
            addressWarehouseField.setText(selectedWarehouse.getAddress());
        }
    }

}
