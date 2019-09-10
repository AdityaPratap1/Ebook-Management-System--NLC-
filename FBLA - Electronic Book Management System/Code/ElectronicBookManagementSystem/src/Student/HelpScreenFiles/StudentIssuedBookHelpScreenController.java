package Student.HelpScreenFiles;

import Student.StudentScreenAnchorpaneChanger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentIssuedBookHelpScreenController implements Initializable {
    @FXML
    private ImageView imageView;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        imageView.setImage(new Image("/Student/HelpScreenImages/issued books.png"));

    }

    public void loadRecomBooksHelpScreen(ActionEvent event) {

        StudentScreenAnchorpaneChanger studentScreenAnchorpaneChanger = new StudentScreenAnchorpaneChanger();
        studentScreenAnchorpaneChanger.changeAnchorPane("HelpScreenFiles/recommendedBookScreenHelp.fxml", "Recom Books Help");

    }
}
