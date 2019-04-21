package models;

import gui.ElevatorDisplay;

import java.util.ArrayList;

public class ElevatorController {
    private ArrayList<Elevator> elevators;
    private static ElevatorController instance;

    public static ElevatorController getInstance()
    {
        if (instance == null) {
            instance = new ElevatorController();
        }
        return instance;
    }


    private ElevatorController() {
        setupElevators();
    }

    private void setupElevators() {
        int numElevators = Building.getInstance().getNumElevators();
        elevators = new ArrayList<>();
        for (int elevatorID = 1; elevatorID <= numElevators; elevatorID++) {
            elevators.add(new Elevator(elevatorID, 10, 1000, 2000, 10000));
            ElevatorDisplay.getInstance().addElevator(elevatorID, 1);
        }
    }

    public int getNumberOfElevators() {
        return elevators.size();
    }

    public Elevator getElevator(int id) {
        return elevators.get(id - 1);
    }

    public void moveElevators(int time) {
        for (Elevator e : elevators) {
            e.doTimeSlice(time);
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
