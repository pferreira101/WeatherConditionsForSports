package robots;

import robocode.*;
import utils.Enemy;
import utils.Message;
import utils.Position;

import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;


import static robocode.util.Utils.normalRelativeAngleDegrees;

public class TesteRobot extends TeamRobot{
    private Enemy enemy = new Enemy();

    Map<String,Position> teamPositions = new HashMap<>();
    Map<String,Enemy> enemies = new HashMap<>();

    public void run() {
        sendPositionToTeammates();
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);
        turnRadarRightRadians(Double.POSITIVE_INFINITY);
        enemy.reset();

    }

    public void onMessageReceived(MessageEvent event) {
        Message message = (Message) event.getMessage();
        switch (message.getTipo()) {
            case 0:
                teamPositions.put(message.getSender(),message.getPosition());
                break;
        }
    }

    public void sendPositionToTeammates(){

        Message msg = new Message(getName(),Message.INFO,new Position(this.getX(), this.getY()));

        try {
            broadcastMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if (!isTeammate(e.getName())){
            Enemy enemy = new Enemy();
            Position position = detectPosition(e);
            enemy.update(e,position);
            enemies.put(e.getName(),enemy);
        }

        /*if (enemy.none() || e.getName().equals(enemy.getName())) {
            enemy.update(e);
            attack(enemy);
        }*/
    }

    public Enemy positionToAttack(){
        double totalDistance = 0;
        double minTotalDistance = 10000;
        Enemy selected = null;
        for (Enemy enemy : enemies.values()) {
            for(Position team : teamPositions.values()) {
                totalDistance += utils.Math.distanceBetween2Points(enemy.getPosition().getX(),enemy.getPosition().getY(),team.getX(),team.getY());
            }
            if (totalDistance < minTotalDistance) {
                minTotalDistance = totalDistance;
                selected = enemy;
            }
        }
        return selected;
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
