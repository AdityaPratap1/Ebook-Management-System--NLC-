package Student;

import Database.DBStudent.DBBookController;
import Database.DataTransferObjects.BookDTO;
import Database.DataTransferObjects.UserBookDTO;
import LogIn.Session.UserSession;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class IssueBookController implements Initializable {
    @FXML
    private JFXTextField bookNameLabel, authorFnameLabel, authorLnameLabel, bookGenreLabel, bookIsbnLabel, sRcodeTxt;
    @FXML
    private Label issueStatusLabel, bookIssueStatusLabel;
    @FXML
    private ImageView bookImage;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<BookDTO> bookDTO = UserSession.getBookDTOTreeItem();
        populateBookDetails(bookDTO);

        //The following code is a change listener that does not allow the user to enter non-numeric values in the redemption code text field.
        sRcodeTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                sRcodeTxt.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /***
     * Populates the text fields with the selected book's information
     * @param bookDTOTreeItem
     */
    private void populateBookDetails(TreeItem<BookDTO> bookDTOTreeItem){
        //access information of selected book from the bookDTOTreeItem and set it to the text fields
        bookNameLabel.setText(bookDTOTreeItem.getValue().getBookName());
        authorFnameLabel.setText(bookDTOTreeItem.getValue().getFname());
        authorLnameLabel.setText(bookDTOTreeItem.getValue().getLname());
        bookIsbnLabel.setText(bookDTOTreeItem.getValue().getBIsbnNumber());
        bookGenreLabel.setText(bookDTOTreeItem.getValue().getBGenre());

        // Loads the book image of the selected book from the BookImages folder using the name of the book
        DBBookController dbBookController = new DBBookController();
        Image image = dbBookController.retrieveBookImage(bookDTOTreeItem.getValue().getBId());
        bookImage.setImage(image);


    }

    /***
     * Changes the current screen to the previous screen.
     * @param event
     */
    public void cancelBookIssue(ActionEvent event) {
        StudentScreenAnchorpaneChanger studentScreenAnchorpaneChanger = new StudentScreenAnchorpaneChanger();
        studentScreenAnchorpaneChanger.changeAnchorPane("bookList.fxml", "Catalog");
    }

    /***
     * Issues current Book. Once Redemption code is correctly entered, the student can issue the book.
     * @param event
     */
    public void issueCurrentBook(ActionEvent event) {
        // checks if the redemption code is correctly entered
        if(!sRcodeTxt.getText().isEmpty()&& Integer.parseInt(sRcodeTxt.getText()) == UserSession.getUserLogInDTO().getRedemptionCode()) {

            UserBookDTO userBookDTO = new UserBookDTO();
            DBBookController dbBookController = new DBBookController();

            //Uses the userBookDTO object to set all the information. The bookDTO object will be used to push the information to the database.
            userBookDTO.setUserId(UserSession.getUserLogInDTO().getUserId());
            userBookDTO.setBookId(UserSession.getBookDTOTreeItem().getValue().getBId());
            userBookDTO.setIssueDate(LocalDate.now().toString());
            userBookDTO.setBookIssueCount(UserSession.getBookDTOTreeItem().getValue().getBookIssueCount() + 1);
            dbBookController.insertStudentbookIssue(userBookDTO);

            //Set Textfield green if redemption code correctly entered
            bookIssueStatusLabel.setVisible(true);
            sRcodeTxt.setFocusColor(Color.GREEN);
            sRcodeTxt.setStyle("-fx-prompt-text-fill: GREEN");
            sRcodeTxt.setUnFocusColor(Color.GREEN);
            issueStatusLabel.setStyle("-fx-font-family: 'Roboto Medium'");
            bookIssueStatusLabel.setTextFill(Color.GREEN);
            bookIssueStatusLabel.setText("Book Issued");

            //wait 0.5 seconds before switching screens
            PauseTransition pause = new PauseTransition(
                    Duration.seconds(0.5)
                    );
            pause.setOnFinished((ActionEvent e) -> {

                StudentScreenAnchorpaneChanger studentScreenAnchorpaneChanger = new StudentScreenAnchorpaneChanger();
                studentScreenAnchorpaneChanger.changeAnchorPane("bookList.fxml" , "Catalog");

            });
            pause.play();



        }

         else if(sRcodeTxt.getText().isEmpty()){
            bookIssueStatusLabel.setVisible(false);
            issueStatusLabel.setTextFill(Color.RED);
            issueStatusLabel.setText("Enter Redemption Code");
            sRcodeTxt.setUnFocusColor(Color.RED);
            sRcodeTxt.setFocusColor(Color.RED);
            sRcodeTxt.setStyle("-fx-prompt-text-fill: red");
            issueStatusLabel.setStyle("-fx-font-family: 'Roboto Medium'");
        }
        else {
            bookIssueStatusLabel.setVisible(false);
            issueStatusLabel.setTextFill(Color.RED);
            issueStatusLabel.setText("Incorrect Redemption Code");
            sRcodeTxt.setUnFocusColor(Color.RED);
            sRcodeTxt.setFocusColor(Color.RED);
            sRcodeTxt.setStyle("-fx-prompt-text-fill: red");
            issueStatusLabel.setStyle("-fx-font-family: 'Roboto Medium'");


        }
    }
}
