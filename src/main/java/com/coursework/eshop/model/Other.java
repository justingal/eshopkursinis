package com.coursework.eshop.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Other extends Product{
    private String otherType;

    public Other(int id, String title, String description, String author, String otherType) {
        super(id, title, description, author);
        this.otherType = otherType;
    }

    public Other(String title, String description, String author, String otherType) {
        super(title, description, author);
        this.otherType = otherType;
    }

}