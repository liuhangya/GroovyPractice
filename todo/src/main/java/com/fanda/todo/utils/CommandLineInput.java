package com.fanda.todo.utils;

import java.util.HashMap;
import java.util.Map;

/* 
 * 命令行操作封装类，枚举
 */
public enum CommandLineInput {

    // 定义枚举成员
    FIND_ALL('a'), FIND_BY_ID('f'), INSERT('i'), UPDATE('u'), DELETE('d'), EXIT('e');

    private final static Map<Character, CommandLineInput> INPUTS;

    // 静态代码块，把所有枚举成员添加到 Map 中
    static {
        INPUTS = new HashMap<>();
        for (CommandLineInput input : values()) {
            INPUTS.put(input.getShortCmd(), input);
        }
    }

    // 成员变量
    private final char shortCmd;

    // 构造方法，必须私有
    private CommandLineInput(char shortCmd) {
        this.shortCmd = shortCmd;
    }

    // 自定义方法
    public char getShortCmd() {
        return shortCmd;
    }

    // 根据输入的字符获取到对应的枚举成员
    public static CommandLineInput getCommandLineInputForInput(char input) {
        return INPUTS.get(input);
    }
}
