package utils;

import robocode.ScannedRobotEvent;

import java.io.Serializable;

public class Enemy implements Serializable {

    double bearing;
    double distance;
    double energy;
    double heading;
    double velocity;
    String name;
    Position position;

    public double getBearing() {
        return bearing;
    }

    public double getDistance() {
        return distance;
    }

    public double getEnergy() {
        return energy;
    }

    public double getHeading() {
        return heading;
    }

    public double getVelocity() {
        return velocity;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public void update(ScannedRobotEvent bot, Position pos) {
        bearing = bot.getBearing();
        distance = bot.getDistance();
        energy = bot.getEnergy();
        heading = bot.getHeading();
        velocity = bot.getVelocity();
        name = bot.getName();
        position = pos;
    }

    public void reset() {
        bearing = 0.0;
        distance = 0.0;
        energy = 0.0;
        heading = 0.0;
        velocity = 0.0;
        name = null;
        position = null;
    }

    public Boolean none() {
        if (name == null || name == "")
            return true;
        else
            return false;
    }

    public Enemy() {
        bearing = 0.0;
        distance = 0.0;
        energy = 0.0;
        heading = 0.0;
        velocity = 0.0;
        name = null;
        position = null;
    }

    public Enemy(ScannedRobotEvent bot, Position pos) {
        bearing = bot.getBearing();
        distance = bot.getDistance();
        energy = bot.getEnergy();
        heading = bot.getHeading();
        velocity = bot.getVelocity();
        name = bot.getName();
        position = pos;
    }

    @Override
    public String toString() {
        return "Nome: " + name;
    }

    public void setName(String name) { this.name = name; }
}