package org.qgtest.page.pages;

import org.qgtest.course.Course;
import org.qgtest.course.CourseManager;
import org.qgtest.page.CourseChangeApplication;
import org.qgtest.page.Page;
import org.qgtest.page.UserInput;
import org.qgtest.user.Student;
import org.qgtest.user.SuperAdmin;
import org.qgtest.user.UserManager;

import java.util.ArrayList;

/**
 * 超级管理员处理申请的页面
 */
public class CourseChangeApplicationPage extends Page {
    private final CourseManager courseManager;
    private final UserManager userManager;
    private final SuperAdmin superAdmin;    // 当前超级管理员
    private final ArrayList<CourseChangeApplication> applications;  // 申请列表

    private int maxListPage;    // 最大页数
    private int nowListPage;    // 当前页数

    public CourseChangeApplicationPage(CourseManager courseManager, UserManager userManager) {
        super("课程改动申请");
        this.courseManager = courseManager;
        this.userManager = userManager;
        this.superAdmin = userManager.getSuperAdmin();
        this.applications = superAdmin.getApplications();
        this.maxListPage = applications.size() / 10 + ((applications.size() % 10 != 0) ? 1 : 0);
        this.nowListPage = (maxListPage > 0) ? 1 : 0;
    }

    public CourseChangeApplicationPage(CourseManager courseManager, UserManager userManager, int nowListPage) {
        super("课程改动申请");
        this.courseManager = courseManager;
        this.userManager = userManager;
        this.superAdmin = userManager.getSuperAdmin();
        this.applications = superAdmin.getApplications();
        this.maxListPage = applications.size() / 10 + ((applications.size() % 10 != 0) ? 1 : 0);
        this.nowListPage = nowListPage;
    }

    @Override
    public void display() {
        UserInput input;
        UserInput listGoto;
        UserInput passOrReject;

        while (true) {
            for(int i = (nowListPage - 1) * 10; i >= 0 && i < nowListPage * 10 && i < applications.size(); i++) {   // 展示申请信息
                CourseChangeApplication application = applications.get(i);
                printLn((i - (nowListPage - 1)*10 + 1) + "-" + application.getType() + "\t" + courseManager.getId_courses().get(application.courseId()).getName());
            }
            printLn("第" + nowListPage + "页，共" + maxListPage + "页");

            printLn("输入序号可查看课程具体信息");
            printLn("输入“pass 序号”或“reject 序号”以同意或拒绝申请");
            printLn("输入“<”，“>”或“goto 页数”可切换列表的页数。若要返回，请输入“back”");

            while (true) {
                input = userInput();
                listGoto = input.getGoto(nowListPage);
                passOrReject = input.getPass();

                if(input.isBack()) return;  // 是否返回
                if(listGoto != null) {  // 是否跳转页数
                    if(listGoto.number() > 0 && listGoto.number() <= maxListPage) {
                        new CourseChangeApplicationPage(courseManager, userManager, listGoto.number()).display();
                        return;
                    }
                }
                if(input.isNumber() && input.number() > 0 && nowListPage > 0 && input.number() <= applications.size() - (nowListPage - 1) * 10) {   // 处理用户的选择
                    Course course = courseManager.getId_courses().get(applications.get(input.number() + (nowListPage - 1) * 10 - 1).courseId());
                    new CourseInfoPage(course, userManager, courseManager).display();
                    return;
                }
                if(passOrReject != null) {  // 用户对申请的处理
                    if(passOrReject.word().equalsIgnoreCase("reject")) reject(passOrReject.number());
                    else pass(passOrReject.number());
                    return;
                }
            }
        }
    }

    /**
     * 申请通过时的逻辑
     * @param index
     */
    private void pass(int index) {
        if(index > 0 && nowListPage > 0 && index <= applications.size() - (nowListPage - 1) * 10) {     // 索引合法
            CourseChangeApplication application = applications.get(index + (nowListPage - 1) * 10 - 1); // 得到申请
            Course course = courseManager.getId_courses().get(application.courseId());  // 得到申请所指的课程
            String type = application.type();   // 申请的种类
            if(type.equalsIgnoreCase("OPEN")) {     // 对不同种类的处理
                course.setOpen(true);
                courseManager.courseSaveTo(course);
            }
            else if(type.equalsIgnoreCase("ADD")) {
                courseManager.getCourses().add(course);     // 添加课程
                courseManager.getPreCourses().remove(course);
                course.setHaveAdd(true);
                courseManager.courseSaveTo(course);
            }
            else if(type.equalsIgnoreCase("DELETE")) {
                for(Student student : userManager.getStudents()) {
                    if(student.getCourses().contains(course)) {
                        student.removeCourse(course);
                        courseManager.studentCoursesSaveTo(student);
                    }
                }
                courseManager.removeCourse(course);     // 移除课程
                courseManager.getId_courses().remove(course.getId());
                courseManager.getCourses().remove(course);
            }
            applications.remove(application);   // 移除申请
            superAdmin.removeApplication(application.id());
        }
    }

    /**
     * 拒绝申请时的逻辑
     * @param index
     */
    private void reject(int index) {
        if(index > 0 && nowListPage > 0 && index <= applications.size() - (nowListPage - 1) * 10) {
            CourseChangeApplication application = applications.get(index + (nowListPage - 1) * 10 - 1);
            String type = application.type();
            if(type.equals("DELETE") || type.equals("OPEN")) {
                applications.remove(application);
                superAdmin.removeApplication(application.id());
            }
            else {
                Course course = courseManager.getId_courses().get(application.courseId());
                courseManager.removeCourse(course);
                courseManager.getPreCourses().remove(course);
                courseManager.getId_courses().remove(course.getId());
                applications.remove(application);
                superAdmin.removeApplication(application.id());
            }
        }
    }
}
