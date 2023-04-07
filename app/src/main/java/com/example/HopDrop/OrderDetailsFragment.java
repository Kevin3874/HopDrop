package com.example.HopDrop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class OrderDetailsFragment extends Fragment {
    private TextView mCustomerNameTextView;
    private TextView mSrcTextView;
    private TextView mDestTextView;
    private TextView mFeeTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_details_fragment, container, false);
        mCustomerNameTextView = view.findViewById(R.id.customer_name);
        mSrcTextView = view.findViewById(R.id.src);
        mDestTextView = view.findViewById(R.id.dest);
        mFeeTextView = view.findViewById(R.id.fee);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String customerName = arguments.getString("customer_name");
            String src = arguments.getString("src");
            String dest = arguments.getString("dest");
            double fee = arguments.getDouble("fee");
            mCustomerNameTextView.setText(customerName);
            mSrcTextView.setText(src);
            mDestTextView.setText(dest);
            mFeeTextView.setText(String.valueOf(fee));
        }

        return view;
    }
}

