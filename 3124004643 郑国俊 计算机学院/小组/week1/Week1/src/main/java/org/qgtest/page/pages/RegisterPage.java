package org.qgtest.page.pages;

import org.qgtest.page.Page;
import org.qgtest.page.PageType;
import org.qgtest.page.UserInput;
import org.qgtest.user.UserManager;

/**
 * 用于注册新用户
 */
public class RegisterPage extends Page {
    private UserManager manager;

    public RegisterPage(UserManager manager) {
        super("注册页");
        this.manager = manager;
    }


    @Override
    public void display() {
        UserInput input;

        printLn("1-注册为管理员");
        printLn("2-注册为学生");

        while (true) {
            printLn("请输入以上选项，或输入“back”返回上一页");
            input = userInput();

            if(input.isBack()) {
                setNext(PageType.MAIN);
                break;
            }
            else if(input.isNumber()) {
                switch (input.number()) {
                    case 1: registerAdmin(); break;
                    case 2: registerStudent(); break;
                    default: continue;
                }
                break;
            }
        }
    }

    /**
     * 注册为管理员
     */
    private void registerAdmin() {
        UserInput input;
        String core;
        String username;
        String password;

        while (true) {
            printLn("请输入管理员注册码");
            input = userInput();
            if(input.isBack()) {
                setNext(PageType.REGISTER);
                return;
            }
            core = input.word();

            while (true) {
                printLn("请输入工号，长度为10位，且以412开头。或输入“back”重新选择注册选项");
                input = userInput();
                if(input.isBack()) {
                    setNext(PageType.REGISTER);
                    return;
                }
                username = input.word();

                if(username.matches("^[0-9]{10}$") && username.startsWith("412")) break;
            }

            password = buildPassword();
            if(password == null) {  // 用户取消输入新密码
                setNext(PageType.REGISTER);
                return;
            }

            if(!manager.getAdminCores().contains(core)) {
                printLn("管理员注册码错误！");
                continue;
            }
            if(manager.addUser(username, password, true)) break;
            printLn("该用户已存在！");
        }

        setNext(PageType.MAIN);
    }

    /**
     * 注册为学生
     */
    private void registerStudent() {
        UserInput input;
        String username;
        String password;

        while (true) {
            while (true) {
                printLn("请输入学号，长度为10位，且以312开头。或输入“back”重新选择注册选项");
                input = userInput();
                if(input.isBack()) {
                    setNext(PageType.REGISTER);
                    return;
                }
                username = input.word();

                if(username.matches("^[0-9]{10}$") && username.startsWith("312")) break;
            }

            password = buildPassword();
            if(password == null) {  // 用户取消输入密码
                setNext(PageType.REGISTER);
                return;
            }

            if(manager.addUser(username, password, false)) break;
            printLn("该用户已存在！");
        }

        setNext(PageType.MAIN);
    }

    /**
     * 由用户生成一条密码
     * @return 生成的密码
     */
    protected static String buildPassword() {
        UserInput input;
        String password;

        while (true) {
            printLn("请输入密码，只能为数字或大小写字母，长度6~16");
            input = userInput();
            if(input.isBack()) return null;
            password = input.word();
            if(!password.matches("^[0-9a-zA-Z]{6,16}$")) continue;

            printLn("请再次输入密码");
            input = userInput();
            if(input.isBack()) return null;
            if(!input.word().equals(password)) {
                printLn("前后密码不一致");
                continue;
            }
            break;
        }
        return password;
    }
}
