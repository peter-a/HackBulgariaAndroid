package com.example.my.keyframeanimator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class MyActivity extends Activity implements View.OnClickListener {

    private ImageView imageView;
    private View root;

    private double startScale;
    private float startRotation;
    private double startTranslationX;
    private double startTranslationY;

    private PointF finger1 = new PointF(0, 0);
    private PointF finger2 = new PointF(0, 0);
    private PointF middle = new PointF(0, 0);

    private double startScaleDistance;
    private double currentScaleDistance;

    private PointF startTranslationCenter = new PointF(0, 0);
    private PointF currentTranslationCenter = new PointF(0, 0);

    private PointF startRotationVector = new PointF(0, 0);
    private PointF currentRotationVector = new PointF(0, 0);

    private Button saveFrame;
    private Button play;
    private AnimatorSet animatorSet = new AnimatorSet();
    private List<Animator> animations = new ArrayList<Animator>();

    private final static String TAG = "debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        saveFrame = (Button) findViewById(R.id.btn_save);
        saveFrame.setOnClickListener(this);
        play = (Button) findViewById(R.id.btn_play);
        play.setOnClickListener(this);

        imageView = (ImageView) findViewById(R.id.image);
        root = findViewById(R.id.root);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                updateFingersCoordinates(motionEvent);
                updateCurrentTranslationCenter(motionEvent);

                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        saveStartScale();
                        saveStartTranslation();
                        saveStartRotation();
                        startTranslationCenter.set(motionEvent.getX(), motionEvent.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        updateTranslation();
                        if (existsId(motionEvent, 0) && existsId(motionEvent, 1)) {
                            updateScale();
                            updateRotation();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        if (existsId(motionEvent, 0) && existsId(motionEvent, 1)) {
                            saveStartScale();
                            saveStartTranslation();
                            saveStartRotation();
                            startTranslationCenter.set((finger1.x + finger2.x) / 2, (finger1.y + finger2.y) / 2);
                            startScaleDistance = calculateDistance();
                            startRotationVector.set(finger1.x - finger2.x, finger1.y - finger2.y);

                            // The following if statement serves the case when the user puts a third finger,
                            // removes the first two and then adds the first, because the case ACTION_DOWN is
                            // bypassed.
                        } else if (existsId(motionEvent, 0)) {
                            saveStartTranslation();
                            startTranslationCenter.set(finger1.x, finger1.y);
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        int actionSourceId = motionEvent.getPointerId(motionEvent.getActionIndex());
                        if (actionSourceId == 0 && existsId(motionEvent, 1)) {
                            saveStartTranslation();
                            startTranslationCenter.set(finger2.x, finger2.y);
                            currentTranslationCenter.set(finger2.x, finger2.y);
                        }
                        if (actionSourceId == 1 && existsId(motionEvent, 0)) {
                            saveStartTranslation();
                            startTranslationCenter.set(finger1.x, finger1.y);
                            currentTranslationCenter.set(finger1.x, finger1.y);
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void saveStartRotation() {
        startRotation = imageView.getRotation();
    }

    private void updateRotation() {
        currentRotationVector.set(finger1.x - finger2.x, finger1.y - finger2.y);
        double angleStart = Math.atan2(startRotationVector.x, startRotationVector.y);
        double angleEnd = Math.atan2(currentRotationVector.x, currentRotationVector.y);
        float angle = (float) (angleStart - angleEnd);
        imageView.setRotation(angle * (180.0f / (float) Math.PI) + startRotation);
    }

    private void updateScale() {
        currentScaleDistance = calculateDistance();
        imageView.setScaleX((float) ((startScale * (currentScaleDistance / startScaleDistance))));
        imageView.setScaleY((float) ((startScale * (currentScaleDistance / startScaleDistance))));
    }

    private void updateTranslation() {
        imageView.setTranslationX((float) (startTranslationX + currentTranslationCenter.x - startTranslationCenter.x));
        imageView.setTranslationY((float) (startTranslationY + currentTranslationCenter.y - startTranslationCenter.y));
    }

    private void saveStartScale() {
        startScale = imageView.getScaleX();
    }

    private void saveStartTranslation() {
        startTranslationX = imageView.getTranslationX();
        startTranslationY = imageView.getTranslationY();
    }

    private void updateFingersCoordinates(MotionEvent motionEvent) {
        for (int i = 0; i < motionEvent.getPointerCount(); i++) {
            if (motionEvent.getPointerId(i) == 0) {
                finger1.set(motionEvent.getX(i), motionEvent.getY(i));
            } else if (motionEvent.getPointerId(i) == 1) {
                finger2.set(motionEvent.getX(i), motionEvent.getY(i));
            }
        }

        if (existsId(motionEvent, 0) && existsId(motionEvent, 1)) {
            middle.set((finger1.x + finger2.x) / 2, (finger1.y + finger2.y) / 2);
        } else if (existsId(motionEvent, 0)) {
            middle.set(finger1.x, finger1.y);
        } else if (existsId(motionEvent, 1)) {
            middle.set(finger2.x, finger2.y);
        }
    }

    private void updateCurrentTranslationCenter(MotionEvent motionEvent) {
        boolean finger1Exists = existsId(motionEvent, 0);
        boolean finger2Exists = existsId(motionEvent, 1);

        if (finger1Exists && finger2Exists) {
            currentTranslationCenter.set((finger1.x + finger2.x) / 2, (finger1.y + finger2.y) / 2);
        } else if (finger1Exists) {
            currentTranslationCenter.set(finger1.x, finger1.y);
        } else if (finger2Exists) {
            currentTranslationCenter.set(finger2.x, finger2.y);
        }
    }

    private boolean existsId(MotionEvent motionEvent, int id) {
        for (int i = 0; i < motionEvent.getPointerCount(); i++) {
            if (motionEvent.getPointerId(i) == id) {
                return true;
            }
        }
        return false;
    }

    private double calculateDistance() {
        float x1 = finger1.x;
        float x2 = finger2.x;
        float y1 = finger1.y;
        float y2 = finger2.y;
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_save) {
            PropertyValuesHolder translationX = PropertyValuesHolder.ofFloat("translationX", imageView.getTranslationX());
            PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", imageView.getTranslationY());
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", imageView.getScaleX());
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", imageView.getScaleY());
            PropertyValuesHolder rotation = PropertyValuesHolder.ofFloat("rotation", imageView.getRotation());
            Animator animation = new ObjectAnimator().ofPropertyValuesHolder(imageView, translationX, translationY, scaleX, scaleY, rotation);
            animation.setDuration(400);
            animations.add(animation);
        } else if (id == R.id.btn_play) {
            animatorSet.playSequentially(animations);
            animatorSet.start();
            animatorSet = new AnimatorSet();
            animations = new ArrayList<Animator>();
        }
    }
}
