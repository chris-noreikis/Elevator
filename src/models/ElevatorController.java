package models;

import exceptions.InvalidStateException;
import exceptions.InvalidValueException;
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
    

    public Elevator getElevator(int id) throws InvalidValueException {
        return elevators.get(id - 1);
    }

    public void moveElevators(int time) throws InvalidValueException, InvalidStateException {
        for (Elevator e : elevators) {
            e.doTimeSlice(time);
        }
    }

    public void resetState() throws InvalidValueException {
        for (Elevator e : elevators) {
            e.resetState();
        }
    }

    public String toString() {
        String output = "Elevator Controller report ...\n";

        output += "Elevators: \n";
        for (Elevator e : elevators) {
            output += e.toString();
        }
        return output;
    }
	
}
