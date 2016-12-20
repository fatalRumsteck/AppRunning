package com.example.antoine.apprunning;

import java.io.Serializable;

/**
 * Created by valentin on 20/12/2016.
 */

public class Mesure implements Serializable {
    public double lon;
    public double lat;
    public double haut;
    public String temps;
    public Mesure(double lo, double la, double ha, String tps){
        lon=lo;
        lat=la;
        haut=ha;
        temps=tps;
    }
}
