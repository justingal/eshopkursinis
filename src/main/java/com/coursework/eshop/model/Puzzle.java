package com.coursework.eshop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Puzzle extends Product {

    private LocalDate puzzleDate;

    public Puzzle(String title, String description, LocalDate puzzleDate) {
        super(title, description);
        this.puzzleDate = puzzleDate;
    }
}