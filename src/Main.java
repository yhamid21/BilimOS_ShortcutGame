//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//
//public class Main extends Application {
//
//    @Override
//    public void start(Stage primaryStage) {
//        try {
//            // Load the FXML file for HomePage
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
//            Parent root = loader.load();
//
//            // Set the title and scene for the stage
//            primaryStage.setTitle("Keyboard Shortcut Adventure");
//            primaryStage.setScene(new Scene(root, 800, 600));
//
//            // Show the stage (main window)
//            primaryStage.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);  // Launch the JavaFX application
//    }
//}

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("IntroPage.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Keyboard Shortcut Adventure");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
