package com.example.my.bonus2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MyActivity extends Activity {
    private int[] colors;
    private int currentColorIndex = 0;

    private View view1;
    private View view2;
    private View view3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        colors = getResources().getIntArray(R.array.rainbow);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeColor(view);
            }
        };
        view1 = findViewById(R.id.view1);
        view1.setOnClickListener(listener);
        view2 = findViewById(R.id.view2);
        view2.setOnClickListener(listener);
        view3 = findViewById(R.id.view3);
        view3.setOnClickListener(listener);
    }

    private void changeColor(View v)  {
        v.setBackgroundColor(colors[currentColorIndex]);
        currentColorIndex++;
        currentColorIndex = currentColorIndex % colors.length;
    }
}
