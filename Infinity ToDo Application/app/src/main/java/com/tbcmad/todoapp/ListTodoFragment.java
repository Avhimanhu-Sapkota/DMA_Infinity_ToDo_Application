package com.tbcmad.todoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.EthiopicCalendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tbcmad.todoapp.model.ETodo;
import com.tbcmad.todoapp.viewModel.TodoViewModel;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListTodoFragment extends Fragment {

    View rootView;
    RecyclerView rvListTodo;
    TodoViewModel viewModel;
    AlertDialog.Builder mAlterDialog;
    CheckBox taskCompletion;
    int todoTaskId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_list_todo, container, false);
        rvListTodo = rootView.findViewById(R.id.list_item_todo_rv_list_todo);
        viewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        taskCompletion = rootView.findViewById(R.id.list_item_tv_status);

        LinearLayoutManager manager= new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvListTodo.setLayoutManager(manager);

        updateRootView();

        new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                List<ETodo> todoList = viewModel.getAllTodos().getValue();

                TodoAdaptor adaptor = new TodoAdaptor(todoList);
                ETodo todo = adaptor.getTodoAt(viewHolder.getAdapterPosition());

                mAlterDialog = new AlertDialog.Builder(getContext());
                mAlterDialog.setMessage(getString(R.string.deleteTask))
                        .setCancelable(false)
                        .setTitle(getString(R.string.app_name))
                        .setIcon(R.mipmap.ic_launcher);
                mAlterDialog.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewModel.deleteById(todo);
                    }
                });
                mAlterDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateRootView();
                    }
                });
                mAlterDialog.show();
            }
        }).attachToRecyclerView(rvListTodo);

        new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                List<ETodo> todoList = viewModel.getAllTodos().getValue();

                TodoAdaptor adaptor = new TodoAdaptor(todoList);
                ETodo todo = adaptor.getTodoAt(viewHolder.getAdapterPosition());

                todo.setCompleted(true);
                viewModel.setCompleted(todo);
                updateRootView();

            }
        }).attachToRecyclerView(rvListTodo);

        return  rootView;
    }

    void updateRootView(){
        viewModel.getAllTodos().observe(getViewLifecycleOwner(), new Observer<List<ETodo>>() {
            @Override
            public void onChanged(List<ETodo> eTodos) {
                TodoAdaptor adaptor = new TodoAdaptor(eTodos);
                rvListTodo.setAdapter(adaptor);
            }
        });
    }

    private class TodoHolder extends RecyclerView.ViewHolder{
        TextView taskTitle, taskDate, taskDescription, taskStatus;
        LinearLayout color_layout;

        public TodoHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_todo, parent, false));

            taskDate = itemView.findViewById(R.id.list_item_tv_date);
            taskTitle = itemView.findViewById(R.id.list_item_tv_title);
            taskStatus = itemView.findViewById(R.id.list_item_tv_status);
            taskDescription = itemView.findViewById(R.id.list_item_tv_description);
            color_layout = itemView.findViewById(R.id.layout_list_item_todo_color);

                    taskTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadUpdateItem();
                }
            });

            taskDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadUpdateItem();
                }
            });

            taskDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadUpdateItem();
                }
            });

            taskStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadUpdateItem();
                }
            });
        }

        void loadUpdateItem(){
            TodoAdaptor adaptor = new TodoAdaptor(viewModel.getAllTodos().getValue());

            int index = getAdapterPosition();
            ETodo todo = adaptor.getTodoAt(index);

            Intent intent = new Intent(getActivity(), EditActivity.class);
            intent.putExtra("TodoId",todo.getId());
            startActivity(intent);
        }

        public void bind(ETodo todo){
            SimpleDateFormat sdfObject = new SimpleDateFormat("yyyy-MM-dd");

            taskTitle.setText(todo.getTitle());
            taskDate.setText(sdfObject.format(todo.getTodoDate()));
            taskDescription.setText(todo.getDescription());

            if (todo.isCompleted()){

                taskStatus.setText(getString(R.string.completedTask));
            }
            else{
                taskStatus.setText(getString(R.string.onGoingTask));
            }

        }
    }

    public class TodoAdaptor extends RecyclerView.Adapter<TodoHolder>{
        List<ETodo> eTodoList;

        public TodoAdaptor(List<ETodo> todoList)
        {
            eTodoList = todoList;
        }

        @NonNull
        @Override
        public TodoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TodoHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TodoHolder holder, int position)
        {
            ETodo todo = eTodoList.get(position);
            LinearLayout layout =(LinearLayout)((ViewGroup)holder.taskTitle.getParent());
            LinearLayout color_layout =(LinearLayout)((ViewGroup)holder.color_layout);

            switch (todo.getPriority()) {
                case 1:
                    layout.setBackgroundColor(getResources().getColor(R.color.color_high));
                    color_layout.setBackgroundColor(getResources().getColor(R.color.color_high_dark));
                    break;
                case 2:
                    layout.setBackgroundColor(getResources().getColor(R.color.color_medium));
                    color_layout.setBackgroundColor(getResources().getColor(R.color.color_medium_dark));
                    break;
                case 3:
                    layout.setBackgroundColor(getResources().getColor(R.color.color_low));
                    color_layout.setBackgroundColor(getResources().getColor(R.color.color_low_dark));
                    break;
            }

            if (todo.isCompleted()){
                layout.setBackgroundColor(getResources().getColor(R.color.color_finished));
                color_layout.setBackgroundColor(getResources().getColor(R.color.color_finished_dark));
            }

            holder.bind(todo);
        }

        @Override
        public int getItemCount() {
            return eTodoList.size();
        }

        public ETodo getTodoAt(int position){
            return eTodoList.get(position);
        }
    }
}