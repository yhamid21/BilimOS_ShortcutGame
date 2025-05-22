
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

    private final int totalLevels = 20; // Total number of levels
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
        for (int i = 1; i <= totalLevels; i++) {
            final int levelNumber = i; // Create a final copy of i for use in lambdas

            Button levelButton = new Button("Level " + levelNumber);
            levelButton.setPrefSize(160, 80);

            if (levelNumber <= completedLevels) {
                // Completed levels
                levelButton.getStyleClass().add("completed-level");
                levelButton.setOnAction(e -> openLevel(levelNumber));
            } else if (levelNumber == completedLevels + 1) {
                // Current unlocked level
                levelButton.getStyleClass().add("unlocked-level");
                levelButton.setOnAction(e -> openLevel(levelNumber));
            } else {
                // Locked levels
                levelButton.getStyleClass().add("locked-level");
                levelButton.setOnAction(e -> showLockedLevelMessage(levelNumber));
            }

            levelGrid.add(levelButton, (levelNumber - 1) % 5, (levelNumber - 1) / 5); // Arrange buttons in a grid
        }
    }

    /**
     * Opens the specified level in a new maximized stage.
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

            Parent root = loader.load();

            Stage levelStage = new Stage();
            levelStage.setTitle("Level " + levelNumber);
            levelStage.setScene(new Scene(root));
            levelStage.setMaximized(true); // Open in maximized state
            levelStage.show();

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

    /**
     * Updates the progress bar based on the number of completed levels.
     */
    private void updateProgressBar() {
        double progress = (double) completedLevels / totalLevels;
        progressBar.setProgress(progress);
    }
}