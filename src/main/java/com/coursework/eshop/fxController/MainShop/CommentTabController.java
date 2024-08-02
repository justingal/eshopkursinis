package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.model.Admin;
import com.coursework.eshop.model.Comment;
import com.coursework.eshop.model.Review;
import com.coursework.eshop.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

public class CommentTabController {

    @FXML
    public TextField commentTitleField;
    @FXML
    public TextArea commentTextArea;
    @FXML
    public ListView<Comment> commentListView;
    @Getter
    @FXML
    public AnchorPane commentTabAnchor;
    @FXML
    public TextArea commentBodyField;
    @FXML
    public TreeView commentTreeView;
    private CustomHib customHib;
    private User currentUser = StartGui.currentUser;
    private JavaFxCustomsUtils JavaFxCustomUtils = new JavaFxCustomsUtils();


    public void setData(CustomHib customHib) {
        this.customHib = customHib;
        loadCommentTree();
    }

    private void loadCommentTree() {
        List<Comment> comments = customHib.readAllRootComments();
        commentTreeView.setRoot(new TreeItem<>());
        commentTreeView.setShowRoot(false);
        commentTreeView.getRoot().setExpanded(true);
        comments.stream()
                .filter(comment -> !(comment instanceof Review))
                .forEach(comment -> addTreeItem(comment, commentTreeView.getRoot()));
    }

    private void addTreeItem(Comment comment, TreeItem<Comment> parentComment) {
        TreeItem<Comment> treeItem = new TreeItem<>(comment);
        parentComment.getChildren().add(treeItem);
        comment.getReplies().stream()
                .filter(reply -> !(reply instanceof Review))
                .forEach(sub -> addTreeItem(sub, treeItem));
    }

    public void addNewComment() {
        if (commentTitleField.getText().isEmpty() || commentBodyField.getText().isEmpty()) {
            JavaFxCustomUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Cannot add comment", "Both title and body must be filled out.");
            return;
        }

        TreeItem<Comment> selectedComment = (TreeItem<Comment>) commentTreeView.getSelectionModel().getSelectedItem();
        if (selectedComment == null)
            customHib.create(new Comment(commentTitleField.getText(), commentBodyField.getText(), this.currentUser));
        else
            customHib.create(new Comment(commentTitleField.getText(), commentBodyField.getText(), selectedComment.getValue(), this.currentUser));
        loadCommentTree();
    }

    public void updateExistingComment() {
        TreeItem<Comment> selectedComment = (TreeItem<Comment>) commentTreeView.getSelectionModel().getSelectedItem();
        Comment comment = customHib.getEntityById(Comment.class, selectedComment.getValue().getId());

        if (commentTitleField.getText().isEmpty() || commentBodyField.getText().isEmpty()) {
            JavaFxCustomUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Cannot update comment", "Both title and body must be filled out.");
            return;
        }
        if (canModifyComment(comment, currentUser)) {
            comment.setCommentTitle(commentTitleField.getText());
            comment.setCommentBody(commentBodyField.getText());
            customHib.update(comment);
            loadCommentTree();
        } else {

            JavaFxCustomUtils.generateAlert(Alert.AlertType.ERROR, "Access denied", "You have no access to this comment", "Please, contact your administrator");
        }
    }

    public void deleteExistingComment() {
        TreeItem<Comment> selectedComment = (TreeItem<Comment>) commentTreeView.getSelectionModel().getSelectedItem();
        if (selectedComment != null) {
            Comment comment = selectedComment.getValue();
            if (canModifyComment(comment, currentUser)) {
                if (comment instanceof Review) {
                    customHib.deleteReview(comment.getId());
                } else {
                    customHib.deleteCommentAndChildren(comment.getId());
                }
                loadCommentTree();
            } else {
                JavaFxCustomUtils.generateAlert(Alert.AlertType.ERROR, "Access denied", "You have no access to this comment", "Please, contact your administrator");
            }
        } else {
            JavaFxCustomUtils.generateAlert(Alert.AlertType.ERROR, "NullPtr", "No comment selected", "You need to select a comment to delete");
        }
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

    public void showCommentInfo() {
        TreeItem<Comment> selectedComment = (TreeItem<Comment>) commentTreeView.getSelectionModel().getSelectedItem();
        if (selectedComment != null) {
            commentTitleField.setText(selectedComment.getValue().getCommentTitle());
            commentBodyField.setText(selectedComment.getValue().getCommentBody());
        }
    }


}
