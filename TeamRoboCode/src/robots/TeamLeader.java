package robots;

import robocode.*;
import utils.Enemy;
import utils.Message;
import utils.Position;

import javax.sound.midi.SysexMessage;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


import static robocode.Rules.MAX_BULLET_POWER;
import static robocode.util.Utils.normalRelativeAngleDegrees;

public class TeamLeader extends TeamRobot {
    private Enemy target;
    private Enemy attacker;

    Map<String, Position> teamPositions = new HashMap<>();
    Map<String, Enemy> enemies = new HashMap<>();

    int moveDirection = 1;
    int aliveDroids = 2;
    int msgsReceived = 0;
    int aliveEnemies = 4;
    int enemiesToScan = aliveEnemies;
    int scannedEnemies = 0;
    boolean fighting = false;


    public void run() {
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);
        turnRadarRightRadians(Double.POSITIVE_INFINITY);
    }


    // ########################## Eventos ##########################


    public void onScannedRobot(ScannedRobotEvent e) {
        Position position = detectPosition(e);

        if (!fighting) {

            if (!isTeammate(e.getName())) {
                Enemy enemy = new Enemy(e, position);
                enemies.put(e.getName(), enemy);
                scannedEnemies++;
            }

            if (scannedEnemies >= enemiesToScan && msgsReceived >= aliveDroids) {
                fighting = true;
                target = selectTarget();
                orderAttack(target);
                attack(target);
            }
        } else {
            if (e.getName().equals(target.getName())) {
                target.update(e, position);
                orderAttack(target);
                attack(target);
            }
        }




    }

    public void onMessageReceived(MessageEvent event) {
        Message message = (Message) event.getMessage();

        switch (message.getTipo()) {
            case Message.INFO:
                msgsReceived++;
                teamPositions.put(message.getSender(), message.getPosition());
                break;
        }
    }

    public void onHitRobot(HitRobotEvent event) {
        if (isTeammate(event.getName())) {
            if (event.getBearing() > -90 && event.getBearing() <= 90) {
                back(100);
                turnRight(50);
                ahead(100);
            } else {
                ahead(100);
            }
        } else {
            back(50);
            turnLeft(30);
        }
    }

    public void onRobotDeath(RobotDeathEvent e) {
        if(!isTeammate(e.getName()))
            enemies.remove(e.getName());

        if (e.getName().equals(target.getName())) {
            target.reset();
            fighting = false;
            enemiesToScan = --aliveEnemies;
            msgsReceived = 0;
            scannedEnemies = 0;
            requestInfo();
        }
        if (isTeammate(e.getName()) && e.getName().contains("Droid")) {
            aliveDroids--;
            this.teamPositions.remove(e.getName());
        }
    }

    public void onHitByBullet(HitByBulletEvent e){
        if(!isTeammate(e.getName())) {
            Enemy enemy = new Enemy();
            enemy.setName(e.getName());
            attacker = enemy;
            requestHelp(attacker);
        }
    }



    // ########################## Ações ##########################


    void attack(Enemy target) {

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

        double gunTurnAmt = normalRelativeAngleDegrees(target.getBearing() + getHeading() - getGunHeading());

        // Our target is close.
        setTurnGunRight(gunTurnAmt);

        // if the gun is cool and we're pointed at the target, shoot!
        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 30)
            setFire(MAX_BULLET_POWER);

        scan();
    }

    public Enemy selectTarget() {
        double totalDistance = 0;
        double minTotalDistance = 10000;
        Enemy selected = null;
        teamPositions.put(getName(), new Position(getX(), getY()));
        for (Enemy enemy : enemies.values()) {
            for (Position teammate : teamPositions.values()) {
                double distance = utils.Math.distanceBetween2Points(enemy.getPosition().getX(), enemy.getPosition().getY(), teammate.getX(), teammate.getY());
                totalDistance += distance;
            }
            if (totalDistance < minTotalDistance) {
                minTotalDistance = totalDistance;
                selected = enemy;
            }
        }

        return selected;
    }

    public Position detectPosition(ScannedRobotEvent e) {
        // Calculate enemy bearing
        double enemyBearing = this.getHeading() + e.getBearing();

        // Calculate enemy's position
        double enemyX = getX() + e.getDistance() * java.lang.Math.sin(java.lang.Math.toRadians(enemyBearing));
        double enemyY = getY() + e.getDistance() * java.lang.Math.cos(java.lang.Math.toRadians(enemyBearing));

        return new Position(enemyX, enemyY);
    }


    // ########################## Comunicação ##########################

    public void orderAttack(Enemy enemy) {

        Message msg = new Message(Message.ATTACK, enemy);

        try {
            broadcastMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void requestInfo() {
        Message msg = new Message(Message.REQUEST_INFO);

        try {
            broadcastMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void requestHelp(Enemy attacker) {
        Message msg = new Message(Message.HELP,attacker);

        try{
            broadcastMessage(msg);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
