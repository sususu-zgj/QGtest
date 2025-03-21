package org.qgtest;

import org.qgtest.course.CourseManager;
import org.qgtest.database.DatabaseManager;
import org.qgtest.page.Page;
import org.qgtest.page.pages.*;
import org.qgtest.user.UserManager;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    static final String path = "src/main/resources/config.properties";  // 配置文件的地址

    public static void main(String[] args) {
        DatabaseManager databaseManager = new DatabaseManager(path);    // 加载数据库
        if(!databaseManager.isConnected()) {
            printLn("数据库未正常连接！请检查配置文件");
            new Scanner(System.in).nextLine();
            return;
        }

        UserManager userManager = new UserManager(databaseManager); // 加载用户管理器
        try {
            userManager.loadSuperAdmin(path);   // 加载超级管理员
        } catch (IOException e) {
            printLn("超级管理员账号读取失败！请检查配置文件");
            new Scanner(System.in).nextLine();
            return;
        }

        CourseManager courseManager = new CourseManager(databaseManager, userManager);  // 加载课程管理器

        Page page = new MainPage(); // 初始页面
        while (page != null) {
            printLn(page.getTitle());   // 输出页面标题
            page.display(); // 显示页面信息
            page = switch (page.nextPage()) {   // 跳转到下一页面
                case MAIN -> new MainPage();    // 初始页面
                case LOGIN -> new LoginPage(userManager);   // 登录页
                case REGISTER -> new RegisterPage(userManager); // 注册页
                case STUDENT -> new StudentPage(userManager, courseManager); // 学生主页
                case ADMIN -> new AdminPage(userManager, courseManager);    // 管理员主页
                case SUPER -> new SuperAdminPage(userManager.getSuperAdmin(), courseManager, userManager);  // 超级管理员主页
                case RESET_PASSWORD -> new ResetPasswordPage(userManager, false);   // 密码重置页
                default -> null;    // 退出页面显示
            };
        }

        databaseManager.closePool();    // 关闭连接池
    }

    /**
     * 用于打印一行信息
     * @param message 要打印的信息
     */
    private static void printLn(String message) {
        System.out.println(message);
    }

    /**
     * 清屏，但是好像对idea的终端不起作用
     */
    public static void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {    //清屏 Plan B
            System.out.println("\033[H\033[2J");
            System.out.flush();
        }
    }
}