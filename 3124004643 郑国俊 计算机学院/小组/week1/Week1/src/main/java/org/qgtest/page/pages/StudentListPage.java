package org.qgtest.page.pages;

import org.qgtest.page.Page;
import org.qgtest.page.UserInput;
import org.qgtest.user.Student;
import org.qgtest.user.UserManager;

import java.util.ArrayList;

public class StudentListPage extends Page {
    private final UserManager userManager;
    private final ArrayList<Student> students;
    private final int maxListPage;
    private final int nowListPage;

    /**
     * 用于展示一个学生列表，单页最大人数为10
     * @param userManager
     * @param students 学生列表
     * @param nowListPage 当前页数
     */
    public StudentListPage(UserManager userManager, ArrayList<Student> students, int nowListPage) {
        super("学生列表");
        this.userManager = userManager;
        this.students = students;
        this.nowListPage = nowListPage;
        this.maxListPage = students.size() / 10 + ((students.size() % 10 != 0) ? 1 : 0);
    }

    public StudentListPage(UserManager userManager, ArrayList<Student> students) {
        super("学生列表");
        this.userManager = userManager;
        this.students = students;
        this.maxListPage = students.size() / 10 + ((students.size() % 10 != 0) ? 1 : 0);
        this.nowListPage = (maxListPage > 0) ? 1 : 0;
    }

    @Override
    public void display() {
        UserInput input;
        UserInput listGoto;

        while (true) {
            printLn("学号\t姓名");
            for(int i = (nowListPage - 1) * 10; i >= 0 && i < nowListPage * 10 && i < students.size(); i++) {   // 显示学生
                Student student = students.get(i);
                printLn((i - (nowListPage - 1)*10 + 1) + "-" + student.getUsername() + "\t" + student.getName());
            }
            printLn("第" + nowListPage + "页，共" + maxListPage + "页");

            printLn("输入序号可查看学生信息");
            printLn("输入“<”，“>”或“goto 页数”可切换列表的页数。若要返回，请输入“back”");

            while (true) {
                input = userInput();
                listGoto = input.getGoto(nowListPage);

                if(input.isBack()) return;  // 是否返回
                if(listGoto != null) {  // 是否进行页面跳转
                    if(listGoto.number() > 0 && listGoto.number() <= maxListPage) { // 跳转是否合法
                        new StudentListPage(userManager, students, listGoto.number()).display();
                        return;
                    }
                }
                if(input.isNumber() && input.number() > 0 && nowListPage > 0 && input.number() <= students.size() - (nowListPage - 1) * 10) {   // 查看学生信息
                    Student student = students.get(input.number() + (nowListPage - 1) * 10 - 1);
                    new StudentInfoPage(student, false, userManager).display();
                    return;
                }
            }
        }
    }
}
