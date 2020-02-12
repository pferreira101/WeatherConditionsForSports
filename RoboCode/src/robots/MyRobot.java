package robots;

import robocode.*;
import standardOdometer.Odometer;
import static robocode.util.Utils.normalRelativeAngleDegrees;


public class MyRobot extends AdvancedRobot {

    public void run() {
        //Go to bottom left corner;
        goTo(18,18);

        // Turn to centre
        turnLeft(180);

        while (true) {
            //ahead(100); // Move ahead 100
            //turnGunRight(360); // Spin gun around
            //back(100); // Move back 100
            //turnGunRight(360); // Spin gun around
        }
    }

    public void goTo(double toX, double toY){
        double fromX = getX();
        double fromY = getY();

        // Pythagoras theorem to calculate the complementary angel
        double complementaryAngle = pythagorasTheorem(fromX,fromY,toX,toY);

        double angleToTurn = 180-complementaryAngle;

        // Turn face to our desired position. getHeading because the robot doesn't start at exactly 0 degrees (north)
        turnLeft(normalRelativeAngleDegrees(angleToTurn + getHeading()));

        // Move on
        ahead(5000);
    }

    public double distanceBetween2Points(double p1x, double p1y, double p2x, double p2y){
        double x = Math.sqrt(((Math.pow((p1x - p2x), 2)) + (Math.pow((p1y - p2y), 2))));
        return x;
    }

    public double pythagorasTheorem(double fromX, double fromY, double toX, double toY){
        // Pythagoras theorem
        double hipotenusa = distanceBetween2Points(fromX,fromY,toX,toY);
        double adjacente = distanceBetween2Points(fromX,fromY,fromX,toY);
        double cosB = adjacente/hipotenusa;

        // Inverse of Cos
        double acosB = Math.acos(cosB);

        // Transform to Degrees
        double acosBDegrees = (180/Math.PI)*acosB;

        return acosBDegrees;
    }

}

