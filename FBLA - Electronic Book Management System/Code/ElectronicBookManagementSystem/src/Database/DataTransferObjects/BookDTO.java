package Database.DataTransferObjects;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.*;
import javafx.scene.image.ImageView;

public class BookDTO extends RecursiveTreeObject<BookDTO> {

    //String Property Variables will be used in TableViews
    private final StringProperty bookTitle;
    private final StringProperty authorFirstName;
    private final StringProperty authorLastName;
    private final StringProperty bookPublisher;
    private final StringProperty bookIsbnNumber;
    private final StringProperty bookGenre;
    private final StringProperty bookIssueStatus;
    private final IntegerProperty bookId;
    private ImageView bookImageView;
    private final IntegerProperty bookIssueCount;
    private final StringProperty authorFullName;


    public BookDTO() {
        // Simple String Property will be used in Table Views
        this.bookTitle= new SimpleStringProperty();
        this.authorFirstName = new SimpleStringProperty();
        this.authorLastName = new SimpleStringProperty();
        this.bookPublisher = new SimpleStringProperty();
        this.bookIsbnNumber = new SimpleStringProperty();
        this.bookGenre = new SimpleStringProperty();
        this.bookIssueStatus = new SimpleStringProperty();
        this.bookId = new SimpleIntegerProperty();
        this.bookIssueCount = new SimpleIntegerProperty();
        this.authorFullName = new SimpleStringProperty();

    }

    /***
     *
     * @param title
     * Sets book title of the book associated with the object of the class. The book title is retrieved from the database.
     */
    public void setBookTitle(String title) {this.bookTitle.set(title); }

    /***
     *
     * @return
     *
     * returns a string property, name of the book,  to be used in table views.
     */

    public StringProperty getBookTitle() { return this.bookTitle; }

    /**
     * returns string book name.
     * @return
     */

    public String getBookName(){return bookTitle.get();}


    //*******************************************************************************************************************


    /**
     * sets the author first name associated with tthe object of the class. The authors first name is retrieved from the database.
     * @param fname
     */
    public void setAuthorFirstName(String fname){authorFirstName.set(fname);}


    /**
     * returns string author first name
     * @return
     */
    public String getFname(){return authorFirstName.get();}


    //*******************************************************************************************************************


    /**
     * sets the author last name associated with the object of the class. The author's last name is retrieved from the database.
     * @param lname
     */
    public void setAuthorLastName(String lname){authorLastName.set(lname);}

    /**
     * returns String Property, author last name, to be used in table views.
     * @return
     */
    public StringProperty getAuthorLastName() {return this.authorLastName;}

    /**
     * returns string author last name
     * @return
     */
    public String getLname(){return authorLastName.get();}


    //*******************************************************************************************************************

    /**
     * sets book publisher to the object associated with the class. The book publisher is retrieved from the database.
     * @param bPublisher
     */
    public void setBookPublisher(String bPublisher){bookPublisher.set(bPublisher);}

    /**
     * returns string book publisher
     * @return
     */
    public String getBPublisher(){return bookPublisher.get();}


    //*******************************************************************************************************************


    /**
     * sets book isbn number to the object associated with this class. The isbn number is retrieved from the database.
     * @param bIsbnNumber
     */
    public void setBookIsbnNumber(String bIsbnNumber){bookIsbnNumber.set(bIsbnNumber);}

    /**
     * returns string book isbn number.
     * @return
     */
    public String getBIsbnNumber(){return bookIsbnNumber.get();}


    //*******************************************************************************************************************


    /**
     * sets book genre to the object associated with the class. The book genre is retrieved from the database.
     * @param bGenre
     */
    public void setBookGenre(String bGenre){bookGenre.set(bGenre);}

    /**
     * returns string book genre
     * @return
     */
    public String getBGenre(){return bookGenre.get();}


    //*******************************************************************************************************************


    /**
     * sets book issue status (issued, available) to the object associated with the class. The issue status is retrieved from the database.
     * @param bStatus
     */
    public void setBookIssueStatus(String bStatus){bookIssueStatus.set(bStatus);}


    //*******************************************************************************************************************


    /**
     * sets book id to the object of this class. The book id is retrieved from the databsse.
     * @param bId
     */
    public void setBookId(int bId){bookId.set(bId);}

    /**
     * returns int book id.
     * @return
     */
    public int getBId(){return bookId.get();}

    //*******************************************************************************************************************


    /**
     * returns Image View.
     * @return
     */
    public ImageView getBookImageView() {
        return bookImageView;
    }

    /**
     * sets book imageview to the object associated with this class.
     * @param bookImageView
     */
    public void setBookImageView(ImageView bookImageView) {
        this.bookImageView = bookImageView;
    }


    //*******************************************************************************************************************


    /**
     * returns int book issue count (number of times a book has been issued).
     * @return
     */
    public int getBookIssueCount() {
        return bookIssueCount.get();
    }

    /**
     * returns Object Property of Intgeer, book issue count, to be used in table views.
     * @return
     */
    public ObjectProperty<Integer> getBookIssueCountProperty() {
        return this.bookIssueCount.asObject();
    }

    /**
     * sets book issue count to the object associated with this class.
     * @param bookIssueCount
     */
    public void setBookIssueCount(int bookIssueCount) {
        this.bookIssueCount.set(bookIssueCount);
    }


    //*******************************************************************************************************************


    /**
     * returns string property author full name to be used in table views.
     * @return
     */
    public StringProperty getAuthorFullNameProperty() {
        return authorFullName;
    }


    /**
     * sets author full name to the abject associated with this class. The full name is retrieved by joining the farst and last names.
     * @param authorFullName
     */
    public void setAuthorFullName(String authorFullName) {
        this.authorFullName.set(authorFullName);
    }




}
