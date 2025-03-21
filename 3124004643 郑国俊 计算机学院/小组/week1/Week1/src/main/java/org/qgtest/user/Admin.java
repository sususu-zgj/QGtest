package org.qgtest.user;

/**
 * 管理员，没有额外信息，主要用于将管理员和玩家分离开
 */
public class Admin extends User{

    public Admin(String username, String password) {
        super(username, password, true);
    }

}
