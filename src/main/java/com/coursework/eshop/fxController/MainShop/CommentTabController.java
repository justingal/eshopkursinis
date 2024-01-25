package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.model.Comment;
import com.coursework.eshop.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lombok.Getter;

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
    private CustomHib customHib;
    private User currentUser;


    public void setData(CustomHib customHib, User currentUser) {

        this.customHib = customHib;
        this.currentUser = currentUser;
        commentListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadCommentData();
            }
        });
        loadCommentList();
    }


    public void addNewComment(ActionEvent actionEvent) {
        Comment comment = new Comment(commentTitleField.getText(), commentTextArea.getText(), currentUser);
        customHib.create(comment);
        loadCommentList();

    }

    private void loadCommentList() {
        commentListView.getItems().clear();
        commentListView.getItems().addAll(customHib.getAllRecords(Comment.class));

    }

    public void updateComment(ActionEvent actionEvent) {
        Comment selectedComment = commentListView.getSelectionModel().getSelectedItem();
        Comment comment = customHib.getEntityById(Comment.class, selectedComment.getId());
        comment.setTitle(commentTitleField.getText());
        comment.setCommentBody(commentTextArea.getText());
        customHib.update(comment);
        loadCommentList();

    }
    public void loadCommentData() {
        Comment selectedComment = commentListView.getSelectionModel().getSelectedItem();
        if (selectedComment != null) {
            commentTitleField.setText(selectedComment.getTitle());
            commentTextArea.setText(selectedComment.getCommentBody());
        }
    }


    public void deleteComment() {
        Comment selectedComment = commentListView.getSelectionModel().getSelectedItem();
        if (selectedComment != null) {
            customHib.deleteComment(selectedComment.getId());
            loadCommentList();
        }
    }
}
