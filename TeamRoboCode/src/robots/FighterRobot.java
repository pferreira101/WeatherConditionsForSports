package robots;

import robocode.*;
import utils.Math;
import utils.Message;
import utils.Position;

import java.io.IOException;
import java.util.List;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class FighterRobot extends TeamRobot {
    Position positionToFire = null;
    double theta = 0;
    boolean onMission = false;

    List<Position> teammatesPos;

    boolean allInfoAvailable = false;
    boolean targetLocked = false;
    boolean chasing = false;
    boolean isTeamLeader = false;

    public void run() {

        addCustomEvent(new
                RadarTurnCompleteCondition(this));
        setAdjustRadarForGunTurn(true);
        setTurnRadarRight(360);


        while(true){
            /*
            if(!targetLocked){
                chooseTargetEnemy();
            }
            */
        }
    }
/*
    void chooseTargetEnemy(){
        sharePosition();
        List<EnemyInfo> infos = scanAllEnemys();

        while(this.teammatesPos.size() != 3);

        selectTarget();

        this.teammatesPos = new ArrayList<>();

        chaseEnemy();
    }

    List<EnemyInfo> scanAllEnemys(){

    }
*/
    void sharePosition(){
        Message msg = new Message(Message.INFO, new Position(this.getX(), this.getY()));
        try {
            broadcastMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void selectTarget(){
        System.out.println("A escolher inimigo. n info: " + this.teammatesPos.size());

    }

    public void onScannedRobot(ScannedRobotEvent event){
        if(isTeammate(event.getName())){
            Message carefull = new Message();
            carefull.setTipo(2);

            try{
                sendMessage(event.getName(),carefull);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onMessageReceived(MessageEvent event) {
        Message message = (Message) event.getMessage();
        switch (message.getTipo()) {
            case 0:
                System.out.println("Sou o Robot " + message.getReceiver() + " e recebi do " + message.getSender() + " -> " + message.getContent());
                break;
            case 1:
                System.out.println("Recebi um turnTo to " + message.getPosition().getX() + " " + message.getPosition().getY());

                positionToFire = message.getPosition();

                //Turn radar to position to fire
                turnRadarTo(message.getPosition());

                onMission = true;

                // Turn to position received
                turnTo(positionToFire);

                // Defense movement & Attack
                movementWhenFiring(positionToFire);

                // Return to the original position at the end of the attack
                turnLeft(normalRelativeAngleDegrees(theta - getHeading())+90);
                turnGunLeft(normalRelativeAngleDegrees(theta - getHeading()));

                onMission = false;
                break;
            case 2:
                System.out.println("Recebi mensagem para me desviar");
                ahead(200);
                break;
        }
    }
    /*
        public void onCustomEvent(CustomEvent e) {
            if (e.getCondition() instanceof
                    RadarTurnCompleteCondition) sweep();
        }

        private int radarDirection=1;

        private void sweep() {
            double maxBearingAbs=0, maxBearing=0;
            int scannedBots=0;
            Iterator iterator = theEnemyMap.values().
                    iterator();

            while(iterator.hasNext()) {
                Enemy tmp = (Enemy)iterator.next();

                if (tmp!=null && tmp.isUpdated()) {
                    double bearing=normalRelativeAngle
                            (getHeading() + tmp.getBearing()
                                    - getRadarHeading());
                    if (java.lang.Math.abs(bearing)>maxBearingAbs) {
                        maxBearingAbs=java.lang.Math.abs(bearing);
                        maxBearing=bearing;
                    }
                    scannedBots++;
                }
            }

            double radarTurn=180*radarDirection;
            if (scannedBots==getOthers())
                radarTurn=maxBearing+sign(maxBearing)*22.5;

            setTurnRadarRight(radarTurn);
            radarDirection=sign(radarTurn);
        }
    */
    public void movementWhenFiring(Position position){
        ahead(100);

        // Calculate angle to target
        double theta = java.lang.Math.toDegrees(java.lang.Math.atan2(position.getX() - this.getX(), position.getY() - this.getY()));

        // Fire to target
        turnGunRight(normalRelativeAngleDegrees(theta - getHeading()+90));
        fire(4);
        turnGunLeft(normalRelativeAngleDegrees(theta - getHeading()+90));

        back(200);

        // Fire to target
        turnGunLeft(normalRelativeAngleDegrees(theta - getHeading()+90));
        fire(4);
        turnGunRight(normalRelativeAngleDegrees(theta - getHeading()+90));

        // Fire to target
        ahead(100);
        fire(4);
    }

    public void turnTo(Position position){
        double dx = position.getX() - this.getX();
        double dy = position.getY() - this.getY();

        // Calculate angle to target
        theta = java.lang.Math.toDegrees(java.lang.Math.atan2(dx, dy));

        // Turn perpendicularly
        turnRight(normalRelativeAngleDegrees(theta - getHeading())+90);

        // Turn gun to target
        turnGunRight(normalRelativeAngleDegrees(theta - getHeading()));
    }

    public void turnRadarTo(Position position){
        double dx = position.getX() - this.getX();
        double dy = position.getY() - this.getY();

        // Calculate angle to target
        double theta = java.lang.Math.toDegrees(java.lang.Math.atan2(dx, dy));

        // Turn to target
        turnRadarRight(normalRelativeAngleDegrees(theta - getRadarHeading()));
    }

    public void goTo(Position position){
        double fromX = getX();
        double fromY = getY();

        turnTo(position);

        double distance =  Math.distanceBetween2Points(fromX, fromY, position.getX(), position.getY());

        // Move on
        ahead(distance);
    }
}
