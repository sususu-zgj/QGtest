package org.qgtest.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.qgtest.config.Setting;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseManager {
    private final static ArrayList<String> tables = new ArrayList<>(List.of(    // 数据库应有的表名
            "users",
            "students",
            "courses",
            "student_courses",
            "admin_cores",
            "super_admin"
    ));

    private final static ArrayList<String> users = new ArrayList<>(List.of( // 名为“users”的表应有的字段。储存用户基本信息
            "username VARCHAR(20) PRIMARY KEY",
            "password VARCHAR(20)",
            "phone VARCHAR(20)",
            "admin TINYINT(1)"
    ));

    private final static ArrayList<String> students = new ArrayList<>(List.of(  // 名为“students”的表应有的字段。储存学生个人信息
            "username VARCHAR(20) PRIMARY KEY",
            "name VARCHAR(20)",
            "sex VARCHAR(2)",
            "age INT",
            "level INT"
    ));

    private final static ArrayList<String> courses = new ArrayList<>(List.of(   // 名为course的表应有的字段。储存一个课程的所有信息
            "id INT AUTO_INCREMENT PRIMARY KEY",
            "name VARCHAR(20)",
            "teacher VARCHAR(20)",
            "max_number INT",
            "now_number INT",
            "creditHour INT",
            "point INT",
            "open TINYINT(1)",
            "have_add TINYINT(1)"
    ));

    private final static ArrayList<String> student_courses = new ArrayList<>(List.of(   // 储存学生的选课信息
            "username VARCHAR(20) PRIMARY KEY",
            "course_1 INT",
            "course_2 INT",
            "course_3 INT",
            "course_4 INT",
            "course_5 INT"
    ));

    private final static ArrayList<String> super_admin = new ArrayList<>(List.of(   // 储存超级管理员接受到的申请
            "id INT AUTO_INCREMENT PRIMARY KEY",
            "type VARCHAR(20)",
            "course_id VARCHAR(20)"
    ));

    private final static ArrayList<String> admin_cores = new ArrayList<>(List.of(   // 储存管理员的注册码
            "core VARCHAR(40) PRIMARY KEY"
    ));

    private final HashMap<String, ArrayList<String>> tableMap = new HashMap<>();    // 表名与表字段相对应

    private Database database;
    private HikariDataSource dataSource;    // Hikari连接池

    private boolean connected;  // 是否成功连接到数据库

    public DatabaseManager(String path) {
        tableMap.put("users", users);
        tableMap.put("students", students);
        tableMap.put("courses", courses);
        tableMap.put("student_courses", student_courses);
        tableMap.put("admin_cores", admin_cores);
        tableMap.put("super_admin", super_admin);

        try {
            this.database = Setting.getDatabase(path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        connected = setupPool();
        connected = ensureExist();
    }

    /**
     * 设置连接池
     * @return 是否连接成功
     */
    private boolean setupPool() {
        HikariConfig config = new HikariConfig();

        //设置连接池的相关配置
        //必须配置的
        config.setJdbcUrl(database.url());          //数据库的url
        config.setUsername(database.username());    //连接到数据库所用的用户名
        config.setPassword(database.password());    //用于登录该用户名的密码

        //非必须配置的
        config.setPoolName("学生信息管理系统");         //连接池的名称
        config.setMaximumPoolSize(10);      //连接池中的最大连接数，默认为10
        config.setMinimumIdle(10);          //连接池中的最小空闲连接数，默认为10
        config.setIdleTimeout(600000);      //连接空闲的最大数据
        config.setMaxLifetime(1800000);     //连接的最大空闲周期
        config.setMinimumIdle(30000);       //获取连接的最大超时时间，默认为30秒

        dataSource = new HikariDataSource(config);

        try (Connection connection = getConnection()){  // 检测是否能得到连接
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * 关闭连接池
     */
    public void closePool() {
        if(dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    /**
     * 从数据库得到一条连接
     * @return 得到的连接
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException{
        return dataSource.getConnection();
    }

    /**
     *
     * @return 是否连接成功
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * 确保各表及其字段存在
     * @return
     */
    private boolean ensureExist() {
        if(!connected) return false;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            connection.setAutoCommit(false);

            try {
                for(String table : tables) {
                    statement.addBatch("CREATE TABLE IF NOT EXISTS " + table + " (timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"); // 确保表

                    ArrayList<String> columnList = getColumns(connection, table);
                    for(String column : tableMap.get(table)) {
                        if (!columnList.contains(column.split(" ")[0])) {
                            statement.addBatch("ALTER TABLE " + table + " ADD COLUMN " + column);   // 确保字段
                        }
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                System.out.println(e.getMessage());
                return false;
            }

            statement.executeBatch();
            connection.commit();
            return true;
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            return false;
        }
    }

    /**
     * 得到一个表中所有的字段
     * @param connection 一条连接
     * @param tableName 表名
     * @return 储存表中所有字段的列表
     * @throws SQLException
     */
    private ArrayList<String> getColumns(Connection connection, String tableName) throws SQLException {
        ArrayList<String> columns = new ArrayList<>();
        try(ResultSet resultSet = connection.getMetaData().getColumns(null, null, tableName, null)) {   // 查询
            while (resultSet.next()) {
                columns.add(resultSet.getString("COLUMN_NAME"));
            }
        }
        return columns;
    }

}
