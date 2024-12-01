package com.coursework.eshop.HibernateControllers;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.assertions.api.Assertions;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class CustomHibTest extends ApplicationTest {

    private Button button;

    @Override
    public void start(Stage stage) {
        button = new Button("click me!");
        button.setOnAction(actionEvent -> button.setText("clicked!"));
        stage.setScene(new Scene(new StackPane(button), 100, 100));
        stage.show();
    }

    @Test
    public void should_contain_button_with_text() {
        Assertions.assertThat(button).hasText("click me!");
    }

    @Test
    public void when_button_is_clicked_text_changes() {
        // when:
        clickOn(".button");

        // then:
        Assertions.assertThat(button).hasText("clicked!");
    }
}
