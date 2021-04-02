package com.tbcmad.todoapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tbcmad.todoapp.model.ETodo;
import com.tbcmad.todoapp.viewModel.TodoViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class    EditTodoFragment extends Fragment {

    View rootView;
    CheckBox taskCompleted;
    RadioGroup rgTaskPriority;
    Button taskSaveButton, taskCancelButton;
    EditText taskTitle, taskDescription, taskDate;

    int todoTaskId;

    public static final int HIGH_PRIORITY = 1;
    public static final int MEDIUM_PRIORITY = 2;
    public static final int LOW_PRIORITY = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView =  inflater.inflate(R.layout.fragment_edit_todo, container, false);

        taskTitle = rootView.findViewById(R.id.txtName_edit_todo_title);
        taskDescription = rootView.findViewById(R.id.txtName_edit_todo_description);
        taskDate = rootView.findViewById(R.id.txtName_edit_todo_Date);
        rgTaskPriority = rootView.findViewById(R.id.edit_fragment_rg_priority);
        taskCompleted = rootView.findViewById(R.id.chk__edit_todo_complete);
        taskSaveButton = rootView.findViewById(R.id.btn_edit_todo_save);
        taskCancelButton = rootView.findViewById(R.id.btn_edit_todo_cancel);

        taskCompleted.setVisibility(View.GONE);

        loadUpdateData();

        taskDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN)
                    DisplayTestDate();
                return false;

            }
        });

        taskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = rgTaskPriority.getCheckedRadioButtonId();

                if (taskTitle.getText().toString().trim().length() == 0){
                    taskTitle.setError(getString(R.string.errorTitleMsg));
                }
                else if (taskDate.getText().toString().trim().length() == 0){
                    taskDate.setError(getString(R.string.errorDateMsg));
                }
                else if (index == -1) {
                    taskTitle.setError(getString(R.string.errorPriorityMsg));
                }
                else{
                    SaveTodo();
                }
            }
        });

        taskCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAlertCancel();
            }
        });

        return rootView;
    }

    void SaveTodo(){
        ETodo entityTodo = new ETodo();
        Date todoDate = new Date();

        int checkedPriority = -1;
        int priority = 0;

        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            todoDate = format.parse(taskDate.getText().toString());
        }
        catch (ParseException ex){
            ex.printStackTrace();
        }

        checkedPriority = rgTaskPriority.getCheckedRadioButtonId();

        switch (checkedPriority){
            case R.id.rb_edit_todo_high:
                priority = HIGH_PRIORITY;
                break;
            case R.id.rb_edit_todo_medium:
                priority = MEDIUM_PRIORITY;
                break;
            case R.id.rb_edit_todo_low:
                priority = LOW_PRIORITY;
                break;
        }

        entityTodo.setTitle(taskTitle.getText().toString());
        entityTodo.setDescription(taskDescription.getText().toString());
        entityTodo.setTodoDate(todoDate);
        entityTodo.setPriority(priority);
        entityTodo.setCompleted(taskCompleted.isChecked());

        TodoViewModel viewModel  = new ViewModelProvider(this).get(TodoViewModel.class);

        if(todoTaskId != -1){
            entityTodo.setId(todoTaskId);
            viewModel.update(entityTodo);
        }
        else {
            viewModel.insert(entityTodo);
        }

        Toast.makeText(getActivity(), getString(R.string.todoSavedMsg),Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
    }

    void loadUpdateData() {
        todoTaskId = getActivity().getIntent().getIntExtra("TodoId", -1);
        TodoViewModel viewModel = new ViewModelProvider(this).get(TodoViewModel.class);

        if (todoTaskId != -1) {
            ETodo todo = viewModel.getTodoById(todoTaskId);

            taskSaveButton.setText(getString(R.string.btnUpdate));
            taskCompleted.setVisibility(View.VISIBLE);
            taskTitle.setText(todo.getTitle());
            taskDescription.setText(todo.getDescription());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            taskDate.setText(format.format( todo.getTodoDate()));

            switch (todo.getPriority()){
                case 1:
                    rgTaskPriority.check(R.id.rb_edit_todo_high);
                    break;
                case 2:
                    rgTaskPriority.check(R.id.rb_edit_todo_medium);
                    break;
                case 3:
                    rgTaskPriority.check(R.id.rb_edit_todo_low);
                    break;
            }

            taskCompleted.setChecked(todo.isCompleted());
        }
    }

    void DisplayTestDate() {
        Calendar calendar = Calendar.getInstance();

        int calendarYear = calendar.get(Calendar.YEAR);
        int calendarMonth = calendar.get(Calendar.MONTH);
        int calendarDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (month >= 0)
                    month = month + 1;

                taskDate.setText(year + "-" + month + "-" + dayOfMonth);
            }
        }, calendarYear, calendarMonth, calendarDay);

        pickerDialog.show();
    }

    void ShowAlertCancel(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

        alertDialog.setMessage(getString(R.string.alert_cancel))
                .setTitle(R.string.app_name)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)

                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                })

                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        alertDialog.show();
    }

}