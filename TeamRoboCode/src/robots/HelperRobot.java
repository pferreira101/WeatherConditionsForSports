package robots;

import robocode.*;
import utils.Enemy;
import utils.Message;
import utils.Position;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static robocode.Rules.MAX_BULLET_POWER;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class HelperRobot extends TeamRobot {

    Enemy target = new Enemy();
    Map<String, Enemy> enemies = new HashMap<>();
    int aliveEnemies = 4;

    int moveDirection = 1;

    boolean helpMode = false;
    boolean teamleader = false;
    boolean fighting = false;

    public void run() {
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);
        turnRadarRightRadians(Double.POSITIVE_INFINITY);
    }

    // ########################## Eventos ##########################

    public void onScannedRobot(ScannedRobotEvent e) {

        if (!isTeammate(e.getName())) {
            Position position = detectPosition(e);
            Enemy enemy = new Enemy(e, position);
            enemies.put(e.getName(), enemy);

            if(!helpMode && !fighting && enemies.values().size() == aliveEnemies){
                target = selectTarget();
                System.out.println("Escolhi primeiro inimigo");
                fighting = true;
            }

            if (!helpMode && e.getName().equals(target.getName())) {
                target.update(e, position);
                attack();
                if(teamleader)
                    orderAttack(target);
            } else {
                if (e.getName().equals(target.getName())) {
                    target.update(e, position);
                    charge();
                }
            }
        }
    }

    public void onMessageReceived(MessageEvent event) {
        Message message = (Message) event.getMessage();

        switch (message.getTipo()) {
            case Message.HELP:
                helpMode = true;
                target = message.getTarget();
                charge();
                break;

            case Message.CHANGELEADER:
                teamleader = true;
                target = selectTarget();
                orderAttack(target);
                break;
        }
    }


    public void onRobotDeath(RobotDeathEvent e) {
        if(!isTeammate(e.getName())) {
            enemies.remove(target.getName());
            aliveEnemies--;
        }

        if (e.getName().equals(target.getName())) {
            target.reset();
            helpMode = false;
            if(teamleader){
                target = selectTarget();
                orderAttack(target);
            }
        }

    }

    // ########################## Ações ##########################


    public void attack() {

        // switch directions if we've stopped
        if (getVelocity() == 0)
            moveDirection *= -1;

        // always square off against our enemy
        setTurnRight(normalizeBearing(target.getBearing() + 90 - (15 * moveDirection)));

        // strafe by changing direction every 5 ticks
        if (getTime() % 5 == 0) {
            moveDirection *= -1;
            setAhead(4000 * moveDirection);
        }

        double gunTurnAmt = normalRelativeAngleDegrees(target.getBearing() + getHeading() - getGunHeading());

        setTurnGunRight(gunTurnAmt);

        // if the gun is cool and we're pointed at the target, shoot!
        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 30)
            setFire(Math.min(400 / target.getDistance(), 3));

    }

    public void charge() {
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

    private Enemy selectTarget() {
        Enemy target = null;
        double minDist = 10000;
        for (Enemy e : this.enemies.values()) {
            if (e.getDistance() < minDist) {
                target = e;
                minDist = e.getDistance();
            }
        }
        return target;
    }

    public Position detectPosition(ScannedRobotEvent e) {
        // Calculate enemy bearing
        double enemyBearing = this.getHeading() + e.getBearing();

        // Calculate enemy's position
        double enemyX = getX() + e.getDistance() * java.lang.Math.sin(java.lang.Math.toRadians(enemyBearing));
        double enemyY = getY() + e.getDistance() * java.lang.Math.cos(java.lang.Math.toRadians(enemyBearing));

        return new Position(enemyX, enemyY);
    }


    double normalizeBearing(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

    // ########################## Comunicação ##########################


    public void orderAttack(Enemy enemy) {

        System.out.println("A mandar atacar");

        Message msg = new Message(Message.ATTACK, enemy);

        try {
            broadcastMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
