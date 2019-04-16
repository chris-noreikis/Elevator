/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.WindowEvent;

/**
 *
 * @author hieldc
 */
public class ElevatorDisplay {

    public static enum Direction {UP, DOWN, IDLE};
    
    private static final ElevatorDisplay instance = new ElevatorDisplay();
    private ElevatorFrame elevatorFrame;
    private boolean initalized = false;

    public static ElevatorDisplay getInstance() {
        return instance;
    }

    public void initialize(int numFloors) {
        checkIntLE(numFloors, 1, "ElevatorDisplay (initialize): Number of floors must be greater than 1.", -1);

        if (elevatorFrame == null) {
            initalized = true;
            elevatorFrame = new ElevatorFrame(numFloors);
        }
    }

    public void addElevator(int id, int initFloor) {
        checkIntLE(id, 0, "ElevatorDisplay (addElevator): Elevator id must be greater than zero.", -2);
        checkIntLE(initFloor, 0, "ElevatorDisplay (addElevator): Initial floor must be greater than zero.", -2);

        if (!initalized) {
            System.err.println("ElevatorDisplay (addElevator): ElevatorDisplay has not been initalized. Please call 'initialize(numFloors)' before use.");
            System.exit(-10);
        }
        elevatorFrame.addElevator(id, initFloor);
    }

    public void updateElevator(int id, int floor, int numRiders, Direction dir) {
        checkIntLE(id, 0, "ElevatorDisplay (updateElevator): Elevator id must be greater than zero.", -1);
        checkIntLE(floor, 0, "ElevatorDisplay (updateElevator): Floor must be greater than zero.", -1);
        checkIntLE(numRiders, -1, "ElevatorDisplay (updateElevator): number of riders must be zero or greater.", -1);

        if (!initalized) {
            System.err.println("ElevatorDisplay (updateElevator): ElevatorDisplay has not been initalized. Please call 'initialize(numFloors)' before use.");
            System.exit(-10);
        }
        elevatorFrame.setFloor(id, floor);
        elevatorFrame.setNumRiders(id, numRiders);
        elevatorFrame.setDirection(id, dir);
    }
    
    public void setIdle(int id) {
        checkIntLE(id, 0, "ElevatorDisplay (setDirection): Elevator id must be greater than zero.", -1);

        if (!initalized) {
            System.err.println("ElevatorDisplay (setDirection): ElevatorDisplay has not been initalized. Please call 'initialize(numFloors)' before use.");
            System.exit(-10);
        }
        elevatorFrame.setDirection(id, Direction.IDLE);
    }
    

    public void openDoors(int id) {
        checkIntLE(id, 0, "ElevatorDisplay (openDoors): Elevator id must be greater than zero.", -1);

        if (!initalized) {
            System.err.println("ElevatorDisplay (openDoors): ElevatorDisplay has not been initalized. Please call 'initialize(numFloors)' before use.");
            System.exit(-10);
        }
        elevatorFrame.openDoors(id);
    }

    public void closeDoors(int id) {
        checkIntLE(id, 0, "ElevatorDisplay (checkIntLE): Elevator id must be greater than zero.", -1);

        if (!initalized) {
            System.err.println("ElevatorDisplay (closeDoors): ElevatorDisplay has not been initalized. Please call 'initialize(numFloors)' before use.");
            System.exit(-10);
        }
        elevatorFrame.closeDoors(id);
    }

    public void shutdown() {
        elevatorFrame.dispatchEvent(new WindowEvent(elevatorFrame, WindowEvent.WINDOW_CLOSING));

    }

    private void checkIntLE(int value, int limit, String msg, int code) {
        if (value <= limit) {
            System.err.println(msg + " [Received: " + value + "]");
            System.out.println(Thread.currentThread().getStackTrace()[2]);
            System.exit(code);
        }
    }
}
