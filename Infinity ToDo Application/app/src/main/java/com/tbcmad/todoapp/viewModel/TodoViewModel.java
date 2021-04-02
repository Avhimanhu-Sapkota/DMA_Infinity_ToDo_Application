package com.tbcmad.todoapp.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.tbcmad.todoapp.data.TodoRepository;
import com.tbcmad.todoapp.model.ETodo;

import java.util.List;

public class TodoViewModel extends AndroidViewModel {

    private TodoRepository mTodoRepository;
    private LiveData<List<ETodo>> allTodoTask;

    public TodoViewModel(@NonNull Application application){
        super(application);

        mTodoRepository=new TodoRepository(application);
        allTodoTask = mTodoRepository.getAllTodoList();
    }

    public LiveData<List<ETodo>> getAllTodos() {
        return allTodoTask;
    }

    public void insert(ETodo todo) {
        mTodoRepository.insert(todo);
    }

    public  void deleteById(ETodo todo){
        mTodoRepository.delete(todo);
    }

    public  void setCompleted(ETodo todo){
        mTodoRepository.setCompleted(todo);
    }

    public ETodo getTodoById(int id) {
        return mTodoRepository.getTodoById(id);
    }

    public void update(ETodo eTodo){
        mTodoRepository.update(eTodo);
    }

    public void deleteAll(){
        mTodoRepository.deleteAll();
    }

    public void deleteCompleted() {
        mTodoRepository.deleteCompleted();
    }

}
