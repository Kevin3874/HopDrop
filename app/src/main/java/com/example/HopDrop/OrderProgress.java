package com.example.HopDrop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderProgress extends AppCompatActivity {
    StepView progress_bar;
    List<String> steps = new ArrayList<>();
    FirebaseFirestore fb = FirebaseFirestore.getInstance();
    StorageReference reference;
    CircleImageView profile_image;
    Order mOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_progress);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOrder = (Order) getIntent().getSerializableExtra("order");

        String deliverer = mOrder.getDeliverer();
        TextView name = findViewById(R.id.customer_name_accept);
        if (!deliverer.equals("Pending Deliverer")) {
            fb.collection("user_id").document(deliverer).addSnapshotListener((value, error) -> {
                String full_name = value.get("firstName") + " " + value.get("lastName");
                name.setText(full_name);
            });

        } else {
            name.setText("Pending Deliverer");
        }

        if (!mOrder.getDeliverer().equals("Pending Deliverer")) {
            reference = FirebaseStorage.getInstance().getReference().child("profile_images").child(mOrder.getDeliverer() + ".jpeg");
            profile_image = findViewById(R.id.customer_profile_image);
            reference.getDownloadUrl().addOnSuccessListener((OnSuccessListener<Uri>) uri -> {
                if (uri != null) { // add null check here
                    Glide.with(OrderProgress.this).load(uri).error(R.drawable.ic_launcher_background)
                            .into(profile_image);
                };
            });
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

        progress_bar = findViewById(R.id.step_view_progress);
        progress_bar.setStepsNumber(4);
        steps.add("Pending"); //Order is pending
        steps.add("Accepted"); // order.getState() == 0
        steps.add("Picked Up"); // order.getState() == 1
        steps.add("Delivered"); // order.getState() == 2
        progress_bar.setSteps(steps);

        //if updated while not open
        updateProgress();

    }


    private void updateProgress() {
        //If not one has picked up yet
        if (Objects.equals(mOrder.getDeliverer(), "Pending Deliverer")) {
            fb.collection("user_id").document(mOrder.getCustomerName()).addSnapshotListener((value, error) -> {
                if (error != null) {
                    return;
                }
                // Set Order object fields using orderData map
                for (Map<String, Object> orderData : (List<Map<String, Object>>) value.get("currentOrders")) {
                    //Make another look to update the mOrder deliverer from the deliverer side
                    if (Objects.equals(orderData.get("orderID"), mOrder.getOrderID()) && String.valueOf(orderData.get("state")).compareTo("-1") != 0) {
                        mOrder.setState(Integer.parseInt(String.valueOf(orderData.get("state"))));
                        mOrder.setOrderID(String.valueOf(orderData.get("orderID")));
                        mOrder.setDeliverer(String.valueOf(orderData.get("deliverer_name")));
                        Log.d("Order state set", "onEvent" + mOrder.getState());
                    }
                }
                if (mOrder.getState() == 0) {
                    progress_bar.go(1, true);
                    Intent intent = new Intent(this, OrderProgress.class);
                    intent.putExtra("order", mOrder);
                    startActivity(intent);
                    finish();
                }

            });
        } else {
            fb.collection("user_id").document(mOrder.getDeliverer()).addSnapshotListener((value, error) -> {
                if (error != null) {
                    return;
                }
                // Set Order object fields using orderData map
                boolean exists = false;
                for (Map<String, Object> orderData : (List<Map<String, Object>>) Objects.requireNonNull(value.get("currentDeliveries"))) {
                    if (Objects.equals(orderData.get("orderID"), mOrder.getOrderID())) {
                        mOrder.setState(Integer.parseInt(String.valueOf(orderData.get("state"))));
                        exists = true;
                        Log.d("Order state set", "onEvent" + mOrder.getState());
                        break;
                    }
                }
                if (Objects.equals(mOrder.getState(), 0)) {
                    progress_bar.go(1, true);
                }
                if (Objects.equals(mOrder.getState(), 1)) {
                    progress_bar.go(2, true);
                }
                if (!exists) {
                    progress_bar.go(3, true);
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("order", mOrder);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}