package Database.DBSignUp;

import Database.DBStudent.DBStudentProfileController;
import Database.DataTransferObjects.UserSignUpDTO;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static Database.DatabaseConnection.getDbConnection;

public class DBSignUpController {
    private static Connection dbConn;
    private static final String insertUserInformationQuery = "INSERT INTO ebm_user(user_first_name, user_last_name, user_type, user_email, user_dob) VALUES(?, ?, ?, ?, ?)";
    private static final String insertStudentInformationQuery = "INSERT INTO ebm_student(student_grade, student_school_code, user_id, student_redemption_code) VALUES(";
    private static final String insertManagerInformationQuery = "INSERT INTO ebm_manager(manager_fname, manager_lname, user_id) VALUES(";
    private static final String selectUserIdQuery = "SELECT a.user_id FROM ebm_user a WHERE a.user_first_name = ?";
    private static final String insertLoginInformationQuery = "INSERT INTO ebm_userlogin(user_login_name, user_login_password, user_id, user_login_status, profile_picture) VALUES( ?, ?, ?, ?, ?)";
    private static PreparedStatement preStatement;
    private static ResultSet resultSet;


    /**
     * takes a UserSignUpDTO object and passes it into the sveSignUpInformation mathod.
     * @param userSignUpDTO
     * @return
     */
    public void signUp(UserSignUpDTO userSignUpDTO, File file) { //Comment

        getConnection();
        saveSignUpInformation(userSignUpDTO,  file);
    }


    /**
     * Establishes connection to database
     */
    private void getConnection() {
        dbConn = getDbConnection();
    }


    /**
     * takes a userSignUPDTO object, gets user information from the object and sets the in the ebm_user table in the database.
     * @param userSignUpDTO
     * @return userSignUpDTO
     */
    private void saveSignUpInformation(UserSignUpDTO userSignUpDTO, File file) {

        getConnection();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            PreparedStatement preStatement= dbConn.prepareStatement(insertUserInformationQuery);
            preStatement.setString(1, userSignUpDTO.getFname());
            preStatement.setString(2, userSignUpDTO.getLname());
            preStatement.setString(3, userSignUpDTO.getRole());
            preStatement.setString(4, userSignUpDTO.getEmailAdderss());
            preStatement.setString(5, userSignUpDTO.getBirthDate());
            preStatement.executeUpdate();

            preStatement = dbConn.prepareStatement(selectUserIdQuery);
            preStatement.setString(1, userSignUpDTO.getFname());
            resultSet = preStatement.executeQuery();

            int y = 0;

            while(resultSet.next()){
                y = resultSet.getInt(1);
            }


            userSignUpDTO.setUserID(y);
            byte[] bytes;  //


            if(file ==null){
                DBStudentProfileController dbStudentProfileController = new DBStudentProfileController();
                bytes = dbStudentProfileController.retrieveUserImageInBytes(1);
            }
            else {

                 FileInputStream fis = new FileInputStream(String.valueOf((file)));
                 //bos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                for (int readnum; (readnum = fis.read(buf)) != -1; ) {
                    bos.write(buf, 0, readnum);
                }
                bytes = bos.toByteArray();

            }
            preStatement = dbConn.prepareStatement(insertLoginInformationQuery);
            preStatement.setString(1, userSignUpDTO.getSignUsername());
            preStatement.setString(2, userSignUpDTO.getSignPassword());
            preStatement.setInt(3, userSignUpDTO.getUserID());
            preStatement.setString(4, userSignUpDTO.getUserLoginStatus());
            preStatement.setBytes(5, bytes);
            preStatement.executeUpdate();



        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                preStatement.close();
                bos.close();

                //fis.close();

            }catch (SQLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    /**
     * takes userSignUPDTO object, gets student information from object, and sets them into the ebm_student table in the database.
     * @param userSignUpDTO
     */
    public void saveStudentSignUpInformation(UserSignUpDTO userSignUpDTO){

        String userStudentQuery2 = insertStudentInformationQuery + "\'" + userSignUpDTO.getGrade() + "\'" + ",\'" + userSignUpDTO.getStudentID() + "\'" + ",\'" + userSignUpDTO.getUserID() + "\'" +",\'" + userSignUpDTO.getRedemptionCode() + "\')";
        try {
            preStatement= dbConn.prepareStatement(userStudentQuery2);
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
     * takes userSignUpDTO object, if manager, gets manager information from the object, and sets the in the ebm_manager table in the database
     * @param userSignUpDTO
     */
    public void saveManagerSignUpInformation(UserSignUpDTO userSignUpDTO){

        String userManagerQuery2 = insertManagerInformationQuery + "\'" + userSignUpDTO.getFname() + "\'" + ",\'" + userSignUpDTO.getLname() + "\'" + ",\'" + userSignUpDTO.getUserID() +  "\')";
        try {
            preStatement= dbConn.prepareStatement(userManagerQuery2);
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
     * takes a string username and checks the database if the username is already taken.
     * @param username
     * @return
     */
    public boolean userExists(String username){
        getConnection();
        String UserNameQuery = "select user_login_name from ebm_userlogin where user_login_name=?";
        boolean userExistFlag=false;
        try {
            preStatement = dbConn.prepareStatement(UserNameQuery);
            preStatement.setString(1, username);

            resultSet = preStatement.executeQuery();

            while (resultSet.next()) {
                String name=resultSet.getString(1);
                if(username.equalsIgnoreCase(name)) userExistFlag=true;

            }
        }  catch (SQLException e) {
            e.printStackTrace();
        }
        return userExistFlag;
    }


}
