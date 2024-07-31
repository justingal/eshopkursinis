package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.HibernateControllers.EntityManagerFactorySingleton;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.model.*;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class CommentTreeController implements Initializable {
    @FXML
    public TreeView<Comment> commentsTree;
    @FXML
    public ListView<Product> productListReview;

    private CustomHib customHibernate;
    private User currentUser = StartGui.currentUser;

    public void setData(CustomHib customHibernate) {
        this.customHibernate = customHibernate;
        loadProducts();
    }

    private void loadProducts() {
        if (productListReview.getItems() != null) {
            productListReview.getItems().clear();
        }
        productListReview.getItems().addAll(customHibernate.getAllRecords(BoardGame.class));
        productListReview.getItems().addAll(customHibernate.getAllRecords(Puzzle.class));
        productListReview.getItems().addAll(customHibernate.getAllRecords(Dice.class));
    }

    public void deleteComment() {
        TreeItem<Comment> commentTreeItem = commentsTree.getSelectionModel().getSelectedItem();
        if (commentTreeItem != null) {
            Comment comment = commentTreeItem.getValue();

            if (canModifyComment(comment, currentUser)) {
                customHibernate.deleteComment(comment.getId());
            } else {
                JavaFxCustomsUtils JavaFxCustomUtils = new JavaFxCustomsUtils();
                JavaFxCustomUtils.generateAlert(Alert.AlertType.ERROR, "Error", "You cannot delete this comment", "You are not the owner of this comment");
            }
        }
        loadComments();
    }


    private boolean canModifyComment(Comment comment, User currentUser) {
        if (comment.getUser().equals(currentUser)) {
            return true;
        }

        if (currentUser instanceof Admin) {
            return true;
        }

        return false;
    }

    public void reply() throws IOException {
        TreeItem<Comment> commentTreeItem = commentsTree.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("commentForm.fxml"));
        Parent parent = fxmlLoader.load();
        CommentFormController commentForm = fxmlLoader.getController();
        commentForm.setData(customHibernate, 0, commentTreeItem.getValue().getId());
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setTitle("E-Shop");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        loadComments();
    }

    public void loadComments() {
        //Product selectedProduct = customHibernate.getEntityById(Product.class, productListReview.getSelectionModel().getSelectedItem().getId());
        Product selectedProduct = productListReview.getSelectionModel().getSelectedItem();
        commentsTree.setRoot(new TreeItem<>());
        commentsTree.setShowRoot(false);
        commentsTree.getRoot().setExpanded(true);
        if (selectedProduct instanceof BoardGame) {
            BoardGame boardGame = (BoardGame) selectedProduct;
            boardGame.getReviews().forEach(comment -> addTreeItem(comment, commentsTree.getRoot()));
        } else if (selectedProduct instanceof Puzzle) {
            Puzzle puzzle = (Puzzle) selectedProduct;
            puzzle.getReviews().forEach(comment -> addTreeItem(comment, commentsTree.getRoot()));
        } else if (selectedProduct instanceof Dice) {
            Dice dice = (Dice) selectedProduct;
            dice.getReviews().forEach(comment -> addTreeItem(comment, commentsTree.getRoot()));
        }
    }

    private void addTreeItem(Comment comment, TreeItem<Comment> parentComment) {
        TreeItem<Comment> treeItem = new TreeItem<>(comment);
        parentComment.getChildren().add(treeItem);
        comment.getReplies().forEach(sub -> addTreeItem(sub, treeItem));
    }

    public void loadResponseForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("commentForm.fxml"));
        Parent parent = fxmlLoader.load();
        CommentFormController commentForm = fxmlLoader.getController();
        commentForm.setData(customHibernate, productListReview.getSelectionModel().getSelectedItem().getId(), 0);
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setTitle("Shop");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        loadComments();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void returnToMainShop() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGui.class.getResource("main-shop.fxml"));
        Parent parent = fxmlLoader.load();
        MainShopController mainShopController = fxmlLoader.getController();
        mainShopController.setData();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) productListReview.getScene().getWindow();
        stage.setTitle("Shop");
        stage.setScene(scene);
        stage.show();
    }
}