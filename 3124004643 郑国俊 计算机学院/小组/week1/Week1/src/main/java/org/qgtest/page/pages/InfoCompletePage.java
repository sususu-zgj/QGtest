package org.qgtest.page.pages;

import org.qgtest.page.Page;
import org.qgtest.page.UserInput;
import org.qgtest.user.Student;
import org.qgtest.user.User;
import org.qgtest.user.UserManager;

/**
 * 用户信息完善页
 */
public class InfoCompletePage extends Page {
    private final User user;
    private final UserManager userManager;

    public InfoCompletePage(User user, UserManager userManager) {
        super("完善信息");
        this.user = user;
        this.userManager = userManager;
    }

    @Override
    public void display() {
        UserInput input;

        if(user.getPhone() == null || user.getPhone().isEmpty()) {
            printLn("请输入电话号码，用于绑定账号，找回密码。长度为11为，且以1开头");
            while (true) {
                input = userInput();

                if(input.word().matches("^[0-9]{11}$") && input.word().startsWith("1")) {
                    user.setPhone(input.word());
                    break;
                }

                printLn("电话号码格式错误！请重新输入");
            }
        }

        userManager.saveTo(user);

        if(!(user instanceof Student student)) return;

        if(student.getName() == null || student.getName().isEmpty()) {
            printLn("请输入您的姓名，长度不超过16");
            while (true) {
                input = userInput();

                if(input.word().length() <= 16) {
                    student.setName(input.word());
                    break;
                }

                printLn("输入太长了！请重新输入");
            }
        }

        if(student.getAge() == 0) {
            printLn("请输入您的年龄");
            while (true) {
                input = userInput();

                if(input.number() > 0) {
                    student.setAge(input.number());
                    break;
                }

                printLn("请输入一个正整数");
            }
        }

        if(student.getSex() == null || student.getSex().isEmpty()) {
            printLn("请选择您的性别");
            printLn("1-男");
            printLn("2-女");
            while (true) {
                input = userInput();
                String sex;

                sex = switch (input.number()) {
                    case 1 -> "男";
                    case 2 -> "女";
                    default -> null;
                };
                if(sex != null) {
                    student.setSex(sex);
                    break;
                }

                printLn("请输入以上选项");
            }
        }

        if(student.getLevel() == 0) {
            printLn("请选择以下年级：");
            printLn("1-大一");
            printLn("2-大二");
            printLn("3-大三");
            printLn("4-大四");
            while (true) {
                input = userInput();

                if(input.number() > 0 && input.number() <= 4) {
                    student.setLevel(input.number());
                    break;
                }

                printLn("请输入以上选项");
            }
        }

        userManager.saveTo(user);
    }
}
