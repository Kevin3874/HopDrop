package com.example.HopDrop;

import static com.example.HopDrop.LoginActivity.username_string;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CustomerUpdateActivity extends AppCompatActivity {
    private Order mOrder;
    List<String> steps = new ArrayList<String>();

    FirebaseFirestore fb = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_update);

        // Add the code for the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the Order object passed from the previous activity
        mOrder = (Order) getIntent().getSerializableExtra("order");
        //pull from firebase to get the order
        Button action_button = findViewById(R.id.pickup_button);
        StepView progress_bar = findViewById(R.id.step_view);
        progress_bar.setStepsNumber(3);
        steps.add("Order Accepted");
        steps.add("Picked Up");
        steps.add("Delivered");
        progress_bar.setSteps(steps);
        if (mOrder.getState() == 0) {
            action_button.setText("Picked Up");
        } else if (mOrder.getState() == 1) {
            progress_bar.go(1, true);
            action_button.setText("Delivered");
        }

        // Update the UI with the Order details
        //TextView customerNameTextView = findViewById(R.id.customer_name);
        //customerNameTextView.setText(mOrder.getCustomer_name());

        TextView srcTextView = findViewById(R.id.pickup_location_label);
        srcTextView.setText(mOrder.getFrom());

        TextView destTextView = findViewById(R.id.delivery_location_label_order);
        destTextView.setText(mOrder.getDest());

        TextView feeTextView = findViewById(R.id.fee_label_order);
        feeTextView.setText(String.valueOf(mOrder.getFee()));

        TextView notesTextView = findViewById(R.id.additional_details_label_delivery);
        notesTextView.setText(String.valueOf(mOrder.getNotes()));


        Button cancelButton = findViewById(R.id.cancel_btn);
        action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (mOrder.getState() == 0) {
                    mOrder.setState(1);

                    // update the order state in firebase
                    /*
                    Task<DocumentSnapshot> tsk = fb.collection("user_id").document(username_string).get();
                      tsk.addOnSuccessListener(result -> {
                    }).addOnFailureListener(e -> {
                          // now do something with the exception
                      });
                    DocumentSnapshot doc = tsk.getResult();
                    for (Map<String, Object> order : (List<Map<String, Object>>) Objects.requireNonNull(doc.get("currentOrders"))) {
                        if (String.valueOf(order.get("orderID")).compareTo(mOrder.getOrderID()) == 0) {
                            order.put("orderID", 1);
                            break;
                        }
                    }

                     */
                    DocumentReference userRef = fb.collection("user_id").document(username_string);
                    DocumentReference ordererRef = fb.collection("user_id").document(mOrder.getCustomerName());
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
                                    // move to past orders
                                    userRef.update("currentDeliveries", FieldValue.arrayRemove(orderData));
                                    userRef.update("currentDeliveries", FieldValue.arrayUnion(mOrder));
                                    //TODO: Add the new order with new state to the orderer currentOrders
                                    break;
                                }
                            }
                        }
                    });

                    progress_bar.go(1, true);
                    action_button.setText("Delivered");
                } else if (mOrder.getState() == 1) {
                    mOrder.setState(2);
                    progress_bar.go(1, true);
                    Intent intent = new Intent(CustomerUpdateActivity.this, ConfirmOrderActivity.class);
                    intent.putExtra("order", mOrder);
                    startActivity(intent);
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(CustomerUpdateActivity.this, MainActivity.class);
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

