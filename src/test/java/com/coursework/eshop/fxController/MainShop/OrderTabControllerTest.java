package com.coursework.eshop.fxController.MainShop;

import com.coursework.eshop.HibernateControllers.CustomHib;
import com.coursework.eshop.StartGui;
import com.coursework.eshop.fxController.tableviews.OrderTableParameters;
import com.coursework.eshop.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import mockit.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

//    @BeforeAll
//    static void initToolkit() {
//        if (!Platform.isFxApplicationThread()) {
//            Platform.startup(() -> {});
//        }
//    }

    @BeforeEach
    void setUp() {
        // Create a real instance of the controller
        controller = new OrderTabController();
        controller.ordersTableView = new TableView<>();
        controller.setData(customHib);
    }

    @Test
    void testLoadOrderData_NoOrders() {
        // Set current user
        User currentUser = new Admin();
        StartGui.currentUser = currentUser;

        // Expect an empty list
        new Expectations() {{
            customHib.getAllRecords(CustomerOrder.class);
            result = Collections.emptyList();
        }};

        // Execute the method
        controller.loadOrderData();

        // Verify the table data
        ObservableList<OrderTableParameters> tableData = controller.ordersTableView.getItems();
        assertNotNull(tableData, "TableView data should not be null");
        assertTrue(tableData.isEmpty(), "TableView should be empty for no orders");
    }

    @Test
    void testLoadOrderData_MultipleOrders_AdminUser() {
        // Set current user as Admin
        Admin admin = new Admin();
        StartGui.currentUser = admin;

        // Create mock orders
        CustomerOrder order1 = createMockOrder(1, "John Doe", LocalDate.now().minusDays(2),
                OrderStatus.PROCESSING, null);
        CustomerOrder order2 = createMockOrder(2, "Jane Smith", LocalDate.now().minusDays(1),
                OrderStatus.PENDING, null);

        // Set up expectations
        new Expectations() {{
            customHib.getAllRecords(CustomerOrder.class);
            result = Arrays.asList(order1, order2);
        }};

        // Execute the method
        controller.loadOrderData();

        // Verify the table data
        ObservableList<OrderTableParameters> tableData = controller.ordersTableView.getItems();
        assertNotNull(tableData, "TableView data should not be null");
        assertEquals(2, tableData.size(), "TableView should have 2 orders");

        // Verify order details
        assertEquals(order1.getId(), tableData.get(0).getId(), "First order ID mismatch");
        assertEquals(order2.getId(), tableData.get(1).getId(), "Second order ID mismatch");
    }

    @Test
    void testLoadOrderData_UrgentOrderPrioritization() {
        // Set current user as Manager
        Manager manager = new Manager();
        manager.setId(1);
        StartGui.currentUser = manager;

        // Create an order that should be marked as URGENT
        CustomerOrder urgentOrder = createMockOrder(1, "Urgent Customer", LocalDate.now().minusDays(2),
                OrderStatus.PENDING, null);

        // Set up expectations
        new Expectations() {{
            customHib.getAllRecords(CustomerOrder.class);
            result = Collections.singletonList(urgentOrder);

            // Expect the order to be updated
            customHib.update(urgentOrder);
        }};

        // Execute the method
        controller.loadOrderData();

        // Verify the table data
        ObservableList<OrderTableParameters> tableData = controller.ordersTableView.getItems();
        assertNotNull(tableData, "TableView data should not be null");
        assertEquals(1, tableData.size(), "TableView should have 1 order");
        assertEquals(OrderStatus.URGENT, tableData.get(0).getOrderStatus(), "Order should be marked as URGENT");

        // Verify the update
        new Verifications() {{
            customHib.update(urgentOrder);
            times = 1;
        }};
    }
    @Test
    void testLoadOrderData_EmptyOrderList() {
        // Nustatome dabartinį naudotoją
        StartGui.currentUser = new Manager();

        // Simuliuojame tuščią užsakymų sąrašą
        new Expectations() {{
            customHib.getAllRecords(CustomerOrder.class);
            result = Collections.emptyList();
        }};

        // Vykdome metodą
        controller.loadOrderData();

        // Tikriname, ar TableView yra tuščias
        ObservableList<OrderTableParameters> tableData = controller.ordersTableView.getItems();
        assertNotNull(tableData, "TableView neturėtų būti null");
        assertTrue(tableData.isEmpty(), "TableView turėtų būti tuščias");
    }

    @Test
    void testLoadOrderData_ManagerOnlySeesTheirOrders() {
        // Sukuriame vadybininką ir priskiriame jį dabartiniam naudotojui
        Manager manager = new Manager();
        manager.setId(1);
        StartGui.currentUser = manager;

        // Sukuriame užsakymus
        CustomerOrder order1 = createMockOrder(1, "John Doe", LocalDate.now().minusDays(1), OrderStatus.PENDING, manager);
        CustomerOrder order2 = createMockOrder(2, "Jane Doe", LocalDate.now().minusDays(2), OrderStatus.PROCESSING, new Manager());

        // Simuliuojame, kad grąžinami visi užsakymai
        new Expectations() {{
            customHib.getAllRecords(CustomerOrder.class);
            result = Arrays.asList(order1, order2);
        }};

        // Vykdome metodą
        controller.loadOrderData();

        // Tikriname, ar TableView turi tik to vadybininko užsakymus
        ObservableList<OrderTableParameters> tableData = controller.ordersTableView.getItems();
        assertEquals(1, tableData.size(), "TableView turėtų rodyti tik vieną užsakymą");
        assertEquals(1, tableData.get(0).getId(), "Rodytas užsakymo ID turėtų sutapti su vadybininko užsakymu");
    }

    @Test
    void testLoadOrderData_AdminSeesAllOrders() {
        // Sukuriame administratorių ir priskiriame jį dabartiniam naudotojui
        Admin admin = new Admin();
        StartGui.currentUser = admin;

        // Sukuriame užsakymus
        CustomerOrder order1 = createMockOrder(1, "John Doe", LocalDate.now().minusDays(1), OrderStatus.PENDING, null);
        CustomerOrder order2 = createMockOrder(2, "Jane Doe", LocalDate.now().minusDays(2), OrderStatus.PROCESSING, null);

        // Simuliuojame, kad grąžinami visi užsakymai
        new Expectations() {{
            customHib.getAllRecords(CustomerOrder.class);
            result = Arrays.asList(order1, order2);
        }};

        // Vykdome metodą
        controller.loadOrderData();

        // Tikriname, ar TableView turi visus užsakymus
        ObservableList<OrderTableParameters> tableData = controller.ordersTableView.getItems();
        assertEquals(2, tableData.size(), "Administratorius turėtų matyti visus užsakymus");
    }

    @Test
    void testLoadOrderData_UrgentOrderMarkedCorrectly() {
        // Sukuriame vadybininką ir priskiriame jį dabartiniam naudotojui
        Manager manager = new Manager();
        StartGui.currentUser = manager;

        // Sukuriame užsakymą, kuris turi būti pažymėtas kaip „URGENT“
        CustomerOrder order = createMockOrder(1, "Urgent Customer", LocalDate.now().minusDays(2), OrderStatus.PENDING, null);

        // Simuliuojame, kad grąžinamas šis užsakymas
        new Expectations() {{
            customHib.getAllRecords(CustomerOrder.class);
            result = Collections.singletonList(order);

            customHib.update(order);
            times = 1; // Tikriname, ar `update` buvo iškviestas vieną kartą
        }};

        // Vykdome metodą
        controller.loadOrderData();

        // Tikriname, ar užsakymas pažymėtas kaip URGENT
        ObservableList<OrderTableParameters> tableData = controller.ordersTableView.getItems();
        assertEquals(OrderStatus.URGENT, tableData.get(0).getOrderStatus(), "Užsakymas turėtų būti pažymėtas kaip URGENT");
    }

    @Test
    void testLoadOrderData_SortingOrders() {
        // Sukuriame vadybininką ir priskiriame jį dabartiniam naudotojui
        Manager manager = new Manager();
        StartGui.currentUser = manager;

        // Sukuriame užsakymus
        CustomerOrder urgentOrder = createMockOrder(1, "Urgent Customer", LocalDate.now().minusDays(2), OrderStatus.URGENT, null);
        CustomerOrder regularOrder = createMockOrder(2, "Regular Customer", LocalDate.now().minusDays(1), OrderStatus.PENDING, null);

        // Simuliuojame, kad grąžinami šie užsakymai
        new Expectations() {{
            customHib.getAllRecords(CustomerOrder.class);
            result = Arrays.asList(regularOrder, urgentOrder);
        }};

        // Vykdome metodą
        controller.loadOrderData();

        // Tikriname, ar užsakymai tinkamai išrūšiuoti
        ObservableList<OrderTableParameters> tableData = controller.ordersTableView.getItems();
        assertEquals(urgentOrder.getId(), tableData.get(0).getId(), "Skubus užsakymas turėtų būti pirmas");
        assertEquals(regularOrder.getId(), tableData.get(1).getId(), "Įprastas užsakymas turėtų būti antras");
    }

    // Helper method to create mock orders
    private CustomerOrder createMockOrder(int id, String customerName, LocalDate dateCreated,
                                          OrderStatus status, Manager manager) {
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
