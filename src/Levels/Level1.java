package Levels;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.text.Font;

public class Level1 {

    private boolean ctrlPressed = false;
    private boolean cPressed = false;

    public void start(Stage stage) {
        Label instruction = new Label("Level 1: Copy the selected text using Ctrl + C");
        instruction.setFont(new Font(18));

        TextArea textArea = new TextArea("Select part of this text and press Ctrl + C to copy.");
        textArea.setWrapText(true);
        textArea.setPrefRowCount(5);
        textArea.setFont(new Font(16));

        Label feedback = new Label();
        feedback.setStyle("-fx-text-fill: green; -fx-font-size: 16;");

        VBox layout = new VBox(20, instruction, textArea, feedback);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 30;");

        Scene scene = new Scene(layout, 600, 400);

        // Handle key presses
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.CONTROL) {
                ctrlPressed = true;
            } else if (event.getCode() == KeyCode.C && ctrlPressed) {
                cPressed = true;
                if (!textArea.getSelectedText().isEmpty()) {
                    feedback.setText("✔ Text copied successfully! Great job!");
                    feedback.setStyle("-fx-text-fill: green;");
                } else {
                    feedback.setText("⚠ Please select some text first.");
                    feedback.setStyle("-fx-text-fill: orange;");
                }
            }
        });

        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.CONTROL) {
                ctrlPressed = false;
            }
        });

        stage.setScene(scene);
        stage.setTitle("Level 1 - Copy Shortcut");
        stage.show();
    }
}
