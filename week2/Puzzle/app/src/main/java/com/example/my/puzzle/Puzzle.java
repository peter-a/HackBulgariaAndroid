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

public class Puzzle extends Activity implements View.OnDragListener, View.OnTouchListener {
    private GridLayout gridLayout;

    private final int rows = 4;
    private final int cols = 4;

    private final String VIEW_POSITIONS = "positions";
    private List<ImageView> imageViews;
    private List<Integer> viewPositions;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(VIEW_POSITIONS, (ArrayList<Integer>) viewPositions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageViews = new ArrayList<ImageView>();
        TypedArray images = getResources().obtainTypedArray(R.array.images);
        for (int i = 0; i < rows * cols; i++) {
            ImageView next = new ImageView(this);
            imageViews.add(next);
        }

        if (savedInstanceState == null) {
            viewPositions = new ArrayList<Integer>();
            for (int i = 0; i < rows * cols; i++) {
                ImageView next = imageViews.get(i);
                next.setTag(i);
                next.setImageDrawable(images.getDrawable(i));
                viewPositions.add(-1);
            }
            Collections.shuffle(imageViews);
            for (int i = 0; i < rows * cols; i++) {
                Integer ithPositionView = (Integer) imageViews.get(i).getTag();
                viewPositions.set(ithPositionView, i);
            }
        } else {
            viewPositions = savedInstanceState.getIntegerArrayList(VIEW_POSITIONS);
            for (int i = 0; i < rows * cols; i++) {
                int ithViewPosition = viewPositions.get(i);
                ImageView view = imageViews.get(ithViewPosition);
                view.setTag(i);
                view.setImageDrawable(images.getDrawable(i));
            }
        }

        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int displayWidth = size.x;
        int quarterWidth = (displayWidth / 4) - 4;

        for (int i = 0; i < images.length(); i++) {
            ImageView next = imageViews.get(i);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = quarterWidth;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.setMargins(2, 2, 2, 2);
            next.setLayoutParams(params);
            next.setAdjustViewBounds(true);
            next.setOnDragListener(this);
            next.setOnTouchListener(this);
            gridLayout.addView(next);
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
        for (int i = 0; i < imageViews.size(); i++) {
            int position = viewPositions.get(i);
            if (i != position) {
                return false;
            }
        }
        return true;
    }
}
