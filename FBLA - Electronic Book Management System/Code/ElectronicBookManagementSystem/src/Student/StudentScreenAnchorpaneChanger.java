package Student;

import LogIn.Session.UserSession;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class StudentScreenAnchorpaneChanger extends StudentScreenController {

    /***
     * Changes the current anchor pane to other fxml files.
     * @param fxmlFile
     * @param title
     */
    public void changeAnchorPane(String fxmlFile, String title){

        StudentScreenController studentScreenController = new StudentScreenController();

        // Get the manager screen anchor pane and change it to the fxmlFile.
        AnchorPane studentScreenAnchorPane = UserSession.getStudentScreenAnchorpane();
        Label studentScreenTitleLbl = (Label) studentScreenAnchorPane.getChildren().get(2);
        studentScreenTitleLbl.setStyle("-fx-font-family: 'Roboto Medium'");
        studentScreenTitleLbl.setText(title);
        BorderPane studentScreenBorderpane = (BorderPane) studentScreenAnchorPane.getChildren().get(1);
        Parent addStudentRoot = null;
        try {
            addStudentRoot = FXMLLoader.load(getClass().getResource(fxmlFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        studentScreenBorderpane.setCenter(addStudentRoot);


    }
}
