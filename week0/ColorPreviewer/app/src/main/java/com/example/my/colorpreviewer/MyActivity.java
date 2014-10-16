package com.example.my.colorpreviewer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MyActivity extends Activity {
    private EditText mColorEditText;
    private Button mButton;
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        mColorEditText = (EditText) findViewById(R.id.edit_text_color);
        mView = findViewById(R.id.view);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String color = mColorEditText.getText().toString();
                try {
                    mView.setBackgroundColor(Color.parseColor(color));
                } catch (IllegalArgumentException ex) {

                }
            }
        });
    }


}
