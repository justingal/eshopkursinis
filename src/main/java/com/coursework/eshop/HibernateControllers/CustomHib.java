package com.coursework.eshop.HibernateControllers;

import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.model.*;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class CustomHib extends GenericHib{
    private EntityManagerFactory entityManagerFactory = EntityManagerFactorySingleton.getEntityManagerFactory();
    public CustomHib() {

    };

    public List<Comment> readAllRootComments() {
        EntityManager entityManager = getEntityManager();
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Comment> query = cb.createQuery(Comment.class);
            Root<Comment> root = query.from(Comment.class);

            query.select(root).where(cb.isNull(root.get("parentComment")));

            TypedQuery<Comment> typedQuery = entityManager.createQuery(query);
            return typedQuery.getResultList();
        } catch (NoResultException e) {
            JavaFxCustomsUtils JavaFxCustomUtils = new JavaFxCustomsUtils();
            JavaFxCustomUtils.generateAlert(
                    javafx.scene.control.Alert.AlertType.ERROR,
                    "Error",
                    "Error",
                    "Error while getting root comments");
            return null;
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

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


    public void deleteReview(int id) {
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Review review = entityManager.find(Review.class, id);
            if (review != null) {
                review = entityManager.merge(review);  // Ensure the review is attached

                // Remove the review from the associated product
                if (review.getBoardGame() != null) {
                    BoardGame boardGame = review.getBoardGame();
                    boardGame.getReviews().remove(review);
                    review.setBoardGame(null);
                } else if (review.getDice() != null) {
                    Dice dice = review.getDice();
                    dice.getReviews().remove(review);
                    review.setDice(null);
                } else if (review.getPuzzle() != null) {
                    Puzzle puzzle = review.getPuzzle();
                    puzzle.getReviews().remove(review);
                    review.setPuzzle(null);
                }

                // Clear other relationships
                clearRelationships(review, entityManager);

                // Delete child comments
                for (Comment reply : new ArrayList<>(review.getReplies())) {
                    entityManager.remove(reply);
                }
                review.getReplies().clear();

                // Remove the review
                entityManager.remove(review);

                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            JavaFxCustomsUtils.generateAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Error while deleting review",
                    e.getMessage()
            );
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public void deleteCommentAndChildren(int commentId) {
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            // Delete child comments
            Query deleteChildrenQuery = entityManager.createQuery("DELETE FROM Comment c WHERE c.parentComment.id = :parentId");
            deleteChildrenQuery.setParameter("parentId", commentId);
            deleteChildrenQuery.executeUpdate();

            // Delete the parent comment
            Query deleteParentQuery = entityManager.createQuery("DELETE FROM Comment c WHERE c.id = :id");
            deleteParentQuery.setParameter("id", commentId);
            deleteParentQuery.executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, "Deletion Error", "Error during comment deletion", e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    private void clearRelationships(Comment comment, EntityManager em) {
        if (comment.getParentComment() != null) {
            Comment parent = comment.getParentComment();
            parent.getReplies().remove(comment);
            comment.setParentComment(null);
        }

        if (comment.getUser() != null) {
            User user = comment.getUser();
            user.getMyComments().remove(comment);
            comment.setUser(null);
        }

        if (comment instanceof Review review) {
            if (review.getBoardGame() != null) {
                BoardGame boardGame = review.getBoardGame();
                boardGame.getReviews().remove(review);
                review.setBoardGame(null);
            }
            if (review.getDice() != null) {
                Dice dice = review.getDice();
                dice.getReviews().remove(review);
                review.setDice(null);
            }
            if (review.getPuzzle() != null) {
                Puzzle puzzle = review.getPuzzle();
                puzzle.getReviews().remove(review);
                review.setPuzzle(null);
            }
        }
    }

    private void deleteCommentRecursive(Comment comment, EntityManager em) {
        comment = em.merge(comment);  // Reattach the comment to the persistence context
        for (Comment reply : new ArrayList<>(comment.getReplies())) {
            deleteCommentRecursive(reply, em);
        }
        clearRelationships(comment,em);
        em.remove(comment);
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

    public User getUserByLogin(String login) {
        EntityManager entityManager = getEntityManager();
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(cb.equal(root.get("login"), login));
            Query q = entityManager.createQuery(query);
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

}
