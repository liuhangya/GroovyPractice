package com.fanda.todo.repository;

import com.fanda.todo.model.ToDoItem;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

// 可以用命令 gradle test 或 gradle test -i ，最好加上 -i ，可以知道具体的报错信息
public class InMemoryToDoRepositoryTest {
    private ToDoRepository inMemoryToDoRepository;

    @Before
    public void setUp() {
        // 在测试方法执行之前会先执行
        inMemoryToDoRepository = new InMemoryToDoRepository();
    }

    @Test
    public void testInsertToDoItem() {
        // 标记该方法作为测试用例运行
        ToDoItem newToDoItem = new ToDoItem();
        newToDoItem.setName("write unit test");
        Long newId = inMemoryToDoRepository.insert(newToDoItem);
        assertNotNull(newId);

        ToDoItem persistedToDoItem = inMemoryToDoRepository.findById(newId);
        assertNotNull(persistedToDoItem);
        assertEquals(newToDoItem, persistedToDoItem);
    }
}
