package Levels;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

public class Level5 {

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
    private Stage searchDialog = null;
    private boolean searchDialogShown = false;
    private boolean treasureFound = false;

    @FXML
    private void initialize() {
        // Set the initial text in the TextArea with a story containing the word "treasure"
        String storyText = "The Adventure of the Hidden Word\n\n" +
                "Once upon a time, in a digital kingdom far away, there lived a brave explorer named Alex. " +
                "Alex was known throughout the land for solving puzzles and finding hidden secrets.\n\n" +
                "One day, a mysterious message appeared on Alex's computer screen. It read: " +
                "'To find the greatest treasure in all the kingdom, you must learn to search efficiently.'\n\n" +
                "Alex knew that somewhere in this story, the word 'treasure' was hidden, but reading through " +
                "everything line by line would take too long. There had to be a better way to search!\n\n" +
                "The wise old wizard of the kingdom had once taught Alex about a magical shortcut - " +
                "pressing Ctrl+F would reveal a search box that could find any word instantly.\n\n" +
                "Alex remembered the wizard's advice and decided to try it. 'If I can find the hidden treasure " +
                "using Ctrl+F, I'll prove that I've mastered the art of efficient searching,' thought Alex.\n\n" +
                "The kingdom waited anxiously. Would Alex find the treasure and save the day? Only by using " +
                "the power of Ctrl+F would the hidden treasure be revealed and the level completed.";
        
        textArea.setText(storyText);
        
        // Make sure congratsPopup is not visible at start
        if (congratsPopup != null) {
            congratsPopup.setVisible(false);
        }

        // Make hint popup initially hidden
        if (hintPopup != null) {
            hintPopup.setVisible(false);
        }

        // Make status label initially hidden
        if (statusLabel != null) {
            statusLabel.setVisible(false);
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

        // Monitor for Ctrl+F keypresses
        textArea.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.F) {
                // User pressed Ctrl+F
                System.out.println("Ctrl+F detected!");
                event.consume(); // Prevent default browser behavior
                
                // Show search dialog if not already shown
                if (!searchDialogShown) {
                    showSearchDialog();
                }
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
     * Shows a custom search dialog when Ctrl+F is pressed
     */
    private void showSearchDialog() {
        searchDialogShown = true;
        
        // Create a custom dialog
        searchDialog = new Stage();
        if (currentStage != null) {
            searchDialog.initOwner(currentStage);
        }
        searchDialog.initModality(Modality.NONE); // Allow interaction with main window
        searchDialog.setTitle("Find");
        
        // Create the layout for the search dialog
        VBox dialogContent = new VBox(10);
        dialogContent.setAlignment(Pos.CENTER);
        dialogContent.setPadding(new javafx.geometry.Insets(15));
        dialogContent.setStyle("-fx-background-color: white; -fx-border-color: #3498db; -fx-border-width: 2px;");
        
        // Add search field and buttons
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER);
        
        Label findLabel = new Label("Find:");
        TextField searchField = new TextField();
        searchField.setPrefWidth(200);
        
        Button findButton = new Button("Find Next");
        findButton.setDefaultButton(true);
        
        searchBox.getChildren().addAll(findLabel, searchField, findButton);
        
        // Add close button
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> searchDialog.close());
        
        dialogContent.getChildren().addAll(searchBox, closeButton);
        
        // Set up search functionality
        findButton.setOnAction(e -> {
            String searchTerm = searchField.getText().toLowerCase();
            String content = textArea.getText().toLowerCase();
            
            if (searchTerm.isEmpty()) {
                return;
            }
            
            // Check if the search term is "treasure"
            if (searchTerm.equals("treasure")) {
                // Highlight all occurrences of "treasure" in the text
                int index = content.indexOf(searchTerm);
                if (index >= 0) {
                    textArea.selectRange(index, index + searchTerm.length());
                    treasureFound = true;
                    
                    // Update status label
                    if (statusLabel != null) {
                        statusLabel.setText("Great job! You found the treasure using Ctrl+F!");
                        statusLabel.setVisible(true);
                    }
                    
                    // Complete the level
                    if (!levelCompleted) {
                        levelCompleted = true;
                        System.out.println("Level completed!");
                        unlockNextLevel();
                        showCompletionPopup();
                    }
                }
            } else {
                // For other search terms, just highlight them if found
                int index = content.indexOf(searchTerm);
                if (index >= 0) {
                    textArea.selectRange(index, index + searchTerm.length());
                    
                    // Update status label with a hint
                    if (statusLabel != null) {
                        statusLabel.setText("You're searching, but not for the right word. Try searching for 'treasure'!");
                        statusLabel.setVisible(true);
                    }
                } else {
                    // Not found
                    if (statusLabel != null) {
                        statusLabel.setText("No matches found. Try searching for 'treasure'!");
                        statusLabel.setVisible(true);
                    }
                }
            }
        });
        
        // Create and style the scene
        Scene dialogScene = new Scene(dialogContent, 400, 120);
        
        // Load the stylesheet
        String cssPath = "/HomePageStyles.css";
        if (getClass().getResource(cssPath) != null) {
            dialogScene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
        }
        
        searchDialog.setScene(dialogScene);
        searchDialog.setResizable(false);
        searchDialog.show();
        
        // Focus on the search field
        searchField.requestFocus();
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
        List<Image> hintImages = loadHintImages("Level5");

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
        // Close search dialog if open
        if (searchDialog != null && searchDialog.isShowing()) {
            searchDialog.close();
        }
        
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
        Text completedText = new Text("🏆 Level 5 Complete!");
        completedText.getStyleClass().add("game-popup-title");

        // Add a congratulatory message
        Text congratsText = new Text("Great job mastering Ctrl+F (Find)!");
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
        // Mark level 5 as completed and unlock level 6
        LevelProgressTracker.getInstance().completeLevel(5);
        System.out.println("Level 5 completed and Level 6 unlocked!");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Levels/Level6.fxml"));
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
            stage.setTitle("Level 6");
        } catch (IOException e) {
            System.err.println("Failed to load Level 6: " + e.getMessage());
            e.printStackTrace();

            // If Level 6 doesn't exist yet, go back to home
            try {
                goToHomePage();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}