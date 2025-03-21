package org.qgtest.page.pages;

import org.qgtest.Main;
import org.qgtest.course.CourseManager;
import org.qgtest.page.Page;
import org.qgtest.page.PageType;
import org.qgtest.page.UserInput;
import org.qgtest.user.Student;
import org.qgtest.user.UserManager;

public class StudentPage extends Page {
    private final Student student;
    private final CourseManager courseManager;
    private final UserManager userManager;

    /**
     * 管理员的主页
     * @param userManager
     * @param courseManager
     */
    public StudentPage(UserManager userManager, CourseManager courseManager) {
        super("主页");
        this.userManager = userManager;
        this.courseManager = courseManager;
        this.student = (Student) userManager.getNowUser();
        setNext(PageType.MAIN);
    }

    @Override
    public void display() {
        new InfoCompletePage(userManager.getNowUser(), userManager).display();  // 完善信息
        Main.clearScreen();

        UserInput input;
        Page childPage;

        while (true) {
            printLn("1-查看个人信息");
            printLn("2-查看已选课程");
            printLn("3-查看可选课程");
            printLn("4-重置密码");
            printLn("5-退出账号");

            while (true) {
                printLn("请输入以上选项");
                input = userInput();
                if(input.isNumber()) break;
            }

            childPage = switch (input.number()) {
                case 1 -> new StudentInfoPage(student, true, userManager);
                case 2 -> new CourseListPage(courseManager, userManager, "我的课程", student.getCourses(), false);
                case 3 -> new CourseListPage(courseManager, userManager, "所有课程", courseManager.getCourses(), true);
                case 4 -> new ResetPasswordPage(userManager, true);
                case 5 -> new MainPage();
                default -> null;
            };

            if(childPage == null) continue;
            if(childPage instanceof MainPage) return;

            Main.clearScreen();
            printLn(childPage.getTitle());  // 显示子页面
            childPage.display();

            Main.clearScreen();
            printLn(getTitle());     // 重新显示本页面标题
        }
    }
}
