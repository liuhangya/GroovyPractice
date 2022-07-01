package com.fanda.todo.utils;

import com.fanda.todo.model.ToDoItem;
import com.fanda.todo.repository.InMemoryToDoRepository;
import com.fanda.todo.repository.ToDoRepository;

import java.util.Collection;


/* 
 * 命令行操作交互类，逻辑处理都在这实现，该类跟 ToDoApp.java 交互 （门面设计模式？）
 */
public class CommandLineInputHandler {

    // 仓库处理依赖，依赖于接口
    private ToDoRepository toDoRepository = new InMemoryToDoRepository();

    // 打印选项到控制台
    public void printOptions() {
        System.out.println("\n--- To Do Application ---");
        System.out.println("Please make a choice:");
        System.out.println("(a)ll items");
        System.out.println("(f)ind a specific item");
        System.out.println("(i)nsert a new item");
        System.out.println("(u)pdate an existing item");
        System.out.println("(d)elete an existing item");
        System.out.println("(e)xit");
    }

    // 读取用户的输入
    public String readInput() {
        return System.console().readLine("> ");
    }

    // 根据枚举命令对象，进行对应的操作
    public void processInput(CommandLineInput input) {
        if (input == null) {
            handleUnknowInput();
        } else {
            switch (input) {
                case FIND_ALL:
                    printAllToDoItems();
                    break;
                case FIND_BY_ID:
                    printToDoItem();
                    break;
                case INSERT:
                    insertToDoItem();
                    break;
                case UPDATE:
                    updateToDoItem();
                    break;
                case DELETE:
                    deleteToDoItem();
                    break;
                case EXIT:
                    break;
                default:
                    handleUnknowInput();
            }
        }
    }

    // 打印全部 Items
    private void printAllToDoItems() {
        Collection<ToDoItem> toDoItems = toDoRepository.findAll();
        if (toDoItems.isEmpty()) {
            System.out.println("Nothing to do. Go relax!");
        } else {
            for (ToDoItem toDoItem : toDoItems) {
                System.out.println(toDoItem);
            }
        }
    }

    // 提示用户输入 id
    private Long askForItemId() {
        System.out.println("Please enter the item ID:");
        String input = readInput();
        return Long.parseLong(input);
    }

    // 找到 id 对应的 Item 对象
    private ToDoItem findToDoItem() {
        Long id = askForItemId();
        ToDoItem toDoItem = toDoRepository.findById(id);
        if (toDoItem == null) {
            // 打印错误提示
            System.err.println("To do item with ID " + id + " could not be found.");
        }
        return toDoItem;
    }

    // 打印 id 对应的 Item 对象
    private void printToDoItem() {
        ToDoItem toDoItem = findToDoItem();
        if (toDoItem != null) {
            System.out.println(toDoItem);
        }
    }

    // 新建 ToDoItem 对象，根据用户输入
    private ToDoItem askForNewToDoAction() {
        ToDoItem toDoItem = new ToDoItem();
        System.out.println("Please enter the name of the item:");
        toDoItem.setName(readInput());
        return toDoItem;
    }

    // 插入 Item
    private void insertToDoItem() {
        ToDoItem toDoItem = askForNewToDoAction();
        Long id = toDoRepository.insert(toDoItem);
        System.out.println("Successfully inserted to do item with ID " + id + ".");
    }

    // 更新 Item
    private void updateToDoItem() {
        ToDoItem toDoItem = findToDoItem();

        if (toDoItem != null) {
            System.out.println(toDoItem);
            System.out.println("Please enter the name of the item:");
            toDoItem.setName(readInput());
            System.out.println("Please enter the done status the item:");
            toDoItem.setCompleted(Boolean.parseBoolean(readInput()));
            toDoRepository.update(toDoItem);
            System.out.println("Successfully updated to do item with ID " + toDoItem.getId() + ".");
        }
    }

    // 移除 Item
    private void deleteToDoItem() {
        ToDoItem toDoItem = findToDoItem();

        if (toDoItem != null) {
            toDoRepository.delete(toDoItem);
            System.out.println("Successfully deleted to do item with ID " + toDoItem.getId() + ".");
        }
    }

    // 输入选项无效提示
    private void handleUnknowInput() {
        System.out.println("Please select a valid option!");
    }
}
