package Database.DataTransferObjects;

public class UserLogInDTO {
    private String username;
    private String password;
    private String userType;
    private String userLoginStatus;
    private int userLoginId;
    private int userId;


    private int redemptionCode;

    public UserLogInDTO() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserLoginStatus() {
        return userLoginStatus;
    }

    public void setUserLoginStatus(String userLoginStatus) {
        this.userLoginStatus = userLoginStatus;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRedemptionCode() {
        return redemptionCode;
    }

    public void setRedemptionCode(int redemptionCode) {
        this.redemptionCode = redemptionCode;
    }
}
