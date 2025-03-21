package org.qgtest.page;

/**
 * 一条向超级管理员提出的申请
 * @param type 申请种类
 * @param courseId 课程id
 * @param id 申请id
 */
public record CourseChangeApplication(String type, int courseId, int id) {
    public String getType() {
        return switch (type) {
            case "ADD" -> "增添课程";
            case "DELETE" -> "删除课程";
            case "OPEN" -> "开启课程";
            default -> "?";
        };
    }
}
