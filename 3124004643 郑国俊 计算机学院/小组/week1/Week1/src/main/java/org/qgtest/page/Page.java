package org.qgtest.page;

import java.util.Scanner;

/**
 * 代表页面的类，被其他具体页面继承
 */
public abstract class Page {
    private static Scanner sc = new Scanner(System.in); // 用于用户输入的Scanner

    private final String pageTitle; // 该页面的标题
    private PageType next;  // 下个页面

    public Page(String title) {
        this.pageTitle = "=======-" + title + "-=======";
    }

    /*
    一个需要被子类重写的方法，用于页面内容的展示
     */
    public abstract void display();

    public String getTitle() {
        return pageTitle;
    }

    public PageType nextPage() {
        return next;
    }

    protected void setNext(PageType next) {
        this.next = next;
    }

    /**
     * 打印一行信息
     * @param message 信息
     */
    protected static void printLn(String message) {
        System.out.println(message);
    }

    /**
     * 用于预处理用户输入
     * @return 处理结果
     */
    protected static UserInput userInput() {
        while (true) {
            if (!sc.hasNextLine()) continue;
            String words = sc.nextLine();

            return UserInput.toUserInput(words);
        }
    }





}
