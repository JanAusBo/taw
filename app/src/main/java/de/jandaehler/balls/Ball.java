package de.jandaehler.balls;

public class Ball {

    private int posX;
    private int posY;
    private int dirX;
    private int dirY;
    private double degree;
    private int speed;
    private static final int DIRECTION_FACTOR = 5;
    private Physics physics;

    Ball(int x, int y, Physics physics){
        posX = x;
        posY = y;
        degree = (Math.random() * 360);
        speed = (int) (Math.random() * 5);

        // über den Sinus die X-Koordinate berechnen
        dirX = (int) ((Math.sin(degree) * DIRECTION_FACTOR));
        // über den Cosinus die Y-Koordinate berechnen
        dirY = (int) ((Math.cos(degree) * DIRECTION_FACTOR));

        this.physics = physics;
    }

    int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    void invertX() {
        dirX*= -1;
    }

    void invertY() {
        dirY*= -1;
    }

    void move() {
        // Physik abfragen
        if (physics != null){
            dirX = dirX + (int) physics.getGravityX();
            dirY = dirY + (int) physics.getGravityY();
        }

        // Neue Position berechnen
        posX += dirX;
        posY += dirY;
    }
}
