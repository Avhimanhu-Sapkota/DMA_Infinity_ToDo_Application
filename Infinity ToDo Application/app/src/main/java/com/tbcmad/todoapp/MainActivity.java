package com.tbcmad.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tbcmad.todoapp.viewModel.TodoViewModel;

public class MainActivity extends AppCompatActivity {

    Fragment fragment;
    FragmentManager fragmentManager;
    FloatingActionButton floatingActionButton;
    AlertDialog.Builder mAlterDialog;
    TodoViewModel mTodoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        fragment = new ListTodoFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.list_activity_container, fragment)
                .commit();

        floatingActionButton = findViewById(R.id.faButton_main_activity);

        floatingActionButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        mTodoViewModel  = ViewModelProviders.of(this).get(TodoViewModel.class);

        switch (item.getItemId()){

            case R.id.mnu_delete_all:
                mAlterDialog = new AlertDialog.Builder(this);
                mAlterDialog.setMessage(getString(R.string.deleteAllMsg))
                        .setCancelable(false)
                        .setTitle(getString(R.string.app_name))
                        .setIcon(R.mipmap.ic_launcher);
                mAlterDialog.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTodoViewModel.deleteAll();
                    }
                });
                mAlterDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mAlterDialog.show();
                break;

            case R.id.mnu_delete_completed:
                mAlterDialog = new AlertDialog.Builder(this);
                mAlterDialog.setMessage(getString(R.string.deleteCompletedMsg))
                        .setCancelable(false)
                        .setTitle(getString(R.string.app_name))
                        .setIcon(R.mipmap.ic_launcher);
                mAlterDialog.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTodoViewModel.deleteCompleted();
                    }
                });
                mAlterDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mAlterDialog.show();
                break;

            case R.id.mnu_logout:

                mAlterDialog = new AlertDialog.Builder(this);
                mAlterDialog.setMessage(getString(R.string.alert_logout))
                        .setCancelable(false)
                        .setTitle(getString(R.string.app_name))
                        .setIcon(R.mipmap.ic_launcher);
                mAlterDialog.setPositiveButton(getString(R.string.logout), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences preferences = getApplicationContext().getSharedPreferences("todo_pref", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                mAlterDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mAlterDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}