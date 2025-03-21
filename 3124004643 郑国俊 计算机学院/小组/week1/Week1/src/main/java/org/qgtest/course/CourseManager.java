package org.qgtest.course;

import org.qgtest.database.DatabaseManager;
import org.qgtest.user.Student;
import org.qgtest.user.UserManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 负责管理课程相关的类
 */
public class CourseManager {
    private final DatabaseManager databaseManager;  // 数据库管理器，用于获取连接
    private final UserManager userManager;      // 用户管理器，用于获取要修改选课的学生
    private ArrayList<Course> courses = new ArrayList<>();  // 正式加入的课程
    private ArrayList<Course> preCourses = new ArrayList<>();   // 正在审核阶段的课程
    private HashMap<Integer, Course> id_courses = new HashMap<>();  // id与课程的键值对

    public CourseManager(DatabaseManager databaseManager, UserManager userManager) {
        this.databaseManager = databaseManager;
        this.userManager = userManager;
        loadCourses();
        loadStudentCourses();
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public ArrayList<Course> getPreCourses() {
        return preCourses;
    }

    public HashMap<Integer, Course> getId_courses() {
        return id_courses;
    }

    /**
     * 用于将一个课程从数据库中移除
     * @param course 将要被移除的课程
     */
    public void removeCourse(Course course) {
        try (Connection connection = databaseManager.getConnection()) { // 获取连接
            connection.setAutoCommit(false);    //开启事务

            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM courses WHERE id=?")) {
                statement.setInt(1, course.getId());
                statement.executeUpdate();

                connection.commit();    //提交事务
            } catch (SQLException exception) {
                connection.rollback();  // 出现异常，回滚
                System.out.println(exception.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 用于向数据库中添加课程
     * @param course 将要被添加的课程
     */
    public void addCourse(Course course) {
        try (Connection connection = databaseManager.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO courses (name, teacher, max_number, now_number, creditHour, point, open, have_add) VALUES (?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, course.getName());
                statement.setString(2, course.getTeacher());
                statement.setInt(3, course.getMaxNumber());
                statement.setInt(4, course.getNowNumber());
                statement.setInt(5, course.getCreditHour());
                statement.setInt(6, course.getPoint());
                statement.setBoolean(7, course.isOpen());
                statement.setBoolean(8, course.isHaveAdd());

                statement.executeUpdate();
                try (ResultSet resultSet = statement.getGeneratedKeys()) {  // 获取数据库为课程生成的id
                    if(resultSet.next()) {
                        course.setId(resultSet.getInt(1));
                    }
                }

                connection.commit();
            } catch (SQLException exception) {
                connection.rollback();
                System.out.println(exception.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 从数据库加载与课程有关的所有信息
     */
    private void loadCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        ArrayList<Course> preCourses = new ArrayList<>();
        HashMap<Integer, Course> id_courses = new HashMap<>();

        try(Connection connection = databaseManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM courses")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String teacher = resultSet.getString("teacher");
                int maxNum = resultSet.getInt("max_number");
                int nowNum = resultSet.getInt("now_number");
                int creditHour = resultSet.getInt("creditHour");
                int point = resultSet.getInt("point");
                boolean open = resultSet.getBoolean("open");
                boolean haveAdd = resultSet.getBoolean("have_add");

                Course course;
                if(haveAdd) course = new Course(id, name, teacher, maxNum, creditHour, point, true);
                else course = new Course(id, name, teacher, maxNum, creditHour, point, false);
                course.setNowNumber(nowNum);
                course.setOpen(open);

                if(haveAdd) courses.add(course);
                else preCourses.add(course);

                id_courses.put(id, course);
            }

            this.courses = courses;
            this.preCourses = preCourses;
            this.id_courses = id_courses;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 将一个课程更新到数据库
     * @param course 将要被更新的课程
     */
    public void courseSaveTo(Course course) {
        try (Connection connection = databaseManager.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement("UPDATE courses SET name=?,teacher=?,max_number=?,now_number=?,creditHour=?,point=?,open=?,have_add=? WHERE id=?")) {
                statement.setString(1, course.getName());
                statement.setString(2, course.getTeacher());
                statement.setInt(3, course.getMaxNumber());
                statement.setInt(4, course.getNowNumber());
                statement.setInt(5, course.getCreditHour());
                statement.setInt(6, course.getPoint());
                statement.setBoolean(7, course.isOpen());
                statement.setBoolean(8, course.isHaveAdd());
                statement.setInt(9, course.getId());

                statement.executeUpdate();
                connection.commit();
            } catch (SQLException exception) {
                connection.rollback();
                System.out.println(exception.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 将学生的选课信息保存到数据库
     * @param student 将要被保存的学生
     */
    public void studentCoursesSaveTo(Student student) {
        ArrayList<Course> courses = student.getCourses();
        int size = courses.size();

        try (Connection connection = databaseManager.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement("UPDATE student_courses SET course_1=?,course_2=?,course_3=?,course_4=?,course_5=? WHERE username=?")){

                for(int i = 0; i < 5; i++) {
                    statement.setInt(i + 1, (size > i ? courses.get(i).getId() : 0));   // 保存所选的课程
                }
                statement.setString(6, student.getUsername());

                statement.executeUpdate();
                connection.commit();
            } catch (SQLException exception) {
                connection.rollback();
                System.out.println(exception.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 从数据库加载学生的选课信息
     */
    private void loadStudentCourses() {

        try (Connection connection = databaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM student_courses")) {
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                int course_1 = resultSet.getInt("course_1");
                int course_2 = resultSet.getInt("course_2");
                int course_3 = resultSet.getInt("course_3");
                int course_4 = resultSet.getInt("course_4");
                int course_5 = resultSet.getInt("course_5");

                Student student = (Student) userManager.getUser(username);  // 通过用户名获取学生
                ArrayList<Course> courses = new ArrayList<>();
                if(id_courses.get(course_1) != null) courses.add(id_courses.get(course_1));
                if(id_courses.get(course_2) != null) courses.add(id_courses.get(course_2));
                if(id_courses.get(course_3) != null) courses.add(id_courses.get(course_3));
                if(id_courses.get(course_4) != null) courses.add(id_courses.get(course_4));
                if(id_courses.get(course_5) != null) courses.add(id_courses.get(course_5));
                student.setCourses(courses);    // 加载选课
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
