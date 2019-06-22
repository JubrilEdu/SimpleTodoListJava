package com.test.mytodolist.interfaces;

public interface View {
    void setUpView();

    void reloadData();

    void showTodoInputView();

    void createTodoItem(String input);
}
