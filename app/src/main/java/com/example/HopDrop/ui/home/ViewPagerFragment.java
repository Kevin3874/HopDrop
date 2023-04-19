package com.example.HopDrop.ui.home;

import static com.example.HopDrop.LoginActivity.username_string;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.HopDrop.Order;
import com.example.HopDrop.OrderAdapter;
import com.example.HopDrop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewPagerFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private OrderAdapter mOrderAdapter;

    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

    private String title;
    private String tab;

    private Context context;

    List<Order> orders;

    public ViewPagerFragment() {
    }


    public ViewPagerFragment(String title, String tab) {
        // Required empty public constructor
        this.title = title;
        this.tab = tab;
        this.orders = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_view_pager, container, false);
        View view = inflater.inflate(R.layout.fragment_order, container, false);


        // Get the list of orders
        getCurrentOrders(tab, new EventChangeListener() {
            @Override
            public void onEventChanged(List<Order> orders) {
                if (orders != null) {
                    // Set up the RecyclerView and adapter
                    mRecyclerView = view.findViewById(R.id.recycler_view);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mOrderAdapter = new OrderAdapter(orders, tab);
                    mRecyclerView.setAdapter(mOrderAdapter);
                }
            }
        });

        return view;
    }

    public interface OnOrdersFetchedListener {
        void onOrdersFetched(List<Order> orders);
    }

    public interface OnCurrentDeliveriesFetchedListener {
        void onCurrentDeliveriesFetched(List<Order> currentDeliveries);
    }

    public interface EventChangeListener {
        void onEventChanged(List<Order> orders);
    }

    private void getCurrentOrders(String tab, final EventChangeListener listener) {
        DocumentReference userRef = rootRef.collection("user_id").document(username_string);
        if (tab.compareTo("home0") == 0) {
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        List<Map<String, Object>> currentDeliveriesData = (List<Map<String, Object>>) document.get("currentDeliveries");

                        if (currentDeliveriesData != null) {
                            for (Map<String, Object> orderData : currentDeliveriesData) {
                                String customer = (String) orderData.get("customer_name");
                                String from = (String) orderData.get("fromLocation");
                                String dest = (String) orderData.get("dest");
                                String fee = (String) orderData.get("fee");
                                String notes = (String) orderData.get("notes");
                                Order order = new Order(customer, from, dest, fee, notes);
                                // Set Order object fields using orderData map
                                orders.add(order);
                            }
                        }
                        listener.onEventChanged(orders);
                    } else {
                        Toast.makeText(context, "Error getting user's current deliveries", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            /*
            rootRef.collection("user_id").whereEqualTo("deliverer_name", username_string)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d("Test", "test");
                                for (DocumentSnapshot document : task.getResult()) {
                                    Order order = document.toObject(Order.class);
                                    orders.add(order);
                                    Log.d("Order array", String.valueOf(orders));
                                }
                                listener.onOrdersFetched(orders);
                                mOrderAdapter.notifyDataSetChanged();
                            }
                        }
                    });

             */
        } else if (tab.compareTo("home1") == 0) {
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        List<Map<String, Object>> currentDeliveriesData = (List<Map<String, Object>>) document.get("currentOrders");

                        if (currentDeliveriesData != null) {
                            for (Map<String, Object> orderData : currentDeliveriesData) {
                                String customer = (String) orderData.get("customer_name");
                                String from = (String) orderData.get("fromLocation");
                                String dest = (String) orderData.get("dest");
                                String fee = (String) orderData.get("fee");
                                String notes = (String) orderData.get("notes");
                                Order order = new Order(customer, from, dest, fee, notes);
                                // Set Order object fields using orderData map
                                orders.add(order);
                            }
                        }
                        listener.onEventChanged(orders);
                    } else {
                        Toast.makeText(context, "Error getting user's current deliveries", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            /*
            rootRef.collection("orders").whereEqualTo("customer_name",  username_string)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d("Test", "test");
                                for (DocumentSnapshot document : task.getResult()) {
                                    Order order = document.toObject(Order.class);
                                    orders.add(order);
                                    Log.d("Order array", String.valueOf(orders));
                                }
                                listener.onOrdersFetched(orders);
                                mOrderAdapter.notifyDataSetChanged();
                            }
                        }
                    });

             */
        }
    }
}