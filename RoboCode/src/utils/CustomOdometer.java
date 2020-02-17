package utils;

import robocode.*;

import java.util.*;

public class CustomOdometer {
    AdvancedRobot ar;
    boolean isRunning;
    boolean raceCompleted;
    List<Position> positions;
    double totalDistance;
    int numRegisters;
    Position startingPoint;

    public CustomOdometer(String name, AdvancedRobot robot){
        isRunning = false;
        raceCompleted = false;
        positions = new ArrayList<>();
        totalDistance = 0;
        numRegisters = 0;
        startingPoint = new Position(18.0, 18.0);
        ar = robot;

        robot.addCustomEvent(
                new Condition(name) {
                    @Override
                    public boolean test() {
                        return !isRunning && raceCompleted;
                    }
                }
        );
    }

    public void registerPosition(Position p){
        if(!isRunning && !raceCompleted && p.equals(startingPoint)){
            isRunning = true;
            positions.add(startingPoint);
            System.out.println("Começou a corrida");
        }
        else if(isRunning && !raceCompleted && p.equals(startingPoint) && numRegisters > 50){
            isRunning = false;
            raceCompleted = true;
            this.ar.setDebugProperty("odometer_distance_measured", String.format("%.4f", totalDistance));
        }
        else if(isRunning && !raceCompleted) {
            totalDistance += Math.distanceBetween2Points(p.x, p.y, positions.get(numRegisters).x, positions.get(numRegisters).y);
            numRegisters++;
            this.positions.add(p);
            if(numRegisters % 100 == 0) {
                System.out.println("CUSTOM ODOMETER: Total percorrido = " + totalDistance);
            }

            this.ar.setDebugProperty("odometer_distance_measured", String.format("%.4f", totalDistance));
        }
    }

    public double getTotal(){
        this.ar.setDebugProperty("odometer_distance_measured", String.format("%.4f", totalDistance));
        return  totalDistance;
    }

    public List<Position> getPositions(){
        return  positions;
    }

    public Position getStartingPoint(){
        return  startingPoint;
    }
}
