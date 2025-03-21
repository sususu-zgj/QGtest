package org.qgtest.page.pages;

import org.qgtest.course.Course;
import org.qgtest.course.CourseManager;
import org.qgtest.page.Page;
import org.qgtest.page.UserInput;
import org.qgtest.user.Student;
import org.qgtest.user.UserManager;

import java.util.ArrayList;

/**
 * 展示课程列表的页面，单页最多展示10条信息
 */
public class CourseListPage extends Page {
    private final UserManager userManager;
    private final CourseManager courseManager;
    private final ArrayList<Course> courses;
    private final int maxListPage;
    private final int nowListPage;
    private final boolean select;   // 如果用户是学生，其是否正在选课

    public CourseListPage(CourseManager courseManager, UserManager userManager, String title, ArrayList<Course> courses, int nowListPage, boolean select) {
        super(title);

        this.userManager = userManager;
        this.courseManager = courseManager;
        this.courses = courses;
        this.nowListPage = nowListPage;
        this.maxListPage = courses.size() / 10 + ((courses.size() % 10 != 0) ? 1 : 0);
        this.select = select;
    }

    public CourseListPage(CourseManager courseManager, UserManager userManager, String title, ArrayList<Course> courses, boolean select) {
        super(title);

        this.userManager = userManager;
        this.courseManager = courseManager;
        this.courses = courses;
        this.maxListPage = courses.size() / 10 + ((courses.size() % 10 != 0) ? 1 : 0);
        this.nowListPage = (maxListPage > 0) ? 1 : 0;
        this.select = select;
    }

    @Override
    public void display() {
        printLn("课程名称\t教学教师\t学时\t学分\t最大人数\t当前人数\t课程状态");
        for(int i = (nowListPage - 1) * 10; i >= 0 && i < nowListPage * 10 && i < courses.size(); i++) {    // 展示信息
            Course course = courses.get(i);
            printLn((i - (nowListPage - 1)*10 + 1) + "-" + course.getName() + "\t" +
                    course.getTeacher() + "\t" + course.getCreditHour() + "\t" +
                    course.getPoint() + "\t" + course.getMaxNumber() + "\t" +
                    course.getNowNumber() + "\t" + (course.isOpen() ? "已开课" : "未开课"));
        }
        printLn("第" + nowListPage + "页，共" + maxListPage + "页");

        if(userManager.getNowUser() instanceof Student student) {   // 不同用户使用不同交互逻辑
            if(select) {
                selectCourses(student);
            }
            else selectedCourses();
        }
        else {
            changeCourses();
        }
    }

    /**
     * 学生选课逻辑
     * @param student
     */
    private void selectCourses(Student student) {
        UserInput input;
        UserInput listGoto;
        ArrayList<Course> studentCourses = student.getCourses();
        printLn("输入对应序号以开始选课。已开课的课程和人数已满的课程无法选择。");
        printLn("每位学生最多可选5门，已选" + studentCourses.size() + "门");
        printLn("输入“<”，“>”或“goto 页数”可切换列表的页数。若要返回，请输入“back”");

        while (true) {
            input = userInput();
            listGoto = input.getGoto(nowListPage);
            if(input.isBack()) return;  // 是否返回
            if(listGoto != null) {      // 是否跳转
                if(listGoto.number() > 0 && listGoto.number() <= maxListPage) {
                    new CourseListPage(courseManager, userManager, getTitle(), courses, listGoto.number(), true).display();
                    return;
                }
            }
            if(input.isNumber() && input.number() > 0 && nowListPage > 0 && input.number() <= courses.size() - (nowListPage - 1) * 10) {    // 对用户的选择进行判断
                Course course = courses.get(input.number() + (nowListPage - 1) * 10 - 1);
                int nowNum = course.getNowNumber();

                if(studentCourses.size() >= 5) {
                    printLn("您已够5门，无法再选");
                    continue;
                }
                if(course.isOpen()) {
                    printLn("已开课的课程无法选择！");
                    continue;
                }
                if(course.getNowNumber() >= course.getMaxNumber()) {
                    printLn("该课程已满人！");
                    continue;
                }
                if(studentCourses.contains(course)) {
                    printLn("您已经选了这门课了！");
                    continue;
                }

                course.setNowNumber(nowNum + 1);
                courseManager.courseSaveTo(course);
                studentCourses.add(course);
                courseManager.studentCoursesSaveTo(student);

                break;
            }
            printLn("无效的输入，请输入课程前的序号！");
        }
    }

    /**
     * 学生查看自己的选课
     */
    private void selectedCourses() {
        UserInput input;
        printLn("若要退选课程，请输入对应序号。已开课的课程不可退选。");
        printLn("若要返回，请输入“back”");

        while (true) {
            input = userInput();
            if(input.isBack()) return;
            if(input.isNumber() && input.number() > 0 && input.number() <= courses.size()) {
                Course course = courses.get(input.number());
                int nowNum = course.getNowNumber();

                if(course.isOpen()) {
                    printLn("已开课的课程无法退选！");
                    continue;
                }

                course.setNowNumber(nowNum - 1);
                courseManager.courseSaveTo(course);
                courses.remove(course);
                courseManager.studentCoursesSaveTo((Student) userManager.getNowUser());

                break;
            }
            printLn("无效的输入，请输入课程前的序号！");
        }
    }

    /**
     * 管理员查看所有的课程信息
     */
    private void changeCourses() {
        UserInput input;
        UserInput listGoto;
        printLn("若要修改对应课程，请输入课程前的序号。输入“add”可添加新课程。");
        printLn("输入“<”，“>”或“goto 页数”可切换列表的页数。若要返回，请输入“back”");

        while (true) {
            input = userInput();
            listGoto = input.getGoto(nowListPage);

            if(input.isBack()) return;
            if(listGoto != null) {
                if(listGoto.number() > 0 && listGoto.number() <= maxListPage) {
                    new CourseListPage(courseManager, userManager, getTitle(), courses, listGoto.number(), true).display();
                    return;
                }
            }
            if(input.isNumber() && input.number() > 0 && nowListPage > 0 && input.number() <= courses.size() - (nowListPage - 1) * 10) {
                Course course = courses.get(input.number() + (nowListPage - 1) * 10 - 1);

                new CourseInfoPage(course, userManager, courseManager).display();
                return;
            }
            if(input.word().equalsIgnoreCase("add")) {
                new CourseCompletePage(userManager, courseManager, new Course()).display();
                return;
            }
            printLn("无效的输入，请输入课程前的序号！");
        }
    }
}
