package com.test.mytodolist.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.test.mytodolist.R;
import com.test.mytodolist.presenters.TodoItemPresenterImpl;
import com.test.mytodolist.data.TodoItem;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {
    TodoItemPresenterImpl todoItemPresenter;

    public TodoListAdapter(TodoItemPresenterImpl todoItemPresenter){
        this.todoItemPresenter = todoItemPresenter;
    }
    @NonNull
    @Override
    public TodoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.individual_todo_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
       final TodoItem todoItem =  todoItemPresenter.getItems().get(i);
       viewHolder.todoItemTextField.setText(todoItem.getItem());
       viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               todoItemPresenter.deleteTodoItem(todoItem.getId());
           }
       });
    }


    @Override
    public int getItemCount() {
        return todoItemPresenter.getItems().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView todoItemTextField;
        public ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            todoItemTextField = (TextView) itemView.findViewById(R.id.textView);
            deleteButton = (ImageButton) itemView.findViewById(R.id.imageButton);
        }


    }

}
