package org.qgtest.user;

import org.qgtest.course.Course;

import java.util.ArrayList;

/**
 * 学生类
 */
public class Student extends User {
    private String name;    // 学生姓名
    private String sex;     // 学生性别
    private int age;        // 学生年龄
    private int level;      // 学生年纪级
    private ArrayList<Course> courses = new ArrayList<>();  // 学生的选课信息

    public Student(String username, String password, int level) {
        super(username, password, false);
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public Student setName(String name) {
        this.name = name;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public Student setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public int getAge() {
        return age;
    }

    public Student setAge(int age) {
        this.age = age;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public Student setLevel(int level) {
        this.level = level;
        return this;
    }

    public boolean removeCourse(Course course) {
        if(course.isOpen()) return false;
        this.courses.remove(course);
        return true;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }
}
