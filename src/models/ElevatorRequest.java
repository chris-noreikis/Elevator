package models;

import gui.ElevatorDisplay.Direction;

public class ElevatorRequest {
    private Direction direction;
    private int floor;

    public ElevatorRequest(Direction direction, int floor) throws InvalidValueException {
        setDirection(direction);
        setFloor(floor);
    }

    public Direction getDirection() {
        return direction;
    }

    public int getFloorNumber() {
        return floor;
    }

    private void setDirection(Direction direction) throws InvalidValueException {
        if (direction == null) {
            throw new InvalidValueException("Direction is null");
        }
        this.direction = direction;
    }

    private void setFloor(int floor) throws InvalidValueException {
        Building.getInstance().checkFloor("Invalid floor", floor);
        this.floor = floor;
    }

    public String toString() {
        return Integer.toString(getFloorNumber());
    }

    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + getDirection().hashCode();
        hash = 97 * hash + getFloorNumber();
        return hash;
    }

    @Override
    public boolean equals(Object e2) {
        if (e2 instanceof ElevatorRequest) {
            ElevatorRequest otherRequest = (ElevatorRequest) e2;
            return otherRequest.getFloorNumber() == getFloorNumber() && otherRequest.getDirection() == getDirection();
        }

        return false;
    }
}
