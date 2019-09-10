package Manager;

import Database.DBManager.DBManagerController;
import Database.DBStudent.DBStudentProfileController;
import Database.DataTransferObjects.UserDTO;
import LogIn.Session.UserSession;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ManagerProfileController implements Initializable {

    @FXML
    private JFXTextField managerProfileFnameTxt, managerProfileLnameTxt, managerProfileEmailTxt, managerProfileUsernameTxt;
    @FXML
    private JFXPasswordField managerProfilePasswordTxt;
    @FXML
    private Circle managerProfilePictureCircle;
    @FXML
    private JFXToggleButton editManagerProfileBtn;
    @FXML
    private Label resultLabel;
    @FXML
    private JFXDatePicker managerProfileDobTxt;
    private Image profileImageUpdate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Create a DBManagerController object
        DBManagerController dbManagerController = new DBManagerController();
        UserDTO userDTO = null;
        try {
            // Retrieves the manager profile of the manager who is logged in through his/her user id and sets it to a UserDTO object
            userDTO = dbManagerController.getManagerProfile(UserSession.getUserLogInDTO().getUserId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Populates the manager profile text fields with all manager information
        populateUserDetails(userDTO);

        // checks if the edit profile toggle button is enabled.
        editManagerProfileBtn.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override

            //if toggled button enabled, set all text fields to editable
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (editManagerProfileBtn.isSelected()) {
                    managerProfileFnameTxt.setEditable(true);
                    managerProfileLnameTxt.setEditable(true);
                    managerProfileEmailTxt.setEditable(true);
                    managerProfileDobTxt.setEditable(true);
                    managerProfilePasswordTxt.setEditable(true);
                    resultLabel.setText(" ");

                }
            //if not enabled, set all text fields to non-editable
                if (!editManagerProfileBtn.isSelected()) {
                    managerProfileFnameTxt.setEditable(false);
                    managerProfileLnameTxt.setEditable(false);
                    managerProfileEmailTxt.setEditable(false);
                    managerProfileDobTxt.setEditable(false);
                    managerProfilePasswordTxt.setEditable(false);

                }
            }
        });


    }

    /***
     * Fills all text fields with manager information.
     * @param userDTO
     */
    private void populateUserDetails(UserDTO userDTO) {

        //set all text fields with manager information obtained from the userDTO object
        managerProfileFnameTxt.setText(userDTO.getUser_first_name());
        managerProfileLnameTxt.setText(userDTO.getUser_last_name());
        managerProfileUsernameTxt.setText(UserSession.getUserLogInDTO().getUsername());
        managerProfilePasswordTxt.setText(UserSession.getUserLogInDTO().getPassword());
        managerProfileEmailTxt.setText(userDTO.getUser_email());
        LocalDate userDOB = LocalDate.parse(userDTO.getUser_dob());

        managerProfileDobTxt.setValue(userDOB);

        //Retrieves manager profile picture from the profile folder usimg the manager's username

        DBStudentProfileController dbStudentProfileController = new DBStudentProfileController();
        managerProfilePictureCircle.setFill(new ImagePattern(dbStudentProfileController.retrieveUserImageFromDatabase(UserSession.getUserLogInDTO().getUserId())));


    }

    /***
     * Updates manager information in database when the "update info" button is clicked
     * @param event
     */
    public void updateManagerInfo(ActionEvent event) {

        //check for any null/empty fields.
        if (managerProfilePasswordTxt.getText().isEmpty() || managerProfileFnameTxt.getText().isEmpty() || managerProfileLnameTxt.getText().isEmpty()) {
            resultLabel.setTextFill(Color.RED);
            resultLabel.setText("Please Fill All Information");
        } else {

            //creates UserDTO object and sets all information from text fields into the object.
            UserDTO userDTO = new UserDTO();
            DBManagerController dbManagerController = new DBManagerController();

            //get information from useDTO object and set it to the setter methods.
            userDTO.setUser_first_name(managerProfileFnameTxt.getText());
            userDTO.setUser_last_name(managerProfileLnameTxt.getText());
            userDTO.setUser_email(managerProfileEmailTxt.getText());
            UserSession.getUserLogInDTO().setPassword(managerProfilePasswordTxt.getText());


            // pass the object into the updateManagerProfile method in the DBManagerController class
            dbManagerController.updateManagerProfile(userDTO);
            editManagerProfileBtn.selectedProperty().setValue(false);
            resultLabel.setTextFill(Color.GREEN);
            resultLabel.setText("Information Saved");



        }
    }

    /***
     * Updates the manager profile picture.
     * @param event
     */
    public void updateProfilePicture(ActionEvent event) {
        if (editManagerProfileBtn.isSelected()) {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("Image files (*.jpg)", "*.jpg");             // Only show and allow files that have a .jpg extension.
                    new FileChooser.ExtensionFilter("Image file (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);
            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) { // if the user selects a picture
                String imagePath = selectedFile.getAbsolutePath();
                imagePath = imagePath.replace("'\'", "\\");

                profileImageUpdate = new Image("file:///" + imagePath);

                //sets image in the profile picture circle
                managerProfilePictureCircle.setFill(new ImagePattern(profileImageUpdate));
            }

            DBStudentProfileController dbStudentProfileController = new DBStudentProfileController();
            dbStudentProfileController.updateUserImageIntoDatabase(UserSession.getUserLogInDTO().getUsername(), profileImageUpdate);
            resultLabel.setTextFill(Color.GREEN);
            resultLabel.setText("Profile Picture Saved");


        }

        else {
            resultLabel.setTextFill(Color.RED);
            resultLabel.setText("Edit Profile not Selected");

        }
    }
}
