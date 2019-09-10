package Manager;

import Database.DBManager.DBManagerController;
import Database.DBStudent.DBBookController;
import Database.DataTransferObjects.BookDTO;
import Database.DataTransferObjects.WeeklyReportDTO;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

import java.net.URL;
import java.util.ResourceBundle;

public class WeeklyReportController implements Initializable {
    @FXML
    private JFXTreeTableView<WeeklyReportDTO>  bookIssueReportTableView;
    @FXML
    private JFXTreeTableView<BookDTO> bookIssuanceTableView;
    @FXML
    private TreeTableColumn<WeeklyReportDTO, String> bookNameColumn, studentNameColumn, dateIssuedColumn;
    @FXML
    private TreeTableColumn<BookDTO, String> bookNameDataColumn, authorNameDataColumn;
    @FXML
    private  TreeTableColumn<BookDTO, Integer> issueCountDataColumn;
    @FXML
    private BarChart<?, ?> genreDataChart;
    @FXML
    private NumberAxis numberOfBooks;
    @FXML
    private Label noBooksIssuedLabel;



     @Override
    public void initialize(URL location, ResourceBundle resources) {
        fillIssueHistoryTable();
        fillBookIssuanceDataTable();
        populateChart();

    }

    /***
     *  This method populates the issue history table with book name, name of student who issued the book, and the date that they issued it on.
     * @param issueReportList
     * @param bookNameColumn
     * @param studentNameColumn
     * @param dateIssuedColumn
     */
    private void populateIssueHistoryTable(ObservableList<WeeklyReportDTO> issueReportList, TreeTableColumn bookNameColumn, TreeTableColumn studentNameColumn, TreeTableColumn dateIssuedColumn ){
        final TreeItem<WeeklyReportDTO> root = new RecursiveTreeItem<>(issueReportList, RecursiveTreeObject::getChildren);
        bookIssueReportTableView.getColumns().setAll(bookNameColumn, studentNameColumn, dateIssuedColumn);
        bookIssueReportTableView.setRoot(root);
        bookIssueReportTableView.setShowRoot(false);
    }

    /***
     * This method retrieves data from the database and fills the issue history table
     */
    private void fillIssueHistoryTable(){

        DBManagerController dbManagerController = new DBManagerController();
        ObservableList<WeeklyReportDTO> issueReportList = dbManagerController.getIssueReportList();

        //checks if the number of books issued this week is 0
        if(issueReportList.size() == 0){
            noBooksIssuedLabel.setVisible(true);
            noBooksIssuedLabel.setText("No Books Issued This Week");
        }

        else{
            noBooksIssuedLabel.setVisible(false);
        }
        bookNameColumn.setCellValueFactory(param -> param.getValue().getValue().getBookTitleProperty());
        studentNameColumn.setCellValueFactory(param -> param.getValue().getValue().getFullNameProperty());
        dateIssuedColumn.setCellValueFactory(param -> param.getValue().getValue().getIssueDateProperty());
        populateIssueHistoryTable(issueReportList, bookNameColumn, studentNameColumn, dateIssuedColumn);

        bookNameColumn.setStyle("-fx-alignment: center");
        studentNameColumn.setStyle("-fx-alignment: center");
        dateIssuedColumn.setStyle("-fx-alignment: center");

    }

    /***
     * This method retrieves book issuance data from the database and fills the book issuance data table.
     */
    private void fillBookIssuanceDataTable(){

        DBBookController dbBookController = new DBBookController();
        ObservableList<BookDTO>  bookDataReportList = dbBookController.getIssueDataReportList();

        bookNameDataColumn.setCellValueFactory(param -> param.getValue().getValue().getBookTitle());
        authorNameDataColumn.setCellValueFactory(param -> param.getValue().getValue().getAuthorLastName());
        issueCountDataColumn.setCellValueFactory(param -> param.getValue().getValue().getBookIssueCountProperty());

        bookNameDataColumn.setStyle("-fx-alignment: center");
        authorNameDataColumn.setStyle("-fx-alignment: center");
        issueCountDataColumn.setStyle("-fx-alignment: center");

        populateIssuanceDataTable(bookDataReportList, bookNameDataColumn, authorNameDataColumn, issueCountDataColumn);
    }

    /***
     * Adds the columns and the observable list to the book issuance data table.
     * @param bookDataReportList
     * @param bookNameDataColumn
     * @param authorNameDataColumn
     * @param issueCountDataColumn
     */
    private void populateIssuanceDataTable(ObservableList<BookDTO> bookDataReportList, TreeTableColumn bookNameDataColumn, TreeTableColumn authorNameDataColumn, TreeTableColumn issueCountDataColumn) {

        final TreeItem<BookDTO> root = new RecursiveTreeItem<>(bookDataReportList, RecursiveTreeObject::getChildren);
        bookIssuanceTableView.getColumns().setAll(bookNameDataColumn, authorNameDataColumn, issueCountDataColumn);
        bookIssuanceTableView.setRoot(root);
        bookIssuanceTableView.setShowRoot(false);
    }


    /***
     * This method populates the chart with issue-genre data.
     */
    private void populateChart(){
         DBManagerController managerController = new DBManagerController();

         ObservableList<WeeklyReportDTO> chartDataList = managerController.getChartDataList();

         // Create a x-y series chart
        XYChart.Series set1 = new XYChart.Series<>();
        numberOfBooks.setScaleY(1);

        for(WeeklyReportDTO weeklyReportDTO : chartDataList) {

            // add data to set
            set1.getData().add(new XYChart.Data(weeklyReportDTO.getBookGenre(), weeklyReportDTO.getIssueCount()));

        }

        // add set to chart
        genreDataChart.getData().addAll(set1);




    }


}

