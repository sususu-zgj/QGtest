package org.qgtest.user;

import org.qgtest.database.DatabaseManager;
import org.qgtest.page.CourseChangeApplication;

import java.sql.*;
import java.util.ArrayList;

/**
 * 超级管理员，与超级管理员有关的信息和逻辑都储存在这里
 */
public class SuperAdmin extends User{
    private final ArrayList<CourseChangeApplication> applications = new ArrayList<>();  // 所有的申请
    private final DatabaseManager databaseManager;

    public SuperAdmin(String username, String password, DatabaseManager databaseManager) {
        super(username, password, true);
        this.databaseManager = databaseManager;
        loadApplications();
    }

    /**
     * 从数据库加载申请信息
     */
    private void loadApplications() {
        try (Connection connection = databaseManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM super_admin")) {

            while (resultSet.next()) {
                CourseChangeApplication application = new CourseChangeApplication(resultSet.getString("type"), resultSet.getInt("course_id"), resultSet.getInt("id"));
                applications.add(application);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 增加一条申请
     * @param type 申请的类型
     * @param course_id 申请的课程的id
     */
    public void addApplication(String type, int course_id) {
        try (Connection connection = databaseManager.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO super_admin (type, course_id) VALUES (?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, type);
                statement.setInt(2, course_id);
                statement.executeUpdate();
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if(resultSet.next()) {
                        CourseChangeApplication application = new CourseChangeApplication(type, course_id, resultSet.getInt(1));    // 生成一条申请
                        applications.add(application);
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

    public ArrayList<CourseChangeApplication> getApplications() {
        return applications;
    }

    /**
     * 将一条申请从数据库中移除
     * @param id 要移除的申请的id
     */
    public void removeApplication(int id) {
        try (Connection connection = databaseManager.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM super_admin WHERE id=?")) {
                statement.setInt(1, id);
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
}
