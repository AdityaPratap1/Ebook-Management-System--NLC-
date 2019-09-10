package Database.DBStudent;

import Database.DataTransferObjects.ManagerScreenStudentProfileDTO;
import Database.DataTransferObjects.StudentProfileDTO;
import Database.DatabaseConnection;
import LogIn.Session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;


public class DBStudentProfileController {
    private static Connection dbConn;
    private static final String studentProfileQuery = "SELECT a.user_first_name, a.user_last_name, a.user_dob, a.user_email, b.student_grade, b.student_redemption_code, b.student_school_code FROM ebm_user a, ebm_student b WHERE a.user_id = b.user_id AND a.user_id = ";
    private static final String userProfileUpdateQuery = "UPDATE ebm_user set user_first_name = ?, user_last_name = ?, user_email = ? WHERE ebm_user.user_id = ?";
    private static final String studentProfileUpdateQuery = "UPDATE ebm_student set student_grade = ? , student_school_code = ? WHERE ebm_student.user_id = ?";
    private static final String userLoginUpdateQuery = "UPDATE ebm_userlogin set user_login_password = ? WHERE ebm_userlogin.user_id = ?";
    private static final String studentInformationForStudentList = "SELECT a.user_id, a.user_first_name, a.user_last_name, a.user_dob, a.user_email, b.student_grade, b.student_redemption_code, b.student_school_code, c.user_login_status, c.user_login_name FROM ebm_user a, ebm_student b, ebm_userlogin c WHERE a.user_id = b.user_id AND b.user_id = c.user_id";
    private static final String updateStudentLoginStatusQuery = "UPDATE ebm_userlogin set user_login_status = ? WHERE ebm_userlogin.user_id = ?";
    private static final String retrieveStudentImageFromDatabaseQuery = "SELECT a.profile_picture FROM ebm_userlogin a WHERE a.user_id= ?";
    private static final String updateStudentImageIntoDatabase = " UPDATE ebm_userlogin set profile_picture = ? WHERE ebm_userlogin.user_login_name = ?";

    private static PreparedStatement preStatement=null;
    private static ResultSet resultSet=null;

    /**
     * Takes in a user id and passes into the getStudentInformation method.
     * @param userId
     * @return studentProfileDTO
     */
    public StudentProfileDTO getStudentProfile(int userId) {

        StudentProfileDTO studentProfileDTO;
        getConnection();
        studentProfileDTO = getStudentInformation(userId);
        return studentProfileDTO;

    }

    /**
     * Establishes connection with the database
     */
    private void getConnection() {
            dbConn = DatabaseConnection.getDbConnection();

    }

