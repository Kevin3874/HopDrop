package com.example.HopDrop;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class QRCode extends AppCompatActivity {
    StorageReference reference;
    ImageView qr_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Order mOrder = (Order) getIntent().getSerializableExtra("order");

        qr_image = findViewById(R.id.qr_code);

        reference = FirebaseStorage.getInstance().getReference().child("images").child(mOrder.getImage() + ".jpeg");
        reference.getDownloadUrl().addOnSuccessListener((OnSuccessListener<Uri>) uri -> {
            if (uri != null) { // add null check here
                Glide.with(QRCode.this).load(uri).error(R.drawable.ic_launcher_background)
                        .into(qr_image);
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