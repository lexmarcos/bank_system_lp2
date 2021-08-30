package bancoCentral.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    Stage window;

    @Override
    public void start(Stage primaryStage){
        window = primaryStage;
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent root = FXMLLoader.load(getClass().getResource("/bancoCentral/view/LoginView.fxml"));
            Scene scene1 = new Scene(root);
            window.setScene(scene1);
            window.show();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);

    }
}