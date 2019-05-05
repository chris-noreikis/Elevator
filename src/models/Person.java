package models;

import gui.ElevatorDisplay.Direction;

public class Person {
    private int startFloor;
    private int endFloor;
    private String id;

    public Person(int startFloor, int endFloor, String id) throws InvalidValueException {
        setStartFloor(startFloor);
        setEndFloor(endFloor);
        setId(id);
        Direction d = ElevatorDirection.determineDirection(startFloor, endFloor);
        ElevatorLogger.getInstance().logAction("Person " + id + " created on Floor " + startFloor + ", wants to go " + d + " to Floor " + endFloor);
    }

    public int getStartFloor() {
        return startFloor;
    }

    public int getEndFloor() {
        return endFloor;
    }

    public String getId() {
        return id;
    }

    private void setId(String idIn) {
        this.id = idIn;
    }

    public boolean isAtDestinationFloor(int floorNum) throws InvalidValueException {
        Building.getInstance().checkFloor("Person asked for invalid destination floor", floorNum);

        return floorNum == getEndFloor();
    }

    public boolean isDirectionOfTravel(Direction d) throws InvalidValueException {
        return d == ElevatorDirection.determineDirection(startFloor, endFloor);
    }

    public String toString() {
        return getId();
    }

    private void setStartFloor(int startFloor) throws InvalidValueException {
        Building.getInstance().checkFloor("Person set to invalid start floor", startFloor);
        this.startFloor = startFloor;
    }

    private void setEndFloor(int endFloor) throws InvalidValueException {
        Building.getInstance().checkFloor("Person set to invalid end floor", endFloor);
        this.endFloor = endFloor;
    }
}
