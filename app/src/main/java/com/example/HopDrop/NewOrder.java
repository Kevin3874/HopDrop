package com.example.HopDrop;

import static android.content.ContentValues.TAG;
import static com.example.HopDrop.LoginActivity.username_string;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;

import io.grpc.Context;

public class NewOrder extends AppCompatActivity {
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private boolean mediaUploaded = false;
    Bitmap bitmap;
    Order order;
    Uri imageUri;
    private String myUri = "";
    String docID = "";
    int GET_IMAGE_CODE = 10001;

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
                            rootRef.collection("orders")
                                    .add(order)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            order.setOrderID(documentReference.getId());
                                            docID = documentReference.getId();
                                            Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                            System.out.println("This is right after you get the document reference");
                                            System.out.println("this is the order in the new order: " + String.valueOf(order.getOrderID()));
                                            userRef.update("currentOrders", FieldValue.arrayUnion(order));
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                            //add to user's collection
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
            startActivityForResult(intent, GET_IMAGE_CODE);
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        /*
        if (requestCode == GET_IMAGE_CODE) {
            switch (resultCode) {

                case RESULT_OK:
                    bitmap = (Bitmap) data.getExtras().get("data");
                    ImageView imageView = findViewById(R.id.test);
                    imageView.setImageBitmap(bitmap);
                    handleUpload(bitmap);
                    mediaUploaded = true;
            }
        }
        */


        ImageView imageView = findViewById(R.id.test);

        if (requestCode == 10001 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                //imageView.setImageBitmap(bitmap); //This makes the image show on the screen which we don't need
                handleUpload(bitmap);
                mediaUploaded = true;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void handleUpload(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        StorageReference reference = FirebaseStorage.getInstance().getReference()
                .child("images")
                .child(username_string + ".jpeg");

        reference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getDownloadUrl(reference);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Failure", "onFailure: ", e.getCause());
                    }
                });
    }

    private void getDownloadUrl(StorageReference reference) {
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("DownloadUrl: ", "onSuccess: " + uri);
                StorageReference imageRef = reference.child("images/" + username_string + ".jpeg");


            }
        });
    }

    private void setUserProfileUrl(Uri uri) {

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