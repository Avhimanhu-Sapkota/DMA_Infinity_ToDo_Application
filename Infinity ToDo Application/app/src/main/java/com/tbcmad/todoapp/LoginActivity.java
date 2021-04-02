package com.tbcmad.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btn_login);
        username = (EditText) findViewById(R.id.plntxt_login_username) ;
        password = (EditText) findViewById(R.id.plntxt_login_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (username.getText().toString().trim().length() == 0){
                    username.setError(getString(R.string.errorEnterUsername));
                }
                else if (password.getText().toString().trim().length() == 0){
                    password.setError(getString(R.string.errorEnterPassword));
                }
                else{

                    if(username.getText().toString().trim().equals("admin") && password.getText().toString().trim().equals("password"))
                    {
                        SharedPreferences preference = getApplicationContext().getSharedPreferences("todo_pref",  0);
                        SharedPreferences.Editor editor = preference.edit();
                        editor.putBoolean("authentication", true);
                        editor.commit();

                        Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        password.setError(getString(R.string.invalidCredentials));
                    }

                }
            }
        });
    }
}