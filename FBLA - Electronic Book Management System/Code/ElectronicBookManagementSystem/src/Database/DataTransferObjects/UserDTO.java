package Database.DataTransferObjects;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserDTO {
    private final IntegerProperty user_id;
    private final StringProperty user_first_name;
    private final StringProperty user_last_name;
    private final StringProperty user_dob;
    private final StringProperty user_type;
    private final StringProperty user_email;

    public UserDTO(){
        this.user_id = new SimpleIntegerProperty();
        this.user_first_name = new SimpleStringProperty();
        this.user_last_name = new SimpleStringProperty();
        this.user_dob = new SimpleStringProperty();
        this.user_type = new SimpleStringProperty();
        this.user_email = new SimpleStringProperty();

    }

    public String getUser_first_name() {
        return user_first_name.get();
    }

    public void setUser_first_name(String user_first_name) {
        this.user_first_name.set(user_first_name);
    }

    public String getUser_last_name() {
        return user_last_name.get();
    }

    public void setUser_last_name(String user_last_name) {
        this.user_last_name.set(user_last_name);
    }

    public String getUser_dob() {
        return user_dob.get();
    }

    public void setUser_dob(String user_dob) {
        this.user_dob.set(user_dob);
    }

    public String getUser_email() {
        return user_email.get();
    }

    public void setUser_email(String user_email) {
        this.user_email.set(user_email);
    }

}
