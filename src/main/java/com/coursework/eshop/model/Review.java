package com.coursework.eshop.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
public class Review extends Comment {
    public Review() {

    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public BoardGame getBoardGame() {
        return boardGame;
    }

    public void setBoardGame(BoardGame boardGame) {
        this.boardGame = boardGame;
    }

    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public Review(String commentTitle, String commentBody, Comment parentComment, User user) {
        super(commentTitle, commentBody, parentComment, user);
    }

    public Review(String commentTitle, String commentBody, Comment parentComment, User user, double rating, Puzzle puzzle, BoardGame boardGame, Dice dice) {
        super(commentTitle, commentBody, parentComment, user);
        this.rating = rating;
        this.puzzle = puzzle;
        this.boardGame = boardGame;
        this.dice = dice;
    }

    private double rating;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Puzzle puzzle;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BoardGame boardGame;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Dice dice;
    public Review(String commentTitle, String commentBody, User user, double rating, BoardGame boardGame) {
        super(commentTitle, commentBody, user);
        this.rating = rating;
        this.boardGame = boardGame;
    }

    public Review(String commentTitle, String commentBody, User user, double rating, Dice dice) {
        super(commentTitle, commentBody, user);
        this.rating = rating;
        this.dice = dice;
    }
    public Review(String commentTitle, String commentBody, User user, double rating, Puzzle puzzle) {
        super(commentTitle, commentBody, user);
        this.rating = rating;
        this.puzzle = puzzle;
    }

    @Override
    public String toString() {
        return "(" + rating + ") " + getCommentBody();
    }


}