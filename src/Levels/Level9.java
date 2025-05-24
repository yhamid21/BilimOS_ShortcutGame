package Levels;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Level9 {

    @FXML private TextArea yourDocumentTextArea;
    @FXML private TextArea targetDocumentTextArea;
    @FXML private Label operationCounterLabel;
    @FXML private Label timerLabel;
    @FXML private Label shortcutUsedLabel;
    @FXML private Label statusLabel;
    @FXML private VBox congratsPopup;
    @FXML private Text congratsText;
    @FXML private ImageView backgroundImage;
    @FXML private Button homeButton;
    @FXML private Button nextLevelButton;
    @FXML private Button topHomeButton;
    @FXML private Button hintButton;
    @FXML private VBox hintPopup;
    @FXML private Text hintText;
    @FXML private ScrollPane hintImagesScroll;
    @FXML private VBox hintImagesContainer;
    @FXML private Button closeHintButton;

    private boolean levelCompleted = false;
    private Stage currentStage;
    private int operationCount = 0;
    private int seconds = 0;
    private Timeline timer;
    private boolean ctrlPressed = false;

    // Starting document with formatting issues, duplicated text, and text in wrong locations
    private final String startingDocument = 
        "INTRODUCTION TO KEYBOARD SHORTCUTS\n\n" +
        "INTRODUCTION TO KEYBOARD SHORTCUTS\n\n" + // Duplicated line
        "Keyboard shortcuts are essential for productivity. They allow users to perform actions quickly without using the mouse.\n\n" +
        "Some common shortcuts include:\n" +
        "- Copy: Ctrl+C\n" +
        "- Paste: Ctrl+V\n" +
        "- Cut: Ctrl+X\n\n" +
        "BENEFITS OF USING SHORTCUTS\n\n" +
        "Using keyboard shortcuts can significantly improve your workflow efficiency.\n" +
        "Using keyboard shortcuts can significantly improve your workflow efficiency.\n" + // Duplicated line
        "Some benefits include:\n" +
        "- Faster document navigation\n" +
        "- Reduced strain from mouse usage\n" +
        "- Increased productivity\n\n" +
        "ADVANCED SHORTCUTS\n\n" +
        "- Undo: Ctrl+Z\n" +
        "- Redo: Ctrl+Y\n" +
        "- Find: Ctrl+F\n" +
        "- Save: Ctrl+S\n\n" +
        "CONCLUSION\n\n" +
        "Learning keyboard shortcuts is an investment that pays off in time saved and increased efficiency.\n" +
        "BENEFITS OF USING SHORTCUTS\n\n" + // Section in wrong location
        "- Faster document navigation\n" + // Duplicated content
        "- Reduced strain from mouse usage\n" + // Duplicated content
        "- Increased productivity"; // Duplicated content

    // Target document with correct formatting and organization
    private final String targetDocument = 
        "INTRODUCTION TO KEYBOARD SHORTCUTS\n\n" +
        "Keyboard shortcuts are essential for productivity. They allow users to perform actions quickly without using the mouse.\n\n" +
        "Some common shortcuts include:\n" +
        "- Copy: Ctrl+C\n" +
        "- Paste: Ctrl+V\n" +
        "- Cut: Ctrl+X\n\n" +
        "BENEFITS OF USING SHORTCUTS\n\n" +
        "Using keyboard shortcuts can significantly improve your workflow efficiency.\n" +
        "Some benefits include:\n" +
        "- Faster document navigation\n" +
        "- Reduced strain from mouse usage\n" +
        "- Increased productivity\n\n" +
        "ADVANCED SHORTCUTS\n\n" +
        "- Undo: Ctrl+Z\n" +
        "- Redo: Ctrl+Y\n" +
        "- Find: Ctrl+F\n" +
        "- Save: Ctrl+S\n\n" +
        "CONCLUSION\n\n" +
        "Learning keyboard shortcuts is an investment that pays off in time saved and increased efficiency.";

    @FXML
    private void initialize() {
        // Set the initial text in the TextAreas
        yourDocumentTextArea.setText(startingDocument);
        targetDocumentTextArea.setText(targetDocument);
        
        // Make sure congratsPopup is not visible at start
        if (congratsPopup != null) {
            congratsPopup.setVisible(false);
        }

        // Make hint popup initially hidden
        if (hintPopup != null) {
            hintPopup.setVisible(false);
        }

        // Make shortcut used label initially hidden
        if (shortcutUsedLabel != null) {
            shortcutUsedLabel.setVisible(false);
        }

        // Set initial status label text
        if (statusLabel != null) {
            statusLabel.setText("Use keyboard shortcuts to transform the document to match the target.");
            statusLabel.setVisible(true);
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

        // Set up top navigation buttons
        if (topHomeButton != null) {
            topHomeButton.setOnAction(e -> goToHomePage());
            topHomeButton.getStyleClass().add("icon-button");
        }

        // Set up hint button and popup
        if (hintButton != null) {
            hintButton.setOnAction(e -> showHintPopup());
            hintButton.getStyleClass().add("icon-button");
        }

        // Style the hint images scroll pane
        if (hintImagesScroll != null) {
            hintImagesScroll.getStyleClass().add("hint-scroll-pane");
        }

        // Set up close hint button
        if (closeHintButton != null) {
            closeHintButton.setOnAction(e -> hintPopup.setVisible(false));
        }

        // Set up key event handlers for shortcut detection
        yourDocumentTextArea.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        yourDocumentTextArea.addEventFilter(KeyEvent.KEY_RELEASED, this::handleKeyReleased);

        // Configure home and next buttons if they exist
        if (homeButton != null) {
            homeButton.setOnAction(e -> goToHomePage());
        }

        if (nextLevelButton != null) {
            nextLevelButton.setOnAction(e -> goToNextLevel());
        }

        // Start the timer
        startTimer();
    }

    /**
     * Handles key press events to detect shortcuts
     */
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.CONTROL) {
            ctrlPressed = true;
            return;
        }

        if (ctrlPressed) {
            switch (event.getCode()) {
                case C:
                    incrementOperationCount("Copy (Ctrl+C)");
                    break;
                case V:
                    incrementOperationCount("Paste (Ctrl+V)");
                    break;
                case X:
                    incrementOperationCount("Cut (Ctrl+X)");
                    break;
                case Z:
                    incrementOperationCount("Undo (Ctrl+Z)");
                    break;
                case Y:
                    incrementOperationCount("Redo (Ctrl+Y)");
                    break;
                case F:
                    incrementOperationCount("Find (Ctrl+F)");
                    break;
                case S:
                    incrementOperationCount("Save (Ctrl+S)");
                    event.consume(); // Prevent default save dialog
                    checkDocumentMatch();
                    break;
            }
        }
    }

    /**
     * Handles key release events
     */
    private void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.CONTROL) {
            ctrlPressed = false;
        }
    }

    /**
     * Increments the operation counter and shows the shortcut used
     */
    private void incrementOperationCount(String shortcutName) {
        operationCount++;
        operationCounterLabel.setText("Operations: " + operationCount);
        
        // Show the shortcut used briefly
        shortcutUsedLabel.setText(shortcutName + " used!");
        shortcutUsedLabel.setVisible(true);
        
        // Hide the shortcut label after a delay
        Timeline timeline = new Timeline(new KeyFrame(
            Duration.seconds(1.5),
            ae -> shortcutUsedLabel.setVisible(false)));
        timeline.play();
    }

    /**
     * Starts the timer for tracking time spent on the level
     */
    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            seconds++;
            updateTimerLabel();
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    /**
     * Updates the timer label with the current time
     */
    private void updateTimerLabel() {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        timerLabel.setText(String.format("Time: %02d:%02d", minutes, remainingSeconds));
    }

    /**
     * Checks if the edited document matches the target document
     */
    private void checkDocumentMatch() {
        String currentText = yourDocumentTextArea.getText();
        
        if (currentText.equals(targetDocument)) {
            // Documents match - level completed!
            levelCompleted = true;
            
            // Stop the timer
            if (timer != null) {
                timer.stop();
            }
            
            // Update status
            statusLabel.setText("Document matches the target! Level completed!");
            
            // Complete level and show completion popup
            unlockNextLevel();
            showCompletionPopup();
        } else {
            // Documents don't match yet
            statusLabel.setText("Documents don't match yet. Keep editing!");
        }
    }

    /**
     * Shows the hint popup with level-specific information and images
     */
    private void showHintPopup() {
        // Clear any previous hint images
        if (hintImagesContainer != null) {
            hintImagesContainer.getChildren().clear();
        }

        // Load hint images from the resources directory
        List<Image> hintImages = loadHintImages("Level9");

        // If we have images, add them to the container
        if (!hintImages.isEmpty()) {
            for (Image img : hintImages) {
                ImageView imgView = new ImageView(img);
                imgView.setFitWidth(400);  // Set appropriate width
                imgView.setPreserveRatio(true);

                // Add image to the container
                if (hintImagesContainer != null) {
                    hintImagesContainer.getChildren().add(imgView);
                }
            }

            // Show the scroll pane if we have images
            if (hintImagesScroll != null) {
                hintImagesScroll.setVisible(true);
                hintImagesScroll.setManaged(true);
            }
        } else {
            // Hide the scroll pane if we don't have images
            if (hintImagesScroll != null) {
                hintImagesScroll.setVisible(false);
                hintImagesScroll.setManaged(false);
            }
        }

        // Show the popup
        if (hintPopup != null) {
            hintPopup.setVisible(true);
        }
    }

    /**
     * Loads hint images for the specified level
     * @param levelPrefix The level prefix (e.g., "Level1", "Level2")
     * @return A list of loaded images
     */
    private List<Image> loadHintImages(String levelPrefix) {
        List<Image> images = new ArrayList<>();

        // Try to load images with sequential numbers
        int imageIndex = 1;
        boolean foundImage = true;

        while (foundImage) {
            String imagePath = "/Resources/Hints/" + levelPrefix + "Hint" + imageIndex + ".png";
            URL resourceUrl = getClass().getResource(imagePath);

            if (resourceUrl != null) {
                try {
                    Image img = new Image(resourceUrl.toExternalForm());
                    if (!img.isError()) {
                        images.add(img);
                        System.out.println("Loaded hint image: " + imagePath);
                    } else {
                        foundImage = false;
                    }
                } catch (Exception e) {
                    foundImage = false;
                }
            } else {
                foundImage = false;
            }

            imageIndex++;
        }

        // If no numbered images found, try the base image
        if (images.isEmpty()) {
            String basePath = "/Resources/Hints/" + levelPrefix + "Hint.png";
            URL resourceUrl = getClass().getResource(basePath);

            if (resourceUrl != null) {
                try {
                    Image img = new Image(resourceUrl.toExternalForm());
                    if (!img.isError()) {
                        images.add(img);
                        System.out.println("Loaded hint image: " + basePath);
                    }
                } catch (Exception e) {
                    System.out.println("No hint images found for " + levelPrefix);
                }
            }
        }

        return images;
    }

    /**
     * Shows a styled completion popup with performance metrics
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
        Text completedText = new Text("🏆 Level 9 Complete!");
        completedText.getStyleClass().add("game-popup-title");

        // Calculate stars based on performance
        int stars = calculateStars();
        String starsText = "⭐".repeat(stars);
        
        // Add a congratulatory message with performance metrics
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        Text congratsText = new Text(
            "Great job mastering document editing shortcuts!\n\n" +
            starsText + "\n\n" +
            "Operations used: " + operationCount + "\n" +
            "Time: " + String.format("%02d:%02d", minutes, remainingSeconds)
        );
        congratsText.getStyleClass().add("game-popup-subtext");

        // Add navigation buttons in a horizontal layout
        HBox buttonBox = new HBox(10);
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
        Scene dialogScene = new Scene(popupContent, 400, 300);

        // Load the stylesheet
        String cssPath = "/HomePageStyles.css";
        if (getClass().getResource(cssPath) != null) {
            dialogScene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        } else {
            System.err.println("Could not find CSS file: " + cssPath);
        }

        dialogStage.setScene(dialogScene);
        dialogStage.show(); // Use show() instead of showAndWait()
    }

    /**
     * Calculates the number of stars earned based on performance
     * @return Number of stars (1-3)
     */
    private int calculateStars() {
        // Basic completion: 1 star
        int stars = 1;
        
        // Efficient completion (fewer operations): 2 stars
        if (operationCount <= 20) {
            stars = 2;
            
            // Expert completion (fast time + efficient operations): 3 stars
            if (seconds <= 120) { // 2 minutes or less
                stars = 3;
            }
        }
        
        return stars;
    }

    private void unlockNextLevel() {
        // Mark level 9 as completed and unlock level 10
        LevelProgressTracker.getInstance().completeLevel(9);
        System.out.println("Level 9 completed and Level 10 unlocked!");
    }

    private void goToHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) yourDocumentTextArea.getScene().getWindow();

            // Create a new scene with the loaded content
            Scene scene = new Scene(root);

            // Apply stylesheet if needed
            String cssPath = "/HomePageStyles.css";
            if (getClass().getResource(cssPath) != null) {
                scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            }

            // Set the scene to the existing stage
            stage.setScene(scene);
            stage.setTitle("Keyboard Shortcut Adventure");
        } catch (IOException e) {
            System.err.println("Failed to load HomePage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void goToNextLevel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Levels/Level10.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) yourDocumentTextArea.getScene().getWindow();

            // Create a new scene with the loaded content
            Scene scene = new Scene(root);

            // Apply stylesheet if needed
            String cssPath = "/HomePageStyles.css";
            if (getClass().getResource(cssPath) != null) {
                scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            }

            // Set the scene to the existing stage
            stage.setScene(scene);
            stage.setTitle("Keyboard Shortcut Adventure - Level 10");
        } catch (IOException e) {
            System.err.println("Failed to load Level10: " + e.getMessage());
            e.printStackTrace();
        }
    }
}