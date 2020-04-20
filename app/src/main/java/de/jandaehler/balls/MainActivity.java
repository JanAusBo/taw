package de.jandaehler.balls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static List<LiveCicleObserver> liveCircleObserverList;
    Physics physics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        Log.d("onCreate","Die Methode wird aufgerufen.");
        liveCircleObserverList = new LinkedList<>();
        physics = new Physics(this.getApplicationContext());
        liveCircleObserverList.add(physics);
        liveCircleObserverList.forEach((v)-> v.onCreate());
        setContentView(new MyView(this));
    }

    public void onResume() {
        super.onResume();
        liveCircleObserverList.forEach((v)-> v.onResume());
    }

    public void onStop() {
        super.onStop();
        liveCircleObserverList.forEach((v)-> v.onStop());
    }

    public class MyView extends View
    {
        Paint paint;
        Paint circleText;
        Paint debugText;
        String debugString = "";

        int x = 0;
        int y = 0;
        Ball ball = new Ball(150, 150, physics);

        private int mInterval = 5; // 5 seconds by default, can be changed later
        private Handler mHandler;

        Runnable mStatusChecker = new Runnable() {
            @Override
            public void run() {
                try {
                    moveBall(); //this function can change value of mInterval.
                } finally {
                    // 100% guarantee that this always happens, even if
                    // your update method throws an exception
                    mHandler.postDelayed(mStatusChecker, mInterval);
                }
            }
        };

        void startRepeatingTask() {
            mStatusChecker.run();
        }

        void stopRepeatingTask() {
            mHandler.removeCallbacks(mStatusChecker);
        }

        public MyView(Context context)
        {
            super(context);
            paint = new Paint();

            circleText = new Paint(Paint.ANTI_ALIAS_FLAG);
            circleText.setColor(Color.parseColor("#000000")); //black
            circleText.setTextSize(30);

            debugText = new Paint(Paint.ANTI_ALIAS_FLAG);
            debugText.setColor(Color.parseColor("#000000"));
            debugText.setTextSize(30);

            mHandler = new Handler();

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

            physics.setHeight(height);
            physics.setWidth(width);


            startRepeatingTask();
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);

            x = getWidth();
            y = getHeight();

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);
            // Use Color.parseColor to define HTML colors
            paint.setColor(Color.parseColor("#CD5C5C"));
            canvas.drawCircle(ball.getPosX(), ball.getPosY(), Ball.RADIUS, paint);
            String myText = "Das ist der Info Text";

            canvas.drawText(myText, 100, 100, circleText);
            canvas.drawText(physics.getGravityString(), 20, 930, debugText);
            canvas.drawText(physics.getMagnetoString(), 20, 960, debugText);
        }

        private void moveBall(){
            ball.move();
            invalidate();
        }

    }
}
