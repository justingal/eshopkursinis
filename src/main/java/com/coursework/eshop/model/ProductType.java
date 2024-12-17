package com.coursework.eshop.model;
import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.fxController.MainShop.Util.ProductInputFields;

public enum ProductType {
    BOARD_GAME {
        @Override
        public void createProduct(CustomHib customHib, ProductInputFields fields, Warehouse warehouse, double price) {
            validateFieldNotEmpty(fields.playersQuantity(), "Players Quantity");
            validateFieldNotEmpty(fields.gameDuration(), "Game Duration");

            customHib.create(new BoardGame(
                    fields.productTitle(),
                    fields.description(),
                    fields.author(),
                    warehouse,
                    price,
                    fields.playersQuantity(),
                    fields.gameDuration()
            ));
        }
        @Override
        public void updateProduct(Product product, ProductInputFields fields) {
            BoardGame boardGame = (BoardGame) product;
            validateFieldNotEmpty(fields.playersQuantity(), "Players Quantity");
            validateFieldNotEmpty(fields.gameDuration(), "Game Duration");

            boardGame.setPlayersQuantity(fields.playersQuantity());
            boardGame.setGameDuration(fields.gameDuration());
        }
    },
    PUZZLE {
        @Override
        public void createProduct(CustomHib customHib, ProductInputFields fields, Warehouse warehouse, double price) {
            validateFieldNotEmpty(fields.puzzleMaterial(), "Puzzle Material");
            validateFieldNotEmpty(fields.puzzleSize(), "Puzzle Size");

            int piecesQuantity = Integer.parseInt(fields.piecesQuantity());
            customHib.create(new Puzzle(
                    fields.productTitle(),
                    fields.description(),
                    fields.author(),
                    warehouse,
                    price,
                    piecesQuantity,
                    fields.puzzleSize(),
                    fields.puzzleMaterial()
            ));
        }
        @Override
        public void updateProduct(Product product, ProductInputFields fields) {
            Puzzle puzzle = (Puzzle) product;
            validateFieldNotEmpty(fields.puzzleMaterial(), "Puzzle Material");
            validateFieldNotEmpty(fields.puzzleSize(), "Puzzle Size");

            int piecesQuantity = Integer.parseInt(fields.piecesQuantity());
            puzzle.setPiecesQuantity(piecesQuantity);
            puzzle.setPuzzleMaterial(fields.puzzleMaterial());
            puzzle.setPuzzleSize(fields.puzzleSize());
        }
    },
    DICE {
        @Override
        public void createProduct(CustomHib customHib, ProductInputFields fields, Warehouse warehouse, double price) {
            int diceNumber = Integer.parseInt(fields.diceNumber());
            customHib.create(new Dice(
                    fields.productTitle(),
                    fields.description(),
                    fields.author(),
                    warehouse,
                    price,
                    diceNumber
            ));
        }
        @Override
        public void updateProduct(Product product, ProductInputFields fields) {
            Dice dice = (Dice) product;
            int diceNumber = Integer.parseInt(fields.diceNumber());
            dice.setDiceNumber(diceNumber);
        }
    };

    public abstract void createProduct(CustomHib customHib, ProductInputFields fields, Warehouse warehouse, double price);
    public abstract void updateProduct(Product product, ProductInputFields fields);

    protected void validateFieldNotEmpty(String fieldValue, String fieldName) {
        if (fieldValue == null || fieldValue.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty.");
        }
    }
}

