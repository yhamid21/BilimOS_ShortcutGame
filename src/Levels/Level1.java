package Levels;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Level1 {

    @FXML private Button nextButton;
    @FXML private TextArea textArea;
    @FXML private VBox congratsPopup;
    @FXML private ImageView backgroundImage;
    @FXML private Button homeButton;
    @FXML private Button nextLevelButton;

    private boolean levelCompleted = false;
    private Stage currentStage;

    @FXML
    private void initialize() {
        // Set the initial text in the TextArea and select it
        textArea.setText("Level 1 Complete!");
        textArea.selectAll();

        // Make sure congratsPopup is not visible at start
        if (congratsPopup != null) {
            congratsPopup.setVisible(false);
        }

        // Make background fill the screen
        if (backgroundImage != null) {
            backgroundImage.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    currentStage = (Stage) newScene.getWindow();
                    backgroundImage.fitWidthProperty().bind(newScene.widthProperty());
                    backgroundImage.fitHeightProperty().bind(newScene.heightProperty());
                    // This is critical - don't preserve ratio for background
                    backgroundImage.setPreserveRatio(false);
                }
            });
        }

        // Monitor for Ctrl+C keypresses
        textArea.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.C) {
                // User pressed Ctrl+C
                System.out.println("Ctrl+C detected!");

                // The text is already selected, so copying should work.
                // Force a clipboard update in case it didn't happen automatically
                ClipboardContent content = new ClipboardContent();
                content.putString(textArea.getText());
                Clipboard.getSystemClipboard().setContent(content);

                // Check the clipboard
                checkClipboardAndComplete();
            }
        });

        // Set up buttons
        nextButton.setOnAction(e -> {
            if (levelCompleted) {
                goToNextLevel();
            } else {
                System.out.println("You must complete the level to proceed!");
            }
        });

        // Configure home and next buttons if they exist
        if (homeButton != null) {
            homeButton.setOnAction(e -> goToHomePage());
        }

        if (nextLevelButton != null) {
            nextLevelButton.setOnAction(e -> goToNextLevel());
        }
    }

    private void checkClipboardAndComplete() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            String clipboardText = clipboard.getString();
            System.out.println("Clipboard content: " + clipboardText);

            if (clipboardText.contains("Level 1 Complete")) {
                if (!levelCompleted) {
                    levelCompleted = true;
                    System.out.println("Level completed!");
                    unlockNextLevel();
                    showCompletionPopup();
                }
            }
        }
    }

    /**
     * Shows a styled completion popup similar to the locked level popup
     */
    private void showCompletionPopup() {
        // Create a custom dialog
        Stage dialogStage = new Stage();
        if (currentStage != null) {
            dialogStage.initOwner(currentStage);
        }
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Level Complete");

        // Create the layout for the popup
        VBox popupContent = new VBox(15);
        popupContent.setAlignment(Pos.CENTER);
        popupContent.getStyleClass().add("game-popup");
        // For completed levels, use green border instead of red
        popupContent.setStyle("-fx-border-color: #4CAF50;");

        // Add a completion title with trophy emoji
        Text completedText = new Text("🏆 Level 1 Complete!");
        completedText.getStyleClass().add("game-popup-title");

        // Add a congratulatory message
        Text congratsText = new Text("Great job mastering Ctrl+C!");
        congratsText.getStyleClass().add("game-popup-subtext");

        // Add navigation buttons in a horizontal layout
        javafx.scene.layout.HBox buttonBox = new javafx.scene.layout.HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        // Home button
        Button homeBtn = new Button("Home");
        homeBtn.getStyleClass().add("game-popup-button");
        homeBtn.setStyle("-fx-background-color: #4CAF50; -fx-border-color: #388E3C;"); // Green for success
        homeBtn.setOnAction(e -> {
            dialogStage.close();
            goToHomePage();
        });

        // Next level button
        Button nextBtn = new Button("Next Level");
        nextBtn.getStyleClass().add("game-popup-button");
        nextBtn.setStyle("-fx-background-color: #4CAF50; -fx-border-color: #388E3C;"); // Green for success
        nextBtn.setOnAction(e -> {
            dialogStage.close();
            goToNextLevel();
        });

        buttonBox.getChildren().addAll(homeBtn, nextBtn);
        popupContent.getChildren().addAll(completedText, congratsText, buttonBox);

        // Create and style the scene
        Scene dialogScene = new Scene(popupContent, 400, 250);

        // Load the stylesheet
        String cssPath = "/HomePageStyles.css";
        if (getClass().getResource(cssPath) != null) {
            dialogScene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        } else {
            System.err.println("Could not find CSS file: " + cssPath);
        }

        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }

    private void unlockNextLevel() {
        // Unlock logic: Save to progress tracker or unlock the level in UI
        System.out.println("Next level unlocked.");

        // Example: Trigger unlock in a persistence layer or UI
        // E.g., AppState.markLevelAsCompleted(1);
    }

    private void goToHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
            Scene homeScene = new Scene(loader.load());

            // Get the current stage using any FXML element that's in the scene
            Stage stage = (Stage) textArea.getScene().getWindow();

            // Load the stylesheet
            String cssPath = "/HomePageStyles.css";
            if (getClass().getResource(cssPath) != null) {
                homeScene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            }

            stage.setScene(homeScene);
        } catch (IOException e) {
            System.err.println("Failed to load HomePage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void goToNextLevel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Levels/Level2.fxml"));
            Scene nextLevelScene = new Scene(loader.load());
            Stage stage = (Stage) textArea.getScene().getWindow();
            stage.setScene(nextLevelScene);
        } catch (IOException e) {
            System.err.println("Failed to load Level 2: " + e.getMessage());
            e.printStackTrace();

            // If Level 2 doesn't exist yet, go back to home
            try {
                goToHomePage();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}