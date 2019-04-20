package models;

import gui.ElevatorDisplay.Direction;

public class ElevatorRequest {
    private Direction direction;
    private int floor;
    private boolean isRiderRequest;

    public ElevatorRequest(Direction direction, int floor) {
        this.setDirection(direction);
        this.setFloor(floor);
    }

    public Direction getDirection() {
        return direction;
    }

    public int getFloorNumber() {
        return floor;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String toString() {
        return "Elevator Request: Floor: " + getFloorNumber() + " Direction: " + getDirection();
    }
}
