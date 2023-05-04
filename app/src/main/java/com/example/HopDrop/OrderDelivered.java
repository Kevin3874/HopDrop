package com.example.HopDrop;
import com.example.HopDrop.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class OrderDelivered extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hasorderdelivered);
    }

    Button order_delivered = findViewById(R.id.yes_button);
    Button not_delivered = findViewById(R.id.no_button);


}
