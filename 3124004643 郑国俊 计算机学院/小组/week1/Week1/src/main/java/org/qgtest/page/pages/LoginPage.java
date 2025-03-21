package org.qgtest.page.pages;

import org.qgtest.page.Page;
import org.qgtest.page.PageType;
import org.qgtest.page.UserInput;
import org.qgtest.user.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * 用户登录页
 */
public class LoginPage extends Page {
    private final UserManager usersManager;
    private final HashMap<String, String> passwords;

    public LoginPage(@NotNull UserManager manager) {
        super("登录页");
        this.usersManager = manager;
        this.passwords = manager.getPasswords();
    }

    @Override
    public void display() {
        UserInput input;
        String username;
        String password;

        while (true) {
            printLn("请输入学/工号，若要取消登录请输入“back”");
            input = userInput();
            if (input.isBack()) {
                setNext(PageType.MAIN);
                return;
            }
            username = input.word();

            printLn("请输入密码");
            input = userInput();
            if (input.isBack()) {
                setNext(PageType.MAIN);
                return;
            }
            password = input.word();

            if(login(username, password)) {
                usersManager.setNowUser(username);
                break;
            }
            printLn("账号或密码错误");
        }
    }

    private boolean login(String username, String password) {   // 用户登录
        if(passwords.containsKey(username) && passwords.get(username).equals(password)) {
            User user =  usersManager.setNowUser(username);
            if(user.isAdmin()) {
                if(user instanceof SuperAdmin) setNext(PageType.SUPER);
                else setNext(PageType.ADMIN);
            }
            else setNext(PageType.STUDENT);
            return true;
        }
        return false;
    }
}
