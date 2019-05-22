package models;

import configuration.SimulationConfiguration;
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
        int numElevators = SimulationConfiguration.getInstance().getNumberOfElevators();
        int elevatorCapacity = SimulationConfiguration.getInstance().getElevatorCapacity();
        int elevatorSpeed = SimulationConfiguration.getInstance().getElevatorSpeed();
        int doorOpenTime = SimulationConfiguration.getInstance().getDoorOpenTime();
        int returnToFirstFloorAfter = SimulationConfiguration.getInstance().getReturnToDefaultFloorTimeout();
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

        floorRequestAssigner.addElevatorRequest(elevatorRequest, person);
    }

    public ArrayList<ElevatorRequest> processPendingRequests() throws InvalidValueException {
        return pendingRequestProcessor.processPendingRequests();
    }

    @Override
    public void addPendingRequest(ElevatorRequest e) throws InvalidValueException {
        if (e == null) {
            throw new InvalidValueException("Elevator request cannot be null");
        }

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

    public boolean isOperating() {
        for (Elevator e : elevators) {
            if (!e.isIdleOnDefaultFloor()) {
                return true;
            }
        }

        return false;
    }
}
