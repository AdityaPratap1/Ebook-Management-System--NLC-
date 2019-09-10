package Manager;

import Database.DBSignUp.DBSignUpController;
import Database.DataTransferObjects.UserSignUpDTO;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class AddStudentController implements Initializable {
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

    /***
     * Event handler method changes the current screen to the previous screen.
     * @param event
     */
    public void cancelAddStudent(ActionEvent event) {
        ManagerScreenAnchorpaneChanger managerScreenAnchorpaneChanger = new ManagerScreenAnchorpaneChanger();
        managerScreenAnchorpaneChanger.changeAnchorPane("studentList.fxml", "Students List");
    }

    /***
     * Retrieves all information entered in text fields and sets them into a UserSignUpDTO constructor. Saves the information in the database through a UserSignUpDTO object.
     * @param event
     */
    public void addNewStudent(ActionEvent event) {
        if (checkNullFields()) {                                  // checks if any required fields are left unfilled in the sign up screen.
            warningLbl.setText("Please Fill All Information");
            warningLbl.setTextFill(Color.RED);

        } else {
            String fname = firstNameTxt.getText();
            String lname = lastNameTxt.getText();
            String grade = gradeTxt.getText();
            String studentId = studentIdTxt.getText();
            String username = usernameTxt.getText();
            String password = passwordTxt.getText();
            String role = chooseRoleComboBox.getValue().toString();
            String email = emailTxt.getText();
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
                    dbSignUpController.signUp(userSignUpDTO, (selectedFile));
                    warningLbl.setText("Student Added");
                                                                // If there is no password mismatch, call the signUp method from SignUpDBController.
                } else {

                    dbSignUpController.signUp(userSignUpDTO, null);
                    warningLbl.setText("Student Added");
                                                                    // If there is no password mismatch, call the signUp method from SignUpDBController.
                }



                if (role.equals("Student")) {
                    dbSignUpController.saveStudentSignUpInformation(userSignUpDTO);                //if the role of the user is "Student", save Student InformaTION.

                }

            }
        }
        warningLbl.setTextFill(Color.GREEN);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        profilePictureCircle.setStroke(Color.rgb(42, 46, 48));
        profilePictureCircle.setEffect(new DropShadow(+25d, 0d, +2d, Color.rgb(42, 46, 48)));
        chooseRoleComboBox.setItems(FXCollections.observableArrayList("Student"));        // set combo-box items (roles) to Manager and Student.

    }

    /***
     * Uploads a picture to the picture circle view located on the top of the screen
     * @param event
     */
    public void uploadImage(ActionEvent event) {
        //Open File chooser for manager to choose images
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter =

                // Only show and allow files that have a .jpg extension.
                new FileChooser.ExtensionFilter("Image files (*.jpg)", "*.jpg");

        fileChooser.getExtensionFilters().add(extFilter);
        selectedFile = fileChooser.showOpenDialog(null);

        // if the user selects a picture
        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            imagePath = imagePath.replace("'\'", "\\");

            profileImage = new Image("file:///" + imagePath);

            // insert image into profile picture circle.
            profilePictureCircle.setFill(new ImagePattern(profileImage));

        }

    }

    /***
     * Checks for and null/empty text fields
     * @return
     */
    private boolean checkNullFields() {
        boolean bool = false;
        //check if mentioned textfields are null
        if(firstNameTxt.getText().isEmpty() || lastNameTxt.getText().isEmpty() || dobTxt.getValue().equals(" ")|| usernameTxt.getText().isEmpty() || passwordTxt.getText().isEmpty() || confirmPassTxt.getText().isEmpty()){
            bool = true;
        }
        return bool;
    }


    /***
     * Generates a random redemption code between 100000 and 900000 and returns it.
     * @return
     */
    private int generateRedemptionCode() {
        //genereate random redemption code and return it
        Random rnd = new Random();
        return 100000 + rnd.nextInt(900000);
    }


    /**
     Saves profile picture in profiles folder with name being the username of the student
     @param image
     */


}
