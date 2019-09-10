package Student.HelpScreenFiles;

import Student.StudentScreenAnchorpaneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentCatalogHelpScreenController implements Initializable {
    @FXML
    private ImageView imageView;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        imageView.setImage(new Image("/Student/HelpScreenImages/catalog.png"));
    }

    public void loadIssuedBooksScreen(ActionEvent event) {
        StudentScreenAnchorpaneChanger studentScreenAnchorpaneChanger = new StudentScreenAnchorpaneChanger();
        studentScreenAnchorpaneChanger.changeAnchorPane("HelpScreenFiles/studentIssuedBooksHelpScreen.fxml", "Issued Books");
    }
}
