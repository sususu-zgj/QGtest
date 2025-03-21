package org.qgtest.page.pages;

import org.qgtest.Main;
import org.qgtest.course.CourseManager;
import org.qgtest.page.Page;
import org.qgtest.page.PageType;
import org.qgtest.page.UserInput;
import org.qgtest.user.SuperAdmin;
import org.qgtest.user.UserManager;


public class SuperAdminPage extends Page {
    private final SuperAdmin superAdmin;
    private final CourseManager courseManager;
    private final UserManager userManager;

    /**
     * 超级管理员的主页
     * @param superAdmin
     * @param courseManager
     * @param userManager
     */
    public SuperAdminPage(SuperAdmin superAdmin, CourseManager courseManager, UserManager userManager) {
        super("主页");
        setNext(PageType.MAIN);
        this.superAdmin = superAdmin;
        this.userManager = userManager;
        this.courseManager = courseManager;
    }


    @Override
    public void display() {
        UserInput input;

        while (true) {
            printLn("1-查看课程更改申请");
            printLn("2-查看管理员注册码");
            printLn("3-退出");
            printLn("请输入以上选项以选择");
            input = userInput();

            if(!input.isNumber()) continue;

            Page childPage = switch (input.number()) {  // 判断用户的选择
                case 1 -> new CourseChangeApplicationPage(courseManager, userManager);
                case 2 -> new AdminCorePage(userManager);
                case 3 -> new MainPage();
                default -> null;
            };

            if(childPage == null) continue; // 没有选择以上选项，重新输入
            if(childPage instanceof MainPage) return;   // 选择退出

            Main.clearScreen(); // 显示子页面
            printLn(childPage.getTitle());
            childPage.display();

            Main.clearScreen();
            printLn(getTitle());    // 重新显示本页面的标题
        }
    }
}
