package com.example.a5_sample.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
// import androidx.lifecycle.ViewModelProvider;

import com.example.a5_sample.R;
import com.example.a5_sample.databinding.FragmentProfileBinding;

import java.util.Objects;
// import com.example.a5_sample.ui.profile.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private Button btn;
    private EditText owner;
    private EditText dog;
    private EditText breed;
    private EditText age;
    private SharedPreferences myPrefs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // eliminated for simplicity
  //      ProfileViewModel dashboardViewModel =
  //              new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        btn = binding.save;
        owner = binding.editTextOwner;
        dog = binding.editTextdogName;
        breed = binding.editTextdogBreed;
        age = binding.editTextDogAge;

        Context context = requireActivity().getApplicationContext();
        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        // TODO: add code to retrieve any previously saved data and display in fields
        //retrieve data
        String owner_text = myPrefs.getString("loginName", "");
        String dog_text = myPrefs.getString("dog_name", "");
        String breed_text = myPrefs.getString("dog_breed", "");
        String age_text = myPrefs.getString("dog_age", "");

        //fill the data in
        owner.setText(owner_text);
        dog.setText(dog_text);
        breed.setText(breed_text);
        age.setText(age_text);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String owner_name = String.valueOf(owner.getText());
                String dog_name = String.valueOf(dog.getText());
                String dog_breed = String.valueOf(breed.getText());
                String dog_age = String.valueOf(age.getText());
                SharedPreferences.Editor peditor = myPrefs.edit();
                peditor.putString("loginName",owner_name);
                peditor.putString("dog_name", dog_name);
                peditor.putString("dog_breed", dog_breed);
                peditor.putString("dog_age", dog_age);
                peditor.apply();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}