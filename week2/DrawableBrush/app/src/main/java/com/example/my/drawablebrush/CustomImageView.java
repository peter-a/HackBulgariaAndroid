package com.example.my.drawablebrush;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by My on 25.10.2014 г..
 */
public class CustomImageView extends ImageView {
    private Canvas canvas;
    private Bitmap currentBitmap;
    private Bitmap canvasBitmap;
    private float currentX;
    private float currentY;

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public void setCurrentBitmap(Bitmap currentBitmap) {
        this.currentBitmap = currentBitmap;
    }

    public void setCurrentX(float currentX) {
        this.currentX = currentX;
    }

    public void setCanvasBitmap(Bitmap canvasBitmap) {
        this.canvasBitmap = canvasBitmap;
    }

    public void setCurrentY(float currentY) {
        this.currentY = currentY;
    }

    @Override
    protected void onDraw(Canvas systemCanvas) {
        super.onDraw(systemCanvas);
        if (currentBitmap != null) {
            Paint paint = new Paint();
            paint.setAlpha(125);
            canvas.drawBitmap(currentBitmap, currentX - (MyActivity.PGN_SIZE / 2), currentY - (MyActivity.PGN_SIZE / 2), paint);
            systemCanvas.drawBitmap(canvasBitmap, 0.0f, 0.0f, new Paint());
        }
    }
}
