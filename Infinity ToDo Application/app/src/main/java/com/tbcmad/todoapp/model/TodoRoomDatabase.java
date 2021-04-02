package com.tbcmad.todoapp.model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.tbcmad.todoapp.data.TodoDAO;

import java.util.Date;

@Database(entities = {ETodo.class}, version = 1, exportSchema = false)
public abstract class TodoRoomDatabase extends RoomDatabase {

    public abstract TodoDAO mTodoDao();
    public static TodoRoomDatabase TRD_INSTANCE;

    public static TodoRoomDatabase getDatabase(Context context){

        if(TRD_INSTANCE == null) {

            synchronized (TodoRoomDatabase.class){

                if(TRD_INSTANCE ==null){
                    TRD_INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodoRoomDatabase.class,
                            "todo.db")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addCallback(sCallback)
                            .build();
                }
            }
        }
        return TRD_INSTANCE;
    }

    private static class PopulateDbAsync extends AsyncTask<ETodo,Void,Void> {
        private  TodoDAO mTodoDAO;
        private PopulateDbAsync(TodoRoomDatabase db)
        {
            mTodoDAO=db.mTodoDao();
        }

        @Override
        protected Void doInBackground(ETodo... todos) {
            Date date=new Date();

            ETodo todo= new ETodo("Your Task Title will appear here!",
                    "Your tasks description will appear here! \n " +
                            "Tutorials:\n- Tap on + button to add task\n" +
                            "- Swipe task towards right to complete the task\n" +
                            "- Swipe task towards left to delete the task\n" +
                            "- Tap on the task to edit task details\n" +
                            "- Tap on the 3 dots to view menu items for other features", date,2,false);
            mTodoDAO.insert(todo);


            return null;
        }
    }

    private static RoomDatabase.Callback sCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsync(TRD_INSTANCE).execute();
        }
    };
}
