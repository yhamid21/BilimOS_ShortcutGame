import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class IntroPageController {

    @FXML
    private ImageView introImage;

    @FXML
    private void initialize() {
        introImage.setImage(new Image(getClass().getResourceAsStream("Resources/Backgrounds/introBG.png")));
        introImage.setPreserveRatio(true);

        introImage.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                introImage.fitWidthProperty().bind(newScene.widthProperty());
                introImage.fitHeightProperty().bind(newScene.heightProperty());

                newScene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
                introImage.setPreserveRatio(false);

            }
        });
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.isControlDown() && event.getCode() == KeyCode.C) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
                Scene homeScene = new Scene(loader.load());
                Stage stage = (Stage) introImage.getScene().getWindow();
                stage.setScene(homeScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