    /**
     * Retrieves all student information from database and sets them to the studentProfileDTO object. Adds the object to the observableList *
     * @param userId
     * @return studentProfileDTO
     */
    private StudentProfileDTO getStudentInformation(int userId) {

        String getStudentInformationQuery = studentProfileQuery + "\'" + userId + "\'";

        try {
            preStatement = dbConn.prepareStatement(getStudentInformationQuery);
            resultSet = preStatement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        StudentProfileDTO studentProfieDTO = new StudentProfileDTO();

        try {
            while (resultSet.next()) {

                studentProfieDTO.setStudent_grade(resultSet.getInt("student_grade"));
                studentProfieDTO.setStudent_code(resultSet.getInt("student_school_code"));
                studentProfieDTO.setRedemption_code(resultSet.getString("student_redemption_code"));
                studentProfieDTO.setUser_first_name(resultSet.getString("user_first_name"));
                studentProfieDTO.setUser_last_name(resultSet.getString("user_last_name"));
                studentProfieDTO.setUser_dob(resultSet.getString("user_dob"));
                studentProfieDTO.setUser_email(resultSet.getString("user_email"));

            }
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("rrrrr");
        }finally{
            try{
                preStatement.close();
                resultSet.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return studentProfieDTO;

    }

    /**
     *  Updates Student information in the database when a student clicks the "update info" button in the profile screen.
     * @param studentProfileDTO
     */
    public void updateStudentProfile(StudentProfileDTO studentProfileDTO) {


        getConnection();


        try {
            preStatement = dbConn.prepareStatement(userProfileUpdateQuery);
            preStatement.setString(1, studentProfileDTO.getUser_first_name());
            preStatement.setString(2, studentProfileDTO.getUser_last_name());
            preStatement.setString(3, studentProfileDTO.getUser_email());
            preStatement.setInt(4, UserSession.getUserLogInDTO().getUserId());
            preStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("sdfsdfsdf");

        }finally {
            try {
                preStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();

            }

        }

        try {
            preStatement = dbConn.prepareStatement(studentProfileUpdateQuery);
            preStatement.setInt(1, studentProfileDTO.getStudent_grade());
            preStatement.setInt(2, studentProfileDTO.getStudentRedemptionCode());
            preStatement.setInt(3, UserSession.getUserLogInDTO().getUserId());
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

        try {
            preStatement = dbConn.prepareStatement(userLoginUpdateQuery);
            preStatement.setString(1, UserSession.getUserLogInDTO().getPassword());
            preStatement.setInt(2, UserSession.getUserLogInDTO().getUserId());
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
     * returns an observable list of students.
     * @return studentList
     */
    public ObservableList<ManagerScreenStudentProfileDTO> getStudentList(){
        ObservableList<ManagerScreenStudentProfileDTO> studentList;
        getConnection();
        studentList =  getManagerScreenStudentListInformation();

        return studentList;
    }

    /**
     * Executes query that retrieves student information in a list to be used in the manager screen under the "view students" tab.
     * @return
     */
    private ObservableList<ManagerScreenStudentProfileDTO> getManagerScreenStudentListInformation(){
        ObservableList<ManagerScreenStudentProfileDTO> studentList = null;
        ResultSet resultSet;
        try {
            preStatement= dbConn.prepareStatement(studentInformationForStudentList);
             resultSet = preStatement.executeQuery();
            studentList = pullStudentListInformation(resultSet);

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return studentList;
    }

    /**
     * Retrieves student information from the database and adds it to ManagerScreenStudentProfileDTO object.
     * The object is then added to the studentList Observable Array List.
     * @param resultSet
     * @return studentList
     */
    private  ObservableList<ManagerScreenStudentProfileDTO> pullStudentListInformation(ResultSet resultSet){
        ObservableList<ManagerScreenStudentProfileDTO> studentList = FXCollections.observableArrayList();

        try {

            int i = 0 ;
            while (resultSet.next()) {
                System.out.println(i);
                ManagerScreenStudentProfileDTO managerScreenStudentProfileDTO = new ManagerScreenStudentProfileDTO();
                managerScreenStudentProfileDTO.setStudentUserName(resultSet.getString("user_login_name"));
                managerScreenStudentProfileDTO.setStudentGrade(resultSet.getInt("student_grade"));
                managerScreenStudentProfileDTO.setStudentFirstName(resultSet.getString("user_first_name"));
                managerScreenStudentProfileDTO.setStudentLastName(resultSet.getString("user_last_name"));
                managerScreenStudentProfileDTO.setStudentEmail(resultSet.getString("user_email"));
                managerScreenStudentProfileDTO.setStudentRedemptionCode(resultSet.getString("student_redemption_code"));
                managerScreenStudentProfileDTO.setStudentSchoolCode(resultSet.getInt("student_school_code"));
                managerScreenStudentProfileDTO.setStudentLogInStatus(resultSet.getString("user_login_status"));
                managerScreenStudentProfileDTO.setStudentId(resultSet.getInt("user_id"));
                setImageToStudentList(managerScreenStudentProfileDTO);
                studentList.add(managerScreenStudentProfileDTO);
                i++;
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
        System.out.println("SIZE OF STUDENT LIST" + studentList.size());
        return studentList;
    }

    /***
     * Retrieves image of student and sets it to the student in the manager screen "view all students" table view.
     * @param managerScreenStudentProfileDTO
     * @return
     */
    private void setImageToStudentList(ManagerScreenStudentProfileDTO managerScreenStudentProfileDTO){

        Image img;
        img = retrieveUserImageFromDatabase(managerScreenStudentProfileDTO.getStudentId());
        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(img);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);
        managerScreenStudentProfileDTO.setStudentImageView(imageView);
    }

    /**
     * Pulls User image from database. Converst the bytes to an  image file.
     * @param userId
     * @return Image
     */
    public  Image retrieveUserImageFromDatabase(int userId)  {
        Image image = null;
        BufferedImage bImg;
        ByteArrayInputStream bis = null;

        try {

            preStatement = dbConn.prepareStatement(retrieveStudentImageFromDatabaseQuery);
            preStatement.setInt(1, userId);
            resultSet = preStatement.executeQuery();
            System.out.println(resultSet.getMetaData() + "JDSHFJKNBSKDHBFLHSDHJKDSFBDSHBF");
            byte[] imageBytes = resultSet.getBytes("profile_picture");
            bis = new ByteArrayInputStream(imageBytes); //get the bytes file
            bImg = ImageIO.read(bis); //convert to buffered image
            image = SwingFXUtils.toFXImage(bImg, null );    //convert buggered image to image
        }catch (SQLException | IOException e){
            e.printStackTrace();
        } finally {
            try{
                System.out.println("FINALLLLYYYY!!!!!!!!!!!!");

                assert preStatement != null;
                preStatement.close();
                assert resultSet != null;
                resultSet.close();
                assert bis != null;
                bis.close();

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    public   byte[] retrieveUserImageInBytes(int userId) throws SQLException {


            getConnection();
            byte[] imageBytes = null;
            ResultSet resultSet = null;
            PreparedStatement preStatement = null;
        try {
            preStatement = dbConn.prepareStatement(retrieveStudentImageFromDatabaseQuery);
            preStatement.setInt(1, userId);
            resultSet = preStatement.executeQuery();
            System.out.println(resultSet.getMetaData() + "JDSHFJKNBSKDHBFLHSDHJKDSFBDSHBF");
             imageBytes = resultSet.getBytes("profile_picture");

        }catch (SQLException e){
            e.printStackTrace();
        } finally {
            System.out.println("FINALLLLYYYY!!!!!!!!!!!!");

            assert preStatement != null;
                preStatement.close();
            assert resultSet != null;
            resultSet.close();

        }
        return imageBytes;
    }

    /**
     * Update the user information in the databse.
     * @param sUserName
     * @param img
     */
    public void updateUserImageIntoDatabase(String sUserName, Image img){
        ByteArrayOutputStream bytesOut = null;
        try {
            BufferedImage buf;
            buf = SwingFXUtils.fromFXImage(img, null);
             bytesOut = new ByteArrayOutputStream();
            //writing the image in memory
            ImageIO.write(buf, "png", bytesOut);
            byte[] bytes = bytesOut.toByteArray();

            preStatement = dbConn.prepareStatement(updateStudentImageIntoDatabase);
            preStatement.setBytes(1, bytes);
            preStatement.setString(2, sUserName);
            preStatement.executeUpdate();
        }catch (IOException e){
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                preStatement.close();
                assert bytesOut != null;
                bytesOut.close();
            } catch (SQLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Updates student Login status in database(Active or Suspended).
     * @param managerScreenStudentProfileDTO
     */
    public void updateStudentLoginStatus(ManagerScreenStudentProfileDTO managerScreenStudentProfileDTO){

        getConnection();

        try {
            preStatement = dbConn.prepareStatement(updateStudentLoginStatusQuery);
            preStatement.setString(1, managerScreenStudentProfileDTO.getSLoginStatus());
            preStatement.setInt(2, managerScreenStudentProfileDTO.getStudentId());
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

    /***
     * Takes a ManagerScreenStudentProfileDTO object, gets student id from then object and deletes the student based on the student id
     * @param managerScreenStudentProfileDTO
     */
    public void deleteStudent(ManagerScreenStudentProfileDTO managerScreenStudentProfileDTO){
        getConnection();
         final String deleteStudentQuery1 = "delete from ebm_bookissue where ebm_bookissue.user_id = ?";
         final String deleteStudentQuery2 = "delete from ebm_student where ebm_student.user_id = ?";
         final String deleteStudentQuery3 = "delete from ebm_userlogin where ebm_userlogin.user_id = ?";
         final String deleteStudentQuery4 = "delete from ebm_user where user_id = ?";

         PreparedStatement preStatement2;
         PreparedStatement preStatement3;
         PreparedStatement preStatement4;


        try{
        preStatement = dbConn.prepareStatement(deleteStudentQuery1);
        preStatement.setInt(1, managerScreenStudentProfileDTO.getStudentId());

        preStatement2 = dbConn.prepareStatement(deleteStudentQuery2);
        preStatement2.setInt(1, managerScreenStudentProfileDTO.getStudentId());

        preStatement3 = dbConn.prepareStatement(deleteStudentQuery3);
        preStatement3.setInt(1, managerScreenStudentProfileDTO.getStudentId());

        preStatement4 = dbConn.prepareStatement(deleteStudentQuery4);
        preStatement4.setInt(1, managerScreenStudentProfileDTO.getStudentId());


        preStatement.execute();
        preStatement2.execute();
        preStatement3.execute();
        preStatement4.execute();

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


}
