package com.example.HopDrop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class NewOrder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button sbtn = (Button) findViewById(R.id.save_btn);
        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: add code here to program the SAVE action
                // put them into a new object then put that object into firestore
                // update "from" so that it is a dropdown
                TextInputLayout fromLayout = findViewById(R.id.from);
                TextInputLayout toLayout = findViewById(R.id.to);
                TextInputLayout feeLayout = findViewById(R.id.fee);
                TextInputLayout detailsLayout = findViewById(R.id.additional_details);
                String from = fromLayout.getEditText().getText().toString();
                String to = toLayout.getEditText().getText().toString();
                String fee = feeLayout.getEditText().getText().toString();
                String details = detailsLayout.getEditText().getText().toString();

                if (TextUtils.isEmpty(from) || TextUtils.isEmpty(to) || TextUtils.isEmpty(fee)) {
                    Toast.makeText(getApplicationContext(), "Please fill out the required fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (fee.toString().chars().filter(ch -> ch == '.').count() > 1) {
                        Toast.makeText(getApplicationContext(), "Please fix the fee input", Toast.LENGTH_SHORT).show();
                    } else {
                        // add to firebase
                        finish();
                    }

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}