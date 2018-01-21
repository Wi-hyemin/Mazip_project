package com.example.totoroto.homework2;

import android.location.Location;

import java.util.Comparator;
/**
 * Created by USER on 2018-01-21.
 */

public class AscendingDistance implements Comparator<ItemData> {
    double lat, lon;
    public AscendingDistance(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public int compare(ItemData o1, ItemData o2) {
        double o1_loc = distance(o1.getLat(),o1.getLon(),lat,lon);
        double o2_loc = distance(o2.getLat(),o2.getLon(),lat,lon);
        return Double.compare(o1_loc, o2_loc);
    }

    private double distance(double lat,double lon,double t_lat,double t_lon) {
        double garo = Math.abs(lat-t_lat);
        double sero = Math.abs(lon-t_lon);
        double result = Math.sqrt(garo*garo + sero*sero);
        return result;
    }
}