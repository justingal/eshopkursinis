package com.coursework.eshop.HibernateControllers;


import com.coursework.eshop.fxController.JavaFxCustomsUtils;
import com.coursework.eshop.model.Customer;
import com.coursework.eshop.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.lang.reflect.Executable;
import java.util.ArrayList;
import java.util.List;
public class UserHib {

    private EntityManagerFactory entityManagerFactory = null;

    public UserHib(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    //persist yra insert
    public void createUser(User user) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    //merge yra update
    public void updateUser(User user) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    //delete user by id
    public void deleteUser(int id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User user = null;
            try {
                user = em.getReference(User.class, id);
                user.getId();
            } catch (Exception e) {
                System.out.println("User not found by ID");
            }
            em.remove(user);
            em.getTransaction().commit();
        }catch
        (Exception e){
            e.printStackTrace();
        }finally {
            if (em != null) em.close();
        }
    }

    public List<Customer> getAllCustomers() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery query = em.getCriteriaBuilder().createQuery();
            query.select(query.from(Customer.class));
            Query q = em.createQuery(query);
            return q.getResultList();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
        return new ArrayList<>();
    }

    public List<User> getAllUsers() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery query = em.getCriteriaBuilder().createQuery();
            query.select(query.from(User.class));
            Query q = em.createQuery(query);
            return q.getResultList();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
        return new ArrayList<>();
    }

    public User getUserByCredentials(String login, String password) {
        EntityManager entityManager = getEntityManager();
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = cb.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(cb.and(cb.like(root.get("login"), login), cb.like(root.get("password"), password)));
            Query q;

            q = entityManager.createQuery(query);
            return (User) q.getSingleResult();
        } catch (NoResultException e) {
            JavaFxCustomsUtils.generateAlert(
                    javafx.scene.control.Alert.AlertType.ERROR,
                    "Error",
                    "Error",
                    "Error while getting user by credentials");
            return null;
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }
}