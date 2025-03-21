package org.qgtest.page.pages;

import org.qgtest.course.Course;
import org.qgtest.course.CourseManager;
import org.qgtest.page.Page;
import org.qgtest.page.UserInput;
import org.qgtest.user.UserManager;

/**
 * 创建新课程时完善信息
 */
public class CourseCompletePage extends Page {
    private final CourseManager courseManager;
    private final Course course;
    private final UserManager userManager;

    public CourseCompletePage(UserManager userManager, CourseManager courseManager, Course course) {
        super("完善课程信息");
        this.courseManager = courseManager;
        this.userManager = userManager;
        this.course = course;
    }

    @Override
    public void display() {
        UserInput input;

        if(course.getId() != 0) {
            if(course.getName() == null || course.getName().isEmpty()) {
                printLn("请输入课程名称，长度不超过16个字");
                while (true) {
                    input = userInput();

                    if(input.word().length() <= 16) {
                        course.setName(input.word());
                        break;
                    }

                    printLn("长度太长了，请重新输入");
                }
            }

            if(course.getTeacher() == null || course.getTeacher().isEmpty()) {
                printLn("请输入教师名称名字，长度不超过16个字");
                while (true) {
                    input = userInput();

                    if(input.word().length() <= 16) {
                        course.setTeacher(input.word());
                        break;
                    }

                    printLn("长度太长了，请重新输入");
                }
            }

            if(course.getCreditHour() == 0) {
                printLn("请输入课程学时");
                while (true) {
                    input = userInput();

                    if(input.number() > 0) {
                        course.setCreditHour(input.number());
                        break;
                    }

                    printLn("请输入一个正整数");
                }
            }

            if(course.getPoint() == 0) {
                printLn("请输入课程学分");
                while (true) {
                    input = userInput();

                    if(input.number() > 0) {
                        course.setPoint(input.number());
                        break;
                    }

                    printLn("请输入一个正整数");
                }
            }

            courseManager.courseSaveTo(course);
        }
        else {
            if(course.getName() == null || course.getName().isEmpty()) {
                printLn("请输入课程名称，长度不超过16个字");
                while (true) {
                    input = userInput();

                    if(input.word().length() <= 16) {
                        course.setName(input.word());
                        break;
                    }

                    printLn("长度太长了，请重新输入");
                }
            }

            if(course.getTeacher() == null || course.getTeacher().isEmpty()) {
                printLn("请输入教师名称名字，长度不超过16个字");
                while (true) {
                    input = userInput();

                    if(input.word().length() <= 16) {
                        course.setTeacher(input.word());
                        break;
                    }

                    printLn("长度太长了，请重新输入");
                }
            }

            if(course.getMaxNumber() == 0) {
                printLn("请输入课程最大选课人数");
                while (true) {
                    input = userInput();

                    if(input.number() > 0) {
                        course.setMaxNumber(input.number());
                        course.setNowNumber(0);
                        break;
                    }

                    printLn("请输入一个正整数");
                }
            }

            if(course.getCreditHour() == 0) {
                printLn("请输入课程学时");
                while (true) {
                    input = userInput();

                    if(input.number() > 0) {
                        course.setCreditHour(input.number());
                        break;
                    }

                    printLn("请输入一个正整数");
                }
            }

            if(course.getPoint() == 0) {
                printLn("请输入课程学分");
                while (true) {
                    input = userInput();

                    if(input.number() > 0) {
                        course.setPoint(input.number());
                        break;
                    }

                    printLn("请输入一个正整数");
                }
            }

            course.setOpen(false);
            course.setHaveAdd(false);

            courseManager.addCourse(course);
            courseManager.getPreCourses().add(course);
            courseManager.getId_courses().put(course.getId(), course);
            userManager.getSuperAdmin().addApplication("ADD", course.getId());
        }
    }
}
