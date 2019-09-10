package Manager;

import Database.DBStudent.DBStudentProfileController;
import Database.DataTransferObjects.ManagerScreenStudentProfileDTO;
import LogIn.Session.UserSession;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentActionController implements Initializable {
    @FXML
    private JFXButton redeemStudentBtn, suspendStudentBtn;
    @FXML
    private JFXTextField firstNameTxt, lastNameTxt, studentIdTxt, gradeTxt, usernameTxt, emailTxt;
    @FXML
    private Circle profilePictureCircle;
    private ManagerScreenStudentProfileDTO managerScreenStudentProfileDTO = new ManagerScreenStudentProfileDTO();
    private final DBStudentProfileController dbStudentProfileController = new DBStudentProfileController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        redeemStudentBtn.setVisible(false);
        TreeItem<ManagerScreenStudentProfileDTO> managerScreenStudentProfileDTOTreeItem = UserSession.getManagerScreenStudentProfileDTOTreeItem();

        //populates the text fields with selected student information.
        populateStudentDetails(managerScreenStudentProfileDTOTreeItem);
    }

    /***
     * Populates all text fileds with selected student information from the ManagerScreenStudentProfileDTO.
     * @param managerScreenStudentProfileDTOTreeItem
     */
    private void populateStudentDetails(TreeItem<ManagerScreenStudentProfileDTO> managerScreenStudentProfileDTOTreeItem){
        //access student information through the managerScreenStudentProfileDTOTreeItem object and sets the text fields from the object.
        firstNameTxt.setText(managerScreenStudentProfileDTOTreeItem.getValue().getStudentFname());
        lastNameTxt.setText(managerScreenStudentProfileDTOTreeItem.getValue().getStudentLname());
        studentIdTxt.setText(String.valueOf(managerScreenStudentProfileDTOTreeItem.getValue().getStudentScode()));
        gradeTxt.setText(String.valueOf(managerScreenStudentProfileDTOTreeItem.getValue().getSGrade()));
        usernameTxt.setText(managerScreenStudentProfileDTOTreeItem.getValue().getSUserName());
        emailTxt.setText(managerScreenStudentProfileDTOTreeItem.getValue().getSEmial());


        //Retrieves the student profile image
        DBStudentProfileController dbStudentProfileController = new DBStudentProfileController();
        profilePictureCircle.setFill(new ImagePattern(dbStudentProfileController.retrieveUserImageFromDatabase(managerScreenStudentProfileDTOTreeItem.getValue().getStudentId())));



        //change buttons "Suspend" and "re-activate" depending on the student login status
        if(managerScreenStudentProfileDTOTreeItem.getValue().getSLoginStatus().equals("Suspended")){
            suspendStudentBtn.setVisible(false);
            redeemStudentBtn.setVisible(true);
        }

        else if(managerScreenStudentProfileDTOTreeItem.getValue().getSLoginStatus().equals("Active")){
            suspendStudentBtn.setVisible(true);
            redeemStudentBtn.setVisible(false);
        }



    }

    /***
     * Sets the login status of a student to "suspended"
     * @param event
     */
    public void suspendStudent(ActionEvent event) {
        //Set login status to "suspended"
        managerScreenStudentProfileDTO.setStudentLogInStatus("Suspended");
        managerScreenStudentProfileDTO.setStudentId(UserSession.getManagerScreenStudentProfileDTOTreeItem().getValue().getStudentId());

        //Update the login status in the database
        dbStudentProfileController.updateStudentLoginStatus(managerScreenStudentProfileDTO);
        suspendStudentBtn.setVisible(false);
        redeemStudentBtn.setVisible(true);

    }

    /***
     * Hard deletes a student from the database
     * @param event
     */
    public void deleteStudent(ActionEvent event) {
        managerScreenStudentProfileDTO.setStudentId(UserSession.getManagerScreenStudentProfileDTOTreeItem().getValue().getStudentId());

        //delete the student from the database
        dbStudentProfileController.deleteStudent(managerScreenStudentProfileDTO);


    }

    /***
     * Sets the student login status to "Active"
     * @param event
     */
    public void reActivateStudent(ActionEvent event) {
        managerScreenStudentProfileDTO = new ManagerScreenStudentProfileDTO();
        managerScreenStudentProfileDTO.setStudentLogInStatus("Active");
        managerScreenStudentProfileDTO.setStudentId(UserSession.getManagerScreenStudentProfileDTOTreeItem().getValue().getStudentId());

        //update the student login status in the database
        dbStudentProfileController.updateStudentLoginStatus(managerScreenStudentProfileDTO);
        redeemStudentBtn.setVisible(false);
        suspendStudentBtn.setVisible(true);
    }
}

