package com.example.HopDrop;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
        context = parent.getContext();
        return new OrderViewHolder(view);
    }


    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = mOrders.get(position);
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        System.out.println(tab + ":" + order.getDeliverer() + ";");
        if (tab.compareTo("home0") == 0 || tab.compareTo("profile0") == 0) {
            holder.customerNameLabel.setText(context.getResources().getString(R.string.courier_name));
            if (order.getDeliverer().equals("")) {
                holder.customerNameTextView.setText("");
            } else {
                rootRef.collection("user_id").document(String.valueOf(order.getDeliverer())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            //get first and last name
                            DocumentSnapshot doc = task.getResult();
                            String full_name = doc.get("firstName") + " " + doc.get("lastName");
                            holder.customerNameTextView.setText(full_name);
                        }
                    }
                });
            }

        } else if (tab.compareTo("home1") == 0 || tab.compareTo("profile1") == 0 || tab.compareTo("orders") == 0) {
            holder.customerNameLabel.setText(context.getResources().getString(R.string.customer_name));
            rootRef.collection("user_id").document(String.valueOf(order.getCustomerName())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        //get first and last name
                        DocumentSnapshot doc = task.getResult();
                        String full_name = doc.get("firstName") + " " + doc.get("lastName");
                        holder.customerNameTextView.setText(full_name);
                    }
                }
            });
        }

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
        public TextView customerNameLabel;

        public OrderViewHolder(View itemView) {
            super(itemView);
            customerNameTextView = itemView.findViewById(R.id.customer_name);
            srcTextView = itemView.findViewById(R.id.src_accept);
            destTextView = itemView.findViewById(R.id.dest_accept);
            feeTextView = itemView.findViewById(R.id.fee_accept);
            notesTextView = itemView.findViewById(R.id.notes);
            detailsButton = itemView.findViewById(R.id.details_button);
            customerNameLabel = itemView.findViewById(R.id.customer_name_label);



            Log.d(TAG, "OrderViewHolder: TESTING DATA");

            detailsButton.setOnClickListener(view -> {
                Order order = mOrders.get(getAdapterPosition());

                Log.d(TAG, order.getOrderID());
                Log.d(TAG, order.getDeliverer());

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

