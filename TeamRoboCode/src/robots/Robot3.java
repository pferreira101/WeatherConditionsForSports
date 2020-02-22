package robots;

import robocode.*;
import utils.Math;
import utils.Message;
import utils.Position;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Robot3 extends TeamRobot{
    Position positionToFire = null;
    double theta = 0;
    boolean onMission = false;

    public void run() {

        while(true) {
            if(!onMission) {
                // Search for enemies
                turnLeft(360);
            }
        }

    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if(!onMission){
            fire(4);
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {
        if(!onMission) {
            ahead(200);
        }
    }

    public void onMessageReceived(MessageEvent event) {
        Message message = (Message) event.getMessage();
        switch (message.getTipo()) {
            case 0:
                System.out.println("Sou o Robot " + message.getReceiver() + " e recebi do " + message.getSender() + " -> " + message.getContent());
                break;
            case 1:
                System.out.println("Recebi um turnTo to " + message.getPosition().getX() + " " + message.getPosition().getY());

                positionToFire = message.getPosition();

                // Turn to position received by message
                turnTo(positionToFire);

                // Defense movement & Fire
                movementWhenFiring(positionToFire);

                break;
        }
    }

    public void movementWhenFiring(Position position){
        ahead(100);

        // Calculate angle to target
        double theta = java.lang.Math.toDegrees(java.lang.Math.atan2(position.getX() - this.getX(), position.getY() - this.getY()));

        // Fire to target
        turnGunRight(normalRelativeAngleDegrees(theta - getHeading()+90));
        fire(4);
        turnGunLeft(normalRelativeAngleDegrees(theta - getHeading()+90));

        back(200);

        // Fire to target
        turnGunLeft(normalRelativeAngleDegrees(theta - getHeading()+90));
        fire(4);
        turnGunRight(normalRelativeAngleDegrees(theta - getHeading()+90));

        // Fire to target
        ahead(100);
        fire(4);

        // Return to the original position at the end of the attack
        turnLeft(normalRelativeAngleDegrees(theta - getHeading())+90);
        turnGunLeft(normalRelativeAngleDegrees(theta - getHeading()));
    }

    public void turnTo(Position position){
        double dx = position.getX() - this.getX();
        double dy = position.getY() - this.getY();

        // Calculate angle to target
        theta = java.lang.Math.toDegrees(java.lang.Math.atan2(dx, dy));

        // Turn perpendicularly
        turnRight(normalRelativeAngleDegrees(theta - getHeading())+90);

        // Turn gun to target
        turnGunRight(normalRelativeAngleDegrees(theta - getHeading()));
    }

    public void goTo(Position position){
        double fromX = getX();
        double fromY = getY();

        turnTo(position);

        double distance =  Math.distanceBetween2Points(fromX, fromY, position.getX(), position.getY());

        // Move on
        ahead(distance);
    }
}
