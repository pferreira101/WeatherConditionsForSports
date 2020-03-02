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

    Enemy target;
    Map<String, Enemy> enemies = new HashMap<>();

    int moveDirection = 1;
    boolean helpMode = false;
    boolean teamleader = false;
    boolean fighting = false;
    boolean choosenTarget = false;
    int aliveEnemies = 4;
    int enemiesToScan = aliveEnemies;
    int scannedEnemies = 0;

    public void run() {
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);
        turnRadarRightRadians(Double.POSITIVE_INFINITY);
    }

    // ########################## Eventos ##########################

    public void onScannedRobot(ScannedRobotEvent e) {

        Position position = detectPosition(e);

        if (!isTeammate(e.getName())) {

            // Meet enemies
            if(!choosenTarget){
                Enemy enemy = new Enemy(e, position);
                enemies.put(e.getName(),enemy);
                scannedEnemies++;
            }

            if(teamleader && enemies.values().size() == aliveEnemies){
                if(!fighting){
                    fighting = true;
                    target = selectTarget();
                    System.out.println("Escolhi inimigo " + target.toString());
                    orderAttack(target);
                    enemies.clear();
                    attack();
                }
                else{
                    if(e.getName().equals(target.getName())){
                        target.update(e, position);
                        orderAttack(target);
                        attack();
                    }
                }
            } else{
                if(!helpMode){
                    if(choosenTarget && e.getName().equals(target.getName())) {
                        //System.out.println("! HELP contra " + target.getName());
                        target.update(e, position);
                        attack();
                    }
                    if (!choosenTarget && scannedEnemies >= enemiesToScan) {
                        target = selectTarget();
                        System.out.println("! HELP Target escolhido " + target.getName());
                        choosenTarget = true;
                        enemies.clear();
                        attack();
                    }
                }else{
                    if(choosenTarget && e.getName().equals(target.getName())) {
                        System.out.println("HELP contra " + target.getName());
                        target.update(e,position);
                        attack();
                    }
                }
            }
        }
    }

    public void onMessageReceived(MessageEvent event) {
        Message message = (Message) event.getMessage();

        switch (message.getTipo()) {
            case Message.HELP:
                target = message.getTarget();
                choosenTarget = true;
                helpMode = true;
                break;

            case Message.CHANGELEADER:
                helpMode = false;
                target = selectTarget();
                choosenTarget = true;
                teamleader = true;
                break;
        }
    }

    public void onHitByBullet(HitByBulletEvent event){
        turnRight(30);
        ahead(50);
    }


    public void onRobotDeath(RobotDeathEvent e) {

        if(!isTeammate(e.getName())) {
            if(teamleader) {
                System.out.println("MORTE1");
                fighting = false;
                target.reset();
                choosenTarget = false;
                enemies.remove(target.getName());
                enemiesToScan = --aliveEnemies;
                scannedEnemies = 0;
            }
            if (e.getName().equals(target.getName())) {
                System.out.println("MORTE2");
                target.reset();
                choosenTarget = false;
                helpMode = false;
            }
        }

    }

    // ########################## Ações ##########################


    public void attack() {

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

        setTurnGunRight(gunTurnAmt);

        // if the gun is cool and we're pointed at the target, shoot!
        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 30)
            fire(MAX_BULLET_POWER);

    }

    public Enemy selectTarget() {
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
