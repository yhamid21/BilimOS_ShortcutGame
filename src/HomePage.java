import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomePage {

    @FXML
    private Text titleText;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private GridPane levelGrid;

    @FXML
    private ScrollPane levelScrollPane;

    private final int totalLevels = 20;
    private int completedLevels = 0;
    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initialize() {
        createLevelButtons();
        updateProgressBar();
    }

    private void createLevelButtons() {
        for (int i = 1; i <= totalLevels; i++) {
            Button levelButton = new Button("Level " + i);
            int levelNumber = i;
            levelButton.setOnAction(e -> handleLevelButtonClick(levelNumber));
            levelButton.getStyleClass().add("level-button");
            levelButton.setPrefSize(100, 40);
            levelGrid.add(levelButton, (i - 1) % 5, (i - 1) / 5);
        }
    }

    private void handleLevelButtonClick(int levelNumber) {
        try {
            switch (levelNumber) {
                case 1 -> new Levels.Level1().start(primaryStage);
                case 2 -> System.out.println("Level 2 coming soon!");
                default -> System.out.println("Level " + levelNumber + " is not implemented yet.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProgressBar() {
        double progress = (double) completedLevels / totalLevels;
        progressBar.setProgress(progress);
    }
}
