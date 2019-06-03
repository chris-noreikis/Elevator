package models;

public interface FloorRequestAssigner {
    public void assignElevatorRequest(ElevatorRequest elevatorRequest) throws InvalidValueException;
}
