package com.example.my.swipeandzoom;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class SwipeAndZoom extends Activity implements View.OnTouchListener{

    private TypedArray images;

    private ImageView imageView;
    private TextView textView;
    private View root;

    private GestureDetector gestureDetector;

    private boolean zoomed;
    private int currentImage;
    private final String CURRENT_IMAGE_KEY = "com.swipeandzoom.currentImage";

    private final float ZOOM_SCALE = 1.5f;
    private final float NORMAL_SCALE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        images = getResources().obtainTypedArray(R.array.images);
        imageView = (ImageView) findViewById(R.id.image_view);
        textView = (TextView) findViewById(R.id.text_view);
        root = findViewById(R.id.root);

        currentImage = 0;
        zoomed = false;
        if (savedInstanceState != null) {
            currentImage = savedInstanceState.getInt(CURRENT_IMAGE_KEY);
        }
        refreshViews();
        gestureDetector = new GestureDetector(this, new SimpleGestureListener());
        root.setOnTouchListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_IMAGE_KEY, currentImage);
    }

    private void refreshViews() {
        imageView.setImageDrawable(images.getDrawable(currentImage));
        imageView.setScaleX(NORMAL_SCALE);
        imageView.setScaleY(NORMAL_SCALE);
        zoomed = false;
        textView.setText((currentImage + 1) + "/4");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class SimpleGestureListener extends GestureDetector.SimpleOnGestureListener  {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if(zoomed)  {
                zoomed = false;
                imageView.setScaleX(NORMAL_SCALE);
                imageView.setScaleY(NORMAL_SCALE);
            } else {
                zoomed = true;
                imageView.setScaleX(ZOOM_SCALE);
                imageView.setScaleY(ZOOM_SCALE);
            }
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(velocityX > 0 && currentImage > 0)  {
                currentImage--;
                refreshViews();
            } else if(velocityX < 0 && currentImage < 3)  {
                currentImage++;
                refreshViews();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}

