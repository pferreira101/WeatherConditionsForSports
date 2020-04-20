package robots;

import robocode.*;
import utils.Enemy;
import utils.Position;

import static robocode.Rules.MAX_BULLET_POWER;
import static robocode.util.Utils.normalRelativeAngleDegrees;



public class RunawayRobot extends AdvancedRobot {
    boolean movingForward;
    Enemy enemy = new Enemy();

    public void run() {
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);
        turnRadarRightRadians(Double.POSITIVE_INFINITY);
    }

    public void onHitWall(HitWallEvent e) {
        reverseDirection();
    }

    public void runaway(){
        setBack(300);
        movingForward = true;

        setTurnRight(90);

        waitFor(new TurnCompleteCondition(this));
    }

    public void goToEnemy(double distance){
        setAhead(distance/3);
        movingForward = true;

        setTurnRight(90);
        setAhead(distance/3);

        setTurnLeft(90);
        setAhead(distance/3);
    }

    public void onHitByBullet(HitByBulletEvent event){
        runaway();
    }


    public void reverseDirection() {
        if (movingForward) {
            setBack(200);
            movingForward = false;
        } else {
            setAhead(200);
            movingForward = true;
        }
    }


    public void onScannedRobot(ScannedRobotEvent e) {
        Position position = detectPosition(e);
        enemy.update(e, position);
        attack();
    }

    void attack(){
        double gunTurnAmt = normalRelativeAngleDegrees(enemy.getBearing() + getHeading() - getGunHeading());

        turnGunRight(gunTurnAmt);
        turnRight(normalizeBearing(enemy.getBearing()));

        goToEnemy(enemy.getDistance()-100);

        setTurnGunRight(gunTurnAmt);

        // if the gun is cool and we're pointed at the enemy, shoot!
        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 30)
            fire(MAX_BULLET_POWER);

    }

    double normalizeBearing(double angle) {
        while (angle >  180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }


    public void onHitRobot(HitRobotEvent e) {
        if (e.isMyFault()) {
            reverseDirection();
        }
    }

    public Position detectPosition(ScannedRobotEvent e) {
        // Calculate enemy bearing
        double enemyBearing = this.getHeading() + e.getBearing();

        // Calculate enemy's position
        double enemyX = getX() + e.getDistance() * java.lang.Math.sin(java.lang.Math.toRadians(enemyBearing));
        double enemyY = getY() + e.getDistance() * java.lang.Math.cos(java.lang.Math.toRadians(enemyBearing));

        return new Position(enemyX,enemyY);
    }
}
