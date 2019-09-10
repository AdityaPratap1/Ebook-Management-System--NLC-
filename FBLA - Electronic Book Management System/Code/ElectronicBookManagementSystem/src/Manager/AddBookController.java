package Manager;

import Database.DBStudent.DBBookController;
import Database.DataTransferObjects.BookDTO;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddBookController implements Initializable {

    @FXML
    private JFXTextField bookNameTxt, authorFnameTxt, authorLnameTxt, isbnTxt, bookPublisherTxt;
    @FXML
    private JFXComboBox genreComboBox;
    @FXML
    private Label warningLbl;
    @FXML
    private ImageView bookImage;
    private File selectedFile;
    private Image bookCoverImage;


    /***
     * Event Handler Method corresponds to the "back" button on the "Add Book" screen. This method changes the current screen to the previous screen which is the "view Catalog" screen.
     * @param event
     */
    public void cancelAddBook(ActionEvent event) {

        ManagerScreenAnchorpaneChanger managerScreenAnchorpaneChanger = new ManagerScreenAnchorpaneChanger();
        //changes the current anchorpane to "studentList.fxml"
        managerScreenAnchorpaneChanger.changeAnchorPane("studentList.fxml", "Add/RemoveBooks");

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set combo-box items (roles) to Manager and Student.
        genreComboBox.setItems(FXCollections.observableArrayList("Drama", "Mystery", "Sci-Fi", "Biography", "Romance"));


    }

    /***
     * Event handler method corresponds to the "Add Book" button on the "Add Book" screen. This method sets all entered information in a bookDTO object and passes to a addBooktoDatabase method.
     * @param event
     */
    public void addBook(ActionEvent event) {


        if (checkNullFields()) {                                  // checks if any required fields are left unfilled in the sign up screen.
            warningLbl.setText("Please Fill All Information");
        } else {

            String bookName = bookNameTxt.getText();
            String authorFname = authorFnameTxt.getText();
            String authorLname = authorLnameTxt.getText();
            String genre = genreComboBox.getValue().toString();
            String isbnNumber = isbnTxt.getText();
            String publisher = bookPublisherTxt.getText();

            BookDTO bookDTO = new BookDTO();
            bookDTO.setBookTitle(bookName);
            bookDTO.setAuthorFirstName(authorFname);
            bookDTO.setAuthorLastName(authorLname);
            bookDTO.setBookGenre(genre);
            bookDTO.setBookIsbnNumber(isbnNumber);
            bookDTO.setBookPublisher(publisher);

            DBBookController dbBookController = new DBBookController();
            dbBookController.addBookToDatabase(bookDTO, selectedFile);

            if (selectedFile != null) { // if the user selects a picture


                saveBookImage(bookCoverImage);
            }

            else if(bookCoverImage == null){
                Image blankImage = new Image("SignUp/blank.png");
                saveBookImage(blankImage);
            }

            //wait 0.5 seconds before switching screens
            PauseTransition pause = new PauseTransition(
                    Duration.seconds(0.5)
            );
            pause.setOnFinished((ActionEvent e) -> {

                ManagerScreenAnchorpaneChanger managerScreenAnchorpaneChanger = new ManagerScreenAnchorpaneChanger();
                managerScreenAnchorpaneChanger.changeAnchorPane("bookControl.fxml", "Catalog");

            });
            pause.play();


        }
    }

    /***
     * This method checks for any null or empty text fields.
     * @return
     */
    private boolean checkNullFields() {

        //check if mentioned textfields are null
        return bookNameTxt.getText() == null || authorFnameTxt.getText() == null || genreComboBox.getValue() == null || isbnTxt.getText() == null || bookPublisherTxt.getText() == null || authorLnameTxt.getText() == null;
    }

    /***
     * Event handler method that uploads book image to image view.
     * @param event
     */
    public void uploadBookImage(ActionEvent event) {
        //Opens file chooser where manager can pick a book Image
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Image files (*.jpg)", "*.jpg");             // Only show and allow files that have a .jpg extension.
        fileChooser.getExtensionFilters().add(extFilter);
        selectedFile = fileChooser.showOpenDialog(null);
        String imagePath = selectedFile.getAbsolutePath();
        imagePath = imagePath.replace("'\'", "\\");


        //gets the chosen path and sets it as an image
        bookCoverImage = new Image("file:///" + imagePath);


        //sets the image to the image view
        bookImage.setImage(bookCoverImage);


    }

    /***
     * Saves the book image to the BookImages Folder
     * @param image
     */
    private void saveBookImage(Image image) {

        //gets the name of the book and saves the image with the name of the book so when later accessed can be accessed by book name.
        File outputFile = new File(String.valueOf(AddBookController.class.getResource("/imgsBooks/" + bookNameTxt.getText() + ".png")));
        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
