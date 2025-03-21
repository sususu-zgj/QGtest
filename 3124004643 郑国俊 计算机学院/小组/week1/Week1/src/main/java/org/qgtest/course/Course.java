package org.qgtest.course;

/**
 * 代表一个课程
 */
public class Course {
    private int id; // 课程的id，由数据库生产
    private String name;    // 课程的名称
    private String teacher; // 课程的任教教师

    private int maxNumber;  // 本课程最大选课人数
    private int nowNumber;  // 当前选课人数
    private int creditHour; // 学时
    private int point;  // 学分

    private boolean open;   // 是否已经开始授课
    private boolean haveAdd;    // 是否正式加入可选课组

    public Course(int id, String name, String teacher, int maxNumber, int creditHour, int point, boolean haveAdd) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.maxNumber = maxNumber;
        this.creditHour = creditHour;
        this.point = point;
        this.haveAdd = haveAdd;
    }

    public Course() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public int getNowNumber() {
        return nowNumber;
    }

    public void setNowNumber(int nowNumber) {
        this.nowNumber = nowNumber;
    }

    public int getCreditHour() {
        return creditHour;
    }

    public void setCreditHour(int creditHour) {
        this.creditHour = creditHour;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isHaveAdd() {
        return haveAdd;
    }

    public void setHaveAdd(boolean haveAdd) {
        this.haveAdd = haveAdd;
    }


}
