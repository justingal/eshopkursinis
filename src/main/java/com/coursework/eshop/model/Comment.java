package com.coursework.eshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String commentBody;
    @ManyToOne
    private User user;


    public Comment(String title, String commentBody, User user) {
        this.title = title;
        this.commentBody = commentBody;
        this.user = user;
    }

    @Override
    public String toString() {
        return title + ":  " + commentBody;
    }
}
