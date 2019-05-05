package models;

import gui.ElevatorDisplay;

import java.util.ArrayList;

public class ElevatorController {
    private ArrayList<Elevator> elevators;
    private static ElevatorController instance;

    public static ElevatorController getInstance() throws InvalidValueException {
        if (instance == null) {
            instance = new ElevatorController();
        }
        return instance;
    }


    private ElevatorController() throws InvalidValueException {
        setupElevators();
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

    public void addElevatorRequest(ElevatorRequest elevatorRequest, Person person, int elevatorId) throws InvalidValueException {
        ElevatorLogger.getInstance().logAction("Person " + person.getId() + " presses " + elevatorRequest.getDirection() + " button on Floor " + elevatorRequest.getFloorNumber());
        getInstance().getElevator(elevatorId).addFloorRequest(elevatorRequest);
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
