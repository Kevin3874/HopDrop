package com.example.HopDrop;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerUpdateActivity extends AppCompatActivity {
    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_update);

        // Add the code for the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the Order object passed from the previous activity
        mOrder = (Order) getIntent().getSerializableExtra("order");
        Button action_button = findViewById(R.id.pickup_button);
        if (mOrder.getState() == 0) {
            action_button.setText("Picked Up");
        } else if (mOrder.getState() == 1) {
            action_button.setText("Delivered");
        }

        // Update the UI with the Order details
        TextView customerNameTextView = findViewById(R.id.customer_name);
        customerNameTextView.setText(mOrder.getCustomer_name());

        TextView srcTextView = findViewById(R.id.src);
        srcTextView.setText(mOrder.getSrc());

        TextView destTextView = findViewById(R.id.dest);
        destTextView.setText(mOrder.getDest());

        TextView feeTextView = findViewById(R.id.fee);
        feeTextView.setText(String.valueOf(mOrder.getFee()));

        TextView notesTextView = findViewById(R.id.notes);
        notesTextView.setText(String.valueOf(mOrder.getNotes()));


        Button cancelButton = findViewById(R.id.cancel_btn);
        action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (mOrder.getState() == 0) {
                    mOrder.setState(1);
                    action_button.setText("Delivered");
                } else if (mOrder.getState() == 1) {
                    mOrder.setState(2);
                    Intent intent = new Intent(CustomerUpdateActivity.this, ConfirmOrderActivity.class);
                    intent.putExtra("order", mOrder);
                    startActivity(intent);
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(CustomerUpdateActivity.this, MainActivity.class);
                startActivity(intent);
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

