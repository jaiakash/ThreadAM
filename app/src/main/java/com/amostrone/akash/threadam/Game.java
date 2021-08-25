package com.amostrone.akash.threadam;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.concurrent.ThreadLocalRandom;

public class Game extends View {

    Paint paint_blue;
    Paint paint_red;
    Paint paint_green;

    int[] random_pos = new int[6];

    public Game(Context context) {
        super(context);
        paint_blue = new Paint();
        paint_red = new Paint();
        paint_green = new Paint();
        paint_blue.setColor(Color.BLUE);
        paint_green.setColor(Color.GREEN);
        paint_red.setColor(Color.RED);

        paint_green.setStrokeWidth(10);
        paint_red.setStrokeWidth(10);
        paint_blue.setStrokeWidth(10);

        randomise_position();
    }

    void randomise_position() {
        for(int i=0;i<6;i++)
            random_pos[i] = ThreadLocalRandom.current().nextInt(0, getHeight() + 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(0, random_pos[0], getWidth(), random_pos[0], paint_blue);
        canvas.drawLine(0, random_pos[1], getWidth(), random_pos[1], paint_red);
        canvas.drawLine(0, random_pos[2], getWidth(), random_pos[2], paint_green);
        canvas.drawLine(0, random_pos[3], getWidth(), random_pos[3], paint_blue);
        canvas.drawLine(0, random_pos[4], getWidth(), random_pos[4], paint_red);
        canvas.drawLine(0, random_pos[5], getWidth(), random_pos[5], paint_green);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
                randomise_position();
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
        }
        postInvalidate();

        return true;
    }

}
