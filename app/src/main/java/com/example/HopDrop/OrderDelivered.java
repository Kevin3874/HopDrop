package com.example.HopDrop;
import com.example.HopDrop.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OrderDelivered extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hasorderdelivered);

        Button orderDeliveredButton = findViewById(R.id.yes_button);
        Button notDeliveredButton = findViewById(R.id.no_button);

        orderDeliveredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDelivered.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        notDeliveredButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
