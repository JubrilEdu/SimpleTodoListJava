package com.test.mytodolist.interfaces;

import com.test.mytodolist.data.TodoItem;

import java.util.List;

public interface Presenter {
    void saveTodoItem(String Item);

    TodoItem getTodoItem(String id);

    List<TodoItem> getItems();

    void deleteTodoItem(int id);
}
