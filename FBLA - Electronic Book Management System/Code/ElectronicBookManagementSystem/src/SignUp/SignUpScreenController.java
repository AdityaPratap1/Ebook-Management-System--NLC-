package SignUp;

import Database.DBSignUp.DBSignUpController;
import Database.DataTransferObjects.UserSignUpDTO;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class SignUpScreenController implements Initializable {

    @FXML
    private Circle profilePictureCircle;
    @FXML
    private JFXComboBox chooseRoleComboBox;
    @FXML
    private JFXTextField firstNameTxt, lastNameTxt, studentIdTxt, gradeTxt, usernameTxt, emailTxt;
    @FXML
    private JFXPasswordField passwordTxt, confirmPassTxt;
    @FXML
    private JFXDatePicker dobTxt;
    @FXML
    private Label warningLbl;

    private File selectedFile;
    private Image profileImage;
    private double x = 0;
    private double y = 0;


    /**
     * This method enables the dragging of a window pane.
     *
     * @param event
     */
    public void draggedSignUpScreen(MouseEvent event) {

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }


    //*****************************************************************************************************************************************************************************************************************


    /**
     * This method obtains the x and y co-ordinates of the mouse when pressed. These co-ordinates are then used in the dragged() method to configure the position of the window pane.
     *
     * @param event
     */
    public void pressedSignUpScreen(MouseEvent event) {

        x = event.getX(); // get x co-ordinate of mouse
        y = event.getY();// get y co-ordinate of mouse
    }


    //*****************************************************************************************************************************************************************************************************************


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        profilePictureCircle.setStroke(Color.rgb(42, 46, 48));
        profilePictureCircle.setEffect(new DropShadow(+25d, 0d, +2d, Color.rgb(42, 46, 48)));             // black shadow around the profile picture circle.
        chooseRoleComboBox.setItems(FXCollections.observableArrayList("Manager", "Student"));           // set combo-box items (roles) to Manager and Student.
        //profilePictureCircle.setRotate(-90);
        File blankFile = new File("SignUp/blank.png");
        warningLbl.setText(" ");


        //The following code is a change listener that does not allow the user to enter non-numeric values in the student ID and grade text fields since both text fields should be integers.
        studentIdTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                studentIdTxt.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        gradeTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                gradeTxt.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });


    }


    //*****************************************************************************************************************************************************************************************************************


    /**
     * This event handler method uploads and saves a selected picture in the profile picture circle.
     *
     * @param event
     */
    public void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Image files (*.jpg)", "*.jpg");             // Only show and allow files that have a .jpg extension.
        fileChooser.getExtensionFilters().add(extFilter);
        selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) { // if the user selects a picture
            String imagePath = selectedFile.getAbsolutePath();
            imagePath = imagePath.replace("'\'", "\\");

            profileImage = new Image("file:///" + imagePath);
            profilePictureCircle.setFill(new ImagePattern(profileImage));                 // insert image into profile picture circle.

        }

    }


    //*****************************************************************************************************************************************************************************************************************


    /**
     * This event handler method corresponds to the minimize button on the screen. The method minimizes the current screen.
     *
     * @param event
     */
    public void signUpMinimize(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();            //minimize the current window
        stage.setIconified(true);

    }


    //*****************************************************************************************************************************************************************************************************************


    /**
     * This event handler method corresponds to the close button on the screen. The method, when called, closes the current window.
     *
     * @param event
     */
    public void signUpClose(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();            // get the current window and close it.
        stage.close();

    }


    //*****************************************************************************************************************************************************************************************************************


    /**
     * This event handler method corresponds to the cancel button on the sign up screen.
     *
     * @param event
     */
    public void cancelSignUp(ActionEvent event) {
        returnToLoginScreen();
    }


    //*****************************************************************************************************************************************************************************************************************


    public void signUpUser(ActionEvent event) {

        if (checkNullFields()) {                                  // checks if any required fields are left unfilled in the sign up screen.
            warningLbl.setText("Please Fill All Information");
        } else {
            String fname = firstNameTxt.getText();
            String lname = lastNameTxt.getText();
            String grade = gradeTxt.getText();
            String studentId = studentIdTxt.getText();
            String username = usernameTxt.getText();
            String password = passwordTxt.getText();
            String email = emailTxt.getText();
            String role = chooseRoleComboBox.getValue().toString();
            int redemptionCode = generateRedemptionCode();
            String birthDate = dobTxt.getValue().toString();
            String userLoginStatus = "Active";

            UserSignUpDTO userSignUpDTO = new UserSignUpDTO(fname, lname, grade, studentId, username, password, role, email, redemptionCode, birthDate, userLoginStatus); // UserSignUpDTO constructor takes all fields and makes getters and setters.


            DBSignUpController dbSignUpController = new DBSignUpController();
            if (dbSignUpController.userExists(username)) {                  // Checks if the username exists and if exists returns true indicating the username is already taken.

                usernameTxt.setFocusColor(Color.RED);                   // set text field colors to red.
                usernameTxt.setUnFocusColor(Color.RED);
                usernameTxt.setStyle("-fx-prompt-text-fill: RED");

                usernameTxt.textProperty().addListener(((observableValue, s, t1) -> {           // Once the user starts changing the text, the color of the text field changes from red to black.
                    usernameTxt.setStyle("-fx-prompt-text-fill: WHITE");
                    usernameTxt.setStyle("-fx-prompt-text-fill: WHITE");
                    usernameTxt.setUnFocusColor(Color.WHITE);
                    usernameTxt.setUnFocusColor(Color.WHITE);
                }));
                warningLbl.setText("Username Taken");

            } else if (!passwordTxt.getText().equals(confirmPassTxt.getText())) {                   // if the password field and the confirm password field do not match.

                passwordTxt.setFocusColor(Color.RED);                   // set passwordfield colors to red.
                passwordTxt.setUnFocusColor(Color.RED);
                passwordTxt.setStyle("-fx-prompt-text-fill: RED");

                confirmPassTxt.setFocusColor(Color.RED);
                confirmPassTxt.setUnFocusColor(Color.RED);
                confirmPassTxt.setStyle("-fx-prompt-text-fill: RED");

                confirmPassTxt.textProperty().addListener(((observableValue, oldValue, newValue) -> {               // Once the user starts changing the text, the color of the text field changes from red to black.
                    confirmPassTxt.setStyle("-fx-prompt-text-fill: WHITE");
                    confirmPassTxt.setStyle("-fx-prompt-text-fill: WHITE");
                    confirmPassTxt.setUnFocusColor(Color.WHITE);
                    confirmPassTxt.setUnFocusColor(Color.WHITE);
                    confirmPassTxt.setStyle("-fx-prompt-text-fill: WHITE");
                    confirmPassTxt.setStyle("-fx-prompt-text-fill: WHITE");
                    confirmPassTxt.setUnFocusColor(Color.WHITE);
                    passwordTxt.setUnFocusColor(Color.WHITE);

                }));


                warningLbl.setText("Password Mismatch");                 // Inform user password fields do not match.
            } else {
                if (profileImage != null) {
                    dbSignUpController.signUp(userSignUpDTO, (selectedFile));                  // If there is no password mismatch, call the signUp method from SignUpDBController.
                } else {

                    dbSignUpController.signUp(userSignUpDTO, null);                  // If there is no password mismatch, call the signUp method from SignUpDBController.
                }


                if (role.equals("Student")) {
                    // If there is no password mismatch, call the signUp method from SignUpDBController.

                    dbSignUpController.saveStudentSignUpInformation(userSignUpDTO);                //if the role of the user is "Student", save Student InformaTION.

                }

                returnToLoginScreen();

            }
        }
    }


    //*****************************************************************************************************************************************************************************************************************


    /**
     * This method closes the current screen and returns to the log in screen.
     */
    private void returnToLoginScreen() {

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/LogIn/LogInScreen.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage prevStage = (Stage) this.chooseRoleComboBox.getScene().getWindow();
        prevStage.close();                   // close the current screen and open the login screen.

        Stage primaryStage = new Stage();
        primaryStage.setTitle("LogIn Screen");
        assert root != null;
        primaryStage.setScene(new Scene(root, 550, 400));
        primaryStage.setResizable(false);
        primaryStage.getScene().setFill(Color.TRANSPARENT);                     // Remove the default pane and control buttons.
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }


    //*****************************************************************************************************************************************************************************************************************


    /**
     * This method genereates a random redemption code for the student to use.
     *
     * @return
     */
    private int generateRedemptionCode() {
        Random rnd = new Random();
        return 100000 + rnd.nextInt(900000);
    }


    //*****************************************************************************************************************************************************************************************************************


    /**
     * This method checks if there are any empty required text fields.
     *
     * @return
     */
    private boolean checkNullFields() {

        boolean bool = false;

        //check if mentioned textfields are null
        if (chooseRoleComboBox.getValue().equals("Student")) {
            if (firstNameTxt.getText().isEmpty() || lastNameTxt.getText().isEmpty() || dobTxt.getValue().equals(" ") || usernameTxt.getText().isEmpty() || passwordTxt.getText().isEmpty() || confirmPassTxt.getText().isEmpty() || gradeTxt.getText().isEmpty()|| studentIdTxt.getText().isEmpty()) {

                bool = true;
            }

        }

        return bool;


        //*********************************************************************************************************************************************************************************************************************


    }

}