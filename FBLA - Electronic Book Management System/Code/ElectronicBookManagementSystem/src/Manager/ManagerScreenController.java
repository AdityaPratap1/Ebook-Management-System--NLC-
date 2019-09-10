package Manager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagerScreenController implements Initializable {


    @FXML
    private BorderPane managerScreenBorderPane;
    @FXML
    private Label managerScreenTitleLbl, myProfileTitleLabel;

    private double xposofmouse;
    private double yposofmouse;


    /**
     * Loads the studentList screen when the student list tab is clicked.
     * @param event
     */
    public void loadStudentListScreen(MouseEvent event) {
        loadManagerScreens("studentList.fxml", "List of Students");
    }

    /***
     * Loads the weeklReport screen when the weekly report tab is clicked.
     * @param event
     */
    public void loadWeeklyReportScreen(MouseEvent event) {
        loadManagerScreens("weeklyReport.fxml", "Weekly Report");
    }

    /***
     * Loads the Book Catalog screen screen when the book catalog list tab is clicked.
     * @param event
     */
    public void loadBookControlScreen(MouseEvent event) {
        loadManagerScreens("bookControl.fxml", "View Catalog");
    }

    /***
     * Loads the manager profile screen when the manager profile tab is clicked.

     * @param event
     */
    public void loadManagerProfileScreen(MouseEvent event) {

        loadManagerScreens("managerProfile.fxml", "My Profile");
        managerScreenTitleLbl.setText(null);
        myProfileTitleLabel.setText("My Profile");
        myProfileTitleLabel.setStyle("-fx-font-family: 'Roboto Medium'");
    }

    /***
     * Loads the Manager Help screen when the Help list tab is clicked.

     * @param event
     */
    public void loadManagerHelpScreen(MouseEvent event) {
        loadManagerScreens("HelpScreenFiles/studentListHelp.fxml", "How to Use");
    }

    /***
     * This method does the changing of the screens when different tabs are clicked
     * @param screen
     * @param screenTitle
     */
    private void loadManagerScreens(String screen, String screenTitle){

        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource(screen));
        } catch (IOException e) {
            e.printStackTrace();
        }
        myProfileTitleLabel.setText(null);

        managerScreenTitleLbl.setText(screenTitle);
        managerScreenTitleLbl.setStyle("-fx-font-family: 'Roboto Medium'");
           managerScreenBorderPane.setCenter(root);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadManagerScreens("studentList.fxml", "List of Students");
        managerScreenTitleLbl.setStyle("-fx-font-family: 'Roboto Medium'");
        xposofmouse = 0;
        yposofmouse = 0;
    }



    /**
     * This method enables the dragging of a window pane.
     * @param event
     */
    public void dragged(javafx.scene.input.MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage  = (Stage) node.getScene().getWindow();

        stage.setX(event.getScreenX() - xposofmouse);
        stage.setY(event.getScreenY() - yposofmouse);
    }


    /**
     * This method obtains the x and y coordinates of the mouse when pressed. These co-ordinates are then used in the dragged() method to configure the position of the window pane.
     * @param event
     */
    public void pressed(javafx.scene.input.MouseEvent event) {
        xposofmouse = event.getX();
        yposofmouse = event.getY();
    }


    /**
     * This method corresponds to the minimize button on the screen. The method minimizes the current screen because the default minimize button is disabled.
     * @param event
     */
    public void minimizeClick(javafx.scene.input.MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * This method corresponds to the close button on the screen. The method, when called, closes the current window.
     * @param event
     */
    public void closeClick(javafx.scene.input.MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

    }

    /***
     * Logs out current user and opens the log in screen
     * @param event
     */
    public void logOutManager(ActionEvent event) {


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("/LogIn/LogInScreen.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setTitle("Log In");
        assert root != null;
        stage.setScene(new Scene(root, 550, 400));
            stage.setResizable(false);
            stage.getScene().setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        }

}
