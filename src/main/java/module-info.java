module com.coursework.eshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires bcrypt;
    requires net.bytebuddy;
//    requires javafx.swing;
    requires junit;

    exports com.coursework.eshop.fxController.tableviews to javafx.base;
    opens com.coursework.eshop to javafx.fxml;
    exports com.coursework.eshop;
    opens com.coursework.eshop.model to javafx.fxml, org.hibernate.orm.core;
    exports com.coursework.eshop.model;
    opens com.coursework.eshop.fxController to javafx.fxml;
    exports com.coursework.eshop.fxController to javafx.fxml;
    opens com.coursework.eshop.fxController.tableviews to javafx.base;
    opens com.coursework.eshop.HibernateControllers to javafx.fxml;
    exports com.coursework.eshop.HibernateControllers to javafx.fxml;
    exports com.coursework.eshop.fxController.MainShop to javafx.fxml;
    opens com.coursework.eshop.fxController.MainShop to javafx.fxml;

}