package Database.DataTransferObjects;

import java.io.FileInputStream;

public class UserSignUpDTO {
    private String fname;
    private String lname;
    private String grade;
    private String studentID;
    private String signUsername;
    private String signPassword;
    private String role;
    private String emailAdderss;
    private String birthDate;
    private String userLoginStatus;
    private int userID;
    private int redemptionCode;
    private FileInputStream  fileInputStream;



    public UserSignUpDTO(String fname, String lname, String grade, String studentID, String signUsername, String signPassword, String role, String emailAdderss, int redemptionCode, String birthDate, String userLoginStatus) {
        this.fname = fname;
        this.lname = lname;
        this.grade = grade;
        this.studentID = studentID;
        this.signUsername = signUsername;
        this.signPassword = signPassword;
        this.role = role;
        this.emailAdderss = emailAdderss;
        this.redemptionCode = redemptionCode;
        this.birthDate=birthDate;
        this.userLoginStatus = userLoginStatus;

    }

    public UserSignUpDTO(){

    }


    public  String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getSignUsername() {
        return signUsername;
    }

    public void setSignUsername(String signUsername) {
        this.signUsername = signUsername;
    }

    public String getSignPassword() {
        return signPassword;
    }

    public void setSignPassword(String signPassword) {
        this.signPassword = signPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmailAdderss() {
        return emailAdderss;
    }

    public void setEmailAdderss(String emailAdderss) {
        this.emailAdderss = emailAdderss;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }


    public int getRedemptionCode() {
        return redemptionCode;
    }

    public void setRedemptionCode(int redemptionCode) {
        this.redemptionCode = redemptionCode;
    }

    public String getUserLoginStatus() {
        return userLoginStatus;
    }

    public void setUserLoginStatus(String userLoginStatus) {
        this.userLoginStatus = userLoginStatus;
    }

}
