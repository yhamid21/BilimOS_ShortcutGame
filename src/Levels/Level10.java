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
import javafx.scene.control.CheckBox;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Level10 {

    @FXML private TextArea codeEditorTextArea;
    @FXML private VBox taskListContainer;
    @FXML private CheckBox task1;
    @FXML private CheckBox task2;
    @FXML private CheckBox task3;
    @FXML private CheckBox task4;
    @FXML private CheckBox task5;
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

    // Starting code with issues to fix
    private final String startingCode = 
        "public class OrderProcessor {\n" +
        "\n" +
        "    // Main method to run the application\n" +
        "    public static void main(String[] args) {\n" +
        "        // Display welcome message\n" +
        "        displayWelcome();\n" +
        "        \n" +
        "        // Process an order\n" +
        "        double orderTotal = processOrder(3, 15.99);\n" +
        "        \n" +
        "        // Display the total\n" +
        "        System.out.println(\"Order total: $\" + orderTotal);\n" +
        "        \n" +
        "        System.out.println(\"Debug: Processing complete\"); // Debug statement\n" +
        "        System.out.println(\"Debug: Exiting application\"); // Debug statement\n" +
        "    }\n" +
        "    \n" +
        "    // Method to display a welcome message\n" +
        "public static void displayWelcome() {\n" +
        "    System.out.println(\"Welcome to the Order Processing System\");\n" +
        "        System.out.println(\"Version 1.0\");\n" +
        "    System.out.println(\"--------------------------------\");\n" +
        "}\n" +
        "    \n" +
        "    // Method to process an order\n" +
        "    public static double processOrder(int quantity, double price) {\n" +
        "        System.out.println(\"Processing order...\");\n" +
        "        System.out.println(\"Quantity: \" + quantity);\n" +
        "        System.out.println(\"Price per item: $\" + price);\n" +
        "        \n" +
        "        // Calculate subtotal\n" +
        "        double subtotal = quantity * price;\n" +
        "        System.out.println(\"Subtotal: $\" + subtotal);\n" +
        "        \n" +
        "        // Calculate tax (8%)\n" +
        "        double tax = subtotal * 0.08;\n" +
        "        System.out.println(\"Tax (8%): $\" + tax);\n" +
        "        \n" +
        "        // Calculate total\n" +
        "        double total = subtotal + tax;\n" +
        "        \n" +
        "        // Duplicate code block - should be removed\n" +
        "        System.out.println(\"Processing order...\");\n" +
        "        System.out.println(\"Quantity: \" + quantity);\n" +
        "        System.out.println(\"Price per item: $\" + price);\n" +
        "        \n" +
        "        return total;\n" +
        "    }\n" +
        "    \n" +
        "    // Method to calculate the total with tax\n" +
        "    public static double calculateTotal(double subtotal) {\n" +
        "        // Calculate tax (8%)\n" +
        "        double tax = subtotal * 0.08;\n" +
        "        \n" +
        "        // Return the total\n" +
        "        return subtotal + tax;\n" +
        "    }\n" +
        "}";

    // Expected code patterns after refactoring
    private final String expectedIndentedDisplayWelcome = 
        "    public static void displayWelcome() {\n" +
        "        System.out.println(\"Welcome to the Order Processing System\");\n" +
        "        System.out.println(\"Version 1.0\");\n" +
        "        System.out.println(\"--------------------------------\");\n" +
        "    }";
    
    private final String expectedNoDuplicateCode =
        "    public static double processOrder(int quantity, double price) {";
    
    private final String expectedCommentedDebug =
        "        // System.out.println(\"Debug: Processing complete\"); // Debug statement\n" +
        "        // System.out.println(\"Debug: Exiting application\"); // Debug statement";

    @FXML
    private void initialize() {
        // Set the initial code in the TextArea
        codeEditorTextArea.setText(startingCode);
        
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
            statusLabel.setText("Use keyboard shortcuts to refactor the code and complete all tasks.");
            statusLabel.setVisible(true);
        }

        // Make all task checkboxes disabled (they will be auto-checked)
        if (task1 != null) task1.setDisable(true);
        if (task2 != null) task2.setDisable(true);
        if (task3 != null) task3.setDisable(true);
        if (task4 != null) task4.setDisable(true);
        if (task5 != null) task5.setDisable(true);

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
        codeEditorTextArea.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        codeEditorTextArea.addEventFilter(KeyEvent.KEY_RELEASED, this::handleKeyReleased);
        
        // Add text change listener to check for task completion
        codeEditorTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            checkTaskCompletion(newValue);
        });

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
                case D:
                    incrementOperationCount("Duplicate Line (Ctrl+D)");
                    break;
                case HOME:
                    incrementOperationCount("Jump to Start (Ctrl+Home)");
                    break;
                case END:
                    incrementOperationCount("Jump to End (Ctrl+End)");
                    break;
                case SLASH:
                    incrementOperationCount("Comment/Uncomment (Ctrl+/)");
                    break;
                case S:
                    incrementOperationCount("Save (Ctrl+S)");
                    event.consume(); // Prevent default save dialog
                    checkAllTasksCompleted();
                    break;
            }
        } else if (event.getCode() == KeyCode.TAB) {
            incrementOperationCount("Indent (Tab)");
        } else if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
            incrementOperationCount("Unindent (Shift+Tab)");
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
     * Checks if all tasks have been completed
     */
    private void checkAllTasksCompleted() {
        if (task1.isSelected() && task2.isSelected() && task3.isSelected() && 
            task4.isSelected() && task5.isSelected()) {
            // All tasks completed - level completed!
            levelCompleted = true;
            
            // Stop the timer
            if (timer != null) {
                timer.stop();
            }
            
            // Update status
            statusLabel.setText("All refactoring tasks completed! Level completed!");
            
            // Complete level and show completion popup
            unlockNextLevel();
            showCompletionPopup();
        } else {
            // Not all tasks completed yet
            statusLabel.setText("Not all tasks are completed yet. Keep refactoring!");
            
            // Show which tasks are still pending
            StringBuilder pendingTasks = new StringBuilder("Pending tasks: ");
            if (!task1.isSelected()) pendingTasks.append("Task 1, ");
            if (!task2.isSelected()) pendingTasks.append("Task 2, ");
            if (!task3.isSelected()) pendingTasks.append("Task 3, ");
            if (!task4.isSelected()) pendingTasks.append("Task 4, ");
            if (!task5.isSelected()) pendingTasks.append("Task 5, ");
            
            // Remove the trailing comma and space
            String pendingTasksStr = pendingTasks.toString();
            if (pendingTasksStr.endsWith(", ")) {
                pendingTasksStr = pendingTasksStr.substring(0, pendingTasksStr.length() - 2);
            }
            
            System.out.println(pendingTasksStr);
        }
    }

    /**
     * Checks for task completion based on the current code
     */
    private void checkTaskCompletion(String currentCode) {
        // Task 1: Move the calculateTotal method above the main method
        if (currentCode.indexOf("public static double calculateTotal") < 
            currentCode.indexOf("public static void main")) {
            task1.setSelected(true);
        } else {
            task1.setSelected(false);
        }
        
        // Task 2: Fix indentation in the displayWelcome method
        if (currentCode.contains(expectedIndentedDisplayWelcome)) {
            task2.setSelected(true);
        } else {
            task2.setSelected(false);
        }
        
        // Task 3: Remove the duplicate code in the processOrder method
        if (currentCode.contains(expectedNoDuplicateCode) && 
            !currentCode.contains("// Duplicate code block - should be removed")) {
            task3.setSelected(true);
        } else {
            task3.setSelected(false);
        }
        
        // Task 4: Comment out the debug print statements
        if (currentCode.contains(expectedCommentedDebug) || 
            (currentCode.contains("// System.out.println(\"Debug: Processing complete\")") && 
             currentCode.contains("// System.out.println(\"Debug: Exiting application\")"))) {
            task4.setSelected(true);
        } else {
            task4.setSelected(false);
        }
        
        // Task 5: Fix the indentation of the entire class
        // This is a more complex check - we'll look for consistent indentation patterns
        boolean hasConsistentIndentation = checkConsistentIndentation(currentCode);
        task5.setSelected(hasConsistentIndentation);
        
        // Update the status label with progress
        int completedTasks = 0;
        if (task1.isSelected()) completedTasks++;
        if (task2.isSelected()) completedTasks++;
        if (task3.isSelected()) completedTasks++;
        if (task4.isSelected()) completedTasks++;
        if (task5.isSelected()) completedTasks++;
        
        statusLabel.setText("Completed " + completedTasks + " out of 5 tasks. Press Ctrl+S when finished.");
    }

    /**
     * Checks if the code has consistent indentation
     */
    private boolean checkConsistentIndentation(String code) {
        // Split the code into lines
        String[] lines = code.split("\n");
        
        // Check for consistent indentation patterns
        boolean inMethod = false;
        int methodIndentLevel = -1;
        int blockIndentLevel = -1;
        
        for (String line : lines) {
            // Skip empty lines
            if (line.trim().isEmpty()) continue;
            
            // Count leading spaces
            int indentLevel = countLeadingSpaces(line) / 4; // Assuming 4 spaces per indent level
            
            // Check for method declarations
            if (line.trim().startsWith("public static") && line.contains("(") && !line.trim().endsWith(";")) {
                inMethod = true;
                methodIndentLevel = indentLevel;
                continue;
            }
            
            // Check for opening braces
            if (line.trim().equals("{")) {
                blockIndentLevel = indentLevel;
                continue;
            }
            
            // Check for closing braces
            if (line.trim().equals("}")) {
                if (inMethod && indentLevel != methodIndentLevel) {
                    return false; // Closing brace doesn't match method indent
                }
                inMethod = false;
                continue;
            }
            
            // Check for consistent indentation within methods
            if (inMethod && !line.trim().startsWith("//") && indentLevel <= methodIndentLevel) {
                return false; // Method body should be indented more than method declaration
            }
        }
        
        return true;
    }

    /**
     * Counts the number of leading spaces in a string
     */
    private int countLeadingSpaces(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ' ') {
                count++;
            } else {
                break;
            }
        }
        return count;
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
        List<Image> hintImages = loadHintImages("Level10");

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
        Text completedText = new Text("🏆 Level 10 Complete!");
        completedText.getStyleClass().add("game-popup-title");

        // Calculate stars based on performance
        int stars = calculateStars();
        String starsText = "⭐".repeat(stars);
        
        // Add a congratulatory message with performance metrics
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        Text congratsText = new Text(
            "Great job mastering code refactoring shortcuts!\n\n" +
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
        if (operationCount <= 25) {
            stars = 2;
            
            // Expert completion (fast time + efficient operations): 3 stars
            if (seconds <= 180) { // 3 minutes or less
                stars = 3;
            }
        }
        
        return stars;
    }

    private void unlockNextLevel() {
        // Mark level 10 as completed and unlock level 11
        LevelProgressTracker.getInstance().completeLevel(10);
        System.out.println("Level 10 completed and Level 11 unlocked!");
    }

    private void goToHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) codeEditorTextArea.getScene().getWindow();

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Levels/Level11.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) codeEditorTextArea.getScene().getWindow();

            // Create a new scene with the loaded content
            Scene scene = new Scene(root);

            // Apply stylesheet if needed
            String cssPath = "/HomePageStyles.css";
            if (getClass().getResource(cssPath) != null) {
                scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            }

            // Set the scene to the existing stage
            stage.setScene(scene);
            stage.setTitle("Keyboard Shortcut Adventure - Level 11");
        } catch (IOException e) {
            System.err.println("Failed to load Level11: " + e.getMessage());
            e.printStackTrace();
            
            // If Level11 doesn't exist, go to home page instead
            goToHomePage();
        }
    }
}