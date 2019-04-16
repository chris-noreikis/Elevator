package models;

import gui.ElevatorDisplay.Direction;
import gui.ElevatorDisplay;

import java.util.ArrayList;

public class Elevator {
    private int id;
    private boolean isDoorOpen;
    private int currentFloor;
    private Building building;
    private ArrayList<Integer> floors;

    private enum ElevatorState {GOING_UP, GOING_DOWN, IDLE};
    private ElevatorState currentState;
    private Direction elevatorDirection;
    private int capacity;
    private int elevatorSpeedInMilliseconds;
    private int doorOpenTimeInMilliseconds;
    private int returnToFirstFloorAfter;

    public Elevator(int id, Building building, int capacity, int elevatorSpeedInMilliseconds, int doorOpenTimeInMilliseconds, int returnToFirstFloorAfter) {
        setIsDoorOpen(false);
        setCurrentFloor(1);
        setId(id);
        setBuilding(building);
        setCapacity(capacity);
        setElevatorSpeedInMilliseconds(elevatorSpeedInMilliseconds);
        setDoorOpenTimeInMilliseconds(doorOpenTimeInMilliseconds);
        setReturnToFirstFloorAfter(returnToFirstFloorAfter);
        setState(ElevatorState.IDLE);
        floors = new ArrayList<>();

        if (!Building.isUnitTest) {
            ElevatorDisplay.getInstance().addElevator(id, 1);
        }

        System.out.println(String.format("Elevator %s created ...", id));
    }

    private void setState(ElevatorState e) { this.currentState = e; }

    public ElevatorState getCurrentState() { return this.currentState; }

    private void setBuilding (Building b) { this.building = b; }

    private ArrayList<Integer> getFloors() { return this.floors; }

    private void setId(int id) { this.id = id; }

    public int getID() {
        return id;
    }

    public boolean isDoorOpen() {
        return this.isDoorOpen;
    }

    public void setIsDoorOpen(boolean nextValue) { this.isDoorOpen = nextValue; }

    private int getCurrentFloor() { return currentFloor;  }

    private void setCurrentFloor (int currentFloor) { this.currentFloor = currentFloor; }

    private Direction getElevatorDirection() {
        return elevatorDirection;
    }

    private void setElevatorDirection(Direction elevatorDirection) {
        this.elevatorDirection = elevatorDirection;
    }

    private int getCapacity() {
        return capacity;
    }

    private void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    private int getElevatorSpeedInMilliseconds() {
        return elevatorSpeedInMilliseconds;
    }

    private void setElevatorSpeedInMilliseconds(int elevatorSpeedInMilliseconds) {
        this.elevatorSpeedInMilliseconds = elevatorSpeedInMilliseconds;
    }

    private int getDoorOpenTimeInMilliseconds() {
        return doorOpenTimeInMilliseconds;
    }

    private void setDoorOpenTimeInMilliseconds(int doorOpenTimeInMilliseconds) {
        this.doorOpenTimeInMilliseconds = doorOpenTimeInMilliseconds;
    }

    private int getReturnToFirstFloorAfter() {
        return returnToFirstFloorAfter;
    }

    private void setReturnToFirstFloorAfter(int returnToFirstFloorAfter) {
        this.returnToFirstFloorAfter = returnToFirstFloorAfter;
    }

    public void addDestination(int floorNumber) {
        if (getFloors().contains(floorNumber)) {
            return;
        }

        if (getCurrentState().equals(ElevatorState.IDLE)) {
            setState(floorNumber >= getCurrentFloor() ? ElevatorState.GOING_UP : ElevatorState.GOING_DOWN);
        }
    }

    public void debugState() {
        System.out.println("Current elevator state...");
        System.out.println(String.format("Current Floor: %s", getCurrentFloor()));
        System.out.println(String.format("Current State: %s", getCurrentState()));
    }
}
