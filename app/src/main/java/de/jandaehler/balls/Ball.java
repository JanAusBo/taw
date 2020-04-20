package de.jandaehler.balls;

import android.os.SystemClock;
import android.util.Log;

public class Ball implements Thing {

    private int posX;
    private int posY;
    private float velocityX;
    private float velocityY;
    private float distanceX = 0;
    private float distanceY = 0;
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
        if (time > 100) time = 100;
        Log.d("move", "" + (time));
        lastTimeStamp = currentTimeStamp;


        Physics.CollisionAxis ca = Physics.CollisionAxis.NONE;

        // Physik abfragen
        if (physics != null){
            velocityX = physics.calcVelocity(physics.getGravityX(), time, velocityX);
            distanceX = physics.calcDistance(velocityX, time);
            velocityY = physics.calcVelocity(physics.getGravityY(), time, velocityY);
            distanceY = physics.calcDistance(velocityY, time);

            ca = physics.checkCollision(
                    this,
                    physics.meterToPixelHorizontal(distanceX),
                    physics.meterToPixelVertical(distanceY));

        }

        // Neue Position berechnen
        if (ca != Physics.CollisionAxis.BOTH && ca != Physics.CollisionAxis.X)
            posX += physics.meterToPixelHorizontal(distanceX);
        if (ca != Physics.CollisionAxis.BOTH && ca != Physics.CollisionAxis.Y)
            posY += physics.meterToPixelVertical(distanceY);
    }

    @Override
    public void setVelocityX(float i) {
        velocityX = i;
    }

    @Override
    public void setVelocityY(float i) {
        velocityY = i;
    }

    public String getDebugString() {

        return String.format("vX: %,.2f dX: %,.2f vY: %,.2f dY: %,.2f", velocityX, distanceX, velocityY, distanceY);

    }
}
