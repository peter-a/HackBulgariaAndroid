package com.example.my.puzzle;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Puzzle extends Activity implements View.OnDragListener, View.OnTouchListener {
    private GridLayout gridLayout;

    private final String IMAGE_IDS = "imageIds";
    private List<Integer> imageIds;
    private List<Integer> viewPositions;

    private final String VIEWS = "views";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        List<Integer> imageIdsNew = new ArrayList<Integer>();
        for(int i = 0; i < imageIds.size(); i++)  {
            imageIdsNew.add(-1);
        }
        for(int i = 0; i < imageIds.size(); i++)  {
            int ithViewPosition = viewPositions.get(i);
            imageIdsNew.set(ithViewPosition, imageIds.get(i));
        }
        outState.putIntegerArrayList(IMAGE_IDS, (ArrayList<Integer>) imageIdsNew);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPositions = new ArrayList<Integer>();
        for (int i = 0; i < 16; i++) {
            viewPositions.add(i);
        }

        if(savedInstanceState == null)  {
            imageIds = new ArrayList<Integer>();
            for (int i = 0; i < 16; i++) {
                imageIds.add(i);
            }
            Collections.shuffle(imageIds);
        } else {
            imageIds = savedInstanceState.getIntegerArrayList(IMAGE_IDS);
        }
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int displayWidth = size.x;
        int quarterWidth = (displayWidth / 4) - 4;

        TypedArray images = getResources().obtainTypedArray(R.array.images);

        for (int i = 0; i < images.length(); i++) {
            ImageView view = new ImageView(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = quarterWidth;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.setMargins(2, 2, 2, 2);
            view.setLayoutParams(params);
            view.setAdjustViewBounds(true);
            if(savedInstanceState == null) {
                view.setImageDrawable(images.getDrawable(imageIds.get(i)));
            } else {
                view.setImageDrawable(images.getDrawable(imageIds.get(viewPositions.get(i))));
            }
            view.setTag(imageIds.get(i));
            view.setOnDragListener(this);
            view.setOnTouchListener(this);
            view.setTag(new Integer(i));
            gridLayout.addView(view);
        }
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        if (dragEvent.getAction() == DragEvent.ACTION_DROP) {
            ImageView imageDestination = (ImageView) view;
            ImageView imageSource = (ImageView) dragEvent.getLocalState();
            int sourceId = (Integer) imageSource.getTag();
            int destId = (Integer) imageDestination.getTag();
            float destX = imageDestination.getX();
            float destY = imageDestination.getY();

            PropertyValuesHolder sourceChangeX = PropertyValuesHolder.ofFloat("x", destX);
            PropertyValuesHolder sourceChangeY = PropertyValuesHolder.ofFloat("y", destY);
            Animator sourceAnim = new ObjectAnimator().ofPropertyValuesHolder(imageSource, sourceChangeX, sourceChangeY);
            sourceAnim.setDuration(300);

            float sourceX = imageSource.getX();
            float sourceY = imageSource.getY();

            PropertyValuesHolder destChangeX = PropertyValuesHolder.ofFloat("x", sourceX);
            PropertyValuesHolder destChangeY = PropertyValuesHolder.ofFloat("y", sourceY);
            Animator destAnim = new ObjectAnimator().ofPropertyValuesHolder(imageDestination, destChangeX, destChangeY);
            destAnim.setDuration(300);

            int imageSourcePosition = viewPositions.get(sourceId);
            viewPositions.set(sourceId, viewPositions.get(destId));
            viewPositions.set(destId, imageSourcePosition);

            AnimatorSet set = new AnimatorSet();
            set.play(sourceAnim).with(destAnim);
            set.start();
            if (isArranged()) {
                Toast toast = Toast.makeText(this, getResources().getString(R.string.puzzle_complete), Toast.LENGTH_LONG);
                toast.show();
            }
        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        view.startDrag(null, new View.DragShadowBuilder(view), view, 0);
        return false;
    }

    private boolean isArranged() {
        for (int i = 0; i < imageIds.size(); i++) {
            int position = viewPositions.get(i);
            int id = imageIds.get(i);
            if (id != position) {
                return false;
            }
        }
        return true;
    }
}
