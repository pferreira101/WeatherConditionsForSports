package robots;

import robocode.*;
import utils.Math;
import utils.Message;
import utils.Position;

import java.io.IOException;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Robot1 extends TeamRobot{
    boolean onMission = false;

    String[] teammates;

    public void run() {

        /*teammates = getTeammates();

        // Check teammates
        if(teammates != null) {
            for (int i = 0; i < teammates.length; i++)
                System.out.println(teammates[i]);

            // Create and send message
            Message message = new Message(getName(), teammates[0], "Então mpt " + teammates[0] + ", daqui fala o " + getName() + "!",0);
            System.out.println("Vou enviar mensagem para o " + teammates[0]);
            System.out.println("Conteúdo: " + message.getContent());

            try {
                sendMessage(teammates[0], message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/


        while(true) {
            if(!onMission) {
                // Search for enemies
                turnLeft(360);
            }
            else {
                // Attack mode
                fire(4);
                onMission = false;
            }
        }
    }


    public void onScannedRobot(ScannedRobotEvent e) {
        if (!isTeammate(e.getName())) {
            onMission = true;

            // Stop to fire
            stop();

            // Detect enemy position
            Position enemy = detectPosition(e);

            // Construct message
            Message fireEnemy = new Message();
            fireEnemy.setTipo(1);
            fireEnemy.setPosition(enemy);

            try {
                // Send enemy position to teammates
                broadcastMessage(fireEnemy);
            } catch (IOException ex) {
                ex.printStackTrace(out);
            }
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {
        ahead(200);
    }

    public Position detectPosition(ScannedRobotEvent e) {

        System.out.println("--- Scanned Robot: ---");
        System.out.println("Name " + e.getName());

        // Calculate enemy bearing
        double enemyBearing = this.getHeading() + e.getBearing();

        // Calculate enemy's position
        double enemyX = getX() + e.getDistance() * java.lang.Math.sin(java.lang.Math.toRadians(enemyBearing));
        double enemyY = getY() + e.getDistance() * java.lang.Math.cos(java.lang.Math.toRadians(enemyBearing));

        System.out.println("Enemy X: " + enemyX);
        System.out.println("Enemy Y: " + enemyY);

        return new Position(enemyX,enemyY);
    }

    public void turnTo(Position position){
        double dx = position.getX() - this.getX();
        double dy = position.getY() - this.getY();

        // Calculate angle to target
        double theta = java.lang.Math.toDegrees(java.lang.Math.atan2(dx, dy));

        // Turn to target
        turnRight(normalRelativeAngleDegrees(theta - getHeading()));
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
