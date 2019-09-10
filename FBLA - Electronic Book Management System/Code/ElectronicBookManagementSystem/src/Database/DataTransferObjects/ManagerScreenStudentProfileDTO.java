package Database.DataTransferObjects;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

public class ManagerScreenStudentProfileDTO extends RecursiveTreeObject<ManagerScreenStudentProfileDTO> {

    private final StringProperty studentFirstName;
    private final StringProperty studentLastName;
    private final StringProperty studentEmail;
    private final StringProperty studentRedemptionCode;
    private final IntegerProperty studentSchoolCode;
    private final StringProperty studentLogInStatus;
    private final StringProperty studentUserName;
    private final IntegerProperty studentGrade;
    private ImageView studentImageView;
    private int studentId;


    public ManagerScreenStudentProfileDTO() {
        this.studentFirstName = new SimpleStringProperty();
        this.studentLastName = new SimpleStringProperty();
        this.studentEmail = new SimpleStringProperty();
        this.studentRedemptionCode = new SimpleStringProperty();
        this.studentSchoolCode = new SimpleIntegerProperty();
        this.studentLogInStatus = new SimpleStringProperty();
        this.studentUserName = new SimpleStringProperty();
        this.studentGrade = new SimpleIntegerProperty();

    }

    public void setStudentFirstName(String fnmae) {this.studentFirstName.set(fnmae); }
    public StringProperty getStudentFirstName() { return this.studentFirstName; }
    public String getStudentFname(){return studentFirstName.get();}

    public void setStudentLastName(String lname){
        studentLastName.set(lname);}
    public StringProperty getStudentLastName() {return this.studentLastName;}
    public String getStudentLname(){return studentLastName.get();}

    public void setStudentEmail(String email){
        studentEmail.set(email);}

    public String getSEmial(){return studentEmail.get();}


    public void setStudentRedemptionCode(String rcode){
        studentRedemptionCode.set(rcode);}


    public void setStudentSchoolCode(int schoolCode){
        studentSchoolCode.set(schoolCode);}

    public int getStudentScode(){return studentSchoolCode.get();}


    public void setStudentLogInStatus(String loginStatus){
        studentLogInStatus.set(loginStatus);}

    public String getSLoginStatus(){return studentLogInStatus.get();}

    public void setStudentUserName(String username){
        studentUserName.set(username);}

    public String getSUserName(){return studentUserName.get();}

    public void setStudentGrade(int grade){
        studentGrade.set(grade);}

    public int getSGrade(){return studentGrade.get();}


    public ImageView getStudentImageView() {
        return studentImageView;
    }

    public void setStudentImageView(ImageView studentImageView) {
        this.studentImageView = studentImageView;
    }


    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }



}
