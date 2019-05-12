package models;

import gui.ElevatorDisplay.Direction;
import gui.ElevatorDisplay;

import java.util.ArrayList;

public class ElevatorController implements PendingRequestProcessor, FloorRequestAssigner {
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

    public Elevator getElevator(int id) throws InvalidValueException {
        try {
            return elevators.get(id - 1);
        } catch (IndexOutOfBoundsException ex) {
            throw new InvalidValueException("Could not find Elevator with given ID in elevator controller");
        }
    }

    public void moveElevators(int time) throws InvalidValueException {
        for (Elevator e : elevators) {
            e.doTimeSlice(time);
        }
    }

    public void addElevatorRequest(ElevatorRequest elevatorRequest, Person person) throws InvalidValueException {
        floorRequestAssigner.addElevatorRequest(elevatorRequest, person);
    }

    public ArrayList<ElevatorRequest> processPendingRequests() throws InvalidValueException {
        return pendingRequestProcessor.processPendingRequests();
    }

    @Override
    public void addPendingRequest(ElevatorRequest e) throws InvalidValueException {
        pendingRequestProcessor.addPendingRequest(e);
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
}
