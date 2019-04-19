package models;

import gui.ElevatorDisplay.Direction;

public class Person {
    private int startFloor;
    private int endFloor;
    private String id;

    public Person(int startFloor, int endFloor, String id) {
        setStartFloor(startFloor);
        setEndFloor(endFloor);
        setID(id);
    }

    public int getStartFloor() {
        return startFloor;
    }

    public int getEndFloor() {
        return endFloor;
    }

    public String getId() { return id; }

    public boolean isDirectionUp() {
        return getStartFloor() < getEndFloor();
    }

    private Direction getDirection() { return isDirectionUp() ? Direction.UP : Direction.DOWN; }

    public ElevatorRequest pressButtonOnFloor(Floor f) {
        f.addWaitingPerson(this);
        return new ElevatorRequest(getDirection(), getStartFloor());
    }

    private void setStartFloor(int startFloor) {
        this.startFloor = startFloor;
    }

    private void setEndFloor(int endFloor) {
        this.endFloor = endFloor;
    }

    private void setID(String ID) {
        this.id = ID;
    }
}
