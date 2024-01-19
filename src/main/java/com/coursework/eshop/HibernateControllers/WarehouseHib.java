package com.coursework.eshop.HibernateControllers;

import com.coursework.eshop.model.Customer;
import com.coursework.eshop.model.User;
import com.coursework.eshop.model.Warehouse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaQuery;

import java.util.ArrayList;
import java.util.List;

public class WarehouseHib {
    private EntityManagerFactory entityManagerFactory = null;

    public WarehouseHib(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    private EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    //persist yra insert
    public void createWarehouse(Warehouse warehouse) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(warehouse);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    //merge yra update
    public void updateWarehouse(Warehouse warehouse) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.merge(warehouse);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
    }

    //delete user by id
    public void deleteWarehouse(int id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Warehouse warehouse = null;
            try {
                warehouse = em.getReference(Warehouse.class, id);
                warehouse.getId();
            } catch (Exception e) {
                System.out.println("User not found by ID");
            }
            em.remove(warehouse);
            em.getTransaction().commit();
        }catch
        (Exception e){
            e.printStackTrace();
        }finally {
            if (em != null) em.close();
        }
    }

    public List<Warehouse> getAllWarehouse() {
        EntityManager em = null;
        try {
            em = getEntityManager();
            CriteriaQuery query = em.getCriteriaBuilder().createQuery();
            query.select(query.from(Warehouse.class));
            Query q = em.createQuery(query);
            return q.getResultList();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            if (em != null) em.close();
        }
        return new ArrayList<>();
    }
}
