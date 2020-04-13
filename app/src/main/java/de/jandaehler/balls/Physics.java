package de.jandaehler.balls;

public class Physics implements LiveCicleObserver {

    String magnetoString = "";
    String gravityString = "";

    public String getMagnetoString() {
        return magnetoString;
    }

    public String getGravityString() {
        return gravityString;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onResume() {

    }

    public Physics(){
        MainActivity.liveCircleObserverList.add(this);
    }

}
