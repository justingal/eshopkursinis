package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.fxController.tableviews.OrderTableParameters;
import com.coursework.eshop.model.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import mockit.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTabControllerTest {

    @Mocked
    private CustomHib customHib;

    @Injectable
    private TableView<OrderTableParameters> tableView;

    private OrderTabController controller;

    @BeforeAll
    static void initToolkit() {
        if (!Platform.isFxApplicationThread()) {
            Platform.startup(() -> {});
        }
    }

    @BeforeEach
    void setUp() {
        controller = new OrderTabController();
        controller.ordersTableView = new TableView<>();
        controller.setData(customHib);
    }


    @Test
    @DisplayName("Table must be created even when there are no orders")
    void testLoadOrderData_NoOrders() {
        givenUserIsAdmin();
        givenNoOrders();

        controller.loadOrderData();

        ObservableList<OrderTableParameters> tableData = controller.ordersTableView.getItems();
        assertNotNull(tableData, "TableView data should not be null");
        assertTrue(tableData.isEmpty(), "TableView should be empty");
    }

    @Test
    @DisplayName("Admin must be able to view all orders")
    void testLoadOrderData_MultipleOrders_AdminUser() {
        givenUserIsAdmin();
        List<CustomerOrder> orders = givenOrders(null);
        orders.getFirst().setResponsibleManager(new Manager());

        controller.loadOrderData();

        ObservableList<OrderTableParameters> tableData = controller.ordersTableView.getItems();
        assertNotNull(tableData, "TableView data should not be null");
        assertEquals(orders.size(), tableData.size(), "TableView should contain all orders");
    }

    @Test
    @DisplayName("Urgent orders must be marked as URGENT")
    void testLoadOrderData_UrgentOrderPrioritization() {
        givenUserIsManager();
        CustomerOrder urgentOrder = givenUrgentOrder();

        new Expectations() {{
            customHib.getAllRecords(CustomerOrder.class);
            result = Collections.singletonList(urgentOrder);
            customHib.update(urgentOrder);  // expect order to be updated
            times = 1;  // call `update` once
        }};

        controller.loadOrderData();

        ObservableList<OrderTableParameters> tableData = controller.ordersTableView.getItems();
        assertNotNull(tableData, "TableView data should not be null");
        assertEquals(1, tableData.size(), "TableView should have 1 order");
        assertEquals(
            OrderStatus.URGENT,
            tableData.getFirst().getOrderStatus(),
            "Order should be marked as URGENT"
        );

        // assert order was updated once
        new Verifications() {{
            customHib.update(urgentOrder);
            times = 1;
        }};
    }

    @Test
    @DisplayName("Manager only sees orders they are assigned to")
    void testLoadOrderData_ManagerOnlySeesTheirOrders() {
        Manager mainManager = givenUserIsManager();
        List<CustomerOrder> orders = givenOrders(mainManager);
        orders.get(1).setResponsibleManager(new Manager());

        controller.loadOrderData();

        ObservableList<OrderTableParameters> tableData = controller.ordersTableView.getItems();
        assertEquals(1, tableData.size(), "TableView should contain only one order");
        assertEquals(
            1,
            tableData.getFirst().getId(),
            "Order in TableView must have main manager's ID"
        );
    }

    @Test
    @DisplayName("Orders must be sorted by urgency")
    void testLoadOrderData_SortingOrders() {
        givenUserIsManager();
        CustomerOrder urgentOrder = createMockOrder(
            1,
            "Urgent Customer",
            LocalDate.now().minusDays(2),
            OrderStatus.URGENT,
            null
        );
        CustomerOrder regularOrder = createMockOrder(
            2,
            "Regular Customer",
            LocalDate.now().minusDays(1),
            OrderStatus.PENDING,
            null
        );
        new Expectations() {{
            customHib.getAllRecords(CustomerOrder.class);
            result = Arrays.asList(regularOrder, urgentOrder);
        }};

        controller.loadOrderData();

        ObservableList<OrderTableParameters> tableData = controller.ordersTableView.getItems();
        assertEquals(urgentOrder.getId(), tableData.get(0).getId(), "Urgent orders must be first");
        assertEquals(
            regularOrder.getId(),
            tableData.get(1).getId(),
            "Regular orders must be after urgent orders"
        );
    }


    private void givenUserIsAdmin() {
        StartGui.currentUser = new Admin();
    }

    private Manager givenUserIsManager() {
        Manager manager = new Manager();
        manager.setId(1);
        StartGui.currentUser = manager;
        return manager;
    }

    private CustomerOrder givenUrgentOrder() {
        return createMockOrder(
            1,
            "Urgent Customer",
            LocalDate.now().minusDays(2),
            OrderStatus.PENDING,
            null);
    }

    private void givenNoOrders() {
        new Expectations() {{
            customHib.getAllRecords(CustomerOrder.class);
            result = Collections.emptyList();
        }};
    }

    private List<CustomerOrder> givenOrders(Manager manager) {
        CustomerOrder order1 = createMockOrder(
                1,
                "John Doe",
                LocalDate.now().minusDays(2),
                OrderStatus.PROCESSING,
                manager
        );
        CustomerOrder order2 = createMockOrder(2,
                "Jane Smith",
                LocalDate.now().minusDays(1),
                OrderStatus.PENDING,
                manager
        );

        new Expectations() {{
            customHib.getAllRecords(CustomerOrder.class);
            result = Arrays.asList(order1, order2);
        }};

        return Arrays.asList(order1, order2);
    }

    private CustomerOrder createMockOrder(
        int id,
        String customerName,
        LocalDate dateCreated,
        OrderStatus status,
        Manager manager
    ) {
        Customer customer = new Customer();
        customer.setName(customerName);

        CustomerOrder order = new CustomerOrder();
        order.setId(id);
        order.setCustomer(customer);
        order.setDateCreated(dateCreated);
        order.setOrderStatus(status);
        order.setResponsibleManager(manager);
        order.setInOrderBoardGames(Collections.emptyList());
        return order;
    }
}