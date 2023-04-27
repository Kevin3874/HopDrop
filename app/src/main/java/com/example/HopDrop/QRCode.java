package com.example.HopDrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import io.grpc.Context;

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
        System.out.println(mOrder.getImage() + ".jpeg");
        reference = FirebaseStorage.getInstance().getReference().child("images").child(mOrder.getImage() + ".jpeg");
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (uri != null) { // add null check here
                    Glide.with(QRCode.this).load(uri).error(R.drawable.ic_launcher_background)
                            .into(qr_image);
                } else {
                    System.out.println("This is null");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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