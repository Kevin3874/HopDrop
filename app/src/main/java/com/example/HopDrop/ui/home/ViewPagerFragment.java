package com.example.HopDrop.ui.home;

import static com.example.HopDrop.LoginActivity.username_string;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.HopDrop.Order;
import com.example.HopDrop.OrderAdapter;
import com.example.HopDrop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPagerFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private OrderAdapter mOrderAdapter;
    private FirebaseFirestore rootRef;
    private List<Order> orders;
    private String title;
    private String tab;

    private Context context;

    public ViewPagerFragment() {
    }

    public ViewPagerFragment(String title, String tab) {
        // Required empty public constructor
        this.title = title;
        this.tab = tab;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootRef = FirebaseFirestore.getInstance();
        context = context.getApplicationContext();
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        //retrieve data
        String username_text = myPrefs.getString("loginName", "");
        CollectionReference ordersRef = rootRef.collection("orders");

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_view_pager, container, false);
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        // Get the list of orders
        orders = new ArrayList<>();
        /*
        ordersRef.whereEqualTo("customer_name", username_text).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    System.out.println("Inhere");
                    for (DocumentSnapshot document : task.getResult()) {
                        //Order order = document.toObject(Order.class);
                        orders.add(new Order("Not determined yet", (String) document.get("fromLocation"), (String) document.get("destination"), (float) document.get("fee"), (String) document.get("notes"), (String) document.get("orderID")));

                    }
                }
            }
        });

         */
        // Set up the RecyclerView and adapter
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrderAdapter = new OrderAdapter(orders, tab, getActivity());
        mRecyclerView.setAdapter(mOrderAdapter);

        EventChangeListener();
        return view;
    }

    private void EventChangeListener() {
        SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        //retrieve data
        String username_text = myPrefs.getString("loginName", "");
        rootRef.collection("orders").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore error", error.getMessage());
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED)  {
                        orders.add(new Order("Not determined yet", (String) dc.getDocument().get("fromLocation"), (String) dc.getDocument().get("destination"), (float) dc.getDocument().get("fee"), (String) dc.getDocument().get("notes"), (String) dc.getDocument().get("orderID")));
                    }
                    mOrderAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}