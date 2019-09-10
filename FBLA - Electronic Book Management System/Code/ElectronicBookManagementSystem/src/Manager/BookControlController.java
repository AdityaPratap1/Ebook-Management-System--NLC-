package Manager;

import Database.DBStudent.DBBookController;
import Database.DataTransferObjects.BookDTO;
import LogIn.Session.UserSession;
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

public class BookControlController implements Initializable {

    @FXML
    private JFXTreeTableView<BookDTO> bookListTreeTableView;
    @FXML
    private TreeTableColumn<BookDTO, ImageView> bookImageColumn;
    @FXML
    private TreeTableColumn<BookDTO, String> bookNameColumn;
    @FXML
    private TreeTableColumn<BookDTO, String> authorNameColumn;


    /***
     * Changes the current screen to the "Add Book" screen.
     * @param event
     */
    public void loadAddBookScreen(ActionEvent event) {

        //changes the current anchorpane to the new anchorpane
       ManagerScreenAnchorpaneChanger managerScreenAnchorpaneChanger = new ManagerScreenAnchorpaneChanger();
       managerScreenAnchorpaneChanger.changeAnchorPane("addBook.fxml", "Add Book");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Creates a new DBBookController object
        DBBookController dbBookController = new DBBookController();

        //Sets the book list to the booklist returned by the dbBookController,getBookList();
        ObservableList<BookDTO> bookList =  dbBookController.getAllBooks();
        BookDTO bookDTO = new BookDTO();

        //Set cell value factory for all three columns (Image View, book Title, and author full name
        bookImageColumn.setCellValueFactory(param -> new SimpleObjectProperty<ImageView>((param.getValue().getValue().getBookImageView())));
        bookNameColumn.setCellValueFactory(param -> param.getValue().getValue().getBookTitle());
        authorNameColumn.setCellValueFactory(param -> param.getValue().getValue().getAuthorFullNameProperty());


        bookImageColumn.setStyle("-fx-alignment: center");
        bookNameColumn.setStyle("-fx-alignment: center");
        authorNameColumn.setStyle("-fx-alignment: center");

        populateTable(bookList, bookImageColumn, bookNameColumn, authorNameColumn);

        // The is a event listener that gets the book information when the manager double clicks on a row in the tableview
        bookListTreeTableView.setOnMouseClicked((javafx.scene.input.MouseEvent event) ->{
            if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                getSelectedBookDetails(bookListTreeTableView.getSelectionModel().getSelectedItem());
            }
        });

    }

    /***
     * Populates the Book List Tree Table with book covers, book names, and book author names.
     * @param bookList
     * @param bookCoverColumn
     * @param bookNameColumn
     * @param authorColumn
     */
    private void populateTable(ObservableList<BookDTO> bookList, TreeTableColumn bookCoverColumn, TreeTableColumn bookNameColumn, TreeTableColumn authorColumn){
        //set the tree table view root to the bookList
        final TreeItem<BookDTO> root = new RecursiveTreeItem<>(bookList, RecursiveTreeObject::getChildren);

        //add all columns to the table
        bookListTreeTableView.getColumns().setAll(bookCoverColumn, bookNameColumn, authorColumn);
        bookListTreeTableView.setRoot(root);
        bookListTreeTableView.setShowRoot(false);
    }

    /***
     * Retrieves all information relating to the selected book from the tabel view.
     * @param selectedBook
     */
    private void getSelectedBookDetails(TreeItem<BookDTO> selectedBook){
        TreeItem<BookDTO> bookDTOTreeItem = selectedBook;

        //set BookDTOTreeItem to the selectd book which will be used to retrieve information from the database
        UserSession.setBookDTOTreeItem(selectedBook);
        ManagerScreenAnchorpaneChanger managerScreenAnchorpaneChanger = new ManagerScreenAnchorpaneChanger();
        managerScreenAnchorpaneChanger.changeAnchorPane("viewBook.fxml", "Book Information");
    }



}
