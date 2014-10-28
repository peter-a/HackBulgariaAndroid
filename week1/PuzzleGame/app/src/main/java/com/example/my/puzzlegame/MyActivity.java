package com.example.my.puzzlegame;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MyActivity extends Activity implements View.OnDragListener, View.OnTouchListener {

    private final String INDEX_ARRAY = "index_array";
    private final int columns = 4;
    private final int rows = 4;
    private List<Integer> correspondingImageIndexex = new ArrayList<Integer>();
    TypedArray images;
    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my);
        images = getResources().obtainTypedArray(R.array.images);
        images.recycle();

        if (savedInstanceState == null) {
            for (int i = 0; i < images.length(); i++) {
                correspondingImageIndexex.add(i);
            }
            randomize();
        } else {
            correspondingImageIndexex = savedInstanceState.getIntegerArrayList(INDEX_ARRAY);
        }

        root = (LinearLayout) findViewById(R.id.root);
        LinearLayout row = new LinearLayout(this);
        for (int i = 0; i < images.length(); i++) {
            if (i % columns == 0) {
                if (i != 0) {
                    root.addView(row);
                }
                row = new LinearLayout(this);
                row.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        0,
                        1f
                ));
                row.setOrientation(LinearLayout.HORIZONTAL);
            }
            ImageView view = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            view.setLayoutParams(params);
            view.setAdjustViewBounds(true);
            view.setPadding(2, 2, 2, 2);
            view.setImageDrawable(images.getDrawable(correspondingImageIndexex.get(i)));
            view.setAdjustViewBounds(true);
            view.setOnTouchListener(this);
            view.setOnDragListener(this);
            view.setTag(new Integer(i));
            row.addView(view);
        }
        root.addView(row);
    }

    private void randomize() {
        int size = correspondingImageIndexex.size();
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            int idx1 = rand.nextInt(size);
            int idx2 = rand.nextInt(size);
            int val1 = correspondingImageIndexex.get(idx1);
            correspondingImageIndexex.set(idx1, correspondingImageIndexex.get(idx2));
            correspondingImageIndexex.set(idx2, val1);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(INDEX_ARRAY, (ArrayList<Integer>) correspondingImageIndexex);
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        if (dragEvent.getAction() == DragEvent.ACTION_DROP) {
            ImageView source = (ImageView) dragEvent.getLocalState();
            Drawable sourceImage = source.getDrawable();
            source.setImageDrawable(((ImageView) view).getDrawable());
            ((ImageView) view).setImageDrawable(sourceImage);
            Integer sourceId = (Integer) source.getTag();
            Integer destinationId = (Integer) view.getTag();
            int sourceImageId = correspondingImageIndexex.get(sourceId);
            correspondingImageIndexex.set(sourceId, correspondingImageIndexex.get(destinationId));
            correspondingImageIndexex.set(destinationId, sourceImageId);
            if(isArranged())  {
                Toast toast = Toast.makeText(this, getResources().getString(R.string.puzzle_complete), Toast.LENGTH_LONG);
                toast.show();
            }
        }
        return true;
    }

    private boolean isArranged() {
        for (int i = 0; i < correspondingImageIndexex.size(); i++) {
            if (correspondingImageIndexex.get(i) != i) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        view.startDrag(null, new View.DragShadowBuilder(view), view, 0);
        return false;
    }
}
