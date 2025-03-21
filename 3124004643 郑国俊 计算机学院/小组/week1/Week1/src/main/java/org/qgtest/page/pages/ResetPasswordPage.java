package org.qgtest.page.pages;

import org.qgtest.page.Page;
import org.qgtest.page.PageType;
import org.qgtest.page.UserInput;
import org.qgtest.user.User;
import org.qgtest.user.UserManager;

/**
 * 重置密码页
 */
public class ResetPasswordPage extends Page {
    private final UserManager userManager;
    private final boolean logined;

    public ResetPasswordPage(UserManager userManager, boolean logined) {
        super("密码重置");
        this.userManager = userManager;
        this.logined = logined;
        setNext(PageType.MAIN);
    }


    @Override
    public void display() {
        if(logined) logined();
        else disLogined();
    }

    /**
     * 已登录用户重置密码
     */
    private void logined() {
        String newPassword = RegisterPage.buildPassword();
        if(newPassword == null) {
            setNext(PageType.MAIN);
            return;
        }

        User user = userManager.getNowUser();
        user.setPassword(newPassword);
        userManager.getPasswords().put(user.getUsername(), newPassword);
        userManager.saveTo(user);
    }

    /**
     * 为登录重置密码
     */
    private void disLogined() {
        UserInput input;
        String username;
        String newPassword;
        String phone;
        User user;

        while (true) {
            printLn("请输入您的学/工号，或输入“back”取消重置");
            input = userInput();
            if(input.isBack()) return;
            username = input.word();

            printLn("请输入该账号绑定的手机号码");
            input = userInput();
            if(input.isBack()) return;
            phone = input.word();

            user = userManager.getUser(username);
            if(user != null && user.getPhone().equals(phone)) break;
            printLn("未能找到该用户或电话号码错误");
        }

        newPassword = RegisterPage.buildPassword();
        if(newPassword == null) {
            setNext(PageType.RESET_PASSWORD);
            return;
        }

        userManager.setNowUser(null);
        user.setPassword(newPassword);
        userManager.getPasswords().put(username, newPassword);
        userManager.saveTo(user);
    }
}
