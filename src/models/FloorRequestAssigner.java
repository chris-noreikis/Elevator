package models;

public interface FloorRequestAssigner {
    public void addElevatorRequest(ElevatorRequest elevatorRequest, Person person) throws InvalidValueException;
}
