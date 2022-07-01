package com.fanda.todo.repository;

import com.fanda.todo.model.ToDoItem;

import java.util.List;


/* 
 * 为了方便切换，定义仓库接口
 */
public interface ToDoRepository {
    List<ToDoItem> findAll();

    ToDoItem findById(Long id);

    Long insert(ToDoItem toDoItem);

    void update(ToDoItem toDoItem);

    void delete(ToDoItem toDoItem);
}