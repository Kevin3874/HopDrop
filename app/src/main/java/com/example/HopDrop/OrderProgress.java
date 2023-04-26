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
            fb.collection("user_id").document(deliverer).addSnapshotListener((value, error) -> {
                String full_name = value.get("firstName") + " " + value.get("lastName");
                name.setText(full_name);
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
        progress_bar.setStepsNumber(4);
        steps.add("Pending"); //Order is pending
        steps.add("Accepted"); // order.getState() == 0
        steps.add("Picked Up"); // order.getState() == 1
        steps.add("Delivered"); // order.getState() == 2
        progress_bar.setSteps(steps);

        //if no one has picked up
        // if firebase orderID/deliverer is empty, return default page
        /*
        if (Objects.equals(mOrder.getOrderID(), "") || Objects.equals(mOrder.getDeliverer(), "")) {
            return;
        }

         */

        //if updated while not open
        updateProgress();


    }


    private void updateProgress() {
        System.out.println("Does it ever get into here: " + String.valueOf(mOrder.getDeliverer()));
        if (Objects.equals(mOrder.getDeliverer(), "")) {
            fb.collection("user_id").document(mOrder.getCustomerName()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        return;
                    }
                    // Set Order object fields using orderData map
                    System.out.println("555555");
                    for (Map<String, Object> orderData : (List<Map<String, Object>>) value.get("currentOrders")) {
                        System.out.println("ininin");
                        System.out.println("Order list" + value.get("currentOrders"));
                        if (orderData.get("orderID").equals("test")) {
                            System.out.println("hit");
                        }
                        System.out.println("This is the first comparison: " + orderData.get("orderID") + " : " + mOrder.getOrderID());
                        System.out.println("This is the second comparison: " + String.valueOf(orderData.get("state")) + " : " + String.valueOf(orderData.get("state")).compareTo("-1"));
                        //Make another look to update the mOrder deliverer from the deliverer side
                        if (Objects.equals(orderData.get("orderID"), mOrder.getOrderID()) && String.valueOf(orderData.get("state")).compareTo("-1") != 0) {
                            System.out.println("8");
                            mOrder.setState(Integer.parseInt(String.valueOf(orderData.get("state"))));
                            mOrder.setOrderID(String.valueOf(orderData.get("orderID")));
                            System.out.println("9");
                            Log.d("Order state set at start", "onEvent" + mOrder.getState());
                        }
                    }
                    System.out.println("This is the state in here: " + mOrder.getState());
                    if (mOrder.getState() == 0) {
                        progress_bar.go(1, true);
                    }
                }
            });
        } else {
            fb.collection("user_id").document(mOrder.getDeliverer()).addSnapshotListener((value, error) -> {
                if (error != null) {
                    return;
                }
                // Set Order object fields using orderData map
                System.out.println("5");
                for (Map<String, Object> orderData : (List<Map<String, Object>>) Objects.requireNonNull(value.get("currentDeliveries"))) {
                    if (Objects.equals(orderData.get("orderID"), mOrder.getOrderID())) {
                        System.out.println("6");
                        mOrder.setState(Integer.parseInt(String.valueOf(orderData.get("state"))));
                        System.out.println("7");
                        Log.d("Order state set", "onEvent" + mOrder.getState());
                    }
                }
                if (mOrder.getState() == 0) {
                    progress_bar.go(1, true);
                }
                if (mOrder.getState() == 1) {
                    progress_bar.go(1, true);
                }
                if(mOrder.getState() == 2) {
                    progress_bar.go(2, true);
                }
            });
        }

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