package com.amostrone.akash.threadam;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.ThreadLocalRandom;

public class Game extends View {

    Rect[] lines = new Rect[6];

    Paint paint_blue;
    Paint paint_red;
    Paint paint_green;
    Paint paint_game;

    float initialY;
    int currnt_move=-1;
    float time=30;
    int score=0;

    int[] random_pos = {0,0,0,0,0,0};

    public Game(Context context) {
        super(context);
        for(int i=0;i<6;i++)
            lines[i] = new Rect();

        paint_blue = new Paint();
        paint_red = new Paint();
        paint_green = new Paint();
        paint_game = new Paint();

        paint_game.setColor(Color.BLACK);
        paint_blue.setColor(Color.BLUE);
        paint_green.setColor(Color.GREEN);
        paint_red.setColor(Color.RED);

        paint_game.setTextSize(40);
        paint_green.setStrokeWidth(10);
        paint_red.setStrokeWidth(10);
        paint_blue.setStrokeWidth(10);
    }

    void randomise_position() {
        for(int i=0;i<6;i++)
            random_pos[i] = ThreadLocalRandom.current().nextInt(0, getHeight() + 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(random_pos[0]==0)randomise_position();

        for(int i=0;i<6;i++) {
            lines[i].left = 0;
            lines[i].right = getWidth();
            lines[i].top = random_pos[i];
            lines[i].bottom = random_pos[i] + 10;
        }

        canvas.drawRect(lines[0],paint_blue);
        canvas.drawRect(lines[1],paint_blue);
        canvas.drawRect(lines[2],paint_green);
        canvas.drawRect(lines[3],paint_green);
        canvas.drawRect(lines[4],paint_red);
        canvas.drawRect(lines[5],paint_red);

        canvas.drawText("Score :  "+score,450,40,paint_game);
        canvas.drawText("Time : "+String.format("%.02f", time),750,40,paint_game);

        if(!checkGameOver()) canvas.drawText("Arrange the threads",5,40,paint_game);
        else {
            canvas.drawText("Game Over",5,40,paint_game);
        }

        if(time<0){
            Toast.makeText(getContext(), "You Lost, Your score was : "+score, Toast.LENGTH_SHORT).show();
            score=0;
            time=30;
            randomise_position();
        }
        update();

    }

    private void update() {
        time-=0.01;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
                initialY = event.getY();
                for(int i=0;i<6;i++) {
                    if(Math.abs(random_pos[i]-event.getY())<=20) {
                        currnt_move = i;
                        break;
                    }
                    else currnt_move=-1;
                }
                // Log.d(TAG, "Action was DOWN");
                break;

            case MotionEvent.ACTION_MOVE:

                float finalY = event.getY();

                //Log.d(TAG, "Action was UP");

                if (initialY < finalY) {
                    if(currnt_move!=-1){
                        random_pos[currnt_move]-=initialY-finalY;
                        initialY=finalY;
                    }
                    // Log.d(TAG, "Up to Down swipe performed");
                }

                if (initialY > finalY) {
                    if(currnt_move!=-1){
                        random_pos[currnt_move]+=finalY-initialY;
                        initialY=finalY;
                    }
                    // Log.d(TAG, "Down to Up swipe performed");
                }
                //Log.d(TAG, "Action was MOVE");
                break;

            case MotionEvent.ACTION_UP:
                if(checkGameOver()){
                    Toast.makeText(getContext(), "You won the round", Toast.LENGTH_SHORT).show();
                    randomise_position();
                    score++;
                    time=30;
                }
                //Log.d(TAG, "Action was UP");
                break;

            case MotionEvent.ACTION_CANCEL:
                //Log.d(TAG,"Action was CANCEL");
                break;

            case MotionEvent.ACTION_OUTSIDE:
                // Log.d(TAG, "Movement occurred outside bounds of current screen element");
                break;
        }
        invalidate();

        return true;
    }

    private boolean checkGameOver() {

        //Check if there is any line between blue lines
        for(int i=2;i<=5;i++){
            if((random_pos[0]<random_pos[i]&&random_pos[1]>random_pos[i]) ||
                    random_pos[1]<random_pos[i]&&random_pos[0]>random_pos[i])
            return false;
        }

        //Check if there is any line between green lines
        for(int i=0;i<=5;i++){
            if((random_pos[2]<random_pos[i]&&random_pos[3]>random_pos[i]) ||
                    random_pos[3]<random_pos[i]&&random_pos[2]>random_pos[i])
                return false;
        }

        //Check if there is any line between red lines
        for(int i=0;i<=3;i++){
            if((random_pos[4]<random_pos[i]&&random_pos[5]>random_pos[i]) ||
                    random_pos[5]<random_pos[i]&&random_pos[4]>random_pos[i])
                return false;
        }

        return true;
    }

}
