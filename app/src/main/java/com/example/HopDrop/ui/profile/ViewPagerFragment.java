package com.example.HopDrop.ui.profile;

import static android.content.ContentValues.TAG;
import static com.example.HopDrop.LoginActivity.username_string;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.HopDrop.Order;
import com.example.HopDrop.OrderAdapter;
import com.example.HopDrop.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewPagerFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private OrderAdapter mOrderAdapter;
    private String title;
    private String tab;
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    List<Order> orders;

    public ViewPagerFragment(){

    }
    public ViewPagerFragment(String title, String tab) {
        // Required empty public constructor
        this.title = title;
        this.tab = tab;
        // Get the list of orders
        this.orders = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_view_pager, container, false);
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        updateData();

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrderAdapter = new OrderAdapter(orders, tab);
        mRecyclerView.setAdapter(mOrderAdapter);


        return view;
    }

    private void updateData() {
        orders = new ArrayList<>();
        Log.d(TAG, "TEST TEST TEST");
        Log.d(TAG, tab);
        if (tab.compareTo("profile0") == 0) {
            rootRef.collection("user_id").addSnapshotListener((value, error) -> {
                orders.clear();
                if (error != null)  {
                    return;
                }
                List<DocumentSnapshot> documentChanges = value.getDocuments();
                for(DocumentSnapshot document : documentChanges) {
                    if (document.getId().compareTo(username_string) == 0) {
                        // Set Order object fields using orderData map
                        for (Map<String, Object> orderData : (List<Map<String, Object>>) document.get("pastOrders")) {
                            String customer = (String) orderData.get("customer_name");
                            String from = (String) orderData.get("fromLocation");
                            String dest = (String) orderData.get("dest");
                            String fee = (String) orderData.get("fee");
                            String notes = (String) orderData.get("notes");
                            Order order = new Order(customer, from, dest, fee, notes, null);
                            orders.add(order);
                        }
                        System.out.println("potato" + String.valueOf(orders));
                        Log.d("Order added", "onEvent" + (ArrayList<Order>) document.get("pastOrders"));
                        mOrderAdapter = new OrderAdapter(orders, tab);
                        mRecyclerView.setAdapter(mOrderAdapter);
                        break;
                    }
                }
                mOrderAdapter.notifyDataSetChanged();
            });
        } else if (tab.compareTo("profile1") == 0) {
            rootRef.collection("user_id").addSnapshotListener((value, error) -> {
                orders.clear();
                if (error != null)  {
                    return;
                }
                List<DocumentSnapshot> documentChanges = value.getDocuments();
                for(DocumentSnapshot document : documentChanges) {
                    if (document.getId().compareTo(username_string) == 0) {
                        // Set Order object fields using orderData map
                        for (Map<String, Object> orderData : (List<Map<String, Object>>) document.get("pastDeliveries")) {
                            String customer = (String) orderData.get("customer_name");
                            String from = (String) orderData.get("fromLocation");
                            String dest = (String) orderData.get("dest");
                            String fee = (String) orderData.get("fee");
                            String notes = (String) orderData.get("notes");
                            Order order = new Order(customer, from, dest, fee, notes, null);
                            orders.add(order);
                        }
                        System.out.println("potato" + String.valueOf(orders));
                        Log.d("Order added", "onEvent" + (ArrayList<Order>) document.get("pastDeliveries"));
                        mOrderAdapter = new OrderAdapter(orders, tab);
                        mRecyclerView.setAdapter(mOrderAdapter);
                        break;
                    }
                }
                mOrderAdapter.notifyDataSetChanged();
            });
        }
    }
}