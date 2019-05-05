package models;

import gui.ElevatorDisplay.Direction;

public class ElevatorDirection {
    public static Direction determineDirection(int startFloor, int endFloor) throws InvalidValueException {
        Building.getInstance().checkFloor("determineDirection called with invalid start floor", startFloor);
        Building.getInstance().checkFloor("determineDirection called with invalid end floor", startFloor);

        return startFloor < endFloor ? Direction.UP : Direction.DOWN;
    }
}
