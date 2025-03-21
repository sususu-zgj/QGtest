package org.qgtest.user;

/**
 * 一个用户类，被具体用户类型继承
 */
public class User {
    private final String username;  // 用户名
    private final boolean admin;    // 是否是管理员
    private String password;    // 密码
    private String phone;   // 联系电话

    public User(String username, String password, boolean admin) {
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
