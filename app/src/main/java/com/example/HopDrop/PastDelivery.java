package com.example.HopDrop;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PastDelivery extends AppCompatActivity {
    StorageReference reference;
    CircleImageView profile_image;

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

        reference = FirebaseStorage.getInstance().getReference().child("profile_images").child(mOrder.getCustomerName() + ".jpeg");
        profile_image = findViewById(R.id.customer_profile_image_delivery);
        reference.getDownloadUrl().addOnSuccessListener((OnSuccessListener<Uri>) uri -> {
            if (uri != null) { // add null check here
                Glide.with(PastDelivery.this).load(uri).error(R.drawable.ic_launcher_background)
                        .into(profile_image);
            };
        });

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