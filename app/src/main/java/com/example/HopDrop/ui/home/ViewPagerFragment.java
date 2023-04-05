package com.example.HopDrop.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.HopDrop.Order;
import com.example.HopDrop.OrderAdapter;
import com.example.HopDrop.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private OrderAdapter mOrderAdapter;
    private String title;
    public ViewPagerFragment(String title) {
        // Required empty public constructor
        this.title = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_view_pager, container, false);
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        // Get the list of orders
        List<Order> orders = getOrders();

        // Set up the RecyclerView and adapter
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrderAdapter = new OrderAdapter(orders);
        mRecyclerView.setAdapter(mOrderAdapter);

        return view;
    }

    // Returns a list of dummy orders
    private List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order("John Smith", "New York", "Los Angeles", 500.00f));
        orders.add(new Order("Jane Doe", "San Francisco", "Seattle", 300.00f));
        orders.add(new Order("Bob Johnson", "Boston", "Chicago", 250.00f));
        return orders;
    }
}