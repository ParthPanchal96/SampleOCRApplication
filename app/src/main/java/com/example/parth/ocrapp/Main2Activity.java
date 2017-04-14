package com.example.parth.ocrapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView = (TextView)findViewById(R.id.textView);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){

            String str = bundle.getString("texts");
            textView.setText(str);
        }
    }
}
