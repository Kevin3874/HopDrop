package com.example.HopDrop;

import static android.content.ContentValues.TAG;
import static com.example.HopDrop.LoginActivity.username_string;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.HopDrop.ui.order.OrderFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerUpdateActivity extends AppCompatActivity {
    private Order mOrder;
    List<String> steps = new ArrayList<>();

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
        //background for cancelling
        cancel_button.setOnClickListener(v -> fb.collection("user_id").document(username_string).get().addOnCompleteListener(task -> {
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

                        // send the message to the customer to say that their order is canceled
                        CollectionReference messagesRef = fb.collection("messages");
                        String customer_id = (String) orderData.get("customer_name");
                        Messages messages = new Messages("Your delivery request has been canceled, your new courier is pending", customer_id);
                        messagesRef.add(messages);
                        fb.collection("messages").document("message_doc").update("message_value", messages);
                        // change the state to 0 and update orderlist and change the courier name
                        orderData.put("state", -1);
                        orderData.put("deliverer_name", "Pending Deliverer");
                        fb.collection("user_id").document(username_string).update("currentDeliveries", currentDeliveriesData);
                        OrderFragment orderFragment = (OrderFragment) getSupportFragmentManager().findFragmentByTag("OrderFragment");
                        if (orderFragment != null) {
                            orderFragment.updateOrders();
                        }
                    }
                }

                // make sure to update the customer
                DocumentReference messageRef = fb.collection("messages").document();
                messageRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Get the message data from the document
                                Messages message = document.toObject(Messages.class);
                                if (username_string != null && username_string.equals(message.getRecipientUserId())) {
                                    View popupView = LayoutInflater.from(CustomerUpdateActivity.this).inflate(R.layout.popup_window, null);
                                    TextView popupText = popupView.findViewById(R.id.popup_text);
                                    popupText.setText(message.getContent());
                                    Button popupButton = popupView.findViewById(R.id.popup_button);

                                    // Declare and initialize alertDialog before setting the OnClickListener for popupButton
                                    AlertDialog alertDialog = new AlertDialog.Builder(CustomerUpdateActivity.this)
                                            .setView(popupView)
                                            .create();

                                    popupButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertDialog.dismiss();
                                        }
                                    });

                                    // Show the AlertDialog
                                    alertDialog.show();
                                }
                            }
                        }
                    }
                });
                //add back to orders
                DocumentReference userRef = fb.collection("user_id").document(username_string);
                DocumentReference orderRef = fb.collection("user_id").document(mOrder.getDeliverer());
                fb.collection("orders").document("orders").get().addOnCompleteListener(task1 -> {
                    Order order = new Order(username_string, mOrder.getFrom(), mOrder.getDest(), mOrder.getFee(), mOrder.getNotes());
                    fb.collection("orders")
                            .add(order)
                            .addOnSuccessListener(documentReference -> {
                                userRef.update("currentOrders", FieldValue.arrayUnion(order));
                            })
                            .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
                });
                //remove from both users
                userRef.get().addOnCompleteListener(task2 -> {
                    if (task2.isSuccessful()) {
                        DocumentSnapshot document = task2.getResult();
                        List<Map<String, Object>> currentDeliveriesData2 = (List<Map<String, Object>>) document.get("currentDeliveries");
                        //remove from current deliveries, add to past deliveries
                        int index = -1;
                        if (currentDeliveriesData2 != null) {
                            for (Map<String, Object> orderData : currentDeliveriesData2) {
                                index++;
                                String id = (String) orderData.get("orderID");
                                if (!Objects.equals(id, mOrder.getOrderID())) {
                                    continue;
                                }
                                currentDeliveriesData2.remove(index);
                                //figure out how to delete
                                //userRef.update("currentDeliveries", FieldValue.arrayRemove(mOrder));
                                userRef.update("currentDeliveries", FieldValue.arrayRemove(orderData));

                                break;
                            }
                        }
                    }
                });

                orderRef.get().addOnCompleteListener(task3 -> {
                    if (task3.isSuccessful()) {
                        DocumentSnapshot document = task3.getResult();
                        List<Map<String, Object>> currentDeliveriesData3 = (List<Map<String, Object>>) document.get("currentOrders");
                        //remove from current deliveries, add to past deliveries
                        int index = -1;
                        if (currentDeliveriesData3 != null) {
                            for (Map<String, Object> orderData : currentDeliveriesData3) {
                                index++;
                                String id = (String) orderData.get("orderID");
                                if (!Objects.equals(id, mOrder.getOrderID())) {
                                    continue;
                                }
                                currentDeliveriesData3.remove(index);
                                //figure out how to delete
                                //userRef.update("currentDeliveries", FieldValue.arrayRemove(mOrder));
                                orderRef.update("currentOrders", FieldValue.arrayRemove(orderData));

                                break;
                            }
                        }
                    }
                });
            }
            finish();
        }));
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
                            cancel_button.setVisibility(View.GONE);
                            progress_bar.go(1, true);
                            action_button.setText("Delivered");
                        }
                        break;
                    }
                }
            }
        });

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
        reference.getDownloadUrl().addOnSuccessListener((OnSuccessListener<Uri>) uri -> {
            if (uri != null) { // add null check here
                Glide.with(CustomerUpdateActivity.this).load(uri).error(R.drawable.ic_launcher_background)
                        .into(profile_image);
            } else {
                System.out.println("This is null");
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

        action_button.setOnClickListener(view -> {
            cancel_button.setVisibility(View.GONE);
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

        });
        qr_button.setOnClickListener(view -> {
            Intent intent = new Intent(CustomerUpdateActivity.this, QRCode.class);
            intent.putExtra("order", mOrder);
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



