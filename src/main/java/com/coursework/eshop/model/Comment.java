package com.coursework.eshop.model;

import jakarta.persistence.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Comment {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommentTitle() {
        return commentTitle;
    }

    public void setCommentTitle(String commentTitle) {
        this.commentTitle = commentTitle;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public Comment getParentComment() {
        return parentComment;
    }

    public void setParentComment(Comment parentComment) {
        this.parentComment = parentComment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment() {
    }

    public Comment(int id, String commentTitle, String commentBody, LocalDate dateCreated, List<Comment> replies, Comment parentComment, User user) {
        this.id = id;
        this.commentTitle = commentTitle;
        this.commentBody = commentBody;
        this.dateCreated = dateCreated;
        this.replies = replies;
        this.parentComment = parentComment;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    private String commentTitle;
    private String commentBody;
    private LocalDate dateCreated;
    @OneToMany(mappedBy = "parentComment", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comment> replies = new ArrayList<>();

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment parentComment;

    @ManyToOne
    private User user;

    @ManyToOne
    private CustomerOrder customerOrder;


    public Comment(String commentTitle, String commentBody, User user) {
        this.commentTitle = commentTitle;
        this.commentBody = commentBody;
        this.dateCreated = LocalDate.now();
        this.user = user;
        this.replies = new ArrayList<>();
    }

    public Comment(String commentTitle, String commentBody, Comment parentComment, User user) {
        this.commentTitle = commentTitle;
        this.commentBody = commentBody;
        this.dateCreated = LocalDate.now();
        this.user = user;
        this.parentComment = parentComment;
        this.replies = new ArrayList<>();
    }

    @Override
    public String toString() {
        return user.getName() + " " + user.getSurname() + ":" + commentTitle + ":" + dateCreated;
    }


    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }
}