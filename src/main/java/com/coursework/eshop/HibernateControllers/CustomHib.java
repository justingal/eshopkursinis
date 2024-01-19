package com.coursework.eshop.HibernateControllers;

import jakarta.persistence.EntityManagerFactory;

public class CustomHib extends GenericHib{
    public CustomHib(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

}
