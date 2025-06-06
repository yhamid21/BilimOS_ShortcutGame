package Levels;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Level7 {

    @FXML private TextArea textArea;
    @FXML private Label statusLabel;
    @FXML private VBox congratsPopup;
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
    private String originalText = "Level 7: Mastering Redo! Make a change to this text, then press Ctrl+Z to undo it, and finally press Ctrl+Y to redo your change.";
    private String modifiedText = null;
    private boolean textModified = false;
    private boolean undoDetected = false;
    private boolean redoDetected = false;

    @FXML
    private void initialize() {
        // Set the initial text in the TextArea
        textArea.setText(originalText);
        
        // Make sure congratsPopup is not visible at start
        if (congratsPopup != null) {
            congratsPopup.setVisible(false);
        }

        // Make hint popup initially hidden
        if (hintPopup != null) {
            hintPopup.setVisible(false);
        }

        // Set initial status label text
        if (statusLabel != null) {
            statusLabel.setText("Step 1: Make a change to the text.");
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

        // Monitor for text changes to detect modifications
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(originalText) && !textModified) {
                textModified = true;
                modifiedText = newValue;
                statusLabel.setText("Step 2: Now press Ctrl+Z to undo your change.");
                statusLabel.setVisible(true);
                System.out.println("Text modified: " + newValue);
            }
        });

        // Monitor for Ctrl+Z and Ctrl+Y keypresses
        textArea.setOnKeyPressed(event -> {
            // Check for Ctrl+Z (Undo)
            if (event.isControlDown() && event.getCode() == KeyCode.Z) {
                // User pressed Ctrl+Z
                System.out.println("Ctrl+Z detected!");
                
                // The undo will happen automatically, we just need to check the result
                textArea.setOnKeyReleased(releaseEvent -> {
                    if (releaseEvent.getCode() == KeyCode.Z) {
                        checkUndoAndUpdateStatus();
                    }
                });
            }
            
            // Check for Ctrl+Y (Redo)
            else if (event.isControlDown() && event.getCode() == KeyCode.Y) {
                // User pressed Ctrl+Y
                System.out.println("Ctrl+Y detected!");
                
                // The redo will happen automatically, we just need to check the result
                textArea.setOnKeyReleased(releaseEvent -> {
                    if (releaseEvent.getCode() == KeyCode.Y) {
                        checkRedoAndComplete();
                    }
                });
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

    /**
     * Checks if undo was successful and updates the status
     */
    private void checkUndoAndUpdateStatus() {
        String currentText = textArea.getText();
        System.out.println("After Ctrl+Z, text is: " + currentText);

        // Check if the text has been restored to original after being modified
        if (currentText.equals(originalText) && textModified) {
            undoDetected = true;
            statusLabel.setText("Step 3: Great! Now press Ctrl+Y to redo your change.");
            statusLabel.setVisible(true);
        }
    }

    /**
     * Checks if redo was successful and completes the level if all steps are done
     */
    private void checkRedoAndComplete() {
        String currentText = textArea.getText();
        System.out.println("After Ctrl+Y, text is: " + currentText);

        // Check if the text has been restored to the modified version after being undone
        if (modifiedText != null && currentText.equals(modifiedText) && undoDetected) {
            redoDetected = true;
            statusLabel.setText("Great job! You've successfully used Ctrl+Z to undo and Ctrl+Y to redo!");
            statusLabel.setVisible(true);
            
            // Complete the level if all steps are done
            if (textModified && undoDetected && redoDetected && !levelCompleted) {
                levelCompleted = true;
                System.out.println("Level completed!");
                unlockNextLevel();
                showCompletionPopup();
            }
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
        List<Image> hintImages = loadHintImages("Level7");

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
        Text completedText = new Text("🏆 Level 7 Complete!");
        completedText.getStyleClass().add("game-popup-title");

        // Add a congratulatory message
        Text congratsText = new Text("Great job mastering Ctrl+Y (Redo)!");
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
        // Mark level 7 as completed and unlock level 8
        LevelProgressTracker.getInstance().completeLevel(7);
        System.out.println("Level 7 completed and Level 8 unlocked!");
    }

    private void goToHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomePage.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) textArea.getScene().getWindow();

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Levels/Level8.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) textArea.getScene().getWindow();

            // Create a new scene with the loaded content
            Scene scene = new Scene(root);

            // Apply stylesheet if needed
            String cssPath = "/HomePageStyles.css";
            if (getClass().getResource(cssPath) != null) {
                scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            }

            // Set the scene to the existing stage
            stage.setScene(scene);
            stage.setTitle("Level 8");
        } catch (IOException e) {
            System.err.println("Failed to load Level 8: " + e.getMessage());
            e.printStackTrace();

            // If Level 8 doesn't exist yet, go back to home
            try {
                goToHomePage();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}