package de.jandaehler.balls;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

        private SensorManager sensorManager;
        private Sensor sensor;

        private int mInterval = 5; // 5 seconds by default, can be changed later
        private Handler mHandler;

        private float gravity[];
        private float linear_acceleration[];


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

            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            gravity = new float[3];
            linear_acceleration = new float[3];

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

        private void collisionChecker(){
            if (ball.getPosX() + radius >= x || ball.getPosX() - radius <= 0){
                ball.invertX();
            }
            if (ball.getPosY() + radius >= y || ball.getPosY() - radius <= 0){
                ball.invertY();
            }
        }

        private void moveBall(){

            // Log.v("moveBall", sensor.toString());
            collisionChecker();
            ball.move();
            invalidate();
        }


        public SensorEventListener gyroListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int acc) {
            }

            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];


                Log.d("X","X : " + (int) x + " rad/s");
                Log.d("Y", "Y : " + (int) y + " rad/s");
                Log.d("Z","Z : " + (int) z + " rad/s");

                // textX.setText("X : " + (int) x + " rad/s");
                // textY.setText("Y : " + (int) y + " rad/s");
                // textZ.setText("Z : " + (int) z + " rad/s");
            }
        };


/*        public void onSensorChanged(SensorEvent event)
        {
            // alpha is calculated as t / (t + dT)
            // with t, the low-pass filter's time-constant
            // and dT, the event delivery rate

            final float alpha = (float) 0.8;

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];
        }
*/


    }
}
