package Student;

import Database.DBStudent.DBBookController;
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

public class ReturnBookController implements Initializable {
    @FXML
    private JFXTextField bookNameLabel, authorFnameLabel, authorLnameLabel, genreLabel, isbnNumberLabel, redemptionCodeTxt;
    @FXML
    private Label redemptionCodeStatusLabel;
    @FXML
    private ImageView bookImageView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateBookDetails(UserSession.getUserBookDTOTreeItem());
        //The following code is a change listener that does not allow the user to enter non-numeric values in the redemption code text field.
        redemptionCodeTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                redemptionCodeTxt.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

    }

    /***
     * Populates values od selected book into the book information text fields.
     * @param userBookDTOTreeItem
     */
    private void populateBookDetails(TreeItem<UserBookDTO> userBookDTOTreeItem) {

        //set values into text fields with information obtained from userBookDTOTreeItem
        bookNameLabel.setText(String.valueOf(userBookDTOTreeItem.getValue().getBookName()));
        authorFnameLabel.setText(userBookDTOTreeItem.getValue().getFname());
        authorLnameLabel.setText((userBookDTOTreeItem.getValue().getLname()));
        isbnNumberLabel.setText(userBookDTOTreeItem.getValue().getBIsbnNumber());
        genreLabel.setText(userBookDTOTreeItem.getValue().getBGenre());

        // Pull Image from the BookImages folder using the name of the book
       DBBookController dbBookController = new DBBookController();
       Image image = dbBookController.retrieveBookImage(userBookDTOTreeItem.getValue().getBId());
        bookImageView.setImage(image);
    }

    /***
     *Return the book by setting book and student information in userBookDTO object and pushing to the database using that object.
     * @param event
     */
    public void returnBook(ActionEvent event) {

        //Checks for any empty/null fields
        if(!redemptionCodeTxt.getText().isEmpty() && Integer.parseInt(redemptionCodeTxt.getText()) == UserSession.getUserLogInDTO().getRedemptionCode()) {
            //create DBBookController object
            DBBookController dbBookController = new DBBookController();
            UserBookDTO userBookDTO = new UserBookDTO();

            // set user and book information in the userBookDTO object
            userBookDTO.setUserId(UserSession.getUserLogInDTO().getUserId());
            userBookDTO.setBookId(UserSession.getUserBookDTOTreeItem().getValue().getBId());
            userBookDTO.setReturnDate(LocalDate.now().toString());
            dbBookController.updateStudentBookReturn(userBookDTO);
            redemptionCodeTxt.setFocusColor(Color.GREEN);
            redemptionCodeTxt.setStyle("-fx-prompt-text-fill: green");
            redemptionCodeTxt.setUnFocusColor(Color.GREEN);
            redemptionCodeStatusLabel.setTextFill(Color.GREEN);
            redemptionCodeStatusLabel.setText("Book Returned");

            //wait 0.5 seconds before switching screens
            PauseTransition pause = new PauseTransition(
                    Duration.seconds(0.5)
            );
            pause.setOnFinished((ActionEvent e) -> {

                StudentScreenAnchorpaneChanger studentScreenAnchorpaneChanger = new StudentScreenAnchorpaneChanger();
                studentScreenAnchorpaneChanger.changeAnchorPane("issuedBooksList.fxml" , "Issued Books");

            });
            pause.play();

        }

        else if (redemptionCodeTxt.getText().isEmpty()){

            //Change text field colors to red
            redemptionCodeStatusLabel.setTextFill(Color.RED);
            redemptionCodeStatusLabel.setText("Enter Redemption Code");
            redemptionCodeTxt.setUnFocusColor(Color.RED);
            redemptionCodeTxt.setFocusColor(Color.RED);
            redemptionCodeTxt.setStyle("-fx-prompt-text-fill: red");
        }
        else {
            redemptionCodeStatusLabel.setTextFill(Color.RED);
            redemptionCodeTxt.setFocusColor(Color.RED);
            redemptionCodeTxt.setStyle("-fx-prompt-text-fill: red");
            redemptionCodeTxt.setUnFocusColor(Color.RED);
            redemptionCodeStatusLabel.setText("Incorrect Redemption Code");}

    }

}
