package com.example.HopDrop.ui.home;

import static com.example.HopDrop.LoginActivity.username_string;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.HopDrop.Order;
import com.example.HopDrop.OrderAdapter;
import com.example.HopDrop.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrderAdapter = new OrderAdapter(orders, tab);
        mRecyclerView.setAdapter(mOrderAdapter);

        updateData();

        return view;
    }

    private void updateData() {
        orders = new ArrayList<>();
        if (tab.compareTo("home0") == 0) {
            rootRef.collection("user_id").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    orders.clear();
                    if (error != null)  {
                        return;
                    }
                    List<DocumentSnapshot> documentChanges = value.getDocuments();
                    for(DocumentSnapshot document : documentChanges) {
                        if (document.getId().compareTo(username_string) == 0) {
                            // Set Order object fields using orderData map
                            for (Map<String, Object> orderData : (List<Map<String, Object>>) document.get("currentOrders")) {
                                String customer = (String) orderData.get("customer_name");
                                String from = (String) orderData.get("fromLocation");
                                String dest = (String) orderData.get("dest");
                                String fee = (String) orderData.get("fee");
                                String notes = (String) orderData.get("notes");
                                Order order = new Order(customer, from, dest, fee, notes);
                                order.setOrderID((String) orderData.get("orderID"));
                                orders.add(order);
                            }
                            Log.d("Order added", "onEvent" + (ArrayList<Order>) document.get("currentOrders"));
                            mOrderAdapter = new OrderAdapter(orders, tab);
                            mRecyclerView.setAdapter(mOrderAdapter);
                            break;
                        }
                    }
                    mOrderAdapter.notifyDataSetChanged();
                }
            });
        } else if (tab.compareTo("home1") == 0) {
            rootRef.collection("user_id").addSnapshotListener((value, error) -> {
                orders.clear();
                if (error != null)  {
                    return;
                }
                List<DocumentSnapshot> documentChanges = value.getDocuments();
                for(DocumentSnapshot document : documentChanges) {
                    if (document.getId().compareTo(username_string) == 0) {
                        // Set Order object fields using orderData map
                        for (Map<String, Object> orderData : (List<Map<String, Object>>) document.get("currentDeliveries")) {
                            String customer = (String) orderData.get("customer_name");
                            String from = (String) orderData.get("fromLocation");
                            String dest = (String) orderData.get("dest");
                            String fee = (String) orderData.get("fee");
                            String notes = (String) orderData.get("notes");
                            Order order = new Order(customer, from, dest, fee, notes);
                            order.setOrderID((String) orderData.get("orderID"));
                            orders.add(order);
                        }
                        Log.d("Order added", "onEvent" + (ArrayList<Order>) document.get("currentDeliveries"));
                        mOrderAdapter = new OrderAdapter(orders, tab);
                        mRecyclerView.setAdapter(mOrderAdapter);
                        break;
                    }
                }
                mOrderAdapter.notifyDataSetChanged();
            });
        }
    }


    public interface OnOrdersFetchedListener {
        void onOrdersFetched(List<Order> orders);
    }

}