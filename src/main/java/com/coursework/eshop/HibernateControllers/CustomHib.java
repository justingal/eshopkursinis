package com.coursework.eshop.HibernateControllers;

import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.model.*;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomHib extends GenericHib {
    private EntityManagerFactory entityManagerFactory = EntityManagerFactorySingleton.getEntityManagerFactory();

    public CustomHib() {
    }

    private final String errorConstant = "Error";
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
            JavaFxCustomsUtils.generateAlert(javafx.scene.control.Alert.AlertType.ERROR, errorConstant, errorConstant, "Error while getting root comments");
            return null;
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public void deleteProduct(int id, ProductType productType) {
        EntityManager entityManager = getEntityManager();

        try {
            entityManager.getTransaction().begin();

            Class<? extends Product> productClass = switch (productType) {
                case BOARD_GAME -> BoardGame.class;
                case PUZZLE -> Puzzle.class;
                case DICE -> Dice.class;
            };

            Product product = entityManager.find(productClass, id);
            if (product == null) {
                throw new IllegalArgumentException("Product not found with id: " + id);
            }

            product.removeFromWarehouse();
            entityManager.merge(product.getWarehouse());
            entityManager.remove(product);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            handleDeletionError(entityManager, e);
        } finally {
            closeEntityManager(entityManager);
        }
    }

    private void handleDeletionError(EntityManager entityManager, Exception e) {
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
        JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, errorConstant, errorConstant, "Error while deleting product");
    }

    private void closeEntityManager(EntityManager entityManager) {
        if (entityManager != null) {
            entityManager.close();
        }
    }

    public void deleteReview(int id) {
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Review review = entityManager.find(Review.class, id);
            if (review != null) {
                review = entityManager.merge(review);

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
                clearRelationships(review, entityManager);
                for (Comment reply : new ArrayList<>(review.getReplies())) {
                    entityManager.remove(reply);
                }
                review.getReplies().clear();
                entityManager.remove(review);

                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            JavaFxCustomsUtils.generateAlert(Alert.AlertType.ERROR, errorConstant, "Error while deleting review", e.getMessage());
        } finally {
            entityManager.close();
        }
    }

    public void deleteCommentAndChildren(int commentId) {
        EntityManager entityManager = getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            Query deleteChildrenQuery = entityManager.createQuery("DELETE FROM Comment c WHERE c.parentComment.id = :parentId");
            deleteChildrenQuery.setParameter("parentId", commentId);
            deleteChildrenQuery.executeUpdate();

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
        comment = em.merge(comment);
        for (Comment reply : new ArrayList<>(comment.getReplies())) {
            deleteCommentRecursive(reply, em);
        }
        clearRelationships(comment, em);
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
            JavaFxCustomsUtils.generateAlert(javafx.scene.control.Alert.AlertType.ERROR, errorConstant, errorConstant, "Error while deleting manager");
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }
    public void deleteOrder(int orderId) {
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            CustomerOrder customerOrder = entityManager.find(CustomerOrder.class, orderId);
            if (customerOrder.getCustomer() != null) {
                Customer customer = customerOrder.getCustomer();
                customer.getUserCustomerOrder().remove(customerOrder);
                customerOrder.setCustomer(null);
            }
            if (customerOrder.getResponsibleManager() != null) {
                Manager manager = customerOrder.getResponsibleManager();
                manager.getManagedOrders().remove(customerOrder);
                customerOrder.setResponsibleManager(null);
            }

            entityManager.remove(customerOrder);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            JavaFxCustomsUtils.generateAlert(javafx.scene.control.Alert.AlertType.ERROR, errorConstant, errorConstant, "Error while deleting order");
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

    public List<CustomerOrder> filterData(CustomerOrderFilter filter) {
        EntityManager em = getEntityManager();
        List<CustomerOrder> result = new ArrayList<>();

        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<CustomerOrder> cq = cb.createQuery(CustomerOrder.class);
            Root<CustomerOrder> order = cq.from(CustomerOrder.class);

            List<Predicate> predicates = buildPredicates(cb, order, filter);
            cq.where(predicates.toArray(new Predicate[0]));

            result = em.createQuery(cq).getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }

        return result;
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<CustomerOrder> order, CustomerOrderFilter filter) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getMinValue() > 0) {
            predicates.add(cb.ge(order.get("totalPrice"), filter.getMinValue()));
        }
        if (filter.getMaxValue() < Double.MAX_VALUE) {
            predicates.add(cb.le(order.get("totalPrice"), filter.getMaxValue()));
        }
        if (filter.getCustomer() != null) {
            predicates.add(cb.equal(order.get("customer"), filter.getCustomer()));
        }
        if (filter.getManager() != null) {
            predicates.add(cb.equal(order.get("responsibleManager"), filter.getManager()));
        }
        if (filter.getOrderStatus() != null) {
            predicates.add(cb.equal(order.get("orderStatus"), filter.getOrderStatus()));
        }
        if (filter.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(order.get("dateCreated"), filter.getStartDate()));
        }
        if (filter.getFinishDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(order.get("dateCreated"), filter.getFinishDate()));
        }

        return predicates;
    }


}
