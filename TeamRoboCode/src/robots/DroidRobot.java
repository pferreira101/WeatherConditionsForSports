package robots;

import robocode.*;
import utils.Enemy;
import utils.Message;
import utils.Position;
import utils.Math;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import static robocode.util.Utils.normalRelativeAngleDegrees;
import static utils.Math.distanceBetween2Points;

public class DroidRobot extends TeamRobot implements Droid {
    Enemy target;
    boolean fighting = false;

    public void run() {
        sendPositionToTeammates();
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);
        turnRadarRightRadians(Double.POSITIVE_INFINITY);
    }


    // ########################## Eventos ##########################

    public void onMessageReceived(MessageEvent event) {
        Message message = (Message) event.getMessage();
        switch (message.getTipo()) {
            case Message.ATTACK:
                System.out.println("Recebi mensagem para atacar");
                target = message.getTarget();
                attack(target);
                break;
            case Message.REQUEST_INFO:
                sendPositionToTeammates();
                break;
        }
    }

    public void onHitRobot(HitRobotEvent e) {
        if (isTeammate(e.getName())) {
            if (e.getBearing() > -90 && e.getBearing() <= 90) {
                back(100);
            } else {
                ahead(100);
            }
        }
        else{
            turnRight(e.getBearing());
            fire(3);
            ahead(40);
        }
    }

    public void onRobotDeath(RobotDeathEvent e) {
        if (e.getName().equals(target.getName())) {
            target.reset();
            fighting = false;

        }
        if (isTeammate(e.getName())) {
            System.out.println("Aliado morreu " + e.getName());
        }
    }

    // ########################## Ações ##########################


    void attack(Enemy target) {

        double distance = distanceBetween2Points(target.getPosition().getX(), target.getPosition().getY(), this.getX(), this.getY());

        double dx = target.getPosition().getX() - this.getX();
        double dy = target.getPosition().getY() - this.getY();

        double theta = java.lang.Math.toDegrees(java.lang.Math.atan2(dx, dy));

        double gunTurnAmt = normalRelativeAngleDegrees(theta - getGunHeading());
        double turnAmt = normalRelativeAngleDegrees(theta - getHeading());

        if (distance > 150) {

            turnGunRight(gunTurnAmt);
            turnRight(turnAmt);

            ahead(distance - 140);
            return;
        }

        // Our target is close.
        turnGunRight(gunTurnAmt);

        // if the gun is cool and we're pointed at the target, shoot!
        if (getGunHeat() == 0 && java.lang.Math.abs(getGunTurnRemaining()) < 30)
            fire(java.lang.Math.min(400 / distance, 3));

    }


    // ########################## Comunicação ##########################

    public void sendPositionToTeammates() {

        Message msg = new Message(getName(), Message.INFO, new Position(this.getX(), this.getY()));
        System.out.println("Enviei mensagem");
        try {
            broadcastMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
