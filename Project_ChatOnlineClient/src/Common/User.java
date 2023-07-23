package Common;

import java.io.Serializable;

/**
 * 用户对象
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L; //增加兼容性
    private String userID; //用户名
    private String passWord; //密码

    public User() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
