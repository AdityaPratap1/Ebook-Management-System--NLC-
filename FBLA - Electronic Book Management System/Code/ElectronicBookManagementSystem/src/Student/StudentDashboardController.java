package Student;

import Database.DBLogIn.DBLogInController;
import Database.DBStudent.DBBookController;
import Database.DataTransferObjects.BookDTO;
import Database.DataTransferObjects.UserBookDTO;
import LogIn.Session.UserSession;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StudentDashboardController implements Initializable {

    @FXML
    private Label redemptionCodeLabel;
    @FXML
    private JFXTreeTableView<UserBookDTO> bookGlimpseTableView;
    @FXML
    private TreeTableColumn<UserBookDTO, String> bookNameColumn, authorColumn;
    @FXML
    private TreeTableColumn<UserBookDTO, ImageView> bookCoverColumn;
    @FXML
    private ImageView mostPopBookImage1, mostPopBookImage2, mostPopBookImage3, mostPopBookImage4;
    private ArrayList<BookDTO> popularBooks;

    /***
     * Load Issued Books Screen
     * @param event
     */
    public void viewAllIssuedBooks(ActionEvent event) {
        StudentScreenAnchorpaneChanger studentScreenAnchorpaneChanger = new StudentScreenAnchorpaneChanger();
        studentScreenAnchorpaneChanger.changeAnchorPane("issuedBooksList.fxml", "Issued Books");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Retrieve and display a student's redemption code
        getStudentRedemptionCode(UserSession.getUserLogInDTO().getUserId());
        redemptionCodeLabel.setText("Your Redemption Code " + UserSession.getUserLogInDTO().getRedemptionCode()  );


        DBBookController dbBookController = new DBBookController();
        ObservableList<UserBookDTO> bookList =  dbBookController.getUserBookList();

        ObservableList<UserBookDTO> studentDashboardGlimpseBookList = FXCollections.observableArrayList();

            //Get only 3 books from the issued book list to add to the "glimpse of issued books" table
            for (int i = 0; i < bookList.size(); i++) {
                studentDashboardGlimpseBookList.add(bookList.get(i));
                if(i ==2 ) break;
            }





        //Set cell value factories for all three columns
        bookCoverColumn.setCellValueFactory(param -> new SimpleObjectProperty<ImageView>((param.getValue().getValue().getBookImageView())));
        bookNameColumn.setCellValueFactory(param -> param.getValue().getValue().getBookTitle());
        authorColumn.setCellValueFactory(param -> param.getValue().getValue().getAuthorLastName());


        bookNameColumn.setStyle("-fx-alignment: center");
        authorColumn.setStyle("-fx-alignment: center");
        bookCoverColumn.setStyle("-fx-alignment: center");

        populateTable(studentDashboardGlimpseBookList, bookCoverColumn, bookNameColumn, authorColumn);


        //event listener retrieves the information of the selected book when the student double clicks the row of the tree table view
        bookGlimpseTableView.setOnMouseClicked((javafx.scene.input.MouseEvent event) ->{
            if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                getSelectedBookDetails(bookGlimpseTableView.getSelectionModel().getSelectedItem());
            }
        });

         popularBooks = dbBookController.getPopularBooks();

         //Gets the Images for the popular books from the BookImages folder using the book name retrieved from the popular book array list.
        // Popular books are determined by the top 4 issued books in the database
        // The books you have already issued will NOT show up on the popular books list.

        Image img;


        img =  dbBookController.retrieveBookImage(popularBooks.get(0).getBId());
        mostPopBookImage1.setImage(img);

        img =  dbBookController.retrieveBookImage(popularBooks.get(1).getBId());
        mostPopBookImage2.setImage(img);

        img =  dbBookController.retrieveBookImage(popularBooks.get(2).getBId());
        mostPopBookImage3.setImage(img);

        img =  dbBookController.retrieveBookImage(popularBooks.get(3).getBId());
        mostPopBookImage4.setImage(img);


    }

    /***
     * Adds the columns and the root to the tree table view
     * @param bookList
     * @param bookCoverColumn
     * @param bookNameColumn
     * @param authorColumn
     */
    private void populateTable(ObservableList<UserBookDTO> bookList, TreeTableColumn bookCoverColumn, TreeTableColumn bookNameColumn, TreeTableColumn authorColumn){
        final TreeItem<UserBookDTO> root = new RecursiveTreeItem<>(bookList, RecursiveTreeObject::getChildren);
        bookGlimpseTableView.getColumns().setAll(bookCoverColumn, bookNameColumn, authorColumn);
        bookGlimpseTableView.setRoot(root);
        bookGlimpseTableView.setShowRoot(false);
    }


    /** public static TreeItem<UserBookDTO> getSelectedBook(){
     return bookDTOTreeItem;
     }*/

    /***
     * Retrieves the details of the selected book.
     * @param selectedBook
     */
   private void getSelectedBookDetails(TreeItem<UserBookDTO> selectedBook){

       // passes the bookDTOTreeItem in to the setUserBookDTOTreeItem() method which uses the object to pull information from the database.
       TreeItem<UserBookDTO> bookDTOTreeItem = selectedBook;
        UserSession.setUserBookDTOTreeItem(selectedBook);
        //loadIssueBookScreen();
    }

    /**
     * Retrieves the redemption code from the database
     * @param userId
     * @return
     */
    private void getStudentRedemptionCode(int userId) {

        DBLogInController dbLogInController = new DBLogInController();
        int code = dbLogInController.retriveStudentRedemptionCode(userId);
        UserSession.getUserLogInDTO().setRedemptionCode(code);

    }

    /***
     * Load the issue book screen
     * @param event
     */
    public void loadIssueBookScreen(MouseEvent event) {
        //The images of the popular books are on a button
        // get the id of the button when clicked
        String id = event.getPickResult().getIntersectedNode().getId();

        //pass the id into the method
        BookDTO bookDTO = getSelctedPopularBook(id);
        TreeItem<BookDTO> root = new TreeItem<>(bookDTO);
        UserSession.setBookDTOTreeItem(root);
        //UserSession.setBookDTO(getSelctedPopularBook(id));
        StudentScreenAnchorpaneChanger studentScreenAnchorpaneChanger = new StudentScreenAnchorpaneChanger();
        studentScreenAnchorpaneChanger.changeAnchorPane("issueBook.fxml", "Issue Book");

    }

    /***
     * Retrieve the book details of the popular book that has been clicked.
     * @param buttonId
     * @return bookDTO
     */
    private BookDTO getSelctedPopularBook(String buttonId){

        //Use the id passed into this method to match the id with the 4 ids there are
        BookDTO bookDTO = null;

        //if button id is matched, get the popular book details that is displayed on the button.
        switch (buttonId){
            case "mostPopBookImage1":{
                bookDTO = popularBooks.get(0);
                break;
            }

            case "mostPopBookImage2":{
                bookDTO = popularBooks.get(1);
                break;
            }

            case "mostPopBookImage3":{
                bookDTO = popularBooks.get(2);
                break;
            }

            case "mostPopBookImage4":{
                bookDTO = popularBooks.get(3);
                break;
            }


        }

        return bookDTO;
    }
}
