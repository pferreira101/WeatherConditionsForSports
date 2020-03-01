package robots;

import robocode.*;
import utils.Enemy;
import utils.Message;
import utils.Position;

import static robocode.Rules.MAX_BULLET_POWER;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class HelpRobot extends TeamRobot{
    Enemy enemy = new Enemy();
    int moveDirection = 1;
    boolean helpMode = false;
    boolean teamleader = false;

    public void run() {
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);
        turnRadarRightRadians(Double.POSITIVE_INFINITY);
    }

    public void onMessageReceived(MessageEvent event) {
        event.setPriority(99);
        Message message = (Message) event.getMessage();

        if(!helpMode) {
            switch (message.getTipo()) {
                case Message.HELP:
                    helpMode = true;
                    enemy = message.getTarget();
                    charge(enemy);
                    break;

                case Message.CHANGELEADER:
                    teamleader = true;
                    break;
            }
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {

        if(!isTeammate(e.getName())) {
            Position position = detectPosition(e);
            if (!helpMode) {
                enemy.update(e, position);
                attack();
            }
            else {
                if (e.getName().equals(enemy.getName())) {
                    enemy.update(e,position);
                    charge(enemy);
                }
            }
        }
    }

    public void attack() {

        // switch directions if we've stopped
        if (getVelocity() == 0)
            moveDirection *= -1;

        // always square off against our enemy
        setTurnRight(normalizeBearing(enemy.getBearing() + 90 - (15 * moveDirection)));

        // strafe by changing direction every 5 ticks
        if (getTime() % 5 == 0) {
            moveDirection *= -1;
            setAhead(4000 * moveDirection);
        }

        double gunTurnAmt = normalRelativeAngleDegrees(enemy.getBearing() + getHeading() - getGunHeading());

        setTurnGunRight(gunTurnAmt);

        // if the gun is cool and we're pointed at the target, shoot!
        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 30)
            setFire(Math.min(400 / enemy.getDistance(), 3));

    }

    public void charge(Enemy target){
        double gunTurnAmt = normalRelativeAngleDegrees(target.getBearing() + getHeading() - getGunHeading());

        //Charge at the target
        turnGunRight(gunTurnAmt);
        turnRight(target.getBearing());
        ahead(target.getDistance());

        //We are close to the target
        setTurnGunRight(gunTurnAmt);

        // if the gun is cool and we're pointed at the target, shoot!
        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 30)
            setFire(MAX_BULLET_POWER);

        scan();
    }

    double normalizeBearing(double angle) {
        while (angle >  180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

    public Position detectPosition(ScannedRobotEvent e) {
        // Calculate enemy bearing
        double enemyBearing = this.getHeading() + e.getBearing();

        // Calculate enemy's position
        double enemyX = getX() + e.getDistance() * java.lang.Math.sin(java.lang.Math.toRadians(enemyBearing));
        double enemyY = getY() + e.getDistance() * java.lang.Math.cos(java.lang.Math.toRadians(enemyBearing));

        return new Position(enemyX,enemyY);
    }

    public void onRobotDeath(RobotDeathEvent e) {

        if (e.getName().equals(enemy.getName())) {
            enemy.reset();
            helpMode = false;
        }

    }

}
