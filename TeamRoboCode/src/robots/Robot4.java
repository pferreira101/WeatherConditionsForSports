package robots;

import robocode.*;
import utils.Math;
import utils.Message;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Robot4 extends TeamRobot{
    public void run() {
        // Robot main loop
        while(true) {
            // Replace the next 4 lines with any behavior you would like
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
        }
    }

    public void onMessageReceived(MessageEvent event) {
        Message message = (Message) event.getMessage();
        System.out.println("Sou o Robot " + message.getReceiver() + " e recebi do " + message.getSender() + " -> " + message.getContent());
    }


    public void onScannedRobot(ScannedRobotEvent e) {
        // Replace the next line with any behavior you would like
        fire(1);
    }

    public void onHitByBullet(HitByBulletEvent e) {
        // Replace the next line with any behavior you would like
        back(10);
    }

    public void onHitWall(HitWallEvent e) {
        // Replace the next line with any behavior you would like
        back(20);
    }

    public void goTo(double toX, double toY){
        double fromX = getX();
        double fromY = getY();
        double distance =  Math.distanceBetween2Points(fromX, fromY, toX, toY);

        // Pythagoras theorem to calculate the complementary angel
        double complementaryAngle = Math.pythagorasTheorem(fromX, fromY, toX, toY);

        double angleToTurn = 180-complementaryAngle;

        // Turn face to our desired position. getHeading because the robot doesn't start at exactly 0 degrees (north)
        turnLeft(normalRelativeAngleDegrees(angleToTurn + getHeading()));

        // Move on
        ahead(distance);
    }
}


