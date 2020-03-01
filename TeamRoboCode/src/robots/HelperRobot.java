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
    boolean choosenTarget = false;

    public void run() {
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);
        turnRadarRightRadians(Double.POSITIVE_INFINITY);
    }

    // ########################## Eventos ##########################

    public void onScannedRobot(ScannedRobotEvent e) {

        if (!isTeammate(e.getName())) {
            Position position = detectPosition(e);
            if(teamleader){
                Enemy enemy = new Enemy(e, position);
                enemies.put(e.getName(), enemy);

                if(!fighting && enemies.values().size() == aliveEnemies){
                    fighting = true;
                    target = selectTarget();
                    System.out.println("Escolhi primeiro inimigo");
                    orderAttack(target);
                }

            } else{
                if(!helpMode){
                    if(e.getName().equals(target.getName())) {
                        System.out.println("! HELP contra " + target.getName());
                        target.update(e, position);
                    }
                    if (choosenTarget == false && e.getDistance() <= 250) {
                        System.out.println("! HELP Target escolhido " + target.getName());
                        target.update(e,position);
                        choosenTarget = true;
                    }
                    attack();
                }else{
                    if(e.getName().equals(target.getName()) && e.getDistance() <= 300) {
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
                System.out.println("HELP RECEBIDO");
                target = message.getTarget();
                helpMode = true;
                break;

            case Message.CHANGELEADER:
                System.out.println("CHANGELEADER RECEBIDO");
                helpMode = false;
                target = selectTarget();
                teamleader = true;
                break;
        }
    }


    public void onRobotDeath(RobotDeathEvent e) {

        System.out.println("MORTE");

        if(!isTeammate(e.getName())) {
            if(teamleader) {
                enemies.remove(target.getName());
                aliveEnemies--;
            }
            if (e.getName().equals(target.getName())) {
                target.reset();
                choosenTarget = false;
                helpMode = false;
                if(teamleader){
                    target = selectTarget();
                    orderAttack(target);
                }
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
            setFire(Math.min(400 / target.getDistance(), 3));

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
