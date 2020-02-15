package utils;

import robocode.*;

public class Math {

    public static double distanceBetween2Points(double p1x, double p1y, double p2x, double p2y){
        double x = java.lang.Math.sqrt(((java.lang.Math.pow((p1x - p2x), 2)) + (java.lang.Math.pow((p1y - p2y), 2))));
        return x;
    }

    public static double pythagorasTheorem(double fromX, double fromY, double toX, double toY){
        // Pythagoras theorem
        double hipotenusa = distanceBetween2Points(fromX, fromY, toX, toY);
        double adjacente = distanceBetween2Points(fromX, fromY, fromX, toY);
        double cosB = adjacente/hipotenusa;

        // Inverse of Cos
        double acosB = java.lang.Math.acos(cosB);

        // Transform to Degrees
        double acosBDegrees = (180/ java.lang.Math.PI)*acosB;

        return acosBDegrees;
    }

}

