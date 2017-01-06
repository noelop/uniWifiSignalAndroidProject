package com.noelop.p_0721.Circles;

/**
 * Created by noelop135899 on 2016/10/10.
 */

public class Intersectdistance {
    private double intersect[] = new double[4];
    private double center[] = new double[2];
    private double sect1[] = new double[2];
    private double sect2[] = new double[2];

    public Intersectdistance(double[] sect, double[] cen){
        intersect = sect;
        center = cen;
    }
    public double distance(double x_1, double y_1, double x_2, double y_2){
        return Math.sqrt(Math.pow(x_1-x_2,2)+Math.pow(y_1-y_2,2));
    }
    public double[] shortIntersect(){
        sect1[0] = intersect[0];
        sect1[1] = intersect[1];
        sect2[0] = intersect[2];
        sect2[1] = intersect[3];
        double intersect1 = distance(sect1[0],sect1[1],center[0],center[1]);
        double intersect2 = distance(sect2[0],sect1[1],center[0],center[1]);
        if (intersect1<=intersect2){
            return new double[]{sect1[0],sect1[1]};
        }else{
            return new double[]{sect2[0],sect2[1]};
        }
    }
}