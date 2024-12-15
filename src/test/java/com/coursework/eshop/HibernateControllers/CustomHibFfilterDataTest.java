package com.coursework.eshop.HibernateControllers;

import com.coursework.eshop.model.Customer;
import com.coursework.eshop.model.CustomerOrder;
import com.coursework.eshop.model.Manager;
import com.coursework.eshop.model.OrderStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomHibFfilterDataTest {

        @Mocked
        private CustomHib customHib;

        @Mocked
        private EntityManager entityManager;

        @Mocked
        private CriteriaBuilder criteriaBuilder;

        @Mocked
        private CriteriaQuery<CustomerOrder> criteriaQuery;

        @Mocked
        private Root<CustomerOrder> root;

    @BeforeEach
    void setUp() {

        new Expectations(customHib) {{
            customHib.getEntityManager();
            result = entityManager;
        }};


        new Expectations() {{
            entityManager.getCriteriaBuilder();
            result = criteriaBuilder;
        }};


        new Expectations() {{
            entityManager.close();
        }};
    }


    @Test
        void testFilterData_NoFilters() {
            new Expectations() {{
                customHib.getEntityManager();
                result = entityManager;

                entityManager.getCriteriaBuilder();
                result = criteriaBuilder;

                criteriaBuilder.createQuery(CustomerOrder.class);
                result = criteriaQuery;

                criteriaQuery.from(CustomerOrder.class);
                result = root;

                entityManager.createQuery(criteriaQuery).getResultList();
                result = new ArrayList<CustomerOrder>();
            }};

            List<CustomerOrder> result = customHib.filterData(
                    0, Double.MAX_VALUE, null, null, null, null, null
            );

            assertNotNull(result);
            assertTrue(result.isEmpty());

            new Verifications() {{
                entityManager.close();
                times = 1;
            }};
        }

        @Test
        void testFilterData_WithMinMaxPrice() {
            Customer mockCustomer = new Customer();
            Manager mockManager = new Manager();
            List<CustomerOrder> expectedOrders = Arrays.asList(new CustomerOrder());

            new Expectations() {{
                customHib.getEntityManager();
                result = entityManager;

                entityManager.getCriteriaBuilder();
                result = criteriaBuilder;

                criteriaBuilder.createQuery(CustomerOrder.class);
                result = criteriaQuery;

                criteriaQuery.from(CustomerOrder.class);
                result = root;

                entityManager.createQuery(criteriaQuery).getResultList();
                result = expectedOrders;
            }};

            List<CustomerOrder> result = customHib.filterData(
                    10.0, 100.0, null, null, null, null, null
            );

            assertNotNull(result);
            assertEquals(1, result.size());

            new Verifications() {{
                // Verify predicates for min and max price
                criteriaBuilder.ge(root.get("totalPrice"), 10.0);
                times = 1;

                criteriaBuilder.le(root.get("totalPrice"), 100.0);
                times = 1;

                entityManager.close();
                times = 1;
            }};
        }

        @Test
        void testFilterData_WithCustomerManagerStatusDates() {
            // Arrange
            Customer mockCustomer = new Customer();
            Manager mockManager = new Manager();
            LocalDate startDate = LocalDate.of(2023, 1, 1);
            LocalDate finishDate = LocalDate.of(2023, 12, 31);
            OrderStatus status = OrderStatus.FINISHED;

            List<CustomerOrder> expectedOrders = Arrays.asList(
                    new CustomerOrder(),
                    new CustomerOrder()
            );

            new Expectations() {{
                customHib.getEntityManager();
                result = entityManager;

                entityManager.getCriteriaBuilder();
                result = criteriaBuilder;

                criteriaBuilder.createQuery(CustomerOrder.class);
                result = criteriaQuery;

                criteriaQuery.from(CustomerOrder.class);
                result = root;

                entityManager.createQuery(criteriaQuery).getResultList();
                result = expectedOrders;
            }};

            List<CustomerOrder> result = customHib.filterData(
                    0, Double.MAX_VALUE,
                    mockCustomer, mockManager, status,
                    startDate, finishDate
            );

            assertNotNull(result);
            assertEquals(2, result.size());

            new Verifications() {{
                criteriaBuilder.equal(root.get("customer"), mockCustomer);
                times = 1;

                criteriaBuilder.equal(root.get("responsibleManager"), mockManager);
                times = 1;

                criteriaBuilder.equal(root.get("orderStatus"), status);
                times = 1;

                criteriaBuilder.greaterThanOrEqualTo(root.get("dateCreated"), startDate);
                times = 1;

                criteriaBuilder.lessThanOrEqualTo(root.get("dateCreated"), finishDate);
                times = 1;

                entityManager.close();
                times = 1;
            }};
        }

        @Test
        void testFilterData_EmptyResultSet() {
            new Expectations() {{
                customHib.getEntityManager();
                result = entityManager;

                entityManager.getCriteriaBuilder();
                result = criteriaBuilder;

                criteriaBuilder.createQuery(CustomerOrder.class);
                result = criteriaQuery;

                criteriaQuery.from(CustomerOrder.class);
                result = root;

                entityManager.createQuery(criteriaQuery).getResultList();
                result = Collections.emptyList();
            }};

            List<CustomerOrder> result = customHib.filterData(
                    50.0, 100.0, null, null, null, null, null
            );

            assertNotNull(result);
            assertTrue(result.isEmpty());

            new Verifications() {{
                entityManager.close();
                times = 1;
            }};
        }

        @Test
        void testFilterData_ExceptionHandling() {
            new Expectations() {{
                customHib.getEntityManager();
                result = entityManager;

                entityManager.getCriteriaBuilder();
                result = criteriaBuilder;

                criteriaBuilder.createQuery(CustomerOrder.class);
                result = criteriaQuery;

                criteriaQuery.from(CustomerOrder.class);
                result = root;

                entityManager.createQuery(criteriaQuery).getResultList();
                result = new RuntimeException("Database error");
            }};

            assertThrows(RuntimeException.class, () -> {
                customHib.filterData(
                        0, Double.MAX_VALUE, null, null, null, null, null
                );
            });

            new Verifications() {{
                entityManager.close();
                times = 1;
            }};
        }
    @Test
    void testFilterData_WithSingleCustomerFilter() {
        // Arrange
        Customer mockCustomer = new Customer();
        mockCustomer.setId(1);
        List<CustomerOrder> expectedOrders = Arrays.asList(new CustomerOrder());

        new Expectations() {{
            customHib.getEntityManager();
            result = entityManager;

            entityManager.getCriteriaBuilder();
            result = criteriaBuilder;

            criteriaBuilder.createQuery(CustomerOrder.class);
            result = criteriaQuery;

            criteriaQuery.from(CustomerOrder.class);
            result = root;

            entityManager.createQuery(criteriaQuery).getResultList();
            result = expectedOrders;
        }};

        List<CustomerOrder> result = customHib.filterData(
                0, Double.MAX_VALUE, mockCustomer, null, null, null, null
        );

        assertNotNull(result);
        assertEquals(1, result.size());

        new Verifications() {{
            criteriaBuilder.equal(root.get("customer"), mockCustomer);
            times = 1;

            entityManager.close();
            times = 1;
        }};
    }
    @Test
    void testFilterData_DefaultParameters() {
        List<CustomerOrder> expectedOrders = Arrays.asList(new CustomerOrder(), new CustomerOrder());

        new Expectations() {{
            customHib.getEntityManager();
            result = entityManager;

            entityManager.getCriteriaBuilder();
            result = criteriaBuilder;

            criteriaBuilder.createQuery(CustomerOrder.class);
            result = criteriaQuery;

            criteriaQuery.from(CustomerOrder.class);
            result = root;

            entityManager.createQuery(criteriaQuery).getResultList();
            result = expectedOrders;
        }};

        List<CustomerOrder> result = customHib.filterData(
                0, Double.MAX_VALUE, null, null, null, null, null
        );

        assertNotNull(result);
        assertEquals(2, result.size());

        new Verifications() {{
            entityManager.close();
            times = 1;
        }};
    }
    @Test
    void testFilterData_NullValuesIgnored() {
        // Arrange
        List<CustomerOrder> expectedOrders = Arrays.asList(new CustomerOrder());

        new Expectations() {{
            customHib.getEntityManager();
            result = entityManager;

            entityManager.getCriteriaBuilder();
            result = criteriaBuilder;

            criteriaBuilder.createQuery(CustomerOrder.class);
            result = criteriaQuery;

            criteriaQuery.from(CustomerOrder.class);
            result = root;

            entityManager.createQuery(criteriaQuery).getResultList();
            result = expectedOrders;
        }};

        List<CustomerOrder> result = customHib.filterData(
                0, 0, null, null, null, null, null
        );

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        new Verifications() {{
            entityManager.close();
            times = 1;
        }};
    }
    @Test
    void testFilterData_InvalidPriceRange() {
        // Arrange
        new Expectations() {{
            customHib.getEntityManager();
            result = entityManager;

            entityManager.getCriteriaBuilder();
            result = criteriaBuilder;

            criteriaBuilder.createQuery(CustomerOrder.class);
            result = criteriaQuery;

            criteriaQuery.from(CustomerOrder.class);
            result = root;

            entityManager.createQuery(criteriaQuery).getResultList();
            result = Collections.emptyList();
        }};

        List<CustomerOrder> result = customHib.filterData(
                100.0, 10.0, null, null, null, null, null
        );

        assertNotNull(result);
        assertTrue(result.isEmpty());

        new Verifications() {{
            entityManager.close();
            times = 1;
        }};
    }
    @Test
    void testFilterData_MultipleFiltersNoResult() {
        // Arrange
        Customer mockCustomer = new Customer();
        Manager mockManager = new Manager();
        OrderStatus status = OrderStatus.PENDING;
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        new Expectations() {{
            customHib.getEntityManager();
            result = entityManager;

            entityManager.getCriteriaBuilder();
            result = criteriaBuilder;

            criteriaBuilder.createQuery(CustomerOrder.class);
            result = criteriaQuery;

            criteriaQuery.from(CustomerOrder.class);
            result = root;

            entityManager.createQuery(criteriaQuery).getResultList();
            result = Collections.emptyList();
        }};

        List<CustomerOrder> result = customHib.filterData(
                100.0, 200.0, mockCustomer, mockManager, status, startDate, endDate
        );

        assertNotNull(result);
        assertTrue(result.isEmpty());

        new Verifications() {{
            entityManager.close();
            times = 1;
        }};
    }





}