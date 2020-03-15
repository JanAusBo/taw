package de.jandaehler.balls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        setContentView(new MyView(this));
    }

    public class MyView extends View
    {
        Paint paint = null;

        int x = 0;
        int y = 0;
        int radius = 150;
        Ball ball = new Ball(150, 150);

        private int mInterval = 5; // 5 seconds by default, can be changed later
        private Handler mHandler;


//        @Override
//        public void onDestroy() {
//            super.onDestroy();
//            stopRepeatingTask();
//        }

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

            mHandler = new Handler();
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
            canvas.drawCircle(ball.getPosX(), ball.getPosY(), radius, paint);
        }

        private void collisionCheck(){


        }

        private void moveBall(){
            ball.move(1, 1);
            invalidate();
        }



    }
}
