package com.coursework.eshop.HibernateControllers;

import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.model.BoardGame;
import com.coursework.eshop.model.Warehouse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class CustomHib extends GenericHib{
    public CustomHib(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public void deleteProduct(int id) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            var product = entityManager.find(BoardGame.class, id);

            Warehouse warehouse = product.getWarehouse();
            if (warehouse != null) {
                warehouse.getInStockBoardGames().remove(product);
                entityManager.merge(warehouse);
            }

            entityManager.remove(product);
            entityManager.getTransaction().commit();
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
}
