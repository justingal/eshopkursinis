module com.coursework.eshop {
    requires javafx.controls;
    requires javafx.fxml;

    requires lombok;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;

    opens com.coursework.eshop to javafx.fxml;
    exports com.coursework.eshop;
    opens com.coursework.eshop.fxController to javafx.fxml;
    exports com.coursework.eshop.fxController;
    opens com.coursework.eshop.model to org.hibernate.orm.core;
    exports com.coursework.eshop.model;
}