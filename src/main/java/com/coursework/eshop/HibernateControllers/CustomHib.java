package com.coursework.eshop.HibernateControllers;

import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class CustomHib extends GenericHib{
    public CustomHib(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    };

    public void deleteProduct(int id, ProductType productType) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();

            if (productType == ProductType.BOARD_GAME) {

                var product = entityManager.find(BoardGame.class, id);

                Warehouse warehouse = product.getWarehouse();
                if (warehouse != null) {
                    warehouse.getInStockBoardGames().remove(product);
                    entityManager.merge(warehouse);
                }

                entityManager.remove(product);
                entityManager.getTransaction().commit();
            } else if (productType == ProductType.PUZZLE) {
                var product = entityManager.find(Puzzle.class, id);

                Warehouse warehouse = product.getWarehouse();
                if (warehouse != null) {
                    warehouse.getInStockPuzzles().remove(product);
                    entityManager.merge(warehouse);
                }

                entityManager.remove(product);
                entityManager.getTransaction().commit();
            } else if (productType == ProductType.DICE) {
                var product = entityManager.find(Dice.class, id);

                Warehouse warehouse = product.getWarehouse();
                if (warehouse != null) {
                    warehouse.getInStockDices().remove(product);
                    entityManager.merge(warehouse);
                }

                entityManager.remove(product);
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            JavaFxCustomsUtils.generateAlert(
                    javafx.scene.control.Alert.AlertType.ERROR,
                    "Error",
                    "Error",
                    "Error while deleting product");
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

        public void deleteComment(int id){
            EntityManager entityManager = getEntityManager();
            try {
                entityManager.getTransaction().begin();
                var comment = entityManager.find(Comment.class, id);

                User user = comment.getUser();
                if (user != null) {
                    user.getMyComments().remove(comment);
                    entityManager.merge(user);
                }

                entityManager.remove(comment);
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                JavaFxCustomsUtils.generateAlert(
                        javafx.scene.control.Alert.AlertType.ERROR,
                        "Error",
                        "Error",
                        "Error while deleting Comment");
            } finally {
                if (entityManager != null) entityManager.close();
            }
        }


    public void deleteManager(int id) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            var manager = entityManager.find(User.class, id);

            entityManager.remove(manager);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            JavaFxCustomsUtils.generateAlert(
                    javafx.scene.control.Alert.AlertType.ERROR,
                    "Error",
                    "Error",
                    "Error while deleting manager");
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }
}
