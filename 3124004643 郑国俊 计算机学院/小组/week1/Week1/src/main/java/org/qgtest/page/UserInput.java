package org.qgtest.page;

/**
 * 用于预处理用户的输入，便于判断用户输入内容
 * @param isNumber 用户输入是否可被转化为一个int类型数字
 * @param word 用户输入的字符串
 * @param number 如果可以转为int的值
 */
public record UserInput(boolean isNumber, String word, int number) {
    /**
     * 是否为“back”，用与返回上个页面
     * @return 是否为“back”
     */
    public boolean isBack() {
        return word.equalsIgnoreCase("back");
    }

    /**
     * 将一条字符串转为UserInput
     * @param words 字符串
     * @return 转化结果
     */
    public static UserInput toUserInput(String words) {
        try {
            int number = Integer.parseInt(words);
            return new UserInput(true, words, number);
        } catch (NumberFormatException e) {
            return new UserInput(false, words, 0);
        }
    }

    /**
     * 将一个int数字转为UserInput
     * @param word 数字
     * @return 转化结果
     */
    private static UserInput toUserInput(int word) {
        return new UserInput(true, String.valueOf(word), word);
    }

    /**
     * 用于从输入得到列表页面跳转的信息
     * @param nowListPage 当前列表页
     * @return
     */
    public UserInput getGoto(int nowListPage) {
        String[] words = this.word.split(" ");
        if(words.length == 1) {
            if(words[0].equals("<")) return toUserInput(nowListPage-1);
            else if(words[0].equals(">")) return toUserInput(nowListPage+1);
            return null;
        }
        if(words.length != 2) return null;
        if(!words[0].equalsIgnoreCase("goto")) return null;
        if(!toUserInput(words[1]).isNumber)  return null;
        return toUserInput(words[1]);

    }

    /**
     * 用于从输入得到超级管理员对申请的处理结果
     * @return 处理结果
     */
    public UserInput getPass() {
        String[] words = this.word.split(" ");
        if(words.length != 2) return null;
        if(!toUserInput(words[1]).isNumber)  return null;
        if(!(words[0].equalsIgnoreCase("pass") || words[0].equalsIgnoreCase("reject"))) return null;
        return new UserInput(false, words[0], toUserInput(words[1]).number);

    }
}
