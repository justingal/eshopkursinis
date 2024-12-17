package com.coursework.eshop.fxController.MainShop.Util;

public class ProductInputFields {
    private final String productTitle;
    private final String description;
    private final String author;
    private final String playersQuantity;
    private final String gameDuration;
    private final String puzzleMaterial;
    private final String puzzleSize;
    private final String piecesQuantity;
    private final String diceNumber;

    public ProductInputFields(String productTitle, String description, String author,
                       String playersQuantity, String gameDuration,
                       String puzzleMaterial, String puzzleSize, String piecesQuantity, String diceNumber) {
        this.productTitle = productTitle;
        this.description = description;
        this.author = author;
        this.playersQuantity = playersQuantity;
        this.gameDuration = gameDuration;
        this.puzzleMaterial = puzzleMaterial;
        this.puzzleSize = puzzleSize;
        this.piecesQuantity = piecesQuantity;
        this.diceNumber = diceNumber;
    }

    public String productTitle() { return productTitle; }
    public String description() { return description; }
    public String author() { return author; }
    public String playersQuantity() { return playersQuantity; }
    public String gameDuration() { return gameDuration; }
    public String puzzleMaterial() { return puzzleMaterial; }
    public String puzzleSize() { return puzzleSize; }
    public String piecesQuantity() { return piecesQuantity; }
    public String diceNumber() { return diceNumber; }
}
