package com.example.HopDrop.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.HopDrop.MainActivity;
import com.example.HopDrop.NewOrder;
import com.example.HopDrop.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {
    private ListView myList;
    private MainActivity myact;

    public static final int MENU_ITEM_EDITVIEW = Menu.FIRST;
    public static final int MENU_ITEM_DELETE = Menu.FIRST + 1;

    Context cntx;
 //   Park current;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TODO: make the homeview with user's current orders/deliveries

        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_home, container, false);

        cntx = getActivity().getApplicationContext();
        myact = (MainActivity) getActivity();


        FloatingActionButton fab = (FloatingActionButton) myview.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launch = new Intent(myact, NewOrder.class);
                startActivity(launch);
            }
        });

        return myview;
    }

}