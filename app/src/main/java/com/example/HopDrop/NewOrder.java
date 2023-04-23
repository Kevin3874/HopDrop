package com.example.HopDrop;

import static com.example.HopDrop.LoginActivity.username_string;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewOrder extends AppCompatActivity {
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private boolean mediaUploaded = false;
    Bitmap bitmap;
    Order order;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button sbtn = (Button) findViewById(R.id.save_btn);

        sbtn.setOnClickListener(v -> {
            // put them into a new object then put that object into firestore
            // update "from" so that it is a dropdown
            TextInputLayout fromLayout = findViewById(R.id.from);
            TextInputLayout toLayout = findViewById(R.id.to);
            TextInputLayout feeLayout = findViewById(R.id.fee);
            TextInputLayout detailsLayout = findViewById(R.id.additional_details);
            String from = fromLayout.getEditText().getText().toString();
            String to = toLayout.getEditText().getText().toString();
            String fee = feeLayout.getEditText().getText().toString();
            String details = detailsLayout.getEditText().getText().toString();

            if (TextUtils.isEmpty(from) || TextUtils.isEmpty(to) || TextUtils.isEmpty(fee)) {
                Toast.makeText(getApplicationContext(), "Please fill out the required fields", Toast.LENGTH_SHORT).show();
            } else if (!mediaUploaded) {
                Toast.makeText(getApplicationContext(), "Please upload your QR code", Toast.LENGTH_SHORT).show();
            } else {
                if (fee.toString().chars().filter(ch -> ch == '.').count() > 1) {
                    Toast.makeText(getApplicationContext(), "Please fix the fee input", Toast.LENGTH_SHORT).show();
                } else {
                    DocumentReference userRef = rootRef.collection("user_id").document(username_string);
                    rootRef.collection("orders").document("orders").get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            //add to firebase for all orders
                            order = new Order(username_string, from, to, fee, details, null);
                            rootRef.collection("orders").add(order);
                            //add to user's collection
                            userRef.update("currentOrders", FieldValue.arrayUnion(order));
                        } else {
                            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    });
                    finish();
                }
            }
        });


        Button qbtn = findViewById(R.id.quit_btn);
        qbtn.setOnClickListener(v -> finish());

        Button qrbtn = findViewById(R.id.upload_qr);
        qrbtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 0);
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView imageView = findViewById(R.id.test);

        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                //imageView.setImageBitmap(bitmap);
                //order.setImage(bitmap);
                mediaUploaded = true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
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