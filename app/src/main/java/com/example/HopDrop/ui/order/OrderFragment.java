package com.example.HopDrop.ui.order;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.HopDrop.Order;
import com.example.HopDrop.OrderAdapter;
import com.example.HopDrop.R;
import com.example.HopDrop.ui.home.ViewPagerFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private RecyclerView mRecyclerView;
    private OrderAdapter mOrderAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);

        // Get the list of orders
        EventChangeListener(new ViewPagerFragment.OnOrdersFetchedListener() {
            @Override
            public void onOrdersFetched(List<Order> orders) {
                if (orders != null) {
                    // Set up the RecyclerView and adapter
                    mRecyclerView = view.findViewById(R.id.recycler_view);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mOrderAdapter = new OrderAdapter(orders, "orders");
                    mRecyclerView.setAdapter(mOrderAdapter);
                }
            }
        });

        return view;
    }
    public interface OnOrdersFetchedListener {
        void onOrdersFetched(List<Order> orders);
    }

    private void EventChangeListener(final ViewPagerFragment.OnOrdersFetchedListener listener) {
        List<Order> orders = new ArrayList<>();
        rootRef.collection("orders").whereNotEqualTo("state", 2)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Order order = document.toObject(Order.class);
                                order.setOrderID(document.getId());
                                orders.add(order);
                                Log.d("Order array", String.valueOf(orders));
                            }
                            listener.onOrdersFetched(orders);
                        }
                    }
                });
    }
}
