package com.fanda.todo;

import com.fanda.todo.utils.CommandLineInput;
import com.fanda.todo.utils.CommandLineInputHandler;
//import org.apache.commons.lang3.CharUtils;

// gradle 运行命令如下： java -cp build/classes/java/main com.fanda.todo.ToDoApp
// gradle 运行命令如下： java -jar build/libs/todo-0.1.jar
// 因为执行了包装器任务，所以可以通过 gradlew.bat 命令来执行，跟 gradle 命令是一致的，比如 gradlew.bat clean 或 gradle clean

/*
* 报错： java.lang.UnsupportedClassVersionError报错
* 运行不了的配置处理如下：
* 1. 创建 gradle.properties ,并配置好 jdk11 的版本 ，比如 org.gradle.java.home=F:\\Android\\Android Studio 4.0\\jre
* 2. build 文件，加上 java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}，让 gradle 生成的java 代码是基于 1.8 版本的 ，因为本地配的java home 是 1.8，这样版本才会一致，才能正常运行
*
* */
public class ToDoApp {
    // 默认输入字符设为空格
    public static final char DEFAULT_INPUT = '\u0000';

    public static void main(String[] args) {
        // 创建命令行交互类
        CommandLineInputHandler handler = new CommandLineInputHandler();
        // 设置默认操作
        char command = DEFAULT_INPUT;
        // 如果输入是 'e'，则退出循环，即程序结束
        while (CommandLineInput.EXIT.getShortCmd() != command) {
            // 1. 打印选项到控制台
            handler.printOptions();
            // 2. 读取用户的输入
            String input = handler.readInput();

//            command = CharUtils.toChar(input, DEFAULT_INPUT);

            // 3. 输入字符长度为1，则直接获取，否则读取第一个字符
             char[] inputChars = input.length() == 1 ? input.toCharArray() : new char[] {
             DEFAULT_INPUT };
             command = inputChars[0];
            // 4. 根据输入字符获取对应的命令对象
            CommandLineInput commandLineInput = CommandLineInput.getCommandLineInputForInput(command);
            // 5. 根据命令对象进行相应操作
            handler.processInput(commandLineInput);
        }

    }
}
