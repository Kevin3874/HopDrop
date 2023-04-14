package com.example.HopDrop;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

public class CustomerUpdateActivity extends AppCompatActivity {
    private Order mOrder;
    List<String> steps = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_update);

        // Add the code for the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the Order object passed from the previous activity
        mOrder = (Order) getIntent().getSerializableExtra("order");
        Button action_button = findViewById(R.id.pickup_button);
        StepView progress_bar = findViewById(R.id.step_view);
        progress_bar.setStepsNumber(3);
        steps.add("Order Accepted");
        steps.add("Picked Up");
        steps.add("Delivered");
        progress_bar.setSteps(steps);
        if (mOrder.getState() == 0) {
            action_button.setText("Picked Up");
        } else if (mOrder.getState() == 1) {
            progress_bar.go(1, true);
            action_button.setText("Delivered");
        }

        // Update the UI with the Order details
        //TextView customerNameTextView = findViewById(R.id.customer_name);
        //customerNameTextView.setText(mOrder.getCustomer_name());

        TextView srcTextView = findViewById(R.id.pickup_location_label);
        srcTextView.setText(mOrder.getFromLocation());

        TextView destTextView = findViewById(R.id.delivery_location_label);
        destTextView.setText(mOrder.getDest());

        TextView feeTextView = findViewById(R.id.fee_label);
        feeTextView.setText(String.valueOf(mOrder.getFee()));

        TextView notesTextView = findViewById(R.id.additional_details_label);
        notesTextView.setText(String.valueOf(mOrder.getNotes()));


        Button cancelButton = findViewById(R.id.cancel_btn);
        action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (mOrder.getState() == 0) {
                    mOrder.setState(1);
                    progress_bar.go(1, true);
                    action_button.setText("Delivered");
                } else if (mOrder.getState() == 1) {
                    mOrder.setState(2);
                    progress_bar.go(1, true);
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

