package com.example.HopDrop.ui.order;

import android.annotation.SuppressLint;
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

public class OrderFragment extends Fragment {
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private RecyclerView mRecyclerView;
    public OrderAdapter mOrderAdapter;
    List<Order> orders = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrderAdapter = new OrderAdapter(orders, "orders");
        mRecyclerView.setAdapter(mOrderAdapter);

        updateOrders();

        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateOrders() {
        List<Order> orders = new ArrayList<>();
        //get all the orders that are not in the delivered state
        rootRef.collection("orders").whereNotEqualTo("state", 2)
                .addSnapshotListener((value, error) -> {
                    orders.clear();
                    if (error != null)  {
                        return;
                    }
                    List<DocumentSnapshot> documentChanges = value.getDocuments();
                    for (DocumentSnapshot document : documentChanges) {
                        Order order = document.toObject(Order.class);
                        order.setOrderID(document.getId());
                        orders.add(order);
                        Log.d("Order array", String.valueOf(orders));
                    }
                    mOrderAdapter = new OrderAdapter(orders, "orders");
                    mRecyclerView.setAdapter(mOrderAdapter);
                    mOrderAdapter.notifyDataSetChanged();
                });
    }
}
