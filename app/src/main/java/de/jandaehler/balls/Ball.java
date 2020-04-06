package de.jandaehler.balls;

public class Ball {

    private int posX = 0;
    private int posY = 0;
    private int dirX = 0;
    private int dirY = 0;
    private double degree = 0;
    private int speed = 0;
    private static final int DIRECTION_FACTOR = 5;

    public Ball(int x, int y){
        posX = x;
        posY = y;
        degree = (Math.random() * 360);
        speed = (int) (Math.random() * 5);

        // über den Sinus die X-Koordinate berechnen
        dirX = (int) ((Math.sin(degree) * DIRECTION_FACTOR));
        // über den Cosinus die Y-Koordinate berechnen
        dirY = (int) ((Math.cos(degree) * DIRECTION_FACTOR));
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



    public void move() {
        // Physik abfragen

        // Neue Position berechnen
        posX += dirX;
        posY += dirY;
    }

    public void invertX() {
        dirX*= -1;
    }

    public void invertY() {
        dirY*= -1;
    }
}
