package java.com.coursework.eshop;

import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TestExample extends ApplicationTest {

    private Button button;

    /**
     * Will be called with {@code @Before} semantics, i. e. before each test method.
     */
    @Override
    public void start(Stage stage) {
        button = new Button("click me!");
        button.setOnAction(actionEvent -> button.setText("clicked!"));
        stage.setScene(new Scene(new StackPane(button), 100, 100));
        stage.show();
    }

    @Test
    public void should_contain_button_with_text() {
        FxAssert.verifyThat(".button", LabeledMatchers.hasText("click me!"));
    }

    @Test
    public void when_button_is_clicked_text_changes() {
        // when:
        clickOn(".button");

        // then:
        FxAssert.verifyThat(".button", LabeledMatchers.hasText("clicked!"));
    }
}