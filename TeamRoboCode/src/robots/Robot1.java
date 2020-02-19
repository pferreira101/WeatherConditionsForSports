package robots;

import robocode.*;
import utils.Message;

import java.io.IOException;

public class Robot1 extends TeamRobot{

    public void run() {

        // Check teammates
        String[] teammates = getTeammates();
        for(int i=0;i<teammates.length;i++)
            System.out.println(teammates[i]);

        // Create and send message
        Message message = new Message(getName(),teammates[0],"Então mpt " + teammates[0] + ", daqui fala o " + getName() + "!");
        System.out.println("Vou enviar mensagem para o " + teammates[0]);
        System.out.println("Conteúdo: " + message.getContent());

        try {
            sendMessage(teammates[0],message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Robot main loop
        while(true) {
            // Replace the next 4 lines with any behavior you would lik
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
        }
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
