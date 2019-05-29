package models;

import gui.ElevatorDisplay.Direction;

public class Person {
    private int startFloor;
    private int endFloor;
    private String id;
    private long waitStart;
    private long waitEnd;
    private long rideStart;
    private long rideEnd;

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

    private void setId(String idIn) throws InvalidValueException {
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

    public long getWaitStart() {
        return waitStart;
    }

    public void startWaiting() {
        this.waitStart = System.currentTimeMillis();
    }

    public long getWaitEnd() {
        return waitEnd;
    }

    public void startElevatorRide() {
        long currentTime = System.currentTimeMillis();
        this.waitEnd = currentTime;
        this.rideStart = currentTime;
    }

    public void endElevatorRide() {
        this.rideEnd = System.currentTimeMillis();
    }


    public long getRideStart() {
        return rideStart;
    }

    public long getRideEnd() {
        return rideEnd;
    }

    public void endRiding(long rideEnd) throws InvalidValueException {
        this.rideEnd = System.currentTimeMillis();
    }

    public double getWaitTime() { return (waitEnd - waitStart) / 1000.0; }

    public double getRideTime() { return (rideEnd - rideStart) / 1000.0; }
}
