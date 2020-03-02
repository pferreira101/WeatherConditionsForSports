package robots;

import robocode.*;
import utils.Enemy;
import utils.Message;
import utils.Position;
import utils.Math;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import static robocode.Rules.MAX_BULLET_POWER;
import static robocode.util.Utils.normalRelativeAngleDegrees;
import static utils.Math.distanceBetween2Points;

public class DroidRobot extends TeamRobot implements Droid {
    Enemy target;
    boolean fighting = false;
    boolean teamLeaderDead = false;
    boolean helperDead = false;
    boolean peek = false;
    boolean wallMode = false;
    int moveDirection = 1;
    double moveAmount = 0;
    static int corner = 0; // Which corner we are currently using

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
                ahead(100);
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

        if(e.getName().equals("robots.TeamLeader*")){
            teamLeaderDead = true;
            if(helperDead) {
                wallMode = true;
                goToWall();
            }
        }

        if(e.getName().equals("robots.HelperRobot*")){
            helperDead = true;
            if(teamLeaderDead) {
                wallMode = true;
                goToWall();
            }
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

        // switch directions if we've stopped
        if (getVelocity() == 0)
            moveDirection *= -1;

        // always square off against our enemy
        setTurnRight(utils.Math.normalizeBearing(target.getBearing() + 90 - (15 * moveDirection)));

        // strafe by changing direction every 5 ticks
        if (getTime() % 5 == 0) {
            moveDirection *= -1;
            setAhead(4000 * moveDirection);
        }

        // Our target is close.
        turnGunRight(gunTurnAmt);

        // if the gun is cool and we're pointed at the target, shoot!
        if (getGunHeat() == 0 && java.lang.Math.abs(getGunTurnRemaining()) < 30)
            fire(MAX_BULLET_POWER);

    }

    public void goToWall() {
        moveAmount = java.lang.Math.max(getBattleFieldWidth(), getBattleFieldHeight());
        turnLeft(getHeading() % 90);
        ahead(moveAmount);
        // Turn the gun to turn right 90 degrees.
        peek = true;
        turnGunRight(90);
        turnRight(90);

        while (true) {
            if (wallMode) {
                ahead(moveAmount);
                turnRight(90);
            }
        }
    }


    // ########################## Comunicação ##########################

    public void sendPositionToTeammates() {

        Message msg = new Message(getName(), Message.INFO, new Position(this.getX(), this.getY()));
        try {
            broadcastMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
