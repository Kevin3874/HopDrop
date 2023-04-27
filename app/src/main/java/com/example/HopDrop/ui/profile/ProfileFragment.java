package com.example.HopDrop.ui.profile;

import static com.example.HopDrop.LoginActivity.username_string;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.HopDrop.MainActivity;
import com.example.HopDrop.Order;
import com.example.HopDrop.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public String [] tab_names = {"Past orders", "Past deliveries"};
    private TextView username;
    private MainActivity myact;
    private TextView number_deliveries;

    StorageReference reference;
    CircleImageView profile_image;
    Context cntx;
    View myview;

    @Override
    public View onCreateView (LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        myview = inflater.inflate(R.layout.fragment_profile, container, false);

        cntx = getActivity().getApplicationContext();
        myact = (MainActivity) getActivity();

        ViewPager2 orderViewPager = myview.findViewById(R.id.pagerProfile);
        orderViewPager.setAdapter(new ViewPagerAdapter(this));
        TabLayout orderTabLayout  = myview.findViewById(R.id.orderTabLayoutProfile);

        new TabLayoutMediator(orderTabLayout, orderViewPager, (tab, position) -> tab.setText(tab_names[position])).attach();

        username = myview.findViewById(R.id.user_name);
        number_deliveries = myview.findViewById(R.id.number_delivered);
        //set data from database
        db.collection("user_id").document(username_string).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                //get first and last name
                DocumentSnapshot document = task.getResult();
                String firstName = document.getString("firstName");
                String lastName = document.getString("lastName");
                String fullName = firstName + " " + lastName;
                username.setText(fullName);

                //get number of deliveries
                ArrayList<Order> numDeliveries = (ArrayList<Order>) document.get("pastDeliveries");
                number_deliveries.setText(String.format("%d", numDeliveries.size()));
            } else {
                Toast.makeText(cntx, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        reference = FirebaseStorage.getInstance().getReference().child("profile_images").child(username_string+ ".jpeg");
        profile_image = myview.findViewById(R.id.customer_profile_image);
        reference.getDownloadUrl().addOnSuccessListener((OnSuccessListener<Uri>) uri -> {
            if (uri != null) { // add null check here
                Glide.with(ProfileFragment.this).load(uri).error(R.drawable.ic_launcher_background)
                        .into(profile_image);
            }
        });

        return myview;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewPager2 orderViewPager = myview.findViewById(R.id.pagerProfile);
        orderViewPager.setAdapter(new ViewPagerAdapter(this));
    }

    class ViewPagerAdapter extends FragmentStateAdapter {

        public ViewPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return new ViewPagerFragment("New Pager", "profile" + position);
        }

        @Override
        public int getItemCount() {
            return tab_names.length;
        }
    }
}