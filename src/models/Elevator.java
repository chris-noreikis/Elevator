package models;

import gui.ElevatorDisplay.Direction;
import gui.ElevatorDisplay;

import java.util.ArrayList;

public class Elevator {
    private int id;
    private boolean isDoorOpen;
    private int currentFloor;
    private Building building;
    private ArrayList<ElevatorRequest> floorRequests;
    private ArrayList<ElevatorRequest> riderRequests;
    private ArrayList<Person> peopleOnElevator;

    private enum ElevatorState {GOING_UP, GOING_DOWN, IDLE}
    private ElevatorState currentState;
    private Direction elevatorDirection;
    private int capacity;
    private int elevatorSpeedInMilliseconds;
    private int doorOpenTimeInMilliseconds;
    private int returnToFirstFloorAfter;
    private int timeTilClose;
    private int timeLeftOnFloor;
    public int idleCount = 0;
    public static final int floorTime = 1000;
    public static final int doorTime = 2000;

    public Elevator(int id, int capacity, int elevatorSpeedInMilliseconds, int doorOpenTimeInMilliseconds, int returnToFirstFloorAfter) {
        setIsDoorOpen(false);
        setCurrentFloor(1);
        setId(id);
        setBuilding(building);
        setCapacity(capacity);
        setElevatorSpeedInMilliseconds(elevatorSpeedInMilliseconds);
        setDoorOpenTimeInMilliseconds(doorOpenTimeInMilliseconds);
        setReturnToFirstFloorAfter(returnToFirstFloorAfter);
        setState(ElevatorState.IDLE);
        floorRequests = new ArrayList<>();
        peopleOnElevator = new ArrayList<>();

        System.out.println(String.format("Elevator %s created ...", id));
    }

    public void addDestination(ElevatorRequest elevatorRequest) {
        int requestFloorElevator = elevatorRequest.getFloorNumber();
        if (this.getElevatorRequests().contains(elevatorRequest)) {
            return;
        }

        if (getCurrentState().equals(ElevatorState.IDLE)) {
            setState(requestFloorElevator >= getCurrentFloor() ? ElevatorState.GOING_UP : ElevatorState.GOING_DOWN);
        }
    }

    public void move(int time) {
        System.out.println("Moving elevator " + this.getID());
    }

    private void setState(ElevatorState e) {
        this.currentState = e;
    }

    private ElevatorState getCurrentState() {
        return this.currentState;
    }

    private void setBuilding(Building b) {
        this.building = b;
    }

    private ArrayList<ElevatorRequest> getElevatorRequests() {
        return this.floorRequests;
    }

    private void setId(int id) {
        this.id = id;
    }

    private int getID() {
        return id;
    }

    private boolean isDoorOpen() {
        return this.isDoorOpen;
    }

    private void setIsDoorOpen(boolean nextValue) {
        this.isDoorOpen = nextValue;
    }

    public void pickUpPassenger(Person p) {
        this.peopleOnElevator.add(p);
    }

    private int getCurrentFloor() {
        return currentFloor;
    }

    private void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

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


    public void debugState() {
        System.out.println("Current elevator state...");
        System.out.println(String.format("Current Floor: %s", getCurrentFloor()));
        System.out.println(String.format("Current State: %s", getCurrentState()));
    }
}
