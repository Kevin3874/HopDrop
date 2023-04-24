package com.example.HopDrop;

import static android.content.ContentValues.TAG;
import static com.example.HopDrop.LoginActivity.username_string;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrderProgress extends AppCompatActivity {
    StepView progress_bar;
    List<String> steps = new ArrayList<>();
    FirebaseFirestore fb = FirebaseFirestore.getInstance();
    FirebaseHelper firebaseHelper = new FirebaseHelper();
    Order mOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //TODO: Complete this class
        // From Users side
        // When steps == 2:
        //    Remove from current orders
        //    Move to past orders
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_progress);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOrder = (Order) getIntent().getSerializableExtra("order");

        String deliverer = mOrder.getDeliverer();
        String orderId = mOrder.getOrderID();
        TextView name = findViewById(R.id.customer_name_accept);
        if (!deliverer.equals("")) {
            fb.collection("user_id").document(deliverer).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        //get first and last name
                        DocumentSnapshot doc = task.getResult();
                        String full_name = doc.get("firstName") + " " + doc.get("lastName");
                        name.setText(full_name);
                    }
                }
            });
        } else {
            name.setText("Pending Deliverer");
        }



        TextView srcTextView = findViewById(R.id.pickup_location_accept);
        String string = "Pickup location: " + mOrder.getFrom();
        srcTextView.setText(string);

        TextView destTextView = findViewById(R.id.destination_accept);
        string = "Destination: " + mOrder.getDest();
        destTextView.setText(string);

        TextView feeTextView = findViewById(R.id.fee_accept);
        string = "Fee: " + mOrder.getFee();
        feeTextView.setText(string);

        TextView notesTextView = findViewById(R.id.additional_details_progress);
        notesTextView.setText(mOrder.getNotes());

        progress_bar = findViewById(R.id.step_view);
        progress_bar.setStepsNumber(3);
        steps.add("Order Accepted"); // order.getState() == 0
        steps.add("Picked Up"); // order.getState() == 1
        steps.add("Delivered"); // order.getState() == 2
        progress_bar.setSteps(steps);

        //if no one has picked up
        // if firebase orderID/deliverer is empty, return default page
        if (Objects.equals(mOrder.getOrderID(), "") || Objects.equals(mOrder.getDeliverer(), "")) {
            return;
        }

        //if updated while not open
        updateProgress();



    }


    private void updateProgress() {
        fb.collection("user_id").document(username_string).addSnapshotListener((value, error) -> {
            if (error != null) {
                return;
            }
            // Set Order object fields using orderData map
            for (Map<String, Object> orderData : (List<Map<String, Object>>) value.get("currentDeliveries")) {
                if (orderData.get("orderID").equals(mOrder.getOrderID())) {
                    mOrder.setState(Integer.parseInt(String.valueOf(orderData.get("state"))));
                    Log.d("Order state set", "onEvent" + mOrder.getState());
                }
            }

            if (mOrder.getState() == 1) {
                progress_bar.go(1, true);
            }
            if(mOrder.getState() == 2) {
                progress_bar.go(2, true);
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