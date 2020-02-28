package utils;

import robocode.ScannedRobotEvent;

public class Enemy {
    double bearing;
    double distance;
    double energy;
    double heading;
    double velocity;
    String name;

    public double getBearing(){
        return bearing;
    }
    public double getDistance(){
        return distance;
    }
    public double getEnergy(){
        return energy;
    }
    public double getHeading(){
        return heading;
    }
    public double getVelocity(){
        return velocity;
    }
    public String getName(){
        return name;
    }
    public void update(ScannedRobotEvent bot){
        bearing = bot.getBearing();
        distance = bot.getDistance();
        energy = bot.getEnergy();
        heading = bot.getHeading();
        velocity = bot.getVelocity();
        name = bot.getName();
    }

    public void reset(){
        bearing = 0.0;
        distance =0.0;
        energy= 0.0;
        heading =0.0;
        velocity = 0.0;
        name = null;
    }

    public Boolean none(){
        if (name == null || name == "")
            return true;
        else
            return false;
    }

    public Enemy(){
        reset();
    }

}