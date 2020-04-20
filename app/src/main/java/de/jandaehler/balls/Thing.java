package de.jandaehler.balls;

interface Thing {
    int getPosX();
    void setPosX(int posX);
    int getPosY();
    void setPosY(int posY);
    void invertX();
    void invertY();
    void move();
    void setVelocityX(float i);

    void setVelocityY(float i);
}
