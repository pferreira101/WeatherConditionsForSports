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

    private Odometer odometer = new Odometer("IsRacing", this);
    private CustomOdometer myOdometer;

    public void run() {
        addCustomEvent(odometer);
        myOdometer = new CustomOdometer("customOdometer",this);

        // Go to bottom left corner
        goTo(18,18);

        // Turn to top
        turnRight(360-getRadarHeading());

        // Waiting for Rockquads
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        raceOngoing = true;

        // Start scanning robots
        turnRight(90);

        /**while (true) {
            circumNavigate();
        }*/
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
            // Stop scanning robots
            stop();

            // Go to the 1st Rockquad
            ahead(e.getDistance()-50);

            goAroundRobot();
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

    public void goAroundRobot(){
        turnLeft(90);
        for(int i=0;i<15;i++){
            ahead(10);
            turnRight(10);
        }
    }


}

