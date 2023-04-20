package com.example.HopDrop;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerUpdateDeliveredActivity extends AppCompatActivity {
    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_delivered);

        // Add the code for the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the Order object passed from the previous activity
        mOrder = (Order) getIntent().getSerializableExtra("order");

        // Update the UI with the Order details
        TextView customerNameTextView = findViewById(R.id.customer_name);
        customerNameTextView.setText(mOrder.getCustomerName());

        TextView srcTextView = findViewById(R.id.src);
        srcTextView.setText(mOrder.getFrom());

        TextView destTextView = findViewById(R.id.dest);
        destTextView.setText(mOrder.getDest());

        TextView feeTextView = findViewById(R.id.fee);
        feeTextView.setText(String.valueOf(mOrder.getFee()));

        TextView notesTextView = findViewById(R.id.notes);
        notesTextView.setText(String.valueOf(mOrder.getNotes()));

        Button deliveredButton = findViewById(R.id.delivery_button);
        Button cancelButton = findViewById(R.id.cancel_button);
        deliveredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Define what should happen when the button is clicked
                Intent intent = new Intent(CustomerUpdateDeliveredActivity.this, ConfirmOrderActivity.class);
                intent.putExtra("order", mOrder);
                startActivity(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Implement this feature
                Intent intent = new Intent(CustomerUpdateDeliveredActivity.this, MainActivity.class);
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
