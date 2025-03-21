package org.qgtest.page.pages;

import org.qgtest.course.Course;
import org.qgtest.course.CourseManager;
import org.qgtest.page.Page;
import org.qgtest.page.UserInput;
import org.qgtest.user.Student;
import org.qgtest.user.SuperAdmin;
import org.qgtest.user.UserManager;

import java.util.ArrayList;

/**
 * 单个课程的信息的详细展示页
 */
public class CourseInfoPage extends Page {
    private final UserManager userManager;
    private final CourseManager courseManager;
    private final Course course;

    public CourseInfoPage(Course course, UserManager userManager, CourseManager courseManager) {
        super("课程详情");
        this.userManager = userManager;
        this.courseManager = courseManager;
        this.course = course;
    }

    @Override
    public void display() {
        if(userManager.getNowUser() instanceof SuperAdmin) superAdmin();
        else admin();
    }

    /**
     * 当用户是管理员时展示内容和逻辑
     */
    private void admin() {
        while (true) {
            UserInput input;
            printLn("课程ID: \t" + course.getId());
            printLn("1-课程名称:\t" + course.getName());
            printLn("2-任教教师:\t" + course.getTeacher());
            printLn("3-学时: \t" + course.getCreditHour());
            printLn("4-学分: \t" + course.getPoint());
            printLn("最大人数:\t" + course.getMaxNumber());
            printLn("当前人数:\t" + course.getNowNumber());
            printLn("课程状态:\t" + (course.isOpen() ? "已开课" : "未开课"));

            printLn("请输入以上序号修改信息，或“open”向超级管理员发送开课申请，“del”向超级管理员发送删除申请");
            printLn("若要查询选择该课程的学生，请输入“students”");
            printLn("若要返回，请输入“back”");

            while (true) {
                input = userInput();

                if(input.isBack()) return;  // 是否返回
                if(input.isNumber()) {  // 判断选择
                    switch (input.number()) {
                        case 1 : course.setName(null); break;
                        case 2 : course.setTeacher(null); break;
                        case 3 : course.setCreditHour(0); break;
                        case 4 : course.setPoint(0); break;
                        default: continue;
                    }

                    new CourseCompletePage(userManager, courseManager, course).display();
                    break;
                }
                if(input.word().equalsIgnoreCase("open")) {     // 发送开课申请
                    if(course.isOpen()) return;
                    userManager.getSuperAdmin().addApplication("OPEN", course.getId());
                    return;
                }
                if(input.word().equalsIgnoreCase("del")) {  // 发送删课申请
                    if(course.isOpen()) {
                        printLn("已开的课程无法删除！");
                        return;
                    }
                    userManager.getSuperAdmin().addApplication("DELETE", course.getId());
                    return;
                }
                if(input.word().equalsIgnoreCase("student")) {  // 查看选择该科的学生
                    ArrayList<Student> students = new ArrayList<>();
                    for(Student student : userManager.getStudents()) {
                        if(student.getCourses().contains(course)) students.add(student);
                    }

                    new StudentListPage(userManager, students).display();   // 展示学生
                    return;
                }
            }
        }
    }

    /**
     * 超级管理员查看详情时的逻辑
     */
    private void superAdmin() {
        while (true) {
            UserInput input;
            printLn("课程ID:  \t" + course.getId());
            printLn("课程名称:\t" + course.getName());
            printLn("任教教师:\t" + course.getTeacher());
            printLn("学时:    \t" + course.getCreditHour());
            printLn("学分:    \t" + course.getPoint());
            printLn("最大人数:\t" + course.getMaxNumber());
            printLn("当前人数:\t" + course.getNowNumber());
            printLn("课程状态:\t" + (course.isOpen() ? "已开课" : "未开课"));

            printLn("若要返回，请输入“back”");

            while (true) {
                input = userInput();

                if (input.isBack()) return;
            }
        }
    }
}
