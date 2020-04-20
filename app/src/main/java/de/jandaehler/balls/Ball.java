package de.jandaehler.balls;

import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;

public class Ball implements Thing {

    private int posX;
    private int posY;
    private float velocityX;
    private float velocityY;
    static int RADIUS = 150;
    private double degree;
    private int speed;
    private int weight;
    private int solidity;
    private static final int DIRECTION_FACTOR = 5;
    private Physics physics;
    private long lastTimeStamp;

    Ball(int x, int y, Physics physics){
        posX = x;
        posY = y;
        degree = (Math.random() * 360);
        speed = (int) (Math.random() * 5);

        // über den Sinus die X-Koordinate berechnen
        // velocityX = (int) Math.abs(Math.sin(degree) * DIRECTION_FACTOR);
        // über den Cosinus die Y-Koordinate berechnen
        // velocityY = (int) Math.abs(Math.cos(degree) * DIRECTION_FACTOR);

        this.physics = physics;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void invertX() {
        velocityX *= -1;
    }

    public void invertY() {
        velocityY *= -1;
    }


    /**
     * Moves the ball across the screen depending on physical principles.
     */
    public void move() {

        long currentTimeStamp = SystemClock.elapsedRealtime();
        long time = currentTimeStamp - lastTimeStamp;
        Log.d("move", "" + (time));
        lastTimeStamp = currentTimeStamp;

        // Physik abfragen
        if (physics != null){
            velocityX = physics.calcVelocity(physics.getGravityX(), time, velocityX);
            float distanceX = physics.calcDistance(velocityX, time);
            velocityY = physics.calcVelocity(physics.getGravityY(), time, velocityY);
            float distanceY = physics.calcDistance(velocityY, time);

            DisplayMetrics metrics = getResources().getDisplayMetrics();

            physics.checkCollision(this, posX, posY);

        }

        // Neue Position berechnen
        posX += velocityX;
        posY += velocityY;
    }
}
