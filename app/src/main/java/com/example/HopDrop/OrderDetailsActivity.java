package com.example.HopDrop;

import static com.example.HopDrop.LoginActivity.username_string;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class OrderDetailsActivity extends AppCompatActivity {
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Get the Order object passed from the previous activity
        mOrder = (Order) getIntent().getSerializableExtra("order");

        // Update the UI with the Order details
        TextView customerNameTextView = findViewById(R.id.customer_name_accept);
        String string = "";
        rootRef.collection("user_id").document(mOrder.getCustomerName()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //get first and last name
                DocumentSnapshot doc = task.getResult();
                String full_name = doc.get("firstName") + " " + doc.get("lastName");
                customerNameTextView.setText(full_name);
            }
        });

        TextView srcTextView = findViewById(R.id.pickup_location_accept);
        string = "Pickup location: " + mOrder.getFrom();
        srcTextView.setText(string);

        TextView destTextView = findViewById(R.id.destination_accept);
        string = "Delivery location: " + mOrder.getDest();
        destTextView.setText(string);

        TextView feeTextView = findViewById(R.id.fee_accept);
        string = "Fee: " + mOrder.getFee();
        feeTextView.setText(string);

        TextView notesTextView = findViewById(R.id.additional_details_progress);
        notesTextView.setText(String.valueOf(mOrder.getNotes()));

        Button acceptButton = findViewById(R.id.accept_button);

        acceptButton.setOnClickListener(view -> {
            DocumentReference orderRef = rootRef.collection("orders").document(mOrder.getOrderID());
            // What happens when the user clicks accept
            Intent intent = new Intent(OrderDetailsActivity.this, CustomerUpdateActivity.class);
            //delete from active orders collections
            orderRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Remove the order from the Firestore collection
                        orderRef.delete();
                        // Add it to the user's collection
                    } else {
                        Toast.makeText(getApplicationContext(), "Order not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error getting order", Toast.LENGTH_SHORT).show();
                }
            });
            //add to deliverer
            DocumentReference userRef = rootRef.collection("user_id").document(username_string);
            mOrder.setDeliverer(username_string);
            mOrder.setState(0);
            userRef.update("currentDeliveries", FieldValue.arrayUnion(mOrder));


            //add to orders for user
            DocumentReference userRef1 = rootRef.collection("user_id").document(mOrder.getCustomerName());
            userRef1.update("currentOrders", FieldValue.arrayUnion(mOrder));


            userRef1.get().addOnCompleteListener(task -> {
                DocumentSnapshot document = task.getResult();
                List<Map<String, Object>> currentDeliveriesData = (List<Map<String, Object>>) document.get("currentOrders");
                if (currentDeliveriesData != null) {
                    for (Map<String, Object> orderData : currentDeliveriesData) {
                        if (Objects.equals(orderData.get("orderID"), "test")) {
                            userRef1.update("currentOrders", FieldValue.arrayRemove(orderData));
                        }
                    }
                }
            });



            intent.putExtra("order", mOrder);
            startActivity(intent);
            finish();
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

