package com.example.HopDrop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> mOrders;
    private String tab;
    Context context;


    public OrderAdapter(List<Order> orders, String tab) {
        mOrders = orders;
        this.tab = tab;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }


    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = (Order) mOrders.get(position);
        holder.customerNameTextView.setText(order.getCustomerName());
        holder.srcTextView.setText(order.getFrom());
        holder.destTextView.setText(order.getDest());
        holder.feeTextView.setText(String.valueOf(order.getFee()));
        holder.notesTextView.setText(String.valueOf(order.getNotes()));
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

        public TextView notesTextView;

        public ImageButton detailsButton;

        public OrderViewHolder(View itemView) {
            super(itemView);
            customerNameTextView = itemView.findViewById(R.id.customer_name);
            srcTextView = itemView.findViewById(R.id.src);
            destTextView = itemView.findViewById(R.id.dest);
            feeTextView = itemView.findViewById(R.id.fee);
            notesTextView = itemView.findViewById(R.id.notes);
            detailsButton = itemView.findViewById(R.id.details_button);


            detailsButton.setOnClickListener(view -> {
                Order order = mOrders.get(getAdapterPosition());
                Intent intent = new Intent(view.getContext(), OrderProgress.class);
                if (tab.compareTo("home0") == 0) {
                    intent = new Intent(view.getContext(), OrderProgress.class);
                } else if (tab.compareTo("home1") == 0) {
                    intent = new Intent(view.getContext(), CustomerUpdateActivity.class);
                } else if (tab.compareTo("orders") == 0) {
                    intent = new Intent(view.getContext(), OrderDetailsActivity.class);
                } else if (tab.compareTo("profile0") == 0) {
                    intent = new Intent(view.getContext(), PastOrders.class);
                } else if (tab.compareTo("profile1") == 0) {
                    intent = new Intent(view.getContext(), PastDelivery.class);
                }
                intent.putExtra("order", order);
                view.getContext().startActivity(intent);
            });

        }
    }
}

