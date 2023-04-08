package com.example.HopDrop;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;

import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

public class OrderProgress extends AppCompatActivity {
    StepView progress_bar;
    List<String> steps = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_progress);
        progress_bar = findViewById(R.id.step_view);
        progress_bar.setStepsNumber(3);
        progress_bar.setSteps(steps);

    }
}