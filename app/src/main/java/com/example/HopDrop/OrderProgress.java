package com.example.HopDrop;

import static android.content.ContentValues.TAG;

import static com.example.HopDrop.LoginActivity.username_string;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class OrderProgress extends AppCompatActivity {
    StepView progress_bar;
    List<String> steps = new ArrayList<>();
    FirebaseFirestore fb = FirebaseFirestore.getInstance();
    FirebaseHelper firebaseHelper = new FirebaseHelper();
    Order mOrder;
    TextView name;
    int counter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //TODO: Complete this class
        // From Users side
        // When steps == 2:
        //    Remove from current orders
        //    Move to past orders
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_progress);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mOrder = (Order) getIntent().getSerializableExtra("order");

        String deliverer = mOrder.getDeliverer();
        String orderId = mOrder.getOrderID();
        name = findViewById(R.id.customer_name_accept);


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

        progress_bar = findViewById(R.id.step_view);
        progress_bar.setStepsNumber(4);
        steps.add("Pending"); //Order is pending
        steps.add("Accepted"); // order.getState() == 0
        steps.add("Picked Up"); // order.getState() == 1
        steps.add("Delivered"); // order.getState() == 2
        progress_bar.setSteps(steps);

        counter = 0;
        if (Objects.equals(mOrder.getOrderID(), "") || Objects.equals(mOrder.getDeliverer(), "Pending Deliverer")) {
            Log.d(TAG, "LISTEN DELIVERER");
//            ListenerRegistration registration = listenForCurrentOrdersChanges(currentOrders -> {
//                // Process the currentOrders list as needed
////                mOrder = currentOrders.get(0);
////                Log.d(TAG, mOrder.getOrderID());
//                Log.d(TAG, "TEST TEST TEST");
//                Log.d(TAG, mOrder.getDeliverer());
//                Log.d(TAG, String.valueOf(mOrder.getState()));
//                //updateProgress();
//            });
            listenDeliverer();
            Log.d(TAG, "LISTEN DELIVERER FINISHED");
        } else {
            Log.d(TAG, "UPDATE PROGRESS");
            //if no one has picked up
            //if updated while not open
            updateProgress();
            Log.d(TAG, "UPDATE PROGRESS FINISHED");
        }
    }
    private void listenDeliverer() {
        name.setText("Pending Deliverer");
        fb.collection("user_id").addSnapshotListener(MetadataChanges.EXCLUDE, (value, error) -> {
            if (error != null) {
                return;
            }
            List<DocumentSnapshot> documentChanges = value.getDocuments();
            for (DocumentSnapshot document : documentChanges) {
                System.out.println("HERE");
                String username = document.getId();
                System.out.println(username);
                if (username.equals(username_string)) {
                    System.out.println("HERE 2");
                    for(Map<String, Object> orderData : (List<Map<String, Object>>) document.get("currentOrders")) {
                        if (!String.valueOf(orderData.get("orderID")).equals(mOrder.getOrderID())) {
                            continue;
                        }
                        String test = (String) orderData.get("deliverer_name");
                        System.out.println(test);
                        System.out.println(String.valueOf(orderData.get("deliverer_name")));
                        mOrder.setState(Integer.parseInt(String.valueOf(orderData.get("state"))));
                        mOrder.setDeliverer(String.valueOf(orderData.get("deliverer_name")));
                        Log.d("Deliverer Set", "Deliverer: " + mOrder.getDeliverer());
                    }
                    break;
                }
            }
        });
    }
//                System.out.println(mOrder.getState());
//                if (mOrder.getState() == 0) {
//                    progress_bar.go(1, true);
//                    updateProgress();
//                    return;
//                }
//                //restart activity
//                Log.d(TAG, "COUNTER: " + counter);
//                if (counter == 2) {
//                    //Somehow want to update everything
//                }
        //return;

    // Set Order object fields using orderData map
//            for (Map<String, Object> orderData : (List<Map<String, Object>>) value.get("currentOrders")) {
//                if (orderData.get("orderID").equals(mOrder.getOrderID())) {
//
//                }
//            }
    private void updateProgress() {
        fb.collection("user_id").document(mOrder.getDeliverer()).addSnapshotListener(MetadataChanges.EXCLUDE, (value, error) -> {
            boolean check = true;
            if(error !=null)

            {
                return;
            }

            String full_name = value.get("firstName") + " " + value.get("lastName");
            name.setText(full_name);
            // Set Order object fields using orderData map
            for(
                    Map<String, Object> orderData :(List<Map<String, Object>>)value.get("currentDeliveries"))

            {
                if (orderData.get("orderID").equals(mOrder.getOrderID())) {
                    mOrder.setState(Integer.parseInt(String.valueOf(orderData.get("state"))));
                    Log.d("Order state set", "onEvent" + mOrder.getState());
                    check = false;
                    break;
                }
            }
            if(check)

            {
                progress_bar.go(3, true);
            }
            if(mOrder.getState()==1)

            {
                progress_bar.go(2, true);
            }
            if(mOrder.getState()==2)

            {
                progress_bar.go(3, true);
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

    public interface CurrentOrdersListener {
        void onCurrentOrdersChanged(List<Order> currentOrders);
    }
    private ListenerRegistration listenForCurrentOrdersChanges(final CurrentOrdersListener listener) {
        name.setText("Pending Deliverer");
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        DocumentReference customerRef = rootRef.collection("user_id").document(mOrder.getCustomerName());

        ListenerRegistration registration = customerRef.addSnapshotListener(MetadataChanges.EXCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(getApplicationContext(), "Error listening for current orders changes", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    List<Order> currentOrders = new ArrayList<>();
                    List<Object> currentOrdersData = (List<Object>) snapshot.get("currentOrders");
                    Log.d("SNAPSHOT_LISTENER", "Data received: " + currentOrdersData); // Log the received data
                    if (currentOrdersData != null) {
                        for (Object orderData : currentOrdersData) {
                            Map<String, Object> order = (HashMap<String, Object>) orderData;
                            if (Objects.equals(order.get("orderID"), mOrder.getOrderID())) {
                                Log.d(TAG, "I AM HERE BUT NOT UPDATING");
                                mOrder.setDeliverer(String.valueOf(order.get("deliverer_name")));
                                mOrder.setState(Integer.parseInt(String.valueOf(order.get("state"))));
                            }
                        }
                    }
                    Log.d("SNAPSHOT_LISTENER", "Processed orders: " + mOrder.getDeliverer()); // Log the processed orders
                    Log.d("SNAPSHOT_LISTENER", "Processed orders: " + mOrder.getState()); // Log the processed orders
                    if (listener != null) {
                        listener.onCurrentOrdersChanged(currentOrders);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No customer document found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return registration;
    }
}