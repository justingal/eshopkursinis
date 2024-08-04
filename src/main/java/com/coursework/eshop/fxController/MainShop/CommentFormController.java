package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.GenericHib;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.model.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CommentFormController {
    @FXML
    public TextField commentTitleField;
    @FXML
    public TextArea commentBodyField;
    @FXML
    public Slider ratingField;
    @FXML
    public Label ratingLabel;

    private Product product;
    private int commentId = 0;
    private GenericHib genericHib;
    private User currentUser = StartGui.currentUser;

    public void setData(GenericHib genericHib, Product product, int commentId) {
        this.genericHib = genericHib;
        this.product = product;
        this.commentId = commentId;
    }

    public void saveData() {
        if (product != null) {
            if (product instanceof BoardGame) {
                BoardGame boardGame = (BoardGame) product;
                Review review = new Review(commentTitleField.getText(), commentBodyField.getText(), currentUser, ratingField.getValue(), boardGame);
                boardGame.getReviews().add(review);
            } else if (product instanceof Dice) {
                Dice dice = (Dice) product;
                Review review = new Review(commentTitleField.getText(), commentBodyField.getText(), currentUser, ratingField.getValue(), dice);
                dice.getReviews().add(review);
            } else if (product instanceof Puzzle) {
                Puzzle puzzle = (Puzzle) product;
                Review review = new Review(commentTitleField.getText(), commentBodyField.getText(), currentUser, ratingField.getValue(), puzzle);
                puzzle.getReviews().add(review);
            }

            genericHib.update(product);
        } else if (commentId != 0) {
            Comment parentComment = genericHib.getEntityById(Comment.class, commentId);
            Comment reply = new Comment(commentTitleField.getText(), commentBodyField.getText(), parentComment, parentComment.getUser());
            parentComment.getReplies().add(reply);
            genericHib.update(parentComment);
        }
        commentTitleField.getScene().getWindow().hide();

    }

    @FXML
    public void initialize() {
        ratingField.valueProperty().addListener((obs, oldVal, newVal) -> {
            int roundedValue = (int) Math.round(newVal.doubleValue());
            ratingLabel.setText("Rating: " + roundedValue);
            ratingField.setValue(roundedValue);
        });
    }
}

