package com.fanda.todo.repository;

import com.fanda.todo.model.ToDoItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


/* 
 * 仓库接口实现类，数据保存在内存中
 */
public class InMemoryToDoRepository implements ToDoRepository {

    // 线程安全的标识符序列号
    private AtomicLong currentId = new AtomicLong();

    // 用于保存 todo 项的集合类
    private ConcurrentHashMap<Long, ToDoItem> toDos = new ConcurrentHashMap<Long, ToDoItem>();

    @Override
    public List<ToDoItem> findAll() {
        List<ToDoItem> toDoItems = new ArrayList<>(toDos.values());
        // 根据 id 属性进行排序
        Collections.sort(toDoItems);
        return toDoItems;
    }

    @Override
    public ToDoItem findById(Long id) {
        return toDos.get(id);
    }

    @Override
    public Long insert(ToDoItem toDoItem) {
        // 每次插入数据时，id 加 1
        Long id = currentId.incrementAndGet();
        toDoItem.setId(id);
        // 如果集合中不存在该 Item ，才添加到集合中
        toDos.putIfAbsent(id, toDoItem);
        return id;
    }

    @Override
    public void update(ToDoItem toDoItem) {
        // 如果存在于集合中，则替换该 Item
        toDos.replace(toDoItem.getId(), toDoItem);
    }

    @Override
    public void delete(ToDoItem toDoItem) {
        // 如果存在于集合中，则移除该 Item
        toDos.remove(toDoItem.getId());
    }

}
