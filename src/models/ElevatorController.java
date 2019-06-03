package models;

import gui.ElevatorDisplay.Direction;
import gui.ElevatorDisplay;

import java.util.ArrayList;

public class ElevatorController {
    private ArrayList<Elevator> elevators;
    private static ElevatorController instance;
    private PendingRequestProcessor pendingRequestProcessor;
    private FloorRequestAssigner floorRequestAssigner;

    public static ElevatorController getInstance() throws InvalidValueException {
        if (instance == null) {
            instance = new ElevatorController();
        }

        return instance;
    }


    private ElevatorController() throws InvalidValueException {
        setupElevators();
        pendingRequestProcessor = PendingRequestProcessorFactory.build();
        floorRequestAssigner = FloorRequestAssignerFactory.build();
    }

    private void setupElevators() throws InvalidValueException {
        int numElevators = Building.getInstance().getNumberOfElevators();
        int elevatorCapacity = Building.getInstance().getElevatorCapacity();
        int elevatorSpeed = Building.getInstance().getElevatorSpeed();
        int doorOpenTime = Building.getInstance().getDoorOpenTime();
        int returnToFirstFloorAfter = Building.getInstance().getReturnToDefaultFloorTimeout();
        elevators = new ArrayList<>();
        for (int elevatorID = 1; elevatorID <= numElevators; elevatorID++) {
            elevators.add(new Elevator(elevatorID, elevatorCapacity, elevatorSpeed, doorOpenTime, returnToFirstFloorAfter));
            ElevatorDisplay.getInstance().addElevator(elevatorID, 1);
        }
    }

    private Elevator getElevator(int id) throws InvalidValueException {
        checkElevatorExists(id);
        return elevators.get(id - 1);
    }

    public void moveElevators(int time) throws InvalidValueException {
        if (time < 0) {
            throw new Error("Time must be greater than 0");
        }

        for (Elevator e : elevators) {
            e.doTimeSlice(time);
        }
    }

    public void addElevatorRequest(ElevatorRequest elevatorRequest, Person person) throws InvalidValueException {
        if (elevatorRequest == null) {
            throw new InvalidValueException("Elevator Request cannot be null");
        }

        if (person == null) {
            throw new InvalidValueException("Person cannot be null");
        }

        Building.getInstance().checkFloor("ElevatorController passed an invalid Elevator Request", elevatorRequest.getFloorNumber());
        ElevatorLogger.getInstance().logAction("Person " + person.getId() + " presses " + elevatorRequest.getDirection() + " button on Floor " + elevatorRequest.getFloorNumber());

        floorRequestAssigner.assignElevatorRequest(elevatorRequest);
    }

    public ArrayList<ElevatorRequest> processPendingRequests(int startFloor) throws InvalidValueException {
        Building.getInstance().checkFloor("Invalid start floor for pending requests", startFloor);

        return pendingRequestProcessor.processPendingRequests(startFloor);
    }

    public boolean isElevatorOnFloor(int elevatorId, int floorNumber) throws InvalidValueException {
        Building.getInstance().checkFloor("Elevator cannot be on this floor", floorNumber);
        checkElevatorExists(elevatorId);
        return getElevator(elevatorId).getCurrentFloor() == floorNumber;
    }

    public boolean isElevatorInDesiredDirection(int elevatorId, int floorNumber, Direction elevatorDirection) throws InvalidValueException {
        if (elevatorDirection == null) {
            throw new InvalidValueException("ElevatorDirection cannot be null");
        }

        checkElevatorExists(elevatorId);
        Building.getInstance().checkFloor("Invalid floor number", floorNumber);

        return getElevator(elevatorId).isInDesiredDirection(floorNumber, elevatorDirection);
    }

    public Direction getElevatorDirection(int elevatorId) throws InvalidValueException {
        checkElevatorExists(elevatorId);
        return getElevator(elevatorId).getElevatorDirection();
    }

    public void addFloorRequestToElevator(int elevatorId, ElevatorRequest elevatorRequest) throws InvalidValueException {
        if (elevatorRequest == null) {
            throw new InvalidValueException("Elevator request cannot be null");
        }
        Building.getInstance().checkFloor("Invalid Elevator Request", elevatorRequest.getFloorNumber());

        checkElevatorExists(elevatorId);
        getElevator(elevatorId).addFloorRequest(elevatorRequest);
    }

    public void addPendingRequest(ElevatorRequest elevatorRequest) throws InvalidValueException {
        if (elevatorRequest == null) {
            throw new InvalidValueException("Elevator request cannot be null");
        }
        Building.getInstance().checkFloor("Invalid Elevator Request", elevatorRequest.getFloorNumber());

        pendingRequestProcessor.addPendingRequest(elevatorRequest);
    }

    public boolean isElevatorSpaceOpen(int elevatorId) throws InvalidValueException {
        checkElevatorExists(elevatorId);
        return getElevator(elevatorId).isElevatorSpaceAvailable();
    }

    public String toString() {
        String output = "Elevator Controller report ...\n";

        output += "Elevators: \n";
        StringBuilder outputBuilder = new StringBuilder(output);
        for (Elevator e : elevators) {
            outputBuilder.append(e.toString());
        }
        output = outputBuilder.toString();
        return output;
    }

    public boolean isOperating() {
        for (Elevator e : elevators) {
            if (!e.isIdleOnDefaultFloor()) {
                return true;
            }
        }

        return false;
    }

    private void checkElevatorExists(int elevatorId) throws InvalidValueException {
        try {
            elevators.get(elevatorId - 1);
        } catch (IndexOutOfBoundsException ex) {
            throw new InvalidValueException("Could not find Elevator with given ID in elevator controller");
        }
    }
}
