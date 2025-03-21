package org.qgtest.page.pages;

import org.qgtest.Main;
import org.qgtest.course.Course;
import org.qgtest.page.Page;
import org.qgtest.page.UserInput;
import org.qgtest.user.Student;
import org.qgtest.user.UserManager;

/**
 * 用于展示单个学生信息
 */
public class StudentInfoPage extends Page {
    private final Student student;
    private final boolean isNowUser;
    private final UserManager userManager;

    public StudentInfoPage(Student student, boolean isNowUser, UserManager userManager) {
        super(student.getName() + "的个人信息");
        this.student = student;
        this.isNowUser = isNowUser;
        this.userManager = userManager;
    }

    @Override
    public void display() {
        if(isNowUser) nowUser();
        else admin();
    }

    /**
     * 学生查看自己的信息
     */
    private void showToStudent() {
        printLn("身份：    \t" + "学生");
        printLn("学号：    \t" + student.getUsername());
        printLn("1-姓名：  \t" + student.getName());
        printLn("2-年龄：  \t" + student.getAge());
        printLn("3-性别：  \t" + student.getSex());
        printLn("4-联系电话：\t" + student.getPhone());
        printLn("5-年级：   \t" + (switch (student.getLevel()) {
            case 1 -> "大一";
            case 2 -> "大二";
            case 3 -> "大三";
            case 4 -> "大四";
            default -> "?";
        }));
    }

    /**
     * 学生查看自己的信息
     */
    private void nowUser() {
        UserInput input;

        showToStudent();
        while (true) {
            printLn("请输入以上选项以修改信息，或输入“back”返回上一页");
            input = userInput();

            if(input.isBack()) return;
            if(!input.isNumber()) continue;

            switch (input.number()) {   // 选择修改信息
                case 1: student.setName(null); break;
                case 2: student.setAge(0); break;
                case 3: student.setSex(null); break;
                case 4: student.setPhone(null); break;
                case 5: student.setLevel(0); break;
                default: continue;
            }
            Main.clearScreen();
            new InfoCompletePage(student, userManager).display();   // 到修改页面

            Main.clearScreen();
            showToStudent();
        }
    }

    /**
     * 管理员查看学生信息
     */
    private void showToAdmin() {
        printLn("身份：    \t" + "学生");
        printLn("学号：    \t" + student.getUsername());
        printLn("姓名：    \t" + student.getName());
        printLn("年龄：    \t" + student.getAge());
        printLn("性别：    \t" + student.getSex());
        printLn("1-联系电话：\t" + student.getPhone());
        printLn("2-年级：   \t" + (switch (student.getLevel()) {
            case 1 -> "大一";
            case 2 -> "大二";
            case 3 -> "大三";
            case 4 -> "大四";
            default -> "?";
        }));
        printLn("所选课程：");
        int i = 1;
        printLn("\t课程名称\t教学教师\t学时\t学分\t最大人数\t当前人数\t课程状态");
        for(Course course : student.getCourses()) {     // 展示选课信息
            printLn("\t" + i++ + "-" + course.getName() + "\t" +
                    course.getTeacher() + "\t" + course.getCreditHour() + "\t" +
                    course.getPoint() + "\t" + course.getMaxNumber() + "\t" +
                    course.getNowNumber() + "\t" + (course.isOpen() ? "已开课" : "未开课"));
        }
    }

    /**
     * 管理员查看学生信息
     */
    private void admin() {
        UserInput input;

        showToAdmin();
        while (true) {
            printLn("请输入以上选项以修改信息，或输入“back”返回上一页");
            input = userInput();

            if(input.isBack()) return;
            if(!input.isNumber()) continue;

            switch (input.number()) {
                case 1: student.setPhone(null); break;
                case 2: student.setLevel(0); break;
                default: continue;
            }
            Main.clearScreen();
            new InfoCompletePage(student, userManager).display();

            Main.clearScreen();
            showToAdmin();
        }
    }
}
