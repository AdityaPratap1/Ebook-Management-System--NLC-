package Student;

import Database.DBStudent.DBStudentProfileController;
import Database.DataTransferObjects.StudentProfileDTO;
import LogIn.Session.UserSession;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class StudentProfileController implements Initializable {
    @FXML
    private JFXToggleButton editStudentProfileToggleBtn;
    @FXML
    private JFXTextField studentProfileFnameTxt, studentProfileLnameTxt, studentProfileGradeTxt, studentProfileIdTxt, studentProfileEmailTxt, studentProfileUsernameTxt, studentProfileRcodeTxt;
    @FXML
    private JFXPasswordField studentProfilePasswordTxt;
    @FXML
    private Circle profilePictureCircle;
    @FXML
    private Label resultLabel, resultLabel1;

    private final DBStudentProfileController dbStudentProfileController = new DBStudentProfileController();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBStudentProfileController dbStudentProfileController = new DBStudentProfileController();
        StudentProfileDTO studentProfileDTO = dbStudentProfileController.getStudentProfile(UserSession.getUserLogInDTO().getUserId());
        populateUserDetails(studentProfileDTO);
        editStudentProfileToggleBtn.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (editStudentProfileToggleBtn.isSelected()) {
                studentProfileFnameTxt.setEditable(true);
                studentProfileLnameTxt.setEditable(true);
                studentProfileGradeTxt.setEditable(true);
                studentProfileIdTxt.setEditable(true);
                studentProfilePasswordTxt.setEditable(true);
                studentProfileEmailTxt.setEditable(true);
                resultLabel.setText(" ");
                resultLabel1.setText(null);

            }

            if (!editStudentProfileToggleBtn.isSelected()) {
                studentProfileFnameTxt.setEditable(false);
                studentProfileLnameTxt.setEditable(false);
                studentProfileGradeTxt.setEditable(false);
                studentProfileIdTxt.setEditable(false);
                studentProfilePasswordTxt.setEditable(false);
                studentProfileEmailTxt.setEditable(false);

            }
        });


        //The following code is a change listener that does not allow the user to enter non-numeric values in the student ID and grade text fields since both text fields should be integers.

        studentProfileIdTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                studentProfileIdTxt.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        studentProfileGradeTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                studentProfileGradeTxt.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });


    }


    /***
     * Populate the student details in the text fields provided in the student profile screen.
     * @param studentProfileDTO
     */
    private void populateUserDetails(StudentProfileDTO studentProfileDTO) {

        //insert values into the text fields through the studentProfileDTO object
        studentProfileFnameTxt.setText(studentProfileDTO.getUser_first_name());
        studentProfileLnameTxt.setText(studentProfileDTO.getUser_last_name());
        studentProfileGradeTxt.setText(String.valueOf(studentProfileDTO.getStudent_grade()));
        studentProfileIdTxt.setText(String.valueOf(studentProfileDTO.getStudentRedemptionCode()));
        studentProfileUsernameTxt.setText(UserSession.getUserLogInDTO().getUsername());
        studentProfilePasswordTxt.setText(UserSession.getUserLogInDTO().getPassword());
        studentProfileEmailTxt.setText(studentProfileDTO.getUser_email());
        studentProfileRcodeTxt.setText(String.valueOf(UserSession.getUserLogInDTO().getRedemptionCode()));

        //Retrieve image of the student using the username
        profilePictureCircle.setFill(new ImagePattern(dbStudentProfileController.retrieveUserImageFromDatabase(UserSession.getUserLogInDTO().getUserId())));

    }


    /***
     * Update the student information in the database.
     * @param event
     */
    public void updateStudentInfo(javafx.event.ActionEvent event) {

        editStudentProfileToggleBtn.selectedProperty().setValue(false);

        //check for any null fields
        if (studentProfilePasswordTxt.getText().isEmpty() || studentProfileFnameTxt.getText().isEmpty() || studentProfileLnameTxt.getText().isEmpty() || studentProfileIdTxt.getText().isEmpty() || studentProfileGradeTxt.getText().isEmpty()) {
            resultLabel.setText("Please Fill All Information");
            resultLabel.setTextFill(Color.RED);
        } else {
            StudentProfileDTO studentProfileDTO = new StudentProfileDTO();
            DBStudentProfileController dbStudentProfileController = new DBStudentProfileController();

            studentProfileDTO.setUser_first_name(studentProfileFnameTxt.getText());
            studentProfileDTO.setUser_last_name(studentProfileLnameTxt.getText());
            studentProfileDTO.setStudent_grade(Integer.valueOf(studentProfileGradeTxt.getText()));
            studentProfileDTO.setStudent_code(Integer.valueOf(studentProfileIdTxt.getText()));
            studentProfileDTO.setUser_email(studentProfileEmailTxt.getText());
            UserSession.getUserLogInDTO().setPassword(studentProfilePasswordTxt.getText());
            dbStudentProfileController.updateStudentProfile(studentProfileDTO);
            resultLabel1.setStyle("-fx-text-fill: green");
            resultLabel1.setText("Information Saved");



         }
    }

    /***
     * Update the student Profile Picture
     * @param event
     */
    public void updateProfilePicture(ActionEvent event) {

        //check if edit profile toggle button is selected

        if (editStudentProfileToggleBtn.isSelected()) {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("Image files (*.jpg)", "*.jpg");             // Only show and allow files that have a .jpg extension.
            fileChooser.getExtensionFilters().add(extFilter);
            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) { // if the user selects a picture
                String imagePath = selectedFile.getAbsolutePath();
                imagePath = imagePath.replace("'\'", "\\");

                Image profileImageUpdate = new Image("file:///" + imagePath);
                profilePictureCircle.setFill(new ImagePattern(profileImageUpdate));


            dbStudentProfileController.updateUserImageIntoDatabase(UserSession.getUserLogInDTO().getUsername(), profileImageUpdate);
            }
            resultLabel.setStyle("-fx-text-fill: green");
            resultLabel.setText("Profile Picture Saved");


        }

          else {

            resultLabel1.setText(null);
            resultLabel.setText("Edit Profile not Selected");}


    }



}