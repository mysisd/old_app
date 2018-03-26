package utils;

/**
 * Created by Administrator on 2018/1/2 0002.
 * 0
 */

public class UserData {
    private String userName;
    private String userPwd;
    private int userId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UserData(String userName, String userPwd) {
        this.userName = userName;
        this.userPwd = userPwd;
    }

    public UserData(String userPwd) {
        this.userPwd = userPwd;
    }

}
