package Student;

import Database.DBStudent.DBBookController;
import Database.DataTransferObjects.BookDTO;
import LogIn.Session.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class RecommendedBooksController implements Initializable {
    private ArrayList<BookDTO> recommendedBooks;
    @FXML
    private ImageView recomBook1, recomBook2, recomBook3, recomBook4, recomBook5;
    @FXML
    private Label issueWarningLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DBBookController dbBookController = new DBBookController();
        recommendedBooks= dbBookController.getRecommendedBookListForUser(UserSession.getUserLogInDTO());
            System.out.println("SIZEEEEE"+recommendedBooks.size());
        //If the user did not issue at least 5 books after creation of his/her account, display message
        if(recommendedBooks.size() < 5) {
            // disply - string
            issueWarningLabel.setText("Issue atleast 5 books to unlock feature");
        }else{
        Image image;
        issueWarningLabel.setText(null);


            image = dbBookController.retrieveBookImage(recommendedBooks.get(0).getBId()); //Passing the id of the book to retrieve an image of it.
            recomBook1.setImage(image); //brings image and sets it to the imageview



            image = dbBookController.retrieveBookImage(recommendedBooks.get(1).getBId());
            recomBook2.setImage(image);



            image = dbBookController.retrieveBookImage(recommendedBooks.get(2).getBId());
            recomBook3.setImage(image);

            System.out.println(recommendedBooks.get(3).getBookName());


            image = dbBookController.retrieveBookImage(recommendedBooks.get(3).getBId());
            recomBook4.setImage(image);

            System.out.println(recommendedBooks.get(4).getBookName());


            image = dbBookController.retrieveBookImage(recommendedBooks.get(4).getBId());
            recomBook5.setImage(image);

        }

    }


    public void loadIssueBookScreen(MouseEvent event) {

        //The images of the popular books are on a button
        // get the id of the button when clicked
        String id = event.getPickResult().getIntersectedNode().getId();

        //pass the id into the method
        BookDTO bookDTO = getSelectedRecommendedBook(id);
        TreeItem<BookDTO> root = new TreeItem<>(bookDTO);
        UserSession.setBookDTOTreeItem(root);
        //UserSession.setBookDTO(getSelectedRecommendedBook(id));
        StudentScreenAnchorpaneChanger studentScreenAnchorpaneChanger = new StudentScreenAnchorpaneChanger();
        studentScreenAnchorpaneChanger.changeAnchorPane("issueBook.fxml", "Issue Book");

    }

    /***
     * Retrieve the book details of the popular book that has been clicked.
     * @param buttonId
     * @return bookDTO
     */
    private BookDTO getSelectedRecommendedBook(String buttonId){

        //Use the id passed into this method to match the id with the 4 ids there are
        BookDTO bookDTO = null;

        //if button id is matched, get the popular book details that is displayed on the button.
        switch (buttonId){
            case "recomBook1":{
                bookDTO = recommendedBooks.get(0);
                break;
            }

            case "recomBook2":{
                bookDTO = recommendedBooks.get(1);
                break;
            }

            case "recomBook3":{
                bookDTO = recommendedBooks.get(2);
                break;
            }

            case "recomBook4":{
                bookDTO = recommendedBooks.get(3);
                break;
            }

            case "recomBook5":{
                bookDTO = recommendedBooks.get(4);
                break;
            }


        }

        return bookDTO;
    }
}
