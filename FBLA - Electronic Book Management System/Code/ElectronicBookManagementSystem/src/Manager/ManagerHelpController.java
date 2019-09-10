package Manager;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ManagerHelpController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ManagerScreenAnchorpaneChanger managerScreenAnchorpaneChanger = new ManagerScreenAnchorpaneChanger();
        managerScreenAnchorpaneChanger.changeAnchorPane("HelpScreenFiles/studentListHelp.fxml", "Student List");
    }
}
