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
            CustomerOrderFilter filter = new CustomerOrderFilter();

            new Expectations() {{
                criteriaBuilder.createQuery(CustomerOrder.class);
                result = criteriaQuery;

                criteriaQuery.from(CustomerOrder.class);
                result = root;

                entityManager.createQuery(criteriaQuery).getResultList();
                result = new ArrayList<>();
            }};

            List<CustomerOrder> result = customHib.filterData(filter);

            assertNotNull(result);
            assertTrue(result.isEmpty());

            new Verifications() {{
                entityManager.close();
                times = 1;
            }};
        }

        @Test
        void testFilterData_WithMinMaxPrice() {
            CustomerOrderFilter filter = new CustomerOrderFilter();
            filter.setMinValue(10.0);
            filter.setMaxValue(100.0);

            new Expectations() {{
                criteriaBuilder.createQuery(CustomerOrder.class);
                result = criteriaQuery;

                criteriaQuery.from(CustomerOrder.class);
                result = root;

                criteriaBuilder.ge(root.get("totalPrice"), 10.0);
                times = 1;

                criteriaBuilder.le(root.get("totalPrice"), 100.0);
                times = 1;

                entityManager.createQuery(criteriaQuery).getResultList();
                result = Arrays.asList(new CustomerOrder());
            }};

            List<CustomerOrder> result = customHib.filterData(filter);

            assertNotNull(result);
            assertEquals(1, result.size());

            new Verifications() {{
                entityManager.close();
                times = 1;
            }};
        }

        @Test
        void testFilterData_WithCustomerManagerStatusDates() {
            CustomerOrderFilter filter = new CustomerOrderFilter();
            filter.setCustomer(new Customer());
            filter.setManager(new Manager());
            filter.setOrderStatus(OrderStatus.PENDING);
            filter.setStartDate(LocalDate.of(2023, 1, 1));
            filter.setFinishDate(LocalDate.of(2023, 12, 31));

            new Expectations() {{
                criteriaBuilder.createQuery(CustomerOrder.class);
                result = criteriaQuery;

                criteriaQuery.from(CustomerOrder.class);
                result = root;

                criteriaBuilder.equal(root.get("customer"), filter.getCustomer());
                times = 1;

                criteriaBuilder.equal(root.get("responsibleManager"), filter.getManager());
                times = 1;

                criteriaBuilder.equal(root.get("orderStatus"), filter.getOrderStatus());
                times = 1;

                criteriaBuilder.greaterThanOrEqualTo(root.get("dateCreated"), filter.getStartDate());
                times = 1;

                criteriaBuilder.lessThanOrEqualTo(root.get("dateCreated"), filter.getFinishDate());
                times = 1;

                entityManager.createQuery(criteriaQuery).getResultList();
                result = Arrays.asList(new CustomerOrder(), new CustomerOrder());
            }};

            List<CustomerOrder> result = customHib.filterData(filter);

            assertNotNull(result);
            assertEquals(2, result.size());

            new Verifications() {{
                entityManager.close();
                times = 1;
            }};
        }

    @Test
    void testFilterData_EmptyResultSet() {
        CustomerOrderFilter filter = new CustomerOrderFilter();
        filter.setMinValue(50.0);
        filter.setMaxValue(100.0);

        new Expectations() {{
            criteriaBuilder.createQuery(CustomerOrder.class);
            result = criteriaQuery;

            criteriaQuery.from(CustomerOrder.class);
            result = root;

            entityManager.createQuery(criteriaQuery).getResultList();
            result = Collections.emptyList();
        }};

        List<CustomerOrder> result = customHib.filterData(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        new Verifications() {{
            entityManager.close();
            times = 1;
        }};
    }


    @Test
    void testFilterData_ExceptionHandling() {
        CustomerOrderFilter filter = new CustomerOrderFilter();

        new Expectations() {{
            entityManager.createQuery(criteriaQuery).getResultList();
            result = new RuntimeException("Database error");
        }};

        assertThrows(RuntimeException.class, () -> customHib.filterData(filter));

        new Verifications() {{
            entityManager.close();
            times = 1;
        }};
    }

    @Test
    void testFilterData_WithSingleCustomerFilter() {
        Customer mockCustomer = new Customer();
        mockCustomer.setId(1);

        CustomerOrderFilter filter = new CustomerOrderFilter();
        filter.setCustomer(mockCustomer);

        new Expectations() {{
            criteriaBuilder.createQuery(CustomerOrder.class);
            result = criteriaQuery;

            criteriaQuery.from(CustomerOrder.class);
            result = root;

            criteriaBuilder.equal(root.get("customer"), mockCustomer);
            times = 1;

            entityManager.createQuery(criteriaQuery).getResultList();
            result = Arrays.asList(new CustomerOrder());
        }};

        List<CustomerOrder> result = customHib.filterData(filter);

        assertNotNull(result);
        assertEquals(1, result.size());

        new Verifications() {{
            entityManager.close();
            times = 1;
        }};
    }


    @Test
    void testFilterData_DefaultParameters() {
        CustomerOrderFilter filter = new CustomerOrderFilter();

        new Expectations() {{
            criteriaBuilder.createQuery(CustomerOrder.class);
            result = criteriaQuery;

            criteriaQuery.from(CustomerOrder.class);
            result = root;

            entityManager.createQuery(criteriaQuery).getResultList();
            result = Arrays.asList(new CustomerOrder(), new CustomerOrder());
        }};

        List<CustomerOrder> result = customHib.filterData(filter);

        assertNotNull(result);
        assertEquals(2, result.size());

        new Verifications() {{
            entityManager.close();
            times = 1;
        }};
    }

    @Test
    void testFilterData_NullValuesIgnored() {
        CustomerOrderFilter filter = new CustomerOrderFilter();

        new Expectations() {{
            criteriaBuilder.createQuery(CustomerOrder.class);
            result = criteriaQuery;

            criteriaQuery.from(CustomerOrder.class);
            result = root;

            entityManager.createQuery(criteriaQuery).getResultList();
            result = Arrays.asList(new CustomerOrder());
        }};

        List<CustomerOrder> result = customHib.filterData(filter);

        assertNotNull(result);
        assertEquals(1, result.size());

        new Verifications() {{
            entityManager.close();
            times = 1;
        }};
    }

    @Test
    void testFilterData_InvalidPriceRange() {
        CustomerOrderFilter filter = new CustomerOrderFilter();
        filter.setMinValue(100.0);
        filter.setMaxValue(10.0);

        new Expectations() {{
            criteriaBuilder.createQuery(CustomerOrder.class);
            result = criteriaQuery;

            criteriaQuery.from(CustomerOrder.class);
            result = root;

            entityManager.createQuery(criteriaQuery).getResultList();
            result = Collections.emptyList();
        }};

        List<CustomerOrder> result = customHib.filterData(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        new Verifications() {{
            entityManager.close();
            times = 1;
        }};
    }


    @Test
    void testFilterData_MultipleFiltersNoResult() {
        CustomerOrderFilter filter = new CustomerOrderFilter();
        filter.setMinValue(100.0);
        filter.setMaxValue(200.0);
        filter.setCustomer(new Customer());
        filter.setManager(new Manager());
        filter.setOrderStatus(OrderStatus.PENDING);
        filter.setStartDate(LocalDate.of(2023, 1, 1));
        filter.setFinishDate(LocalDate.of(2023, 12, 31));

        new Expectations() {{
            criteriaBuilder.createQuery(CustomerOrder.class);
            result = criteriaQuery;

            criteriaQuery.from(CustomerOrder.class);
            result = root;

            entityManager.createQuery(criteriaQuery).getResultList();
            result = Collections.emptyList();
        }};

        List<CustomerOrder> result = customHib.filterData(filter);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        new Verifications() {{
            entityManager.close();
            times = 1;
        }};
    }






}