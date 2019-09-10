package Manager.HelpScreenFiles;

import Manager.ManagerScreenAnchorpaneChanger;
import javafx.event.ActionEvent;

public class StudentListHelpController {
    public void loadWeeklyReportScreenHelp(ActionEvent event) {
        ManagerScreenAnchorpaneChanger managerScreenAnchorpaneChanger = new ManagerScreenAnchorpaneChanger();
        managerScreenAnchorpaneChanger.changeAnchorPane("HelpScreenFiles/weeklyReportHelp.fxml", "WeeklyReport Help");
    }
}
