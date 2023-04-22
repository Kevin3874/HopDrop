package com.example.HopDrop;

import static com.example.HopDrop.LoginActivity.username_string;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.HopDrop.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class OrderDetailsActivity extends AppCompatActivity {
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the Order object passed from the previous activity
        mOrder = (Order) getIntent().getSerializableExtra("order");

        // Update the UI with the Order details
        TextView customerNameTextView = findViewById(R.id.customer_name);
        String string = "";
        customerNameTextView.setText(mOrder.getCustomerName());

        TextView srcTextView = findViewById(R.id.src);
        string = "Pickup location: " + mOrder.getFrom();
        srcTextView.setText(string);

        TextView destTextView = findViewById(R.id.dest);
        string = "Delivery location: " + mOrder.getDest();
        destTextView.setText(string);

        TextView feeTextView = findViewById(R.id.fee);
        string = "Fee: " + mOrder.getFee();
        feeTextView.setText(string);

        TextView notesTextView = findViewById(R.id.notes);
        notesTextView.setText(String.valueOf(mOrder.getNotes()));

        Button acceptButton = findViewById(R.id.accept_button);

        acceptButton.setOnClickListener(view -> {
            DocumentReference orderRef = rootRef.collection("orders").document(mOrder.getOrderID());
            // What happens when the user clicks accept
            Intent intent = new Intent(OrderDetailsActivity.this, CustomerUpdateActivity.class);
            intent.putExtra("order", mOrder);
            //delete from active orders collections
            orderRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
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
                }
            });
            //add to user
            DocumentReference userRef = rootRef.collection("user_id").document(username_string);
            mOrder.setDeliverer(username_string);
            userRef.update("currentDeliveries", FieldValue.arrayUnion(mOrder));

            startActivity(intent);
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

