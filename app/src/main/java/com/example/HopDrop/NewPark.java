package com.example.HopDrop;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewPark extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_park);

        Button sbtn = (Button) findViewById(R.id.save_btn);
        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add code here to program the SAVE action
                EditText new_park = findViewById(R.id.new_park);
                if (new_park.getText().toString().trim().length() == 0) {
                    Toast.makeText(NewPark.this,"Name cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    /*
                    Order newPark = new Park(new_park.getText().toString(), 0);
                    MainActivity.orders.add(newPark);
                    */
                    finish();
                }
            }
        });

        Button qbtn = (Button) findViewById(R.id.quit_btn);
        qbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}