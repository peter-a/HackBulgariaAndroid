package com.example.my.drawablebrush;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;


public class MyActivity extends Activity implements View.OnTouchListener {

    private CustomImageView customImage;
    private Bitmap green;
    private Bitmap blue;
    private Bitmap red;
    public static final int PGN_SIZE = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        customImage = (CustomImageView) findViewById(R.id.custom_image);
        customImage.setOnTouchListener(this);

        green = ((BitmapDrawable) getResources().getDrawable(R.drawable.green)).getBitmap();
        blue = ((BitmapDrawable) getResources().getDrawable(R.drawable.blue)).getBitmap();
        red = ((BitmapDrawable) getResources().getDrawable(R.drawable.red)).getBitmap();

        green = Bitmap.createScaledBitmap(green, PGN_SIZE, PGN_SIZE, true);
        blue = Bitmap.createScaledBitmap(blue, PGN_SIZE, PGN_SIZE, true);
        red = Bitmap.createScaledBitmap(red, PGN_SIZE, PGN_SIZE, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int width = size.x;
        int height = size.y;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        customImage.setCanvas(canvas);
        customImage.setCanvasBitmap(bitmap);
    }

    public void changeBitmap(View view)  {
        switch (view.getId())  {
            case R.id.happy_image:
                customImage.setCurrentBitmap(green);
                break;
            case R.id.crying_image:
                customImage.setCurrentBitmap(blue);
                break;
            case R.id.scared_image:
                customImage.setCurrentBitmap(red);
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        customImage.setCurrentX(x);
        customImage.setCurrentY(y);
        customImage.invalidate();
        return true;
    }
}
