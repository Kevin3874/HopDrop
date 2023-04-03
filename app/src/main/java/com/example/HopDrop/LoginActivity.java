package com.example.HopDrop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        Button login_button = findViewById(R.id.login_button);
        //Gets username text box
        TextInputLayout username = findViewById(R.id.username);
        //Gets password text box
        TextInputLayout password = findViewById(R.id.password);

        //save information
        Context context = getApplicationContext();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        //This would probably only be if we wanted to auto-populate the username box with previous login
        String name = myPrefs.getString("loginName", "Owner");

        //when user clicks to login
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                SharedPreferences.Editor editor = myPrefs.edit();
                //Currently only autopopulates/saves the email that was entered as the username
                //Need to figure out how to get the actual name of the person from the email (probably using the database)
                if (!TextUtils.isEmpty(username.getEditText().getText().toString())) {
                    String username_string = username.getEditText().getText().toString();
                    editor.putString("loginName", username_string);
                    editor.apply();
                    startActivity(intent);
                    finish();
                } else {
                    //Creates a toast if username isn't filled in
                    //Still need to do this for the password
                    Toast.makeText(getApplicationContext(),"Please fill in the required fields",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
