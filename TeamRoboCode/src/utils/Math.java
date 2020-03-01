package utils;

public class Math {

    public static double distanceBetween2Points(double p1x, double p1y, double p2x, double p2y){
        double x = java.lang.Math.sqrt(((java.lang.Math.pow((p1x - p2x), 2)) + (java.lang.Math.pow((p1y - p2y), 2))));
        return x;
    }

    public static double normalizeBearing(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

}