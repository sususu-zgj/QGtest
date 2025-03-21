package org.qgtest.page.pages;

import org.qgtest.Main;
import org.qgtest.course.CourseManager;
import org.qgtest.page.Page;
import org.qgtest.page.PageType;
import org.qgtest.page.UserInput;
import org.qgtest.user.Admin;
import org.qgtest.user.UserManager;

/**
 * 管理员主页
 */
public class AdminPage extends Page {
    private final UserManager userManager;
    private final CourseManager courseManager;
    private final Admin admin;

    public AdminPage(UserManager userManager, CourseManager courseManager) {
        super("主页");
        this.userManager = userManager;
        this.courseManager = courseManager;
        admin = (Admin) userManager.getNowUser();
    }

    @Override
    public void display() {
        setNext(PageType.MAIN);
        new InfoCompletePage(userManager.getNowUser(), userManager).display();
        Main.clearScreen();

        UserInput input;
        Page childPage;

        while (true) {
            printLn("当前登录账号：" + admin.getUsername());
            printLn("手机号：" + admin.getPhone());
            printLn("1-修改手机号");
            printLn("2-查看所有学生");
            printLn("3-查看所有课程");
            printLn("4-重置密码");
            printLn("5-退出账号");

            while (true) {
                printLn("请输入以上选项");
                input = userInput();
                if(input.isNumber()) break;
            }

            childPage = switch (input.number()) {   // 用户的选择
                case 1 -> resetPhone();
                case 2 -> new StudentListPage(userManager, userManager.getStudents());
                case 3 -> new CourseListPage(courseManager, userManager, "所有课程", courseManager.getCourses(), false);
                case 4 -> new ResetPasswordPage(userManager, true);
                case 5 ->new MainPage();
                default -> null;
            };

            if(childPage == null) continue;
            if(childPage instanceof MainPage) return;

            Main.clearScreen();
            printLn(childPage.getTitle());  // 打开子页面
            childPage.display();

            Main.clearScreen();
            printLn(getTitle());    // 重新显示本页面标题
        }
    }

    /**
     * 选择重置电话号码
     * @return
     */
    private Page resetPhone() {
        admin.setPhone(null);
        return new InfoCompletePage(userManager.getNowUser(), userManager);
    }
}
