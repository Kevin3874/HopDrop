package com.example.HopDrop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class OrderDetailsFragment extends Fragment {
    private Order mOrder;

    public OrderDetailsFragment(Order order) {
        mOrder = order;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_order_details, container, false);

        // Find any views in the layout and populate them with data
        TextView customerNameTextView = view.findViewById(R.id.customer_name);
        TextView srcTextView = view.findViewById(R.id.src);
        TextView destTextView = view.findViewById(R.id.dest);
        TextView feeTextView = view.findViewById(R.id.fee);

        customerNameTextView.setText(mOrder.getCustomer_name());
        srcTextView.setText(mOrder.getSrc());
        destTextView.setText(mOrder.getDest());
        feeTextView.setText(String.valueOf(mOrder.getFee()));

        return view;
    }
}
