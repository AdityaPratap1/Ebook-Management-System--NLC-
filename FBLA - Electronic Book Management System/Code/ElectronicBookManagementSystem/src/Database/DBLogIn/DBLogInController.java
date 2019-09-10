package Database.DBLogIn;

import Database.DataTransferObjects.UserLogInDTO;
import Database.DatabaseConnection;

import java.sql.*;



public class DBLogInController {
    private static Connection dbConn;

    //Query brings the user's (who is trying to log in) username, password, user role, user id, and user login status by their username typed in the username field in the login screen.
    private static final String loginQuery = "SELECT a.user_login_name, a.user_login_password, b.user_type , a.user_id, a.user_login_status FROM ebm_userlogin a, ebm_user b WHERE a.user_id = b.user_id AND a.user_login_name = ";
    private static PreparedStatement preStatement;
    private static  ResultSet resultSet;


    /**
     * takes username and password and passes them into the executeValidateLogin method.
     * Returns the userLogInDTO object.
     * @param username
     * @return userLoginDTO
     */
    public UserLogInDTO validateLogin(String username){

        getConnection();
        return executeLogInValidation(username);

    }

    /**
     * Establishes connection to database
     */

    private void getConnection(){
        if(dbConn==null) {
            dbConn = DatabaseConnection.getDbConnection();
        }
    }

    /**
     * takes username and verifies if username exists, and if it does, brings it and the password associated with it and sets it to the userLogInDTO object.
     * @param username
     * @return userLogInDTO
     *
     */
    private UserLogInDTO executeLogInValidation(String username){
        UserLogInDTO userLogInDTO = null;
        try {
            String query= loginQuery+"\'"+username+"\'";
            preStatement= dbConn.prepareStatement(query);
            resultSet = preStatement.executeQuery();

            if(resultSet.isBeforeFirst()){
                userLogInDTO = new UserLogInDTO();

                ResultSetMetaData rmd = resultSet.getMetaData();

                while(resultSet.next()){

                    int columnCount= rmd.getColumnCount();
                    userLogInDTO.setUsername(resultSet.getString(1));
                    userLogInDTO.setPassword(resultSet.getString(2));
                    userLogInDTO.setUserType(resultSet.getString(3));
                    userLogInDTO.setUserId(resultSet.getInt("user_id"));
                    userLogInDTO.setUserLoginStatus(resultSet.getString("user_login_status"));
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {

                preStatement.close();
                resultSet.close();

            }catch (SQLException e){
                e.printStackTrace();
            }
        }


        return userLogInDTO;

    }

    /**
     * Retrieves students redemption code by their user id and returns it.
     * @param userId
     * @return redemption code
     */
    public int retriveStudentRedemptionCode(int userId)  {
        getConnection();
        int code = 0;
        String redemptionCodeQuery = "SELECT student_redemption_code From ebm_student WHERE user_id = ?";
        try {
            preStatement = dbConn.prepareStatement(redemptionCodeQuery);
            preStatement.setInt(1, userId);
            resultSet = preStatement.executeQuery();
            while (resultSet.next()){
                try {
                    code = resultSet.getInt("student_redemption_code");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return code;
    }


}
