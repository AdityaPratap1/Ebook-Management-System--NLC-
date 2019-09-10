package Student;

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

class StudentScreenController implements Initializable {

    @FXML
    private BorderPane studentScreenBorderPane;
    @FXML
    private Label studentScreenTitleLbl;
    @FXML
    private  Label recomBooksTitle;
    private double xposofmouse, yposofmouse;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadStudentScreens("studentDashboard.fxml", "Dashboard");
        xposofmouse = 0;
        yposofmouse = 0;
    }

    public void loadBookListScreen(MouseEvent event) {
       loadStudentScreens("bookList.fxml", "Catalog");
}

    public void loadRecomBookScreen(MouseEvent event) {
        loadStudentScreens("recommendedBooks.fxml", "Recommended Books");
        studentScreenTitleLbl.setText(null);
        recomBooksTitle.setStyle("-fx-font-family: 'Roboto Medium'");
        recomBooksTitle.setText("Recommended Books");

    }

    public void loadProfileScreen(MouseEvent event) {
        loadStudentScreens("studentProfile.fxml", "My Profile");
    }

    public void loadHelpScreen(MouseEvent event) {
       loadStudentScreens("HelpScreenFiles/dashBoardHelpScreen.fxml", "How to Use");
    }

    public void loadReturnBookListScreen(MouseEvent event) {

       loadStudentScreens("issuedBooksList.fxml", "Issued Books");



    }

    public void loadStudentDashboard(MouseEvent event) {
        loadStudentScreens("studentDashboard.fxml", "Dashboard");
    }

    /***
     * Method that changes the changes the current anchor pane to other fxml files.
     * @param screen
     * @param screenTitle
     */
    private void loadStudentScreens(String screen, String screenTitle){

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(screen));
        } catch (IOException e) {
            e.printStackTrace();
        }
        studentScreenTitleLbl.setText(screenTitle);
        studentScreenTitleLbl.setStyle("-fx-font-family: 'Roboto Medium'");
        recomBooksTitle.setText(null);
        //issuedBooksScreenTitle.setText(null);

        studentScreenBorderPane.setCenter(root);


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

    /**
     * Logs out the Student and loads the login screen
     * @param event
     */

    public void logOutStudent(ActionEvent event) {
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
