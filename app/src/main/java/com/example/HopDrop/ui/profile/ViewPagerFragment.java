package com.example.HopDrop.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.HopDrop.Order;
import com.example.HopDrop.OrderAdapter;
import com.example.HopDrop.R;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private OrderAdapter mOrderAdapter;
    private String title;
    private String tab;

    public ViewPagerFragment(){}
    public ViewPagerFragment(String title, String tab) {
        // Required empty public constructor

        this.title = title;
        this.tab = tab;
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
        mOrderAdapter = new OrderAdapter(orders, tab);
        mRecyclerView.setAdapter(mOrderAdapter);

        return view;
    }

    // Returns a list of dummy orders
    private List<Order> getOrders() {
        //TODO: Update information based on Firestore account
        List<Order> orders = new ArrayList<>();
        //orders.add(new Order("John Smith", "New York", "Los Angeles", 500.00f, "I am Brody cafe"));
        //orders.add(new Order("Jane Doe", "San Francisco", "Seattle", 300.00f, "please give me a call when arrived"));
        //orders.add(new Order("Bob Johnson", "Boston", "Chicago", 250.00f, "ketchup and fork please"));
        return orders;
    }
}