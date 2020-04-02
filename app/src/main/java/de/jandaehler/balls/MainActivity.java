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


    SensorManager sensorManager;
    Sensor gravitiySensor;
    Sensor magnetometer;
    String magnetoString = "";
    String gravityString = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        setContentView(new MyView(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravitiySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY); // (Sensor.TYPE_GYROSCOPE);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(gyroListener, gravitiySensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(magnetoListener, magnetometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(gyroListener);
        sensorManager.unregisterListener(magnetoListener);
    }

    public SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            String msg = "X : " + (int) x + " rad/s, Y : " + (int) y + " rad/s, Z : " + (int) z + " rad/s,";

            Log.d("Gravity",msg);
            gravityString = "Gravity " + msg;
            // textX.setText("X : " + (int) x + " rad/s");
            // textY.setText("Y : " + (int) y + " rad/s");
            // textZ.setText("Z : " + (int) z + " rad/s");
        }
    };

    public SensorEventListener magnetoListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            String msg = "X : " + (int) x + " rad/s, Y : " + (int) y + " rad/s, Z : " + (int) z + " rad/s,";

            Log.d("Magneto",msg);
            magnetoString = "Magneto " + msg;


        }
    };

    public class MyView extends View
    {
        Paint paint = null;
        Paint circleText = null;
        Paint debugText = null;
        String debugString = "";

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

            circleText = new Paint(Paint.ANTI_ALIAS_FLAG);
            circleText.setColor(Color.parseColor("#000000")); //black
            circleText.setTextSize(30);

            debugText = new Paint(Paint.ANTI_ALIAS_FLAG);
            debugText.setColor(Color.parseColor("#000000"));
            debugText.setTextSize(30);

            mHandler = new Handler();

            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
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
            String myText = "Das ist der Info Text";

            canvas.drawText(myText, 100, 100, circleText);
            canvas.drawText(magnetoString, 20, 930, debugText);
            canvas.drawText(gravityString, 20, 960, debugText);

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

    }
}
