package robots;

import robocode.*;
import utils.Math;
import utils.Message;
import utils.Position;

import java.io.IOException;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Robot1 extends TeamRobot{

    public void run() {
        // Check teammate
        String[] teammates = getTeammates();

        if(teammates != null) {
            for (int i = 0; i < teammates.length; i++)
                System.out.println(teammates[i]);

            // Create and send message
            Message message = new Message(getName(), teammates[0], "Então mpt " + teammates[0] + ", daqui fala o " + getName() + "!");
            //System.out.println("Vou enviar mensagem para o " + teammates[0]);
            //System.out.println("Conteúdo: " + message.getContent());

            try {
                sendMessage(teammates[0], message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Robot main loop
        while(true) {
            // Replace the next 4 lines with any behavior you would like
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
        }
    }


    public void onScannedRobot(ScannedRobotEvent e) {
        // Replace the next line with any behavior you would like
        detectPosition(e);
        fire(1);
    }

    public Position detectPosition(ScannedRobotEvent e) {

        System.out.println("-----");
        System.out.println("X: " + getX());
        System.out.println("Y: " + getY());
        System.out.println("Radar Heading Degrees: " + (getRadarHeading()));
        System.out.println("Radar Heading Radians: " + (getRadarHeadingRadians()));

        System.out.println("--- Scanned Robot: ---");
        System.out.println("Name " + e.getName());
        System.out.println("Distance " + e.getDistance());

        // "90º - x" because the "referencial cartesiano" is 90 degrees different
        double oppositeX = getX() + e.getDistance()*java.lang.Math.cos(java.lang.Math.toRadians(90) - getRadarHeadingRadians());
        double oppositeY = getY() + e.getDistance()*java.lang.Math.sin(java.lang.Math.toRadians(90) - getRadarHeadingRadians());

        System.out.println("Opposite X: " + oppositeX);
        System.out.println("Opposite Y: " + oppositeY);

        return new Position(oppositeX,oppositeY);
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
