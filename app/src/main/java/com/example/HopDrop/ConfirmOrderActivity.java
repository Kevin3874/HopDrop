package com.example.HopDrop;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmOrderActivity extends AppCompatActivity {

    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        // Get the Order object passed from the previous activity
        mOrder = (Order) getIntent().getSerializableExtra("order");

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
    }
}

