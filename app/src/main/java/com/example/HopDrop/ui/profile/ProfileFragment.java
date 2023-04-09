package com.example.HopDrop.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
// import androidx.lifecycle.ViewModelProvider;

import com.example.HopDrop.R;
import com.example.HopDrop.databinding.FragmentProfileBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
// import com.example.a5_sample.ui.profile.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private Button btn;
    private String [] tab_names = {"Past orders", "Past deliveries"};
    private TextView username;

    private TextView number_deliveries;
    private SharedPreferences myPrefs;

    public ProfileFragment() {}
    public View onCreateView (@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // eliminated for simplicity
  //      ProfileViewModel dashboardViewModel =
  //              new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewPager2 orderViewPager = root.findViewById(R.id.pager);
        orderViewPager.setAdapter(new ViewPagerAdapter(this));
        TabLayout orderTabLayout  = root.findViewById(R.id.orderTabLayout);

        new TabLayoutMediator(orderTabLayout, orderViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tab_names[position]);
            }
        }).attach();


        username = binding.userName;
        number_deliveries = binding.numberDelivered;

        Context context = requireActivity().getApplicationContext();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        // TODO: add code to retrieve any previously saved data and display in fields
        //retrieve data
        String username_text = myPrefs.getString("loginName", "");
        int num_deliveries_text = myPrefs.getInt("numberDeliveries", 0);


        //fill the data in
        username.setText(username_text);
        number_deliveries.setText(String.format("%d", num_deliveries_text));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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
            return new ViewPagerFragment("New Pager", "profile");
        }

        @Override
        public int getItemCount() {
            return tab_names.length;
        }
    }
}