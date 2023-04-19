package com.example.HopDrop.ui.profile;

import static com.example.HopDrop.LoginActivity.username_string;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.HopDrop.Order;
import com.example.HopDrop.OrderAdapter;
import com.example.HopDrop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private OrderAdapter mOrderAdapter;
    private String title;
    private String tab;
    private FirebaseFirestore rootRef;
    List<Order> orders;

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
        rootRef = FirebaseFirestore.getInstance();

        // Get the list of orders
        List<Order> orders = getOrders();


        // Set up the RecyclerView and adapter
        EventChangeListener(new com.example.HopDrop.ui.home.ViewPagerFragment.OnOrdersFetchedListener() {
            @Override
            public void onOrdersFetched(List<Order> orders) {
                if (orders != null) {
                    // Set up the RecyclerView and adapter
                    mRecyclerView = view.findViewById(R.id.recycler_view);
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mOrderAdapter = new OrderAdapter(orders, tab);
                    mRecyclerView.setAdapter(mOrderAdapter);
                }
            }
        }, tab);

        return view;
    }

    public interface OnOrdersFetchedListener {
        void onOrdersFetched(List<Order> orders);
    }

    private void EventChangeListener(final com.example.HopDrop.ui.home.ViewPagerFragment.OnOrdersFetchedListener listener, String tab) {
        orders = new ArrayList<>();
        if (tab.compareTo("profile0") == 0) {
            DocumentReference docRef = rootRef.collection("user_id").document(username_string);
            docRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            orders = (ArrayList<Order>) document.get("pastDeliveries");
                            Log.d("Test", "test");
                            listener.onOrdersFetched(orders);
                            mOrderAdapter.notifyDataSetChanged();
                        }
                    }
                });
        } else if (tab.compareTo("profile1") == 0) {
            DocumentReference docRef = rootRef.collection("user_id").document(username_string);
            docRef
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                orders = (ArrayList<Order>) document.get("pastOrders");
                                Log.d("Test", "test");
                                listener.onOrdersFetched(orders);
                                mOrderAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }

    }

    // Returns a list of dummy orders
    private List<Order> getOrders() {
        //TODO: Update information based on Firestore account
        List<Order> orders = new ArrayList<>();
        //orders.add(new Order("John Smith", "New York", "Los Angeles", 500.00f, "I am Brody cafe", "as7d9h"));
        //orders.add(new Order("Jane Doe", "San Francisco", "Seattle", 300.00f, "please give me a call when arrived"));
        //orders.add(new Order("Bob Johnson", "Boston", "Chicago", 250.00f, "ketchup and fork please"));
        return orders;
    }
}