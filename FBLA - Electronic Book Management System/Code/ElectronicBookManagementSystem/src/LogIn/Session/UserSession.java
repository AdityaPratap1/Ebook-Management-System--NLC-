package LogIn.Session;

import Database.DataTransferObjects.BookDTO;
import Database.DataTransferObjects.ManagerScreenStudentProfileDTO;
import Database.DataTransferObjects.UserBookDTO;
import Database.DataTransferObjects.UserLogInDTO;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;


public class UserSession {
    // STATIC CLASS THAT HOUSES DATA THAT CAN BE ACCESSED THROUGHOUT THE PROJECT WITH CREATING AN INSTANCE OF THIS CLASS.
    private static UserLogInDTO userLogInDTO;
    private static AnchorPane managerScreenAnchorpane;
    private static AnchorPane studentScreenAnchorpane;
    private static TreeItem<BookDTO> bookDTOTreeItem;
    private static BookDTO bookDTO;
    private static TreeItem<ManagerScreenStudentProfileDTO> managerScreenStudentProfileDTOTreeItem;
    private static  TreeItem<UserBookDTO> userBookDTOTreeItem;



    /***
     * returns a tree item of managerScreenStudentProfileDTOTreeItem to be used in table views since JFXTreeTableViews only take tree items.
     * @return managerScreenStudentProfileDTOTreeItem
     */
    public static TreeItem<ManagerScreenStudentProfileDTO> getManagerScreenStudentProfileDTOTreeItem() {
        return managerScreenStudentProfileDTOTreeItem;
    }

    /***
     * sets managerScreenStudentProfileDTOTreeItem
      * @param managerScreenStudentProfileDTOTreeItem
     */
    public static void setManagerScreenStudentProfileDTOTreeItem(TreeItem<ManagerScreenStudentProfileDTO> managerScreenStudentProfileDTOTreeItem) {
        UserSession.managerScreenStudentProfileDTOTreeItem = managerScreenStudentProfileDTOTreeItem;
    }

    /***
     * static method that returns a TreeItem<BookDTO> object.
     * @return bookDTOTreeItem
     */
    public static TreeItem<BookDTO> getBookDTOTreeItem() {
        return bookDTOTreeItem;
    }

    /***
     * Takes a anchor pane and sets it to managerAnchorPane.
     * @param managerScreenAnchorpane
     */
    public static void setManagerAnchorpane(AnchorPane managerScreenAnchorpane){
        UserSession.managerScreenAnchorpane = managerScreenAnchorpane;
    }

    /***
     * returns manager screen anchorpane. This anchorpane is used to change the anchor pane of the manager screen when the manager changes tab or clicks a button that opens a new fxml document.
     * @return
     */
    public static AnchorPane getManagerScreenAnchorpane() {
        return managerScreenAnchorpane;
    }

    /***
     *
     * @param studentScreenAnchorpane
     */
    public static void setStudentScreenAnchorpane(AnchorPane studentScreenAnchorpane) {
        UserSession.studentScreenAnchorpane = studentScreenAnchorpane;
    }

    /***
     * returns the student screen anchorpane. This anchorpane will be used to change the anchorpane on the student screen when a student chnages tab or clicks on a button that opens a new fxml documant.
     * @return studentScreenAnchorPane
     */
    public static AnchorPane getStudentScreenAnchorpane() {
        return studentScreenAnchorpane;
    }

    /***
     * returns a UserLogInDTO object
     * @return userLogInDTO
     */
    public static UserLogInDTO getUserLogInDTO() {
        return userLogInDTO;
    }

    /***
     * The method takes a UserLogInDTO object and sets it to userLogInDTO object.
     * @param userLogInDTO
     */
    public static void setUserLogInDTO(UserLogInDTO userLogInDTO) {
        UserSession.userLogInDTO = userLogInDTO;
    }

    /***
     * Takes a  BookDTOTreeItem object and sets it into bookDTOTreeItem object
     * @param bookDTOTreeItem
     */
    public static void setBookDTOTreeItem(TreeItem<BookDTO> bookDTOTreeItem){UserSession.bookDTOTreeItem = bookDTOTreeItem;}

    /***
     * returns a tree item of UserBook class
     * @return
     */
    public static TreeItem<UserBookDTO> getUserBookDTOTreeItem() {
        return userBookDTOTreeItem;
    }

    /***
     * sets TreeItem of class UserBookDTO to be accessed in a JFXTreeTableView.
     * @param userBookDTOTreeItem
     */
    public static void setUserBookDTOTreeItem(TreeItem<UserBookDTO> userBookDTOTreeItem) {
        UserSession.userBookDTOTreeItem = userBookDTOTreeItem;
    }

    /***
     *
     * @param bookDTO
     */
    public static void setBookDTO(BookDTO bookDTO) {
        UserSession.bookDTO = bookDTO;
    }




}
