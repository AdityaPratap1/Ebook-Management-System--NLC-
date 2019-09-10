package Database.DataTransferObjects;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

public class UserBookDTO extends RecursiveTreeObject<UserBookDTO> {

    private final StringProperty bookTitle;
    private final StringProperty authorFirstName;
    private final StringProperty authorLastName;
    private final StringProperty bookPublisher;
    private final StringProperty bookIsbnNumber;
    private final StringProperty bookGenre;
    private final StringProperty bookIssueStatus;
    private final IntegerProperty bookId;
    private final IntegerProperty userId;
    private final StringProperty issueDate;
    private final StringProperty returnDate;
    private ImageView bookImageView;
    private int bookIssueCount;



    public UserBookDTO() {
        this.bookTitle= new SimpleStringProperty();
        this.authorFirstName = new SimpleStringProperty();
        this.authorLastName = new SimpleStringProperty();
        this.bookPublisher = new SimpleStringProperty();
        this.bookIsbnNumber = new SimpleStringProperty();
        this.bookGenre = new SimpleStringProperty();
        this.bookIssueStatus = new SimpleStringProperty();
        this.bookId = new SimpleIntegerProperty();
        this.userId = new SimpleIntegerProperty();
        this.issueDate = new SimpleStringProperty();
        this.returnDate = new SimpleStringProperty();

    }

    public String getReturnDate() {
        return returnDate.get();
    }

    public void setReturnDate(String returnDate) {
        this.returnDate.set(returnDate);
    }


    public String getIssueDate() { return issueDate.get(); }

    public void setIssueDate(String issueDate) {this.issueDate.set(issueDate);}

    //public void setStudentFirstName(String value) { bookTitleProperty().set(value); }
    public void setBookTitle(String title) {this.bookTitle.set(title); }
    public StringProperty getBookTitle() { return this.bookTitle; }
    public String getBookName(){return bookTitle.get();}

    public void setAuthorFirstName(String fname){authorFirstName.set(fname);}

    public String getFname(){return authorFirstName.get();}

    public void setAuthorLastName(String lname){authorLastName.set(lname);}
    public StringProperty getAuthorLastName() {return this.authorLastName;}
    public String getLname(){return authorLastName.get();}


    public void setBookPublisher(String bPublisher){bookPublisher.set(bPublisher);}


    public void setBookIsbnNumber(String bIsbnNumber){bookIsbnNumber.set(bIsbnNumber);}

    public String getBIsbnNumber(){return bookIsbnNumber.get();}


    public void setBookGenre(String bGenre){bookGenre.set(bGenre);}

    public String getBGenre(){return bookGenre.get();}

    public void setBookId(int bId){bookId.set(bId);}

    public int getBId(){return bookId.get();}

    public int getUserId() {
        return userId.get();
    }

    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public ImageView getBookImageView() {
        return bookImageView;
    }

    public void setBookImageView(ImageView bookImageView) {
        this.bookImageView = bookImageView;
    }

    public int getBookIssueCount() {
        return bookIssueCount;
    }

    public void setBookIssueCount(int bookIssueCount) {
        this.bookIssueCount = bookIssueCount;
    }

}
