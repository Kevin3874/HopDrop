package com.example.HopDrop.ui.home;

import static com.example.HopDrop.LoginActivity.username_string;
import static com.example.HopDrop.MainActivity.orders;
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
import com.google.firebase.firestore.model.Document;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ViewPagerFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private OrderAdapter mOrderAdapter;
    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();;
    //private List<Order> orders;
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
        //context = context.getApplicationContext();
        //SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        //retrieve data
        //String username_text = myPrefs.getString("loginName", "");
        //CollectionReference ordersRef = rootRef.collection("orders");

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_view_pager, container, false);
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        // Get the list of orders

        List<Order> test = EventChangeListener();
        test.add(new Order("TestHome", "New York", "Los Angeles", "500.00", "I am Brody cafe", "ai7s7dyg"));
        // Set up the RecyclerView and adapter
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mOrderAdapter = new OrderAdapter(test, tab);
        mRecyclerView.setAdapter(mOrderAdapter);



        return view;
    }

    private List<Order> EventChangeListener() {
        List<Order> orders = new ArrayList<>();
        rootRef.collection("orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Order order = document.toObject(Order.class);
                                orders.add(order);
                            }
                        }
                    }
                });
        return orders;

        //retrieve data

        /*
        rootRef.collection("orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot dc : task.getResult()) {
                    orders.add(new Order("Not determined yet", (String) dc.get("fromLocation"), (String) dc.get("dest"), (String) dc.get("fee"), (String) dc.get("notes"), (String) dc.get("orderID")));
                }
            }
        });
        */

        /*
        rootRef.collection("orders").addSnapshotListener(new EventListener<QuerySnapshot>() {


            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.e("Firestore error", error.getMessage());
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    //HashMap<String, Order> map = (HashMap<String, Order>) dc.getDocument().getData();

                    if (dc.getType() == DocumentChange.Type.ADDED)  {
                        //orders.add(dc.getDocument().toObject(Order.class));
                        //orders.add((Order) dc.getDocument().get("orders"));
                        //orders.add(new Order((String) dc.getDocument().get("customer_name"), (String) dc.getDocument().get("fromLocation"), (String) dc.getDocument().get("dest"), (String) dc.getDocument().get("fee"), (String) dc.getDocument().get("notes"), (String) dc.getDocument().get("orderID")));
                        //String customerName = map.get("customer_name");
                        //String destination = (String) map.get("dest");
                        //String fee = (String) map.get("fee");
                        //String fromLocation = (String) map.get("fromLocation");
                        //String notes = (String) map.get("notes");
                        //String orderID = (String) map.get("orderID");
                        //orders.add(new Order(customerName, destination, fee, fromLocation, notes, orderID));
                        //System.out.println(map.toString());
                    }
                    mOrderAdapter.notifyDataSetChanged();
                }

            }
        });

             */

    }
}