package com.example.HopDrop;

import static android.content.ContentValues.TAG;
import static com.example.HopDrop.LoginActivity.username_string;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerUpdateActivity extends AppCompatActivity {
    private Order mOrder;
    List<String> steps = new ArrayList<String>();

    FirebaseFirestore fb = FirebaseFirestore.getInstance();
    String curr_state;
    StorageReference reference;
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_update);

        // Add the code for the back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Get the Order object passed from the previous activity
        mOrder = (Order) getIntent().getSerializableExtra("order");

        //pull from firebase to get the order
        Button action_button = findViewById(R.id.pickup_button);
        Button cancel_button = findViewById(R.id.cancel_btn);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fb.collection("user_id").document(username_string).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        List<Map<String, Object>> currentDeliveriesData = (List<Map<String, Object>>) doc.get("currentDeliveries");
                        if (currentDeliveriesData != null) {
                            // find the specific delivery with the customer name and update the state and then refresh on the available orders
                            for (Map<String, Object> orderData : currentDeliveriesData) {
                                String id = (String) orderData.get("orderID");
                                if (!Objects.equals(id, mOrder.getOrderID())) {
                                    continue;
                                }
                                // change the state to 0 and update orderlist and change the courier name
                                orderData.put("state", -1);
                                orderData.put("deliverer_name", "Pending Deliverer");
                                fb.collection("user_id").document(username_string).update("currentDeliveries", currentDeliveriesData);

                            }
                        }
                    } else {
                        // handle the error
                    }
                });
            }
        });
        StepView progress_bar = findViewById(R.id.step_view);
        progress_bar.setStepsNumber(3);
        steps.add("Order Accepted");
        steps.add("Picked Up");
        steps.add("Delivered");
        progress_bar.setSteps(steps);
        fb.collection("user_id").document(username_string).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                List<Map<String, Object>> currentDeliveriesData = (List<Map<String, Object>>) doc.get("currentDeliveries");
                if (currentDeliveriesData != null) {
                    for (Map<String, Object> orderData : currentDeliveriesData) {
                        String id = (String) orderData.get("orderID");
                        if (!Objects.equals(id, mOrder.getOrderID())) {
                            continue;
                        }
                        curr_state = String.valueOf(orderData.get("state"));
                        if (curr_state.compareTo("0") == 0) {
                            action_button.setText("Picked Up");
                        } else if (curr_state.compareTo("1") == 0) {
                            progress_bar.go(1, true);
                            action_button.setText("Delivered");
                        }
                        break;
                    }
                }
            }
        });


        // Update the UI with the Order details
        //TextView customerNameTextView = findViewById(R.id.customer_name);
        //customerNameTextView.setText(mOrder.getCustomer_name());
        TextView name = findViewById(R.id.customer_name);
        fb.collection("user_id").document(mOrder.getCustomerName()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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

        reference = FirebaseStorage.getInstance().getReference().child("profile_images").child(mOrder.getCustomerName() + ".jpeg");
        profile_image = findViewById(R.id.customer_image);
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (uri != null) { // add null check here
                    Glide.with(CustomerUpdateActivity.this).load(uri).error(R.drawable.ic_launcher_background)
                            .into(profile_image);
                } else {
                    System.out.println("This is null");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        TextView srcTextView = findViewById(R.id.pickup_confirm);
        String string = "Pickup location: " + mOrder.getFrom();
        srcTextView.setText(string);

        TextView destTextView = findViewById(R.id.destination_accept);
        string = "Delivery location: " + mOrder.getDest();
        destTextView.setText(string);

        TextView feeTextView = findViewById(R.id.fee_accept);
        string = "Fee: " + mOrder.getFee();
        feeTextView.setText(string);

        TextView notesTextView = findViewById(R.id.additional_details_confirm);
        notesTextView.setText(mOrder.getNotes());

        ImageButton qr_button = findViewById(R.id.qr_button);

        Button cancelButton = findViewById(R.id.cancel_btn);
        action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (curr_state.compareTo("0") == 0) {
                    mOrder.setState(1);
                    curr_state = "1";
                    DocumentReference userRef = fb.collection("user_id").document(username_string);
                    userRef.get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            List<Map<String, Object>> currentDeliveriesData = (List<Map<String, Object>>) document.get("currentDeliveries");
                            if (currentDeliveriesData != null) {
                                for (Map<String, Object> orderData : currentDeliveriesData) {
                                    String id = (String) orderData.get("orderID");
                                    if (!Objects.equals(id, mOrder.getOrderID())) {
                                        continue;
                                    }
                                    Log.d(TAG, "onClick: Updating in Firebase");
                                    // move to past orders
                                    userRef.update("currentDeliveries", FieldValue.arrayRemove(orderData));
                                    userRef.update("currentDeliveries", FieldValue.arrayUnion(mOrder));
                                    break;
                                }
                            }
                        }
                    });

                    progress_bar.go(1, true);
                    action_button.setText("Delivered");
                } else if (curr_state.compareTo("1") == 0) {
                    mOrder.setState(2);
                    DocumentReference userRef1 = fb.collection("user_id").document(mOrder.getCustomerName());
                    userRef1.get().addOnCompleteListener(task -> {
                        DocumentSnapshot document = task.getResult();
                        List<Map<String, Object>> currentDeliveriesData = (List<Map<String, Object>>) document.get("currentDeliveries");
                        if (currentDeliveriesData != null) {
                            for (Map<String, Object> orderData : currentDeliveriesData) {
                                if (!Objects.equals(orderData.get("orderID"), mOrder.getOrderID())) {
                                    continue;
                                }
                                userRef1.update("currentDeliveries", FieldValue.arrayRemove(orderData));
                                userRef1.update("currentDeliveries", FieldValue.arrayUnion(orderData));
                                break;
                            }
                        }
                    });
                    progress_bar.go(1, true);
                    Intent intent = new Intent(CustomerUpdateActivity.this, ConfirmOrderActivity.class);
                    intent.putExtra("order", mOrder);
                    startActivity(intent);
                }

            }
        });
        qr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerUpdateActivity.this, QRCode.class);
                intent.putExtra("order", mOrder);
                startActivity(intent);
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

