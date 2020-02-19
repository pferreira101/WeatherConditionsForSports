package robots;

import robocode.*;
import utils.Message;

public class Robot4 extends TeamRobot{
    public void run() {
        // Robot main loop
        while(true) {
            // Replace the next 4 lines with any behavior you would like
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
        }
    }


    public void onMessageReceived(MessageEvent event) {
        Message message = (Message) event.getMessage();
        System.out.println("Sou o Robot " + message.getReceiver() + " e recebi do " + message.getSender() + " -> " + message.getContent());
    }


    public void onScannedRobot(ScannedRobotEvent e) {
        // Replace the next line with any behavior you would like
        fire(1);
    }

    public void onHitByBullet(HitByBulletEvent e) {
        // Replace the next line with any behavior you would like
        back(10);
    }

    public void onHitWall(HitWallEvent e) {
        // Replace the next line with any behavior you would like
        back(20);
    }
}


