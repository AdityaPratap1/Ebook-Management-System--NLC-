package Student;

import Database.DBStudent.DBBookController;
import Database.DataTransferObjects.BookDTO;
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
import javafx.scene.input.MouseButton;

import java.net.URL;
import java.util.ResourceBundle;

public class BookListController implements Initializable {

    @FXML
    private JFXTreeTableView<BookDTO> bookListTreeTableView;
    @FXML
    private TreeTableColumn<BookDTO, String>  authorColumn, bookNameColumn;
    @FXML
    private TreeTableColumn<BookDTO, javafx.scene.image.ImageView> bookCoverColumn;
    @FXML
    private JFXTextField filterDataTxt;
    private static TreeItem<BookDTO> bookDTOTreeItem;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Creates a DBController object
        DBBookController object = new DBBookController();

        //set observable list booklist to the list returned by the getBookList method
        ObservableList<BookDTO> bookList =  object.getBookList();
        BookDTO bookDTO = new BookDTO();

        //set cell value factory for all three columns
        bookCoverColumn.setCellValueFactory(param -> new SimpleObjectProperty<>((param.getValue().getValue().getBookImageView())));
        bookNameColumn.setCellValueFactory(param -> param.getValue().getValue().getBookTitle());
        authorColumn.setCellValueFactory(param -> param.getValue().getValue().getAuthorLastName());


        bookNameColumn.setStyle("-fx-alignment: center");
        authorColumn.setStyle("-fx-alignment: center");
        bookCoverColumn.setStyle("-fx-alignment: center");

        populateTable(bookList, bookCoverColumn, bookNameColumn, authorColumn);
        filterData();


        // event listener is called when the student double clicks on the row of the table view.
        bookListTreeTableView.setOnMouseClicked((javafx.scene.input.MouseEvent event) ->{
            if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                getSelectedBookDetails(bookListTreeTableView.getSelectionModel().getSelectedItem());
            }
        });

    }

    /***
     * Adds the columns and the root to the tree table view
     * @param bookList
     * @param bookCoverColumn
     * @param bookNameColumn
     * @param authorColumn
     */
    private void populateTable(ObservableList<BookDTO> bookList, TreeTableColumn bookCoverColumn, TreeTableColumn bookNameColumn, TreeTableColumn authorColumn){
        final TreeItem<BookDTO> root = new RecursiveTreeItem<>(bookList, RecursiveTreeObject::getChildren);
        bookListTreeTableView.getColumns().setAll(bookCoverColumn, bookNameColumn, authorColumn);
        bookListTreeTableView.setRoot(root);
        bookListTreeTableView.setShowRoot(false);
    }

    /***
     * filters the rows based on the search text entered by the student in the search text field.
     */
    private void filterData(){
        filterDataTxt.textProperty().addListener((observable, oldValue, newValue) -> bookListTreeTableView.setPredicate(bookDTOTreeItem -> {
            return (Boolean) (bookDTOTreeItem.getValue().getBookTitle().getValue().toLowerCase().contains(newValue.toLowerCase()) ||  bookDTOTreeItem.getValue().getAuthorLastName().getValue().toLowerCase().contains(newValue.toLowerCase()));
        }));

    }


    /***
     * Takes a TreeItem of class BookDTO  and sets in the UserSession class
     * @param selectedBook
     */
    private void getSelectedBookDetails(TreeItem<BookDTO> selectedBook){
        bookDTOTreeItem = selectedBook;
        UserSession.setBookDTOTreeItem(selectedBook);
        loadIssueBookScreen();
    }


    /***
     * Loads the issue book screen
     */
    private void loadIssueBookScreen(){
        StudentScreenAnchorpaneChanger studentScreenAnchorpaneChanger = new StudentScreenAnchorpaneChanger();
        studentScreenAnchorpaneChanger.changeAnchorPane("issueBook.fxml", "Book Info");
    }
}
