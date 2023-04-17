package com.example.HopDrop.ui.home;

import static com.example.HopDrop.LoginActivity.username_string;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.HopDrop.Order;
import com.example.HopDrop.OrderAdapter;
import com.example.HopDrop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private OrderAdapter mOrderAdapter;

    private DatabaseReference dbref;

    FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private FirebaseDatabase mdbase;
    //private List<Order> orders;
    private String title;
    private String tab;

    private Context context;

    List<Order> orders;

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
        EventChangeListener(new OnOrdersFetchedListener() {
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

        System.out.println(tab);
        System.out.println(username_string);
        /*
        mdbase = FirebaseDatabase.getInstance();
        dbref = mdbase.getReference();

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                long count = snapshot.getChildrenCount();
                Log.d("count", "Children count: " + count);
                Log.d("count", "Client count: " + snapshot.child("clients").getChildrenCount());

                // need to recreate the mItems list somehow
                // another way is to use a FirebaseRecyclerView - see Sample Database code

                orders.clear();
                Iterable<DataSnapshot> order_collection = snapshot.child("orders").getChildren();
                for (DataSnapshot pair : order_collection) {
                    orders.add(pair.getValue(Order.class));
                }
                mOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("error", "Failed to read value.", error.toException());
            }
        });
         */

        return view;
    }

    public interface OnOrdersFetchedListener {
        void onOrdersFetched(List<Order> orders);
    }

    private void EventChangeListener(final OnOrdersFetchedListener listener, String tab) {
        orders = new ArrayList<>();
        if (tab.compareTo("home0") == 0) {
            System.out.println("It got into home0");
            rootRef.collection("orders").whereEqualTo("deliverer_name", username_string)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d("Test", "test");
                                for (DocumentSnapshot document : task.getResult()) {
                                    Order order = document.toObject(Order.class);
                                    orders.add(order);
                                    Log.d("Order array", String.valueOf(orders));
                                }
                                listener.onOrdersFetched(orders);
                                mOrderAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        } else if (tab.compareTo("home1") == 0) {
            System.out.println("It got into home1");
            rootRef.collection("orders").whereEqualTo("customer_name", username_string)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d("Test", "test");
                                for (DocumentSnapshot document : task.getResult()) {
                                    Order order = document.toObject(Order.class);
                                    orders.add(order);
                                    Log.d("Order array", String.valueOf(orders));
                                }
                                listener.onOrdersFetched(orders);
                                mOrderAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }

    }
}