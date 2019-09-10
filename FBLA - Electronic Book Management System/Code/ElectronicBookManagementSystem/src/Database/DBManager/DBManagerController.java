package Database.DBManager;

import Database.DataTransferObjects.UserDTO;
import Database.DataTransferObjects.WeeklyReportDTO;
import Database.DatabaseConnection;
import LogIn.Session.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DBManagerController {


    private static Connection dbConn;
    private static PreparedStatement preStatement;
    private static ResultSet resultSet;
    private static final String weeklyBookIssueSelectQuery = "SELECT a.user_first_name, a.user_last_name, a.user_id, b.book_title, c.book_issue_date FROM ebm_user a, ebm_book b, ebm_bookissue c WHERE  a.user_id = c.user_id AND b.book_id = c.book_id AND  c.book_issue_date between  DATE( 'now', 'weekday 0', '-7 day' ) and  DATE( 'now', 'weekday 0' ) ";
    private static final String chartQuery = "select a.book_genre, count(*) Issued from ebm_book a, ebm_bookissue b where a.book_id = b.book_id and b.book_issue_date group by a.book_genre";
    private static final String userProfileUpdateQuery = "UPDATE ebm_user set user_first_name = ?, user_last_name = ?, user_email = ? WHERE ebm_user.user_id = ?";
    private static final String userLoginUpdateQuery = "UPDATE ebm_userlogin set user_login_password = ? WHERE ebm_userlogin.user_id = ?";
    private static final String getManagerDetailsQuery = "SELECT a.user_first_name, a.user_last_name, a.user_dob, a.user_email FROM ebm_user a WHERE  a.user_id = ? ";


    /**
     * Establishes connection to the database.
     */
    private void getConnection() {
        if (dbConn == null) {
            dbConn = DatabaseConnection.getDbConnection();
        }
    }


    /**
     * Retrieves student name, the book they issued, and the date they issued them on from database. Sets this information in WeeklyReportDTO object.
     * Information used in manager weekly report.
     *
     * @return studentBookIssueList
     */
    public ObservableList<WeeklyReportDTO> getIssueReportList() {

        getConnection();
        ObservableList<WeeklyReportDTO> studentBookIssueList = FXCollections.observableArrayList();

        try {
            preStatement = dbConn.prepareStatement(weeklyBookIssueSelectQuery);
            resultSet = preStatement.executeQuery();

            while (resultSet.next()) {
                WeeklyReportDTO weeklyReportDTO = new WeeklyReportDTO();
                weeklyReportDTO.setFirstName(resultSet.getString("user_first_name"));
                weeklyReportDTO.setLastName(resultSet.getString("user_last_name"));
                weeklyReportDTO.setBookTitle(resultSet.getString("book_title"));
                weeklyReportDTO.setIssueDate(resultSet.getString("book_issue_date"));
                weeklyReportDTO.setFullName(weeklyReportDTO.getFirstName() + ", " + weeklyReportDTO.getLastName());
                studentBookIssueList.add(weeklyReportDTO);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return studentBookIssueList;
    }


    /**
     * Retrieves book genres and how many books issued in those categories from database and sets them to WeeklyReportDTO object.
     * (Example- Fiction -5 books issued)
     *
     * @return
     */
    public ObservableList<WeeklyReportDTO> getChartDataList() {

        getConnection();
        ObservableList<WeeklyReportDTO> chartDataList = FXCollections.observableArrayList();


        try {
            preStatement = dbConn.prepareStatement(chartQuery);
            resultSet = preStatement.executeQuery();

            while (resultSet.next()) {
                WeeklyReportDTO weeklyReportDTO = new WeeklyReportDTO();
                weeklyReportDTO.setBookGenre(resultSet.getString("book_genre"));
                weeklyReportDTO.setIssueCount(resultSet.getInt("Issued"));
                chartDataList.add(weeklyReportDTO);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return chartDataList;
    }


    /**
     * Retrieves Manager profile information from database and sets them to userDTO object.
     * Returns the userDTO object
     *
     * @param userId
     * @return userDTO
     * @throws SQLException
     */
    public UserDTO getManagerProfile(int userId) throws SQLException {
        getConnection();
        try {
            preStatement = dbConn.prepareStatement(getManagerDetailsQuery);
            preStatement.setInt(1, userId);
            resultSet = preStatement.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        UserDTO userDTO = new UserDTO();

        while (resultSet.next()) {


            userDTO.setUser_first_name(resultSet.getString("user_first_name"));
            userDTO.setUser_last_name(resultSet.getString("user_last_name"));
            userDTO.setUser_dob(resultSet.getString("user_dob"));
            userDTO.setUser_email(resultSet.getString("user_email"));

        }

        return userDTO;
    }


    /**
     * Updates manager profile information in database. This method is executed when manager clicks "update info" button on My Profile screen.
     *
     * @param userDTO
     */
    public void updateManagerProfile(UserDTO userDTO) {


        getConnection();


        try {
            preStatement = dbConn.prepareStatement(userProfileUpdateQuery);
            preStatement.setString(1, userDTO.getUser_first_name());
            preStatement.setString(2, userDTO.getUser_last_name());
            preStatement.setString(3, userDTO.getUser_email());
            preStatement.setInt(4, UserSession.getUserLogInDTO().getUserId());
            preStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
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
            } finally {
                try {
                    preStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();

                }

            }

    }
}

