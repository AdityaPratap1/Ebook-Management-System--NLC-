package Manager;

import Database.DBStudent.DBBookController;
import Database.DataTransferObjects.BookDTO;
import LogIn.Session.UserSession;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewBookController implements Initializable {
    @FXML
    private JFXTextField bookNameLabel, authorFnameLabel, authorLnameLabel, bookIsbnLabel, bookGenreLabel;
    @FXML
    private ImageView bookImage;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<BookDTO> bookDTO = UserSession.getBookDTOTreeItem();
        populateBookDetails(bookDTO);
    }

    /***
     * Fills the text fields with information of selected book
     * @param bookDTOTreeItem
     */

    private void populateBookDetails(TreeItem<BookDTO> bookDTOTreeItem){
        //set values in text field from bookDTOTreeItem object
        bookNameLabel.setText(bookDTOTreeItem.getValue().getBookName());
        authorFnameLabel.setText(bookDTOTreeItem.getValue().getFname());
        authorLnameLabel.setText(bookDTOTreeItem.getValue().getLname());
        bookIsbnLabel.setText(bookDTOTreeItem.getValue().getBIsbnNumber());
        bookGenreLabel.setText(bookDTOTreeItem.getValue().getBGenre());
        DBBookController dbBookController = new DBBookController();
        Image image = dbBookController.retrieveBookImage(bookDTOTreeItem.getValue().getBId());
        bookImage.setImage(image);


    }

    public void loadManagerBookListScreen(ActionEvent event) {

        ManagerScreenAnchorpaneChanger managerScreenAnchorpaneChanger = new ManagerScreenAnchorpaneChanger();
        managerScreenAnchorpaneChanger.changeAnchorPane("bookControl.fxml", "Catalog");

    }
}
