package com.example.HopDrop.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.HopDrop.MainActivity;
import com.example.HopDrop.NewOrder;
import com.example.HopDrop.R;
import com.example.HopDrop.databinding.FragmentHomeBinding;
import com.example.HopDrop.databinding.FragmentProfileBinding;
import com.example.HopDrop.ui.home.ViewPagerFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {
    private ListView myList;
    private MainActivity myact;

    private String [] tab_names = { "Your orders", "Your Deliveries"};

    public static final int MENU_ITEM_EDITVIEW = Menu.FIRST;
    public static final int MENU_ITEM_DELETE = Menu.FIRST + 1;

    Context cntx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // TODO: make the homeview with user's current orders/deliveries

        // Inflate the layout for this fragment
        View myview = inflater.inflate(R.layout.fragment_home, container, false);

        cntx = getActivity().getApplicationContext();
        myact = (MainActivity) getActivity();

        //binding = FragmentHomeBinding.inflate(inflater, container, false);
        //View root = binding.getRoot();

        ViewPager2 orderViewPager = myview.findViewById(R.id.pagerHome);
        orderViewPager.setAdapter(new ViewPagerAdapter(this));
        TabLayout orderTabLayout  = myview.findViewById(R.id.orderTabLayoutHome);

        new TabLayoutMediator(orderTabLayout, orderViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tab_names[position]);
            }
        }).attach();

        //Launch new order page
        FloatingActionButton fab = (FloatingActionButton) myview.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launch = new Intent(myact, NewOrder.class);
                startActivity(launch);
            }
        });

        return myview;
    }

class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new ViewPagerFragment("New Pager", "home" + Integer.toString(position));
    }

    @Override
    public int getItemCount() {
        return tab_names.length;
    }
}
}