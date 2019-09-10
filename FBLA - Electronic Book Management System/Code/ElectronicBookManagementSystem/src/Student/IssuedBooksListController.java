package Student;

import Database.DBStudent.DBBookController;
import Database.DataTransferObjects.UserBookDTO;
import LogIn.Session.UserSession;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class IssuedBooksListController implements Initializable {

    @FXML
    private JFXTreeTableView<UserBookDTO> issuedBooksListTableView;
    @FXML
    private TreeTableColumn<UserBookDTO, ImageView> bookCoverColumn;
    @FXML
    private TreeTableColumn<UserBookDTO, String> bookNameColumn, authorColumn;
    @FXML
    private JFXTextField filterDataTxt;
    private static TreeItem<UserBookDTO> userBookDTOTreeItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBBookController dbBookController = new DBBookController();
        ObservableList<UserBookDTO> userIssuedBookList = dbBookController.getUserBookList();
        bookCoverColumn.setCellValueFactory(param -> new SimpleObjectProperty<ImageView>((param.getValue().getValue().getBookImageView())));
        bookNameColumn.setCellValueFactory(param -> param.getValue().getValue().getBookTitle());
        authorColumn.setCellValueFactory(param -> param.getValue().getValue().getAuthorLastName());
        bookNameColumn.setStyle("-fx-alignment: center");
        authorColumn.setStyle("-fx-alignment: center");
        bookCoverColumn.setStyle("-fx-alignment: center");

        //event listener gets selected book's information when the manager double clicks on the row.
        issuedBooksListTableView.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                getSelectedBookDetails(issuedBooksListTableView.getSelectionModel().getSelectedItem());
            }
        });

        populateTable(userIssuedBookList, bookCoverColumn, bookNameColumn, authorColumn);
        filterData();

    }


    /***
     * Adds all columns and the root to the Tree Table View.
     * @param bookList
     * @param bookCoverColumn
     * @param bookNameColumn
     * @param authorColumn
     */
    private void populateTable(ObservableList<UserBookDTO> bookList, TreeTableColumn bookCoverColumn, TreeTableColumn bookNameColumn, TreeTableColumn authorColumn){
        final TreeItem<UserBookDTO> root = new RecursiveTreeItem<>(bookList, RecursiveTreeObject::getChildren);
        issuedBooksListTableView.getColumns().setAll(bookCoverColumn, bookNameColumn, authorColumn);
        issuedBooksListTableView.setRoot(root);
        issuedBooksListTableView.setShowRoot(false);
    }

    /***
     * Live Filtering of rows when the student enters text in the search book text field.
     */
    private void filterData(){
        filterDataTxt.textProperty().addListener((observable, oldValue, newValue) -> issuedBooksListTableView.setPredicate(userBookDTOTreeItem -> {
            return (Boolean) (userBookDTOTreeItem.getValue().getBookTitle().getValue().toLowerCase().contains(newValue.toLowerCase()) ||  userBookDTOTreeItem.getValue().getAuthorLastName().getValue().toLowerCase().contains(newValue.toLowerCase()));
        }));

    }

    /***
     * returns a UserBookDTOTreeItem
     * @return userBookDTOTreeItem
     */
    public static TreeItem<UserBookDTO> getSelectedBook(){
        return userBookDTOTreeItem;
    }

    /***
     * Takes a tree item object of class UserBookDTO and passes into the setUserBookDTOTreeItem() method.
     * @param selectedBook
     */
    private void getSelectedBookDetails(TreeItem<UserBookDTO> selectedBook){
        userBookDTOTreeItem = selectedBook;

        //set user book to pull book information from the database
        UserSession.setUserBookDTOTreeItem(userBookDTOTreeItem);
        loadReturnBookScreen();
    }


    /***
     * Load the return book screen
     */
    private void loadReturnBookScreen(){
        StudentScreenAnchorpaneChanger studentScreenAnchorpaneChanger = new StudentScreenAnchorpaneChanger();
        studentScreenAnchorpaneChanger.changeAnchorPane("returnBook.fxml", "ReturnBook");

    }
}
