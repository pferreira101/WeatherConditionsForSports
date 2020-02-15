package robots;

import robocode.*;
import standardOdometer.Odometer;
import static robocode.util.Utils.normalRelativeAngleDegrees;
import static robocode.util.Utils.normalRelativeAngle;
import static utils.Math.pythagorasTheorem;
import static utils.Math.distanceBetween2Points;

public class CircumNavigator extends AdvancedRobot {
    private boolean raceCompleted = false;
    private boolean raceOngoing;

    private Odometer odometer = new Odometer("IsRacing", this);

    public void run() {
        addCustomEvent(odometer);

        //Go to bottom left corner;
        goTo(18,18);

        // Turn to centre
        turnLeft(getHeading() % 90);

        raceOngoing = true;

        while (true) {
            circumNavigate();
        }
    }

    void circumNavigate(){
        if(!raceCompleted){
            ahead(5000);
            turnRight(90);
        }
    }

    void goTo(double toX, double toY){
        double fromX = getX();
        double fromY = getY();
        double distance =  distanceBetween2Points(fromX, fromY, toX, toY);
        // Pythagoras theorem to calculate the complementary angel
        double complementaryAngle = pythagorasTheorem(fromX, fromY, toX, toY);

        double angleToTurn = 180-complementaryAngle;

        // Turn face to our desired position. getHeading because the robot doesn't start at exactly 0 degrees (north)
        turnLeft(normalRelativeAngleDegrees(angleToTurn + getHeading()));

        // Move on
        ahead(distance);
    }

    public void onCustomEvent(CustomEvent ev) {
        Condition cd = ev.getCondition();
        if (cd.getName().equals("IsRacing")) {
            raceCompleted = true;
            this.odometer.getRaceDistance();
        }
    }

}

