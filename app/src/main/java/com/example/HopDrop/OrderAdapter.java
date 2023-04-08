package com.example.HopDrop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> mOrders;

    public OrderAdapter(List<Order> orders) {
        mOrders = orders;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = mOrders.get(position);
        holder.customerNameTextView.setText(order.getCustomer_name());
        holder.srcTextView.setText(order.getSrc());
        holder.destTextView.setText(order.getDest());
        holder.feeTextView.setText(String.valueOf(order.getFee()));
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView customerNameTextView;
        public TextView srcTextView;
        public TextView destTextView;
        public TextView feeTextView;

        public Button detailsButton;

        public OrderViewHolder(View itemView) {
            super(itemView);
            customerNameTextView = itemView.findViewById(R.id.customer_name);
            srcTextView = itemView.findViewById(R.id.src);
            destTextView = itemView.findViewById(R.id.dest);
            feeTextView = itemView.findViewById(R.id.fee);
            detailsButton = itemView.findViewById(R.id.details_button);
            detailsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Order order = mOrders.get(getAdapterPosition());
                    FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
                    Fragment fragment = new OrderDetailsFragment(order);
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });

        }
    }
}

