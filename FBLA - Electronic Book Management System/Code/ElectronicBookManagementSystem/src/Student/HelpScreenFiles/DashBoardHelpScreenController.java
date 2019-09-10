package Student.HelpScreenFiles;

import Student.StudentScreenAnchorpaneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardHelpScreenController implements Initializable {
    @FXML
    private ImageView imageView;
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        Image image = new Image("/Student/HelpScreenImages/dashboard.png");
        imageView.setImage(image);

        //imageView.setImage(new Image());

    }

    public void loadCatalogHelpScreen(ActionEvent event) {
        StudentScreenAnchorpaneChanger studentScreenAnchorpaneChanger = new StudentScreenAnchorpaneChanger();
        studentScreenAnchorpaneChanger.changeAnchorPane("HelpScreenFiles/studentCatalogHelpScreen.fxml", "Catalog");
    }
}
