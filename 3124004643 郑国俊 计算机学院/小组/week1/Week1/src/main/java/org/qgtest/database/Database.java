package org.qgtest.database;

/**
 * 保存连接到数据库的相关信息
 * @param url 数据库的url
 * @param username 登录数据库的账号
 * @param password 登录该账号的密码
 */
public record Database(String url, String username, String password) {
}
