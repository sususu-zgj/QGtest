package org.qgtest.page.pages;

import org.qgtest.page.Page;
import org.qgtest.page.PageType;
import org.qgtest.page.UserInput;

/**
 * 程序的初始页面
 */
public class MainPage extends Page {
    public MainPage() {
        super("学生信息管理系统");
    }

    @Override
    public void display() {
        UserInput input;

        printLn("1-登录");
        printLn("2-注册");
        printLn("3-忘记密码");
        printLn("4-退出");

        while (true) {
            printLn("请选择以上选项");
            input = userInput();
            if(input.isNumber()) switch (input.number()) {  // 选择页面
                case 1: setNext(PageType.LOGIN); return;
                case 2: setNext(PageType.REGISTER); return;
                case 3: setNext(PageType.RESET_PASSWORD); return;
                case 4: setNext(PageType.QUIT); return;
            }
        }
    }

}
