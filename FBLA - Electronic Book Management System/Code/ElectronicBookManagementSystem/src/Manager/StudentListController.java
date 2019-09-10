package Manager;

import Database.DBStudent.DBStudentProfileController;
import Database.DataTransferObjects.ManagerScreenStudentProfileDTO;
import LogIn.Session.UserSession;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentListController implements Initializable {

    @FXML
    private JFXTreeTableView<ManagerScreenStudentProfileDTO> studentListTreeTableView;
    @FXML
    private TreeTableColumn<ManagerScreenStudentProfileDTO, ImageView> studentImageColumn;
    @FXML
    private TreeTableColumn<ManagerScreenStudentProfileDTO, String> studentFirstNameColumn, studentLastNameColumn;
    @FXML
    private JFXTextField searchStudentTxt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Create dbStudentProfileController object
        DBStudentProfileController dbStudentProfileController = new DBStudentProfileController();

        //set studentList to the list returned by the method getStudentList
        ObservableList<ManagerScreenStudentProfileDTO> studentList = dbStudentProfileController.getStudentList();


        //set cell value factories for all three columns param.getvalue() gives the treeitem of the class. Adding another .getValue() gives the object of the class ManagerScreenStudentProfileDTO.
        studentImageColumn.setCellValueFactory(param -> new SimpleObjectProperty<ImageView>((param.getValue().getValue().getStudentImageView())));
        studentFirstNameColumn.setCellValueFactory(param -> param.getValue().getValue().getStudentFirstName());
        studentLastNameColumn.setCellValueFactory(param -> param.getValue().getValue().getStudentLastName());

        studentImageColumn.setStyle("-fx-alignment: center");
        studentFirstNameColumn.setStyle("-fx-alignment: center");
        studentLastNameColumn.setStyle("-fx-alignment: center");

        populateTable(studentList, studentImageColumn, studentFirstNameColumn, studentLastNameColumn);
        filterData();


        //Event listener that gets the selected item when the manager double clicks on a row.
        studentListTreeTableView.setOnMouseClicked((javafx.scene.input.MouseEvent event) ->{
            if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                if(studentListTreeTableView.getSelectionModel().getSelectedItem() != null) {
                    getSelectedStudentDetails(studentListTreeTableView.getSelectionModel().getSelectedItem());
                }
            }
        });


    }

    /***
     * Passes the selected student item into a ManagerScreenStudentProfileDTOTTreeItem setter.
     * @param selectedStudent
     */
    private void getSelectedStudentDetails(TreeItem<ManagerScreenStudentProfileDTO> selectedStudent) {

        TreeItem<ManagerScreenStudentProfileDTO> studentDTOTreeItem = selectedStudent;
        UserSession.setManagerScreenStudentProfileDTOTreeItem(selectedStudent);
        loadViewStudentProfileScreen();
    }

    /***
     * Populate the table view with all students registered in the system.
     * @param studentList
     * @param studentImageColumn
     * @param studentFirstNameColumn
     * @param studentLastNameColumn
     */
    private void populateTable(ObservableList<ManagerScreenStudentProfileDTO> studentList, TreeTableColumn studentImageColumn, TreeTableColumn studentFirstNameColumn, TreeTableColumn studentLastNameColumn){
        final TreeItem<ManagerScreenStudentProfileDTO> root = new RecursiveTreeItem<>(studentList, RecursiveTreeObject::getChildren);
        studentListTreeTableView.getColumns().setAll(studentImageColumn, studentFirstNameColumn, studentLastNameColumn);
        studentListTreeTableView.setRoot(root);
        studentListTreeTableView.setShowRoot(false);
    }

    /***
     * Filter students by text entered in the search text field.
     */
    private void filterData() {
        searchStudentTxt.textProperty().addListener((observable, oldValue, newValue) -> studentListTreeTableView.setPredicate(bookDTOTreeItem -> {
            return (Boolean) (bookDTOTreeItem.getValue().getStudentFirstName().getValue().toLowerCase().contains(newValue.toLowerCase()) || bookDTOTreeItem.getValue().getStudentLastName().getValue().toLowerCase().contains(newValue.toLowerCase()));
        }));
    }

    /***
     * Loads a detailed view of the selected student.
     */
    private void loadViewStudentProfileScreen(){
        ManagerScreenAnchorpaneChanger managerScreenAnchorpaneChanger = new ManagerScreenAnchorpaneChanger();
        managerScreenAnchorpaneChanger.changeAnchorPane("studentAction.fxml", "Student Profile");
    }

    /***
     * Loads the "addStudent.fxml" when the Add Student button is clicked.
     * @param event
     */
    public void loadAddStudentScreen(ActionEvent event) {
        //change manager screen anchorpane to addStudent.fxml
        ManagerScreenAnchorpaneChanger managerScreenAnchorpaneChanger = new ManagerScreenAnchorpaneChanger();
        managerScreenAnchorpaneChanger.changeAnchorPane("addStudent.fxml", "Add New Student");

        }
        }




