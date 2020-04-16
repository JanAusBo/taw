package de.jandaehler.balls;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Physics implements LiveCicleObserver {

    private SensorManager sensorManager;
    private Sensor gravitySensor;
    private Sensor magneticSensor;
    private String gravityString = "";
    private String magnetoString = "";
    private Context mContext;
    private float gravityX;
    private float gravityY;

    String getMagnetoString() {
        return magnetoString;
    }

    String getGravityString() {
        return gravityString;
    }

    float getGravityX() {
        return gravityX;
    }

    float getGravityY() {
        return gravityY;
    }

    Physics(Context context){
        MainActivity.liveCircleObserverList.add(this);
        mContext = context;
    }

    @Override
    public void onCreate() {
        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null){
            gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY); // (Sensor.TYPE_GYROSCOPE);
            magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD); //TYPE_MAGNETIC_FIELD
        }
    }

    @Override
    public void onResume() {
        sensorManager.registerListener(gyroListener, gravitySensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(magnetoListener, magneticSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onStop(){
        sensorManager.unregisterListener(gyroListener);
        sensorManager.unregisterListener(magnetoListener);
    }


    private SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            gravityX = event.values[0];
            gravityY = event.values[1];
            float z = event.values[2];

            String msg = "X : " + (int) gravityX + " rad/s, Y : " + (int) gravityY + " rad/s, Z : " + (int) z + " rad/s,";

            Log.d("Gravity",msg);
            gravityString = "Gravity " + msg;
        }
    };

    private SensorEventListener magnetoListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            x = (float) (Math.PI / 180.0 * x * 100);
            y = (float) (Math.PI / 180.0 * y * 100);
            z = (float) (Math.PI / 180.0 * z * 100);

            String msg = "X : " + (int) x + " rad/s, Y : " + (int) y + " rad/s, Z : " + (int) z + " rad/s,";

            Log.d("Magneto",msg);
            magnetoString = "Magneto " + msg;


        }
    };


}
