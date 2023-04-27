package com.example.HopDrop.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.example.HopDrop.MainActivity;
import com.example.HopDrop.NewOrder;
import com.example.HopDrop.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {
    private MainActivity myact;
    public String [] tab_names = { "Your orders", "Your Deliveries"};
    Context cntx;
    View myview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_home, container, false);

        cntx = getActivity().getApplicationContext();
        myact = (MainActivity) getActivity();

        ViewPager2 orderViewPager = myview.findViewById(R.id.pagerHome);
        orderViewPager.setAdapter(new ViewPagerAdapter(this));
        TabLayout orderTabLayout  = myview.findViewById(R.id.orderTabLayoutHome);

        new TabLayoutMediator(orderTabLayout, orderViewPager, (tab, position) -> tab.setText(tab_names[position])).attach();

        //Launch new order page
        FloatingActionButton fab = myview.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent launch = new Intent(myact, NewOrder.class);
            startActivity(launch);
        });

        return myview;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewPager2 orderViewPager = myview.findViewById(R.id.pagerHome);
        orderViewPager.setAdapter(new HomeFragment.ViewPagerAdapter(this));
    }

    class ViewPagerAdapter extends FragmentStateAdapter {

        public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            System.out.println("Makes another home fragment");
            return new ViewPagerFragment("New Pager", "home" + position);
        }

        @Override
        public int getItemCount() {
        return tab_names.length;
    }
    }
}