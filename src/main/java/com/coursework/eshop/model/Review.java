package com.coursework.eshop.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review extends Comment {
    private double rating;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    private Puzzle puzzle;
    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    private BoardGame boardGame;
    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    private Dice dice;
    public Review(String commentTitle, String commentBody, User user, double rating) {
        super(commentTitle, commentBody, user);
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "(" + rating + ") " + getCommentBody();
    }


}