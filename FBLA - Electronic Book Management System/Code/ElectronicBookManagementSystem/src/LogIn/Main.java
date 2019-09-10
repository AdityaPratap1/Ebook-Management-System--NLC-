package LogIn;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {

    /**
     *
     * This method is called when the application is launched. This method opens the Login Screen.
     * @param primaryStage
     * @throws Exception
     *
     *
     */
    @Override



    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("LogInScreen.fxml"));
        primaryStage.setTitle("Log In");
        primaryStage.setScene(new Scene(root, 550, 400));
        primaryStage.setResizable(false);
        primaryStage.getScene().setFill(Color.TRANSPARENT);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
