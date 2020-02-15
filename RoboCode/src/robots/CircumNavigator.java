package robots;

import robocode.*;
import standardOdometer.Odometer;
import utils.CustomOdometer;
import utils.Position;

import static robocode.util.Utils.normalRelativeAngleDegrees;
import static utils.Math.distanceBetween2Points;
import static utils.Math.pythagorasTheorem;

public class CircumNavigator extends AdvancedRobot {
    private boolean raceCompleted = false;
    private boolean raceOngoing = false;
    private boolean robotScanned = false;

    private Odometer odometer = new Odometer("IsRacing", this);
    private CustomOdometer myOdometer;

    public void run() {
        addCustomEvent(odometer);
        myOdometer = new CustomOdometer("customOdometer",this);

        // Go to bottom left corner
        goTo(18,18);

        // Turn to top
        turnRight(360-getRadarHeading());

        raceOngoing = true;
        // Start scanning robots
        turnRight(45);
        int i,j;
        j = 0;

        while(j<3) {
            i = 0;
            while(i<10 && !robotScanned) {
                ahead(10);
                turnRight(10);
                i++;
            }
            j++;
        }

        goTo(18,18);
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
            raceOngoing = false;
            this.odometer.getRaceDistance();
        }
        else if (cd.getName().equals("customOdometer")){
            System.out.println("Total " + myOdometer.getTotal());
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if(raceOngoing){
            robotScanned = true;
            turnLeft(10);
            ahead(e.getDistance()+10);
            robotScanned = false;
        }
    }

    public void onStatus(StatusEvent event){
        if(event == null || event.getStatus() == null){
            System.out.println("Null Event or Status");
            return ;
        }
        RobotStatus rs = event.getStatus();
        Position current = new Position( rs.getX(), rs.getY());
        if(myOdometer != null){
            myOdometer.registerPosition(current);
        }
    }

}

