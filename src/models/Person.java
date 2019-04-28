package models;

import exceptions.*;
import gui.ElevatorDisplay.Direction;

public class Person {
    private int startFloor;
    private int endFloor;
    private String id;

    public Person(int startFloor, int endFloor, String id) throws InvalidValueException {
        setStartFloor(startFloor);
        setEndFloor(endFloor);
        setId(id);
    }

    public int getStartFloor() {
        return startFloor;
    }

    public int getEndFloor() {
        return endFloor;
    }

    public String getId() { return id; }

    private void setId(String idIn) {
        this.id = idIn;
    }

    public boolean isAtDestinationFloor(int floorNum) throws InvalidValueException {
        Building.getInstance().validateFloor("Person asked for invalid destination floor", floorNum);

        return floorNum == getEndFloor();
    }

    public boolean isTravellingInSameDirection(Direction d) {
        if (startFloor < endFloor) {
            return d == Direction.UP;
        } else {
            return d == Direction.DOWN;
        }
    }

    public String toString() {
        return getId();
    }

    private void setStartFloor(int startFloor) throws InvalidValueException  {
        Building.getInstance().validateFloor("Person set to invalid start floor", startFloor);
        this.startFloor = startFloor;
    }

    private void setEndFloor(int endFloor) throws InvalidValueException{
        Building.getInstance().validateFloor("Person set to invalid end floor", endFloor);
        this.endFloor = endFloor;
    }
}
