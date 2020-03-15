package de.jandaehler.balls;

public class Ball {

    int posX = 0;
    int posY = 0;

    public Ball(int x, int y){
        posX = x;
        posY = y;
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

    public void move(int x, int y) {
        posX += x;
        posY += y;
    }
}
