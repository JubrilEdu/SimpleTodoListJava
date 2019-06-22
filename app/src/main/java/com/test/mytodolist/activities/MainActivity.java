package com.test.mytodolist.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.test.mytodolist.helpers.DatabaseHelper;
import com.test.mytodolist.R;
import com.test.mytodolist.presenters.TodoItemPresenterImpl;
import com.test.mytodolist.adapters.TodoListAdapter;

public class MainActivity extends AppCompatActivity implements com.test.mytodolist.interfaces.View {
    RecyclerView recyclerView;
    TodoItemPresenterImpl todoItemPresenter;
    TodoListAdapter todoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        setUpView();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTodoInputView();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUpView() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this.getBaseContext());
        todoItemPresenter = new TodoItemPresenterImpl(databaseHelper,this);
        todoListAdapter = new TodoListAdapter(todoItemPresenter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(todoListAdapter);
    }

    @Override
    public void showTodoInputView() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.add_todoitem_view, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);

        final EditText inputNote = view.findViewById(R.id.editText);

        builder.setCancelable(false).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    inputNote.setError("Cannot Add empty entry");
                    return;
                } else {
                    createTodoItem(inputNote.getText().toString());
                    alertDialog.dismiss();
                }}
        });

    }

    @Override
    public void createTodoItem(String input) {
        todoItemPresenter.saveTodoItem(input);
        todoListAdapter.notifyDataSetChanged();

    }

    @Override
    public void reloadData() {
        todoListAdapter.notifyDataSetChanged();
    }
}
