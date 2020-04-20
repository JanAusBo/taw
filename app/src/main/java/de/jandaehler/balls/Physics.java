package de.jandaehler.balls;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
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
    private int height;
    private int width;
    private enum Orientation{HORIZONTAL, VERTICAL}
    public enum CollisionAxis{NONE, X, Y, BOTH};

    String getMagnetoString() {
        return magnetoString;
    }

    String getGravityString() {
        return gravityString;
    }

    /**
     * Returns the acceleration in X-direction. Range is 0 .. 9.81 m/s².
     * Based on a Top-Left oriented coordinate systems acceleration to the right
     * is represented as positive values, acceleration to the left is represented
     * as negative values.
     * @return
     */
    float getGravityX() {
        return gravityX;
    }

    /**
     * Returns the acceleration in Y-direction. Range is 0 .. 9.81 m/s².
     * Based on a Top-Left oriented coordinate systems downwards acceleration
     * is represented as positive values, upwards acceleration is represented
     * as negative values.
     * @return
     */
    float getGravityY() {
        return gravityY;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
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
            gravityX = event.values[0] * -1;
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

    public CollisionAxis checkCollision(Thing thing, int dirX, int dirY) {

        CollisionAxis ca = CollisionAxis.NONE;

        if (thing.getClass() == Ball.class) {
            // check top boundary
            if (dirY < 0 && thing.getPosY() - Ball.RADIUS + dirY <= 0){
                thing.setPosY(Ball.RADIUS);
                ca = CollisionAxis.Y;
            }
            // check bottom boundary
            if (dirY > 0 && thing.getPosY() + Ball.RADIUS + dirY >= height) {
                thing.setPosY(height - Ball.RADIUS);
                ca = CollisionAxis.Y;
            }
            //check left boundary
            if (dirX < 0 && thing.getPosX() - Ball.RADIUS + dirX <= 0){
                thing.setPosX(Ball.RADIUS);
                ca = ca == CollisionAxis.Y ? CollisionAxis.BOTH : CollisionAxis.X;
            }
            // check right boundary
            if (dirX > 0 && thing.getPosX() + Ball.RADIUS + dirX >= width){
                thing.setPosX(width - Ball.RADIUS);
                ca = ca == CollisionAxis.Y ? CollisionAxis.BOTH : CollisionAxis.X;
            }
        }
        return ca;
    }

    /**
     * Convert from SI-unit to screen oriented unit in vertical orientation.
     * @param length: Length in meter.
     * @return Number of pixels.
     */
    public int meterToPixelVertical(float length){
        return meterToPixel(Orientation.VERTICAL, length);
    }

    /**
     * Convert from SI-unit to screen oriented unit in horizontal orientation.
     * @param length: Length in meter.
     * @return Number of pixels.
     */
    public int meterToPixelHorizontal(float length){
        return meterToPixel(Orientation.HORIZONTAL, length);
    }

    private int meterToPixel(Orientation orientation, float length){
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        float pixels;
        if (orientation == Orientation.VERTICAL){
            pixels = metrics.ydpi;
        }else //Orientation.HORIZONTAL
        {
            pixels = metrics.xdpi;
        }
        // pixels per centimeter
        pixels /= 2.54f;
        // pixels per meter
        pixels *= 100;
        return (int) (pixels * length);
    }

    /**
     * Calculates the new velocity by acceleration over time.
     * Formula: v_neu = a * t + v_alt
     * @param acceleration
     * @param time
     * @param velocity
     * @return The new velocity value which can be negative.
     */
    float calcVelocity (float acceleration, long time, float velocity){
        float t2 = (float) time / 1000;

        float v2 = acceleration * t2 + velocity;

        return v2; // acceleration * (time / 1000) + velocity;
    }


    /**
     * calculates the distance in meter with single precision.
     * Formula: s = v * t
     * @param velocity: Velocity in m/s.
     * @param time: In milliseconds.
     * @return: Distance in meters with float precision.
     */
    public float calcDistance (float velocity, long time){
        return velocity * ((float) time / 1000);
    }

}
