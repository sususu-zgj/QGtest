package org.qgtest.page.pages;

import org.qgtest.page.Page;
import org.qgtest.page.UserInput;
import org.qgtest.user.UserManager;

import java.util.UUID;

/**
 * 用于显示管理员注册码的页面
 */
public class AdminCorePage extends Page {
    private final UserManager userManager;

    public AdminCorePage(UserManager userManager) {
        super("管理员注册码");
        this.userManager = userManager;
    }

    @Override
    public void display() {
        UserInput input;
        while (true) {
            for(String core : userManager.getAdminCores()) {
                printLn(core);
            }

            while (true) {
                printLn("输入“back”返回。输入“add”或“del”添加或删除一条管理员注册码");
                input = userInput();

                if(input.isBack()) return;
                if(input.word().equalsIgnoreCase("add")) {
                    addCore();
                    break;
                }
                else if(input.word().equalsIgnoreCase("del")) {
                    removeCore();
                    break;
                }

            }
        }
    }

    /**
     * 用户选择添加一条注册码
     */
    private void addCore() {
        String core = UUID.randomUUID().toString();
        while(userManager.getAdminCores().contains(core)) core = UUID.randomUUID().toString();  // 使用随机uuid作为管理员注册码
        userManager.getAdminCores().add(core);
        userManager.saveAdminCoreTo(core);
    }

    /**
     * 用户选择移除一条注册码
     * 移除第一条
     */
    private void removeCore() {
        if(userManager.getAdminCores().isEmpty()) return;
        String core = userManager.getAdminCores().get(0);
        userManager.getAdminCores().remove(core);
        userManager.removeAdminCore(core);
    }
}
