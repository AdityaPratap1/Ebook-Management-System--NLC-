package Database.DataTransferObjects;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class WeeklyReportDTO extends RecursiveTreeObject<WeeklyReportDTO> {

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }



    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }


    public StringProperty getIssueDateProperty() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate.set(issueDate);
    }


    public StringProperty getFullNameProperty() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }


    public StringProperty getBookTitleProperty() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle.set(bookTitle);
    }


    public String getBookGenre() {  return bookGenre;}

    public void setBookGenre(String bookGenre) {  this.bookGenre = bookGenre;
    }

    public int getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(int issueCount) {
        this.issueCount = issueCount;
    }





    private final StringProperty fullName;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty bookTitle;


    private final StringProperty issueDate;
    private String bookGenre;
    private int issueCount;


    public WeeklyReportDTO(){
        this.firstName = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.issueDate = new SimpleStringProperty();
        this.fullName = new SimpleStringProperty();
        this.bookTitle = new SimpleStringProperty();
    }
}
