
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import Levels.LevelProgressTracker;

import java.io.IOException;

public class HomePage {

    @FXML
    private Text titleText;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private GridPane levelGrid;

    @FXML
    private ScrollPane levelScrollPane;

    private final int totalLevels = 10; // Total number of levels
    private int completedLevels = 0;   // Number of completed levels
    private Stage primaryStage;       // Reference to the primary stage

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initialize() {
        createLevelButtons();
        updateProgressBar();
    }

    /**
     * Dynamically creates the level buttons and customizes their behavior and appearance.
     */
    private void createLevelButtons() {
        LevelProgressTracker tracker = LevelProgressTracker.getInstance();
        int completedLevels = tracker.getCompletedLevelsCount();

        for (int i = 1; i <= totalLevels; i++) {
            final int levelNumber = i;

            Button levelButton = new Button("Level " + levelNumber);
            levelButton.setPrefSize(160, 80);

            if (tracker.isLevelCompleted(levelNumber)) {
                // Completed levels (gold)
                levelButton.getStyleClass().add("completed-level");
                levelButton.setOnAction(e -> openLevel(levelNumber));
            } else if (tracker.isLevelUnlocked(levelNumber)) {
                // Current unlocked level (green)
                levelButton.getStyleClass().add("unlocked-level");
                levelButton.setOnAction(e -> openLevel(levelNumber));
            } else {
                // Locked levels (red)
                levelButton.getStyleClass().add("locked-level");
                levelButton.setOnAction(e -> showLockedLevelMessage(levelNumber));
            }

            levelGrid.add(levelButton, (levelNumber - 1) % 5, (levelNumber - 1) / 5);
        }
    }

    private void updateProgressBar() {
        LevelProgressTracker tracker = LevelProgressTracker.getInstance();
        int completedLevels = tracker.getCompletedLevelsCount();
        double progress = (double) completedLevels / totalLevels;
        progressBar.setProgress(progress);
    }


    /**
     * Opens the specified level in the same window.
     *
     * @param levelNumber The number of the level to be opened.
     */
    private void openLevel(int levelNumber) {
        try {
            // Corrected path to find the level FXML files
            // Path should match your project's actual structure
            String levelPath = "/Level" + levelNumber + ".fxml";

            // Try the path without "Levels/" prefix first
            FXMLLoader loader = new FXMLLoader(getClass().getResource(levelPath));

            // If that fails, try with the "Levels/" prefix
            if (loader.getLocation() == null) {
                levelPath = "/Levels/Level" + levelNumber + ".fxml";
                loader = new FXMLLoader(getClass().getResource(levelPath));
            }

            if (loader.getLocation() == null) {
                throw new IOException("Cannot find level file at: " + levelPath);
            }

            // Load the level content
            Parent root = loader.load();

            // Get the current stage using any component in the scene
            Stage stage = (Stage) levelGrid.getScene().getWindow();

            // Create a new scene with the level content and set it to the existing stage
            Scene scene = new Scene(root);

            // Load the stylesheet if needed
            String cssPath = "/HomePageStyles.css";
            if (getClass().getResource(cssPath) != null) {
                scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            }

            // Set the new scene to the existing stage
            stage.setScene(scene);
            stage.setTitle("Level " + levelNumber);

            System.out.println("Successfully loaded Level " + levelNumber);
        } catch (IOException e) {
            System.err.println("Failed to load Level " + levelNumber + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Displays a styled popup message when a locked level is clicked.
     *
     * @param levelNumber The number of the locked level.
     */
    private void showLockedLevelMessage(int levelNumber) {
        // Create a custom dialog
        Stage dialogStage = new Stage();
        dialogStage.initOwner(primaryStage);
        dialogStage.setTitle("Level Locked");

        // Create the layout for the popup
        VBox popupContent = new VBox(15);
        popupContent.setAlignment(Pos.CENTER);
        popupContent.getStyleClass().add("game-popup"); // Apply CSS class for styling

        // Add a "locked" title
        Text lockedText = new Text("🔒 Level " + levelNumber + " is Locked!");
        lockedText.getStyleClass().add("game-popup-title"); // Apply CSS class for styling

        // Add a subtext with a suggestion
        Text suggestionText = new Text("Complete previous levels to unlock.");
        suggestionText.getStyleClass().add("game-popup-subtext"); // Apply CSS class for styling

        // Add a "close" button
        Button closeButton = new Button("OK");
        closeButton.getStyleClass().add("game-popup-button"); // Apply CSS class for styling
        closeButton.setOnAction(e -> dialogStage.close());

        popupContent.getChildren().addAll(lockedText, suggestionText, closeButton);

        // Center the popup in the window
        Scene dialogScene = new Scene(popupContent, 400, 200);

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


}