package com.test.mytodolist.presenters;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.test.mytodolist.helpers.DatabaseHelper;
import com.test.mytodolist.data.TodoItem;
import com.test.mytodolist.interfaces.Presenter;
import com.test.mytodolist.interfaces.View;

import java.util.ArrayList;
import java.util.List;

public class TodoItemPresenterImpl implements Presenter {
    private DatabaseHelper databaseHelper;
    private View view;

    public TodoItemPresenterImpl(DatabaseHelper databaseHelper, View view){
        this.databaseHelper = databaseHelper;
        this.view = view;
    }

    @Override
    public void saveTodoItem(String item) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(databaseHelper.COLUMN_ITEM, item);
        db.insert(databaseHelper.TABLE_NAME, null, values);
        String countQuery = "SELECT  * FROM " +databaseHelper.TABLE_NAME;
         db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        Log.e("size",String.valueOf(count));
        db.close();
    }

    @Override
    public TodoItem getTodoItem(String id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(databaseHelper.TABLE_NAME,
                new String[]{databaseHelper.COLUMN_ID, databaseHelper.COLUMN_ITEM},
                databaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TodoItem todoItem = new TodoItem();
        todoItem.setId(cursor.getInt(cursor.getColumnIndex(databaseHelper.COLUMN_ID)));
        todoItem.setItem(cursor.getString(cursor.getColumnIndex(databaseHelper.COLUMN_ITEM)));
        cursor.close();

        return todoItem;
    }

    @Override
    public List<TodoItem> getItems() {
        List<TodoItem> todoItems = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + databaseHelper.TABLE_NAME;
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TodoItem todoItem = new TodoItem();
                todoItem.setId(cursor.getInt(cursor.getColumnIndex(databaseHelper.COLUMN_ID)));
                todoItem.setItem(cursor.getString(cursor.getColumnIndex(databaseHelper.COLUMN_ITEM)));
                todoItems.add(todoItem);
            } while (cursor.moveToNext());
        }

        databaseHelper.getReadableDatabase().close();
        return todoItems;
    }

    @Override
    public void deleteTodoItem(int id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(databaseHelper.TABLE_NAME, databaseHelper.COLUMN_ID+ " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        view.reloadData();
    }
}
