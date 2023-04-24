package com.example.HopDrop;

import static com.example.HopDrop.LoginActivity.username_string;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

public class PastDelivery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_delivery);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Order mOrder = (Order) getIntent().getSerializableExtra("order");
        FirebaseFirestore fb = FirebaseFirestore.getInstance();

        TextView name = findViewById(R.id.customer_name_delivery);
        TextView fee = findViewById(R.id.fee_label_delivery);
        TextView pickup = findViewById(R.id.pickup_location_label_delivery);
        TextView dest = findViewById(R.id.delivery_location_label_delivery);
        TextView add_details = findViewById(R.id.additional_details_delivery);



        fb.collection("user_id").document(mOrder.getCustomerName()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //get first and last name
                DocumentSnapshot doc = task.getResult();
                String full_name = doc.get("firstName") + " " + doc.get("lastName");
                name.setText(full_name);
                String string = "Fee: " + mOrder.getFee();
                fee.setText(string);
                string = "Destination: " + mOrder.getDest();
                dest.setText(string);
                string = "Pickup location: " + mOrder.getFrom();
                pickup.setText(string);
                add_details.setText(mOrder.getNotes());
            }
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