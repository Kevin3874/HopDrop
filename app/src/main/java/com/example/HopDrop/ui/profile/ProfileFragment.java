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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
// import androidx.lifecycle.ViewModelProvider;

import com.example.HopDrop.Order;
import com.example.HopDrop.R;
import com.example.HopDrop.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;
// import com.example.a5_sample.ui.profile.ProfileViewModel;

public class ProfileFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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

        ViewPager2 orderViewPager = root.findViewById(R.id.pagerProfile);
        orderViewPager.setAdapter(new ViewPagerAdapter(this));
        TabLayout orderTabLayout  = root.findViewById(R.id.orderTabLayoutProfile);

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

        //retrieve data
        String username_text = myPrefs.getString("loginName", "");

        //set data from database
        db.collection("user_id").document(username_text).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    //get first and last name
                    DocumentSnapshot document = task.getResult();
                    String firstName = document.getString("firstName");
                    String lastName = document.getString("lastName");
                    String fullName = firstName + " " + lastName;
                    username.setText(fullName);
                    // TODO: update with past deliveries

                    //get number of deliveries
                    Map<String, Order> numDeliveries = (Map<String, Order>) document.get("pastDeliveries");
                    number_deliveries.setText(String.format("%d", numDeliveries.size()));
                } else {
                    Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });

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