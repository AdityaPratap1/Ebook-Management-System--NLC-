package LogIn;

import Database.DBLogIn.DBLogInController;
import Database.DataTransferObjects.UserLogInDTO;
import LogIn.Session.UserSession;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInScreenController implements Initializable {


    @FXML
    private JFXButton signUpButton;
    @FXML
    private JFXTextField loginUsernameTxt, passwordTextField;
    @FXML
    private JFXPasswordField loginPasswordTxt;
    @FXML
    private Label warningLbl, logInLabel;
    @FXML
    private JFXCheckBox showPasswordCBox;

    private double xposofmouse = 0;
    private double yposofmouse = 0;

    private UserLogInDTO userLogInDTO = new UserLogInDTO();




    /**
     * This method enables the dragging of a window pane.
     * @param event
     */
    public void dragged(javafx.scene.input.MouseEvent event) {
        Node node = (Node) event.getSource();
        Stage stage  = (Stage) node.getScene().getWindow();

        //gets the x and y positions of the mouse and sets them to the stage
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


       @Override
    public void initialize(URL location, ResourceBundle resources) {
        passwordTextField.setVisible(false);
        logInLabel.setStyle("-fx-font-family: 'Roboto Medium'");
        warningLbl.setText(" ");
        warningLbl.setStyle("-fx-font-family: 'Roboto Medium'");

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
     * This event handler method corresponds to the sign up button on the Log In Screen. When called, this method opens the sign up screen.
     * @param event
     * @throws IOException
     */
    public void signUpUser(ActionEvent event) throws IOException {

        //Loads the Sign Up screen
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/SignUp/SignUpScreen.fxml"));
        AnchorPane root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Sign Up");
        stage.setScene(new Scene(root1, 600  , 600));
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getScene().setFill(Color.TRANSPARENT);
        stage.show();
        Stage prevStage = (Stage) this.signUpButton.getScene().getWindow(); // Close the previous pane
        prevStage.close();
    }


    /**This event handler method corresponds to the log in button on the Log In Screen. When called, this method logs in the user.
     * @param event
     */
    public void logInUser(ActionEvent event) {


        // Checks if the username and password exist and are correct. Also checks if the user status is "Active" and not "Suspended"
         if(validateLogin(loginUsernameTxt.getText(), loginPasswordTxt.getText()) && (userLogInDTO.getUserLoginStatus().equals("Active"))) {
             DBLogInController dbLogInController = new DBLogInController();
             //sets the userLogInDTO object in the setUserLogInDTO method.
             //This is a static method that means that the login information such as username can be accessed anywhere to retrieve data.
              UserSession.setUserLogInDTO(userLogInDTO);

             if (userLogInDTO.getUserType().equals("Student")) {
                openStudentWindow();
            }

            if (userLogInDTO.getUserType().equals("Manager")) {
                 openManagerWindow();
            }

            //If validateLogin returns false, the inputted credentials do not exist or do not match
        } else if(!validateLogin(loginUsernameTxt.getText(), loginPasswordTxt.getText())) {

             //The following chunk of code changes the text fields' color to red
             warningLbl.setText("Incorrect Credentials");
            loginUsernameTxt.setUnFocusColor(Color.RED);
            loginPasswordTxt.setUnFocusColor(Color.RED);

            loginUsernameTxt.setFocusColor(Color.RED);
            loginPasswordTxt.setFocusColor(Color.RED);

            loginPasswordTxt.setStyle("-fx-prompt-text-fill: RED");
            loginUsernameTxt.setStyle("-fx-prompt-text-fill: RED");

            loginUsernameTxt.textProperty().addListener((observable, oldValue, newValue) -> {
                loginPasswordTxt.setStyle("-fx-prompt-text-fill: black");
                loginUsernameTxt.setStyle("-fx-prompt-text-fill: black");
                loginUsernameTxt.setUnFocusColor(Color.BLACK);
                loginPasswordTxt.setUnFocusColor(Color.BLACK);
            });
        }

        //if the user exists but is suspended, the following code executes

        else{

             //let user know that he/she is suspended
             warningLbl.setText("User Suspended");
             loginUsernameTxt.setUnFocusColor(Color.RED);
             loginPasswordTxt.setUnFocusColor(Color.RED);

             loginUsernameTxt.setFocusColor(Color.RED);
             loginPasswordTxt.setFocusColor(Color.RED);

             loginPasswordTxt.setStyle("-fx-prompt-text-fill: RED");
             loginUsernameTxt.setStyle("-fx-prompt-text-fill: RED");

             loginUsernameTxt.textProperty().addListener((observable, oldValue, newValue) -> {
                 loginPasswordTxt.setStyle("-fx-prompt-text-fill: black");
                 loginUsernameTxt.setStyle("-fx-prompt-text-fill: black");
                 loginUsernameTxt.setUnFocusColor(Color.BLACK);
                 loginPasswordTxt.setUnFocusColor(Color.BLACK);
             });
         }

    }


    /**  This method validates if the user log in credentials exist and are correct.
     * @param username
     * @param password
     * @return
     */
    private boolean validateLogin(String username, String password) {
        boolean bool = true;
        //Creates DBLogInController object
        DBLogInController dbLogInController = new DBLogInController();
        // passes the username and password into the validate Login method which checks if the username and password are correct.
        userLogInDTO = dbLogInController.validateLogin(username);
        if ((userLogInDTO == null || (!(userLogInDTO.getPassword().equals(loginPasswordTxt.getText()))))) {
            bool = false;
        }
        return bool;
    }

    /**
     * This method opens the student window.
     */
    private void openStudentWindow() {

        //loads student window
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Student/studentScreen.fxml"));
            AnchorPane studentScreenRoot = fxmlLoader.load();
            UserSession.setStudentScreenAnchorpane(studentScreenRoot);
            Stage stage = new Stage();
            stage.setTitle("Student Window");
            stage.setScene(new Scene(studentScreenRoot, 750  , 700));
            stage.setResizable(true);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.getScene().setFill(Color.TRANSPARENT);
            stage.show();
            Stage prevStage = (Stage) this.signUpButton.getScene().getWindow(); // Close the previous pane
            prevStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /***
     * This method opens the manager window
     */
    private void openManagerWindow(){

        //loads the manager window
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Manager/managerScreen.fxml"));
            AnchorPane managerScreenRoot = fxmlLoader.load();
            UserSession.setManagerAnchorpane(managerScreenRoot);
            Stage stage = new Stage();
            stage.setTitle("Manager Window");
            stage.setScene(new Scene(managerScreenRoot, 750  , 700));
            stage.setResizable(true);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.getScene().setFill(Color.TRANSPARENT);
            stage.show();
            Stage prevStage = (Stage) this.signUpButton.getScene().getWindow(); // Close the previous pane
            prevStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /***
     * Unmasks the password field in the login screen.
     * @param event
     */
    public void showPassword(ActionEvent event) {

        //Unmask and mask password if the show password checkbox is selected
        if(showPasswordCBox.isSelected()) {
            loginPasswordTxt.setVisible(false);
            passwordTextField.setVisible(true);
            passwordTextField.setText(loginPasswordTxt.getText());

            passwordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                loginPasswordTxt.setText(passwordTextField.getText());
            });
        }

        else{
            passwordTextField.setVisible(false);
            loginPasswordTxt.setVisible(true);
            loginPasswordTxt.setText(passwordTextField.getText());
        }
    }
}
