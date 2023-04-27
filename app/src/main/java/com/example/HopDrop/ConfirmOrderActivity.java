package com.example.HopDrop;

import static com.example.HopDrop.LoginActivity.username_string;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfirmOrderActivity extends AppCompatActivity {
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private Order mOrder;
    StorageReference reference;
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Button confirm_button = findViewById(R.id.confirm_btn);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Get the Order object passed from the previous activity
        mOrder = (Order) getIntent().getSerializableExtra("order");

        // Update the UI with the Order details
        TextView customerNameTextView = findViewById(R.id.customer_name_confirm);
        rootRef.collection("user_id").document(mOrder.getCustomerName()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //get first and last name
                DocumentSnapshot doc = task.getResult();
                String full_name = doc.get("firstName") + " " + doc.get("lastName");
                customerNameTextView.setText(full_name);
            }
        });
        //customerNameTextView.setText(mOrder.getCustomer_name());
        reference = FirebaseStorage.getInstance().getReference().child("profile_images").child(mOrder.getCustomerName() + ".jpeg");
        profile_image = findViewById(R.id.customer_profile_image);
        reference.getDownloadUrl().addOnSuccessListener((OnSuccessListener<Uri>) uri -> {
            if (uri != null) { // add null check here
                Glide.with(ConfirmOrderActivity.this).load(uri).error(R.drawable.ic_launcher_background)
                        .into(profile_image);
            } else {
                System.out.println("This is null");
            }
        });

        TextView srcTextView = findViewById(R.id.pickup_location_confirm);
        String string = "Pickup location: " + mOrder.getFrom();
        srcTextView.setText(string);

        TextView destTextView = findViewById(R.id.destination_confirm);
        string = "Destination: " + mOrder.getDest();
        destTextView.setText(string);

        TextView feeTextView = findViewById(R.id.fee_confirm);
        string = "Fee: " + mOrder.getFee();
        feeTextView.setText(string);

        TextView notesTextView = findViewById(R.id.additional_details_confirm);
        notesTextView.setText(String.valueOf(mOrder.getNotes()));

        confirm_button.setOnClickListener(view -> {
            // Define what should happen when the button is clicked
            // Move the delivery to past deliveries and past orders
            DocumentReference userRef = rootRef.collection("user_id").document(username_string);
            DocumentReference orderRef = rootRef.collection("user_id").document(mOrder.getCustomerName());
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

            orderRef.get().addOnCompleteListener(task -> {
               if (task.isSuccessful()) {
                   DocumentSnapshot document = task.getResult();
                   List<Map<String, Object>> currentDeliveriesData = (List<Map<String, Object>>) document.get("currentOrders");
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
                           orderRef.update("pastOrders", FieldValue.arrayUnion(mOrder));
                           currentDeliveriesData.remove(index);
                           //figure out how to delete
                           //userRef.update("currentDeliveries", FieldValue.arrayRemove(mOrder));
                           orderRef.update("currentOrders", FieldValue.arrayRemove(orderData));

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
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

