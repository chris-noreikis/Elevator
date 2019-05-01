package models;

import exceptions.InvalidValueException;
import gui.ElevatorDisplay.Direction;

public class ElevatorRequest {
    private Direction direction;
    private int floor;

    public ElevatorRequest(Direction direction, int floor) throws InvalidValueException {
        this.setDirection(direction);
        this.setFloor(floor);
    }
    
    public Direction getDirection() {
        return direction;
    }

    public int getFloorNumber() {
        return floor;
    }

    public void setDirection(Direction direction) throws InvalidValueException {
    	if(direction == null) {
    		throw new InvalidValueException("Direction is null");
    	}
        this.direction = direction;
    }

    public void setFloor(int floor) throws InvalidValueException {
    	if(floor < 0) {
    		throw new InvalidValueException("Floor is less than 0.");
    	}
        this.floor = floor;
    }

    public String toString() {
        return "Elevator Request: Floor: " + getFloorNumber() + " Direction: " + getDirection();
    }

    // override equals should override hashcode
    // instead, could use method called matches to avoid overriding
    @Override
    public boolean equals(Object e2) {
        if (e2 instanceof ElevatorRequest) {
            ElevatorRequest otherRequest = (ElevatorRequest) e2;
            return otherRequest.getFloorNumber() == getFloorNumber() && otherRequest.getDirection() == getDirection();
        }

        return false;
    }
}
