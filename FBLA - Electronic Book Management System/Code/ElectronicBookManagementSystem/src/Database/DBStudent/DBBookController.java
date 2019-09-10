package Database.DBStudent;

import Database.DataTransferObjects.BookDTO;
import Database.DataTransferObjects.UserBookDTO;
import Database.DataTransferObjects.UserLogInDTO;
import Database.DatabaseConnection;
import LogIn.Session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class DBBookController {
    private static Connection dbConn;
    private static final String bookQuery = "SELECT a.* FROM ebm_book a Where a.book_id NOT IN (SELECT b.book_id FROM ebm_bookissue b  where b.book_return_date is null)";
    private static final String issuedBooksQuery = "SELECT a.book_id,b.user_id, b.book_issue_date , b.book_return_date FROM ebm_book a, ebm_bookissue b WHERE a.book_id = b.book_id AND a.book_id =  ";
    private static final String userIssuedBooksQuery = "SELECT a.book_id, a.book_title, a.book_author_fname, a.book_author_lname, a.book_genre, a.book_publisher, a.book_isbn, b.book_issue_date FROM ebm_book a, ebm_bookissue b WHERE a.book_id = b.book_id and book_return_date is null AND b.user_id = ? ORDER BY b.book_issue_date asc";
    private static final String issueBookInsertQuery = "INSERT INTO ebm_bookissue(user_id, book_id, book_issue_date) VALUES(?, ?, ?)";
    private static final String issueBookCountInseryQuery = "UPDATE  ebm_book SET book_issue_count = ? WHERE book_id = ?";
    private static final String returnBookUpdateQuery = "UPDATE ebm_bookissue set book_return_date = ? WHERE user_id = ? AND book_id = ?";
    private static final String topFourBooksQuery = "SELECT a.* FROM ebm_book a where a.book_id not in ( select b.book_id from ebm_bookissue b where  b.book_issue_date is not null and b.book_return_date is null) ORDER BY book_issue_count desc";
    private static final String bookIssuanceDataQuery = "SELECT a.book_title, a.book_author_fname, a.book_author_lname, a.book_issue_count from ebm_book a ORDER BY book_issue_count desc";
    private static final String addBookToDatabaseQuery = "INSERT INTO ebm_book(book_title, book_author_fname, book_author_lname, book_publisher, book_isbn, book_genre, book_picture) VALUES(?, ?, ?, ?, ?, ?, ?)";
    private static final String issuedBooksGenreQuery = "select a.book_genre,count(*)  count from ebm_book a, ebm_bookissue b where a.book_id = b.book_id and b.user_id=? group by book_genre order by count desc";
    private static final String recommendedBookQuery = "Select * from ebm_book a where a.book_genre=? or a.book_genre=? and a.book_id not in (select book_id from ebm_bookissue where user_id=?)";
    private static final String bookIssueCountQuery =  "select count(*)BookIssue from ebm_bookissue b where b.user_id=?";
    private static final String getAllBooksQuery = "SELECT a.* FROM ebm_book a";
    private static final String retrieveBookImageQuery = "SELECT book_picture from ebm_book WHERE book_id = ";

    private static PreparedStatement preStatement;
    private static PreparedStatement preStatement2;
    private static ResultSet resultSet;


    /**
     * Establishes connection to database
     */
    private void getConnection() {
            dbConn = DatabaseConnection.getDbConnection();

    }


    /**
     * returns all Books in database as an ObservableList bookList
     * @return
     */
    public ObservableList<BookDTO> getBookList() {

        ObservableList<BookDTO> bookList;
        getConnection();
        bookList = getBookInformation();

        return bookList;
    }


    /**
     * Executes query that retrieves all information relating to a book and sets it in a ObservableList.
     * @return bookList
     */
    private static ObservableList<BookDTO> getBookInformation() {
        ObservableList<BookDTO> bookList1 = null;

        try {
            preStatement = dbConn.prepareStatement(bookQuery);
            resultSet = preStatement.executeQuery();
            bookList1 = pullBookInfoDatabase(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookList1;

    }


    /**
     * Retrieves all information relating to a book from tne database and saves it in BookDTO object.
     * adds bookDTO object to bookList and returns it.
     * @param resultSet
     * @return bookList
     */
    private static ObservableList<BookDTO> pullBookInfoDatabase(ResultSet resultSet) {

        ObservableList<BookDTO> bookList = FXCollections.observableArrayList();

        try {
            while (resultSet.next()) {

                BookDTO bookDTO = new BookDTO();
                bookDTO.setBookId(resultSet.getInt("book_id"));
                bookDTO.setBookTitle(resultSet.getString("book_title"));
                bookDTO.setAuthorFirstName(resultSet.getString("book_author_fname"));
                bookDTO.setAuthorLastName(resultSet.getString("book_author_lname"));
                bookDTO.setBookPublisher(resultSet.getString("book_publisher"));
                bookDTO.setBookIsbnNumber(resultSet.getString("book_isbn"));
                bookDTO.setBookGenre(resultSet.getString("book_genre"));
                bookDTO.setBookIssueCount(resultSet.getInt("book_issue_count"));
                bookDTO.setAuthorFullName(bookDTO.getFname() + " " + bookDTO.getLname());
                bookIssueStatus(bookDTO);
                setImagetoBook(bookDTO);
                bookList.add(bookDTO);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookList;

    }


    /**
     * Retrieves the status if a book from the database (Issued or Available)
     * @param bookDTO
     */
    private static void bookIssueStatus(BookDTO bookDTO) {
        String returnDateString = null;
        String issueDateString = null;
        try {
            preStatement = dbConn.prepareStatement(issuedBooksQuery + bookDTO.getBId());
            resultSet = preStatement.executeQuery();

            while (resultSet.next()) {
                returnDateString = resultSet.getString("book_return_date");
                issueDateString = resultSet.getString("book_issue_date");
            }

            //If there is an issue date  and no return date that means the book has been issued.
            if ((null != issueDateString && (null == returnDateString || returnDateString.trim().length() == 0))) {

                bookDTO.setBookIssueStatus("Issued");

            } else {
                //if there is no issue date, or there is a return date, the book is available
                bookDTO.setBookIssueStatus("Available");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Retrieves name of book from bookDTO object and using the book name, retrieves corresponding image from folder and sets it in the bookDTO ImageView
     * @param bookDTO
     * @return bookDTO
     */
    private static void setImagetoBook(BookDTO bookDTO) {

        DBBookController dbBookController = new DBBookController();
        Image image = dbBookController.retrieveBookImage(bookDTO.getBId());
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        bookDTO.setBookImageView(imageView);
    }


    /**
     * Inserts issue date of book and the count of the book into database. The count is the number of times the book has been issued.
     * @param userBookDTO
     */
    public void insertStudentbookIssue(UserBookDTO userBookDTO) {
        getConnection();
        try {
            preStatement = dbConn.prepareStatement(issueBookInsertQuery);
            preStatement.setInt(1, userBookDTO.getUserId());
            preStatement.setInt(2, userBookDTO.getBId());
            preStatement.setString(3, userBookDTO.getIssueDate());
            preStatement.executeUpdate();

            preStatement2 = dbConn.prepareStatement(issueBookCountInseryQuery);
            preStatement2.setInt(1, userBookDTO.getBookIssueCount()); //count in 1 added to the previous count when someone issues a book
            preStatement2.setInt(2, userBookDTO.getBId());
            preStatement2.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                preStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();

            }

        }

    }


    /**
     * Retrieves Book List of books that a user has issued.
     * @return
     */
    public ObservableList<UserBookDTO> getUserBookList() {

        ObservableList<UserBookDTO> bookList;
        getConnection();
        bookList = getUserBookInformation();

        return bookList;
    }


    /**
     * executes query that retrieves lost of books that a user has issued.
     * @return
     */
    private static ObservableList<UserBookDTO> getUserBookInformation() {

        ObservableList<UserBookDTO> userBookList1 = null;

        try {
            preStatement = dbConn.prepareStatement(userIssuedBooksQuery);
            preStatement.setInt(1, UserSession.getUserLogInDTO().getUserId());
            System.out.println("HAHAHAHAHAHAHAHAHAHA" + preStatement.getMetaData().toString());

            resultSet = preStatement.executeQuery();

            userBookList1 = pullReturnBookInfoDatabase(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userBookList1;

    }


    /**
     * retrieves of the books a user has issued nd sets in a BookDTO object. Adds the bookDTO object to the ObservableList.
     * @param resultSet
     * @return
     */
    private static ObservableList<UserBookDTO> pullReturnBookInfoDatabase(ResultSet resultSet) {

        ObservableList<UserBookDTO> userBookList = FXCollections.observableArrayList();

        try {
            while (resultSet.next()) {

                UserBookDTO userBookDTO = new UserBookDTO();
                //book.setUserId(resultSet.getInt("user_id"));
                userBookDTO.setBookId(resultSet.getInt("book_id"));
                userBookDTO.setBookTitle(resultSet.getString("book_title"));
                userBookDTO.setAuthorFirstName(resultSet.getString("book_author_fname"));
                userBookDTO.setAuthorLastName(resultSet.getString("book_author_lname"));
                userBookDTO.setBookPublisher(resultSet.getString("book_publisher"));
                userBookDTO.setBookIsbnNumber(resultSet.getString("book_isbn"));
                userBookDTO.setBookGenre(resultSet.getString("book_genre"));
                setImageToReturnBook(userBookDTO);
                //populateBookIssueStatus(book);
                userBookList.add(userBookDTO);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


        return userBookList;


    }


    /**
     * gets image of a book and sets it to the books that a user has issued.
     * @param userBookDTO
     * @return userBookDTO
     */
    private static void setImageToReturnBook(UserBookDTO userBookDTO) {

        DBBookController dbBookController = new DBBookController();
        Image image = dbBookController.retrieveBookImage(userBookDTO.getBId());
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        userBookDTO.setBookImageView(imageView);
    }


    /**
     * sets return date to a book in the database when a student returns a book
     * @param userBookDTO
     */
    public void updateStudentBookReturn(UserBookDTO userBookDTO) {
        getConnection();
        try {
            preStatement = dbConn.prepareStatement(returnBookUpdateQuery);
            preStatement.setString(1, userBookDTO.getReturnDate());
            preStatement.setInt(2, userBookDTO.getUserId());
            preStatement.setInt(3, userBookDTO.getBId());
            preStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                preStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();

            }

        }

    }


    /**
     * sets image to books in the popular books strip located in the student dashboard.
     * @param bookDTO
     * @return
     */
    private static void setImageToPopularBook(BookDTO bookDTO) {
        DBBookController dbBookController = new DBBookController();
        Image image = dbBookController.retrieveBookImage(bookDTO.getBId());
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        bookDTO.setBookImageView(imageView);
    }


    /**
     * retrieves information of popular books, sets it to a bookDTO object and adds the object to an arrayList.
     *
     * @return popularBookList
     */
    public ArrayList<BookDTO> getPopularBooks() {
        getConnection();
        ResultSet resultSet = null;
        ArrayList<BookDTO> popularBookList = new ArrayList<>();

        try {
            PreparedStatement preStatement = dbConn.prepareStatement(topFourBooksQuery);
            resultSet = preStatement.executeQuery();



            while (resultSet.next()) {
                BookDTO bookDto = new BookDTO();
                bookDto.setBookId(resultSet.getInt("book_id"));
                bookDto.setBookTitle(resultSet.getString("book_title"));
                bookDto.setAuthorFirstName(resultSet.getString("book_author_fname"));
                bookDto.setAuthorLastName(resultSet.getString("book_author_lname"));
                bookDto.setBookPublisher(resultSet.getString("book_publisher"));
                bookDto.setBookIsbnNumber(resultSet.getString("book_isbn"));
                bookDto.setBookGenre(resultSet.getString("book_genre"));
                bookDto.setBookIssueCount(resultSet.getInt("book_issue_count"));
                setImageToPopularBook(bookDto);
                popularBookList.add(bookDto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                preStatement.close();
                assert resultSet != null;
                resultSet.close();

            }catch (SQLException e){
                e.printStackTrace();
            }

        }

        return popularBookList;
    }


    /**
     * retrieves book issuance data from database and adds it to a ObservableList. Issue data is how many times a book has been issued
     * @return
     */
    public ObservableList<BookDTO> getIssueDataReportList() {
        getConnection();

       ObservableList<BookDTO> issueDataBookList = FXCollections.observableArrayList();

        try {
            preStatement = dbConn.prepareStatement(bookIssuanceDataQuery);

            resultSet = preStatement.executeQuery();

            while (resultSet.next()) {
                BookDTO bookDTO = new BookDTO();
                bookDTO.setBookTitle(resultSet.getString("book_title"));
                bookDTO.setAuthorFirstName(resultSet.getString("book_author_fname"));
                bookDTO.setAuthorLastName(resultSet.getString("book_author_lname"));
                bookDTO.setBookIssueCount(resultSet.getInt("book_issue_count"));
                issueDataBookList.add(bookDTO);
            }

        }catch(SQLException e){
                    e.printStackTrace();
                }



            return issueDataBookList;

        }


    /**
     * Inserts a new book into the database. This method is called when the add book button pressed located in the manager profile.
     * @param bookDTO
     */
    public void addBookToDatabase(BookDTO bookDTO, File file){
        getConnection();
        ByteArrayOutputStream bos = null;
        FileInputStream fis = null;
        try {


            fis = new FileInputStream((file));
            bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for(int readnum; (readnum = fis.read(buf)) != -1;){
                bos.write(buf, 0, readnum);
            }

            byte[] bytes = bos.toByteArray();
            preStatement = dbConn.prepareStatement(addBookToDatabaseQuery);
            preStatement.setString(1, bookDTO.getBookName());
            preStatement.setString(2, bookDTO.getFname());
            preStatement.setString(3, bookDTO.getLname());
            preStatement.setString(4, bookDTO.getBPublisher());
            preStatement.setString(5, bookDTO.getBIsbnNumber());
            preStatement.setString(6, bookDTO.getBGenre());
            preStatement.setBytes(7, bytes);
            preStatement.executeUpdate();
        }catch (SQLException | IOException e){
            e.printStackTrace();
        } finally {
            try {
                preStatement.close();
                assert bos != null;
                bos.close();
                assert fis != null;
                fis.close();
            } catch (SQLException | IOException e) {
                e.printStackTrace();

            }

        }

    }


    /**
     * This method retrieves the recommended book list and returns it.
     * @param userLogInDTO
     * @return bookList
     */
    public ArrayList<BookDTO> getRecommendedBookListForUser(UserLogInDTO userLogInDTO){
        getConnection();
        ArrayList<BookDTO> bookList = new ArrayList<>();
        int issueCount = getUserBookIssueCount(userLogInDTO.getUserId()); //Get # of books issued by user
        if (issueCount> 4){ // if 5 books issued by a user
            ArrayList<String> issuedBookGenre = getGenreUsingIssueHistory(userLogInDTO.getUserId());
            bookList = prepareRecommendedList(issuedBookGenre, userLogInDTO.getUserId());
        }

        return bookList; //return bokList

    }

    /***
     * Returns an int that refers to the nun=mber of books a user has issued.
     * @param userId
     * @return bookCount
     */
    private int getUserBookIssueCount(int userId){
        getConnection();
        int bookCount = 0;
        ResultSet resultSet;
        try {
            PreparedStatement preStatement = dbConn.prepareStatement(bookIssueCountQuery);
            preStatement.setInt(1,userId);
            resultSet = preStatement.executeQuery();
            while(resultSet.next()){
                bookCount= resultSet.getInt("BookIssue");
            }

        }    catch (SQLException e) {
            e.printStackTrace();
        }
        return bookCount;
    }

    /***
     * Gets number of different genres the user has issued. This information will be used to reccomend books to the user.
     * @param userId
     * @return bookGenre
     */
    private ArrayList<String> getGenreUsingIssueHistory(int userId){

        ArrayList<String> bookGenre = new ArrayList<>();
        try {
            PreparedStatement preStatement = dbConn.prepareStatement(issuedBooksGenreQuery);
            preStatement.setInt(1,userId);
            resultSet = preStatement.executeQuery();
            while(resultSet.next()){
                bookGenre.add(resultSet.getString("book_genre"));
            }

        }    catch (SQLException e) {
            e.printStackTrace();
        }
        return bookGenre;
    }

    /**
     * makes the arraylist for recommended books
     * @param bookGenre
     * @param userId
     * @return arrayList
     */
    private ArrayList<BookDTO> prepareRecommendedList(ArrayList<String> bookGenre, int userId){

        ArrayList<BookDTO> bookList = new ArrayList<>();
        if (bookGenre.size()!=0){
        try {
            PreparedStatement preStatement = dbConn.prepareStatement(recommendedBookQuery);
            preStatement.setString(1, bookGenre.get(0));
            if(bookGenre.size() > 1){ preStatement.setString(2, bookGenre.get(1));

            }else{
                preStatement.setString(2, " ");
            }
            preStatement.setInt(3, userId);
            resultSet = preStatement.executeQuery();
            while(resultSet.next()){
                BookDTO bookDTO = new BookDTO();
                bookDTO.setBookTitle(resultSet.getString("book_title"));
                bookDTO.setAuthorFirstName(resultSet.getString("book_author_fname"));
                bookDTO.setAuthorLastName(resultSet.getString("book_author_lname"));
                bookDTO.setBookGenre(resultSet.getString("book_genre"));
                bookDTO.setBookPublisher(resultSet.getString("book_publisher"));
                bookDTO.setBookIsbnNumber(resultSet.getString("book_isbn"));
                bookDTO.setBookId(resultSet.getInt("book_id"));
                bookList.add(bookDTO);

            }
        }  catch (SQLException e) {
            e.printStackTrace();
        } }
        return bookList;
    }

    /**
     * Selcts and brings all the books that exist in the databse.
     * @return ObservableList
     */
    public ObservableList<BookDTO> getAllBooks()  {
        ResultSet resultSet;
        getConnection();
        ObservableList<BookDTO> allBooksList =FXCollections.observableArrayList();
        try {
             PreparedStatement preStatement = dbConn.prepareStatement(getAllBooksQuery);

            resultSet = preStatement.executeQuery();

            while (resultSet.next()) {

                BookDTO bookDTO = new BookDTO();
                bookDTO.setBookId(resultSet.getInt("book_id"));
                bookDTO.setBookTitle(resultSet.getString("book_title"));
                bookDTO.setAuthorFirstName(resultSet.getString("book_author_fname"));
                bookDTO.setAuthorLastName(resultSet.getString("book_author_lname"));
                bookDTO.setBookPublisher(resultSet.getString("book_publisher"));
                bookDTO.setBookIsbnNumber(resultSet.getString("book_isbn"));
                bookDTO.setBookGenre(resultSet.getString("book_genre"));
                bookDTO.setBookIssueCount(resultSet.getInt("book_issue_count"));
                bookDTO.setAuthorFullName(bookDTO.getFname() + " " + bookDTO.getLname());
                //bookIssueStatus(bookDTO);
                setImagetoBook(bookDTO);
                allBooksList.add(bookDTO);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

       return allBooksList;
    }

    public Image retrieveBookImage(int bookId){
        getConnection();
        Image image = null;
        BufferedImage bImg;
        ByteArrayInputStream bis = null;
        try {

           resultSet = dbConn.createStatement().executeQuery(retrieveBookImageQuery + bookId);

            byte[] imageBytes = resultSet.getBytes("book_picture");
            bis = new ByteArrayInputStream(imageBytes);

            bImg = ImageIO.read(bis);
            image = SwingFXUtils.toFXImage(bImg, null );
        }catch (SQLException | IOException e){
            e.printStackTrace();
        } finally {

            assert preStatement != null;
            //                preStatement.close();
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            assert bis != null;
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return image;
    }


}
