package models;

import gui.ElevatorDisplay;

import java.util.ArrayList;

public class Elevator {
    private int id;
    private boolean isDoorOpen;
    private int currentFloor;
    private Building building;

    public Elevator(int id, Building building) {
        this.isDoorOpen = false;
        currentFloor = 1;
        this.id = id;
        if (!Building.isUnitTest) {
            ElevatorDisplay.getInstance().addElevator(id, 1);
        }
        this.building = building;
    }

    public int getID() {
        return id;
    }

    public boolean isDoorOpen() {
        return this.isDoorOpen;
    }

    public void openDoors() {
        this.isDoorOpen = true;
        if (!Building.isUnitTest) {
            ElevatorDisplay.getInstance().openDoors(getID());
        }
    }

    public void closeDoors() {
        this.isDoorOpen = false;
        if (!Building.isUnitTest) {
            ElevatorDisplay.getInstance().closeDoors(getID());
        }
    }
}
