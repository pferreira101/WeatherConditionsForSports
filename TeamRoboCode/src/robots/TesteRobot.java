package robots;

import robocode.*;
import utils.Enemy;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class TesteRobot extends TeamRobot{
    private Enemy enemy = new Enemy();

    public void run() {
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);
        turnRadarRightRadians(Double.POSITIVE_INFINITY);
        enemy.reset();

    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if (enemy.none() || e.getName().equals(enemy.getName())) {
            enemy.update(e);
            attack(enemy);
        }
    }

    void attack(Enemy target){
        double gunTurnAmt = normalRelativeAngleDegrees(target.getBearing() + getHeading() - getGunHeading());

        if (target.getDistance() > 150) {

            turnGunRight(gunTurnAmt);
            turnRight(target.getBearing());

            ahead(target.getDistance() - 140);
            return;
        }

        // Our target is close.
        setTurnGunRight(gunTurnAmt);
        fire(3);

        // Our target is too close!  Back up.
        if (target.getDistance() < 100) {
            if (target.getBearing() > -90 && target.getBearing() <= 90) {
                back(40);
            } else {
                ahead(40);
            }
        }

        scan();
    }

    public void onRobotDeath(RobotDeathEvent e) {
        if (e.getName().equals(enemy.getName())) {
            enemy.reset();
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {
        ahead(200);
    }


}
