package com.example.HopDrop;

import static android.content.ContentValues.TAG;
import static com.example.HopDrop.LoginActivity.username_string;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConfirmOrderActivity extends AppCompatActivity {
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Button confirm_button = findViewById(R.id.confirm_btn);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the Order object passed from the previous activity
        mOrder = (Order) getIntent().getSerializableExtra("order");

        // Update the UI with the Order details
        //TextView customerNameTextView = findViewById(R.id.customer_name);
        //customerNameTextView.setText(mOrder.getCustomer_name());

        TextView srcTextView = findViewById(R.id.src);
        srcTextView.setText(mOrder.getFrom());

        TextView destTextView = findViewById(R.id.dest);
        destTextView.setText(mOrder.getDest());

        TextView feeTextView = findViewById(R.id.fee);
        feeTextView.setText(String.valueOf(mOrder.getFee()));

        TextView notesTextView = findViewById(R.id.notes);
        notesTextView.setText(String.valueOf(mOrder.getNotes()));

        confirm_button.setOnClickListener(view -> {
            // Define what should happen when the button is clicked
            // Move the delivery to past deliveries and past orders
            DocumentReference userRef = rootRef.collection("user_id").document(username_string);
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    List<Map<String, Object>> currentDeliveriesData = (List<Map<String, Object>>) document.get("currentDeliveries");
                    //remove from current deliveries, add to past deliveries
                    int index = -1;
                    if (currentDeliveriesData != null) {
                        for (Map<String, Object> orderData : currentDeliveriesData) {
                            index++;
                            String id = (String) orderData.get("orderID");
                            if (!Objects.equals(id, mOrder.getOrderID())) {
                                continue;
                            }
                            // move to past orders
                            userRef.update("pastDeliveries", FieldValue.arrayUnion(mOrder));
                            currentDeliveriesData.remove(index);
                            //figure out how to delete
                            //userRef.update("currentDeliveries", FieldValue.arrayRemove(mOrder));
                            userRef.update("currentDeliveries", FieldValue.arrayRemove(orderData));
                            break;
                        }
                    }
                }
            });

            Intent intent = new Intent(ConfirmOrderActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
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

