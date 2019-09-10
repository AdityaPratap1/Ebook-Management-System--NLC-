package Manager;

import LogIn.Session.UserSession;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class ManagerScreenAnchorpaneChanger {
    /***
     * Takes a fxml file path and a title and changes the anchor pane to the given fxml file path and title to the given title.
     * @param fxmlFile
     * @param title
     */
    public void changeAnchorPane(String fxmlFile, String title){

        //Load another fxml file into the Manager Screen anchorpane.
        AnchorPane managerScreenAnchorPane = UserSession.getManagerScreenAnchorpane();
        Label managerScreenTitleLbl = (Label) managerScreenAnchorPane.getChildren().get(0);
        managerScreenTitleLbl.setText(title);
        BorderPane managerScreenBorderPane = (BorderPane) managerScreenAnchorPane.getChildren().get(4);
        Parent addStudentRoot = null;
        try {
            addStudentRoot = FXMLLoader.load(getClass().getResource(fxmlFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        managerScreenBorderPane.setCenter(addStudentRoot);
    }
}
