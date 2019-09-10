package Database.DataTransferObjects;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StudentProfileDTO extends UserDTO {
    private final IntegerProperty student_id;
    private final IntegerProperty student_grade;
    private final StringProperty redemption_code;
    private final IntegerProperty student_code;

    public StudentProfileDTO(){

        super();

        this.student_id =  new SimpleIntegerProperty();
        this.student_grade = new SimpleIntegerProperty();
        this.student_code = new SimpleIntegerProperty();
        this.redemption_code = new SimpleStringProperty();


    }

    public int getStudent_id() {
        return student_id.get();
    }

    public IntegerProperty get_student_idProperty() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id.set(student_id);
    }

    public int getStudent_grade() {
        return student_grade.get();
    }

    public IntegerProperty get_student_gradeProperty() {
        return student_grade;
    }

    public void setStudent_grade(int student_grade) {
        this.student_grade.set(student_grade);
    }

    public String getRedemption_code() {
        return redemption_code.get();
    }

    public StringProperty get_redemption_codeProperty() {
        return redemption_code;
    }

    public void setRedemption_code(String redemption_code) {
        this.redemption_code.set(redemption_code);
    }

    public int getStudentRedemptionCode() {
        return student_code.get();
    }

    public IntegerProperty get_student_codeProperty() {
        return student_code;
    }

    public void setStudent_code(int student_code) {
        this.student_code.set(student_code);
    }
}
