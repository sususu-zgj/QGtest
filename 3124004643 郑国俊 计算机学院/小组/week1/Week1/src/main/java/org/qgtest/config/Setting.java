package org.qgtest.config;

import org.qgtest.database.Database;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Setting {

    /**
     *
     * @param path 配置文件的路径
     * @return 返回储存着连接到目标数据库信息的对象
     * @throws IOException 如果无法找到文件，报错
     */
    public static Database getDatabase(String path) throws IOException {
        Properties config = new Properties();
        config.load(new FileInputStream(path));

        String url = "jdbc:mysql://" + config.getProperty("database.ip") + ":" + config.getProperty("database.port") + "/" + config.getProperty("database.name");
        String username = config.getProperty("database.username");
        String password = config.getProperty("database.password");

        return new Database(url, username, password);
    }

    /**
     *
     * @param path 配置文件的路径
     * @return 返回超级管理员的用户名
     * @throws IOException
     */
    public static String getSuperUsername(String path) throws IOException{
        Properties config = new Properties();
        config.load(new FileInputStream(path));
        return config.getProperty("super.admin.username");
    }

    /**
     *
     * @param path 配置文件的路径
     * @return 返回超级管理员的用户名
     * @throws IOException
     */
    public static String getSuperPassword(String path) throws IOException{
        Properties config = new Properties();
        config.load(new FileInputStream(path));
        return config.getProperty("super.admin.password");
    }











}
