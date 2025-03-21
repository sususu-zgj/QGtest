package org.qgtest.user;

import org.qgtest.config.Setting;
import org.qgtest.database.DatabaseManager;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 用户管理器
 */
public class  UserManager {
    private final DatabaseManager database;                             // 所连接的数据库
    private ArrayList<String> adminCores = new ArrayList<>();           // 管理员注册码
    private HashMap<String, String> user_password = new HashMap<>();    // 用户名与密码的键值对
    private HashMap<String, User> users = new HashMap<>();              // 用户名与用户的键值对
    private SuperAdmin superAdmin;  // 超级管理员

    private User nowUser;       // 当前登录的用户

    public UserManager(DatabaseManager database) {
        this.database = database;
        loadUsers();
        loadStuInfo();
        loadAdminCores();
    }

    /**
     *
     * @return 获取管理员注册码
     */
    public ArrayList<String> getAdminCores() {
        return adminCores;
    }

    /**
     *
     * @return 获取账号密码对
     */
    public HashMap<String, String> getPasswords() {
        return user_password;
    }

    /**
     * 通过用户名获取用户
     * @param username 用户名
     * @return 用户
     */
    public User getUser(String username) {
        return users.get(username);
    }

    public SuperAdmin getSuperAdmin() {
        return superAdmin;
    }

    public User getNowUser() {
        return nowUser;
    }

    public ArrayList<Student> getStudents() {
        ArrayList<Student> students = new ArrayList<>();
        for(User user : this.users.values()) {
            if(user instanceof Student student) {
                students.add(student);
            }
        }
        return students;
    }

    /**
     * 从配置文件加载超级管理员
     * @param path 配置文件路径
     * @throws IOException
     */
    public void loadSuperAdmin(String path) throws IOException {
        String username = Setting.getSuperUsername(path);
        String password = Setting.getSuperPassword(path);
        SuperAdmin superAdmin = new SuperAdmin(username, password, database);
        user_password.put(username, password);
        users.put(username, superAdmin);
        this.superAdmin = superAdmin;
    }

    public User setNowUser(String username) {
        nowUser = users.get(username);
        return nowUser;
    }

    /**
     * 添加一名用户
     * @param username 用户名
     * @param password 密码
     * @param isAdmin 是否是管理员
     * @return 是否添加成功
     */
    public boolean addUser(String username, String password, boolean isAdmin) {
        if(user_password.containsKey(username)) return false;   // 用户名已存在
        user_password.put(username, password);

        if(isAdmin) {
            Admin admin = new Admin(username, password);
            addTo(admin);   // 添加到数据库
            users.put(username, admin);
        }
        else {
            Student student = new Student(username, password, 0);
            addTo(student); // 添加到数据库
            users.put(username, student);
        }
        return true;
    }

    /**
     * 从数据库加载用户
     */
    private void loadUsers() {
        HashMap<String ,String> passwords = new HashMap<>();
        HashMap<String, User> users = new HashMap<>();

        try(Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String phone = resultSet.getString("phone");
                boolean isAdmin = resultSet.getBoolean("admin");
                User user;

                passwords.put(username, password);
                if(isAdmin) user = new Admin(username, password);
                else user = new Student(username, password, 0);
                user.setPhone(phone);
                users.put(username, user);
            }

            this.user_password = passwords;
            this.users = users;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 加载学生信息
     */
    private void loadStuInfo() {
        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM students")){
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String name = resultSet.getString("name");
                String sex = resultSet.getString("sex");
                int age = resultSet.getInt("age");
                int level = resultSet.getInt("level");

                if(this.users.get(username) instanceof Student student) {
                    student.setName(name)
                            .setSex(sex)
                            .setAge(age)
                            .setLevel(level);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 加载管理员注册码
     */
    private void loadAdminCores() {
        ArrayList<String> cores = new ArrayList<>();

        try (Connection connection = database.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM admin_cores")) {
            while (resultSet.next()) {
                cores.add(resultSet.getString("core"));
            }

            this.adminCores = cores;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 添加一名用户到数据库
     * @param user 用户
     */
    private void addTo(User user) {
        try (Connection connection = database.getConnection()){
            connection.setAutoCommit(false);

            try (PreparedStatement statementU = connection.prepareStatement("INSERT INTO users (username, password, phone, admin) VALUES (?,?,?,?)");
                 PreparedStatement statementS = connection.prepareStatement("INSERT INTO students (username, level) VALUES (?,?)");
                 PreparedStatement statementSC = connection.prepareStatement("INSERT INTO student_courses (username, course_1, course_2, course_3, course_4, course_5) VALUES (?,0,0,0,0,0)")) {

                statementU.setString(1, user.getUsername());
                statementU.setString(2, user.getPassword());
                statementU.setString(3, user.getPhone());
                statementU.setBoolean(4, user.isAdmin());
                statementU.executeUpdate();

                if(user instanceof Student student) {
                    statementS.setString(1, student.getUsername());
                    statementS.setInt(2, student.getLevel());
                    statementS.executeUpdate();

                    statementSC.setString(1, student.getUsername());
                    statementSC.executeUpdate();
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
     * 更新用户的信息到数据库
     * @param user 用户
     */
    public void saveTo(User user) {
        try (Connection connection = database.getConnection()){
            connection.setAutoCommit(false);

            try (PreparedStatement statementPP = connection.prepareStatement("UPDATE users SET password=?,phone=? WHERE username=?");
                 PreparedStatement statementS = connection.prepareStatement("UPDATE students SET name=?,sex=?,age=?,level=? WHERE username=?")){

                statementPP.setString(1, user.getPassword());   // 保存用户的密码
                statementPP.setString(2, user.getPhone());
                statementPP.setString(3, user.getUsername());
                statementPP.executeUpdate();

                if(user instanceof Student student) {   // 保存学生的信息
                    statementS.setString(1, student.getName());
                    statementS.setString(2, student.getSex());
                    statementS.setInt(3, student.getAge());
                    statementS.setInt(4, student.getLevel());
                    statementS.setString(5, student.getUsername());
                    statementS.executeUpdate();
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
     * 保存管理员注册码
     * @param core 注册码
     */
    public void saveAdminCoreTo(String core) {
        try (Connection connection = database.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO admin_cores (core) value (?)")) {
                statement.setString(1, core);
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
     * 移除一条注册码
     * @param core 注册码
     */
    public void removeAdminCore(String core) {
        try (Connection connection = database.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM admin_cores WHERE core=?")) {
                statement.setString(1, core);
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
