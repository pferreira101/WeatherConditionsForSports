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
    private Enemy enemy;

    Map<String,Position> teamPositions = new HashMap<>();
    Map<String,Enemy> enemies = new HashMap<>();

    int aliveEnemies = 4;
    int enemiesToScan = aliveEnemies;
    int scannedEnemies = 0;
    int aliveTeammates = 1;
    int msgsReceived = 0;
    boolean fighting = false;


    public void run() {
        sendPositionToTeammates();
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);
        turnRadarRightRadians(Double.POSITIVE_INFINITY);
    }

    public void onMessageReceived(MessageEvent event) {
        Message message = (Message) event.getMessage();

        switch (message.getTipo()) {
            case Message.INFO:
                msgsReceived++;
                teamPositions.put(message.getSender(),message.getPosition());
                break;
        }
    }

    public void onHitRobot(HitRobotEvent event) {
        if(isTeammate(event.getName())) {
            if (event.getBearing() > -90 && event.getBearing() <= 90) {
                back(100);
            } else {
                ahead(100);
            }
        }
        else {
            back(50);
            turnLeft(30);
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
        Position position = detectPosition(e);

        System.out.println(enemiesToScan);

        if(!fighting) {

            if (!isTeammate(e.getName())) {
                Enemy enemy = new Enemy();
                enemy.update(e, position);
                enemies.put(e.getName(), enemy);
                scannedEnemies++;
            }

            //System.out.println("Inimigo detetado. "+ this.scannedEnemies + " ||  " + msgsReceived);

            if(scannedEnemies >= enemiesToScan) {
                fighting = true;
                enemy = selectTarget();
                System.out.println("Inimigo escolhido " + enemy.toString());
                attack(enemy);
            }
        }
        else {
            if(e.getName().equals(enemy.getName())){
                enemy.update(e, position);
                attack(enemy);
            }
        }


    }

    public Enemy selectTarget(){
        double totalDistance = 0;
        double minTotalDistance = 10000;
        Enemy selected = null;
        teamPositions.put(getName(),new Position(getX(),getY()));
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
        // Calculate enemy bearing
        double enemyBearing = this.getHeading() + e.getBearing();

        // Calculate enemy's position
        double enemyX = getX() + e.getDistance() * java.lang.Math.sin(java.lang.Math.toRadians(enemyBearing));
        double enemyY = getY() + e.getDistance() * java.lang.Math.cos(java.lang.Math.toRadians(enemyBearing));

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

        // if the gun is cool and we're pointed at the target, shoot!
        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 30)
            setFire(Math.min(400 / target.getDistance(), 3));

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
            enemies.remove(enemy.getName());
            enemy.reset();
            fighting = false;
            enemiesToScan= --aliveEnemies;
            msgsReceived = 0;
            scannedEnemies = 0;
        }
        if(isTeammate(e.getName())){
            System.out.println("Aliado morreu " + e.getName());
            aliveTeammates--;
            this.teamPositions.remove(e.getName());
            enemy.reset();
            fighting = false;
            msgsReceived = 0;
            scannedEnemies = 0;
        }
    }


}
