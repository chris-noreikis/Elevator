package models;

import java.util.ArrayList;

public class ElevatorController {
    private ArrayList<Elevator> elevators;
    private static ElevatorController instance;

    public static ElevatorController getInstance()
    {
        if (instance == null) {
            return new ElevatorController(Building.getInstance().getNumberOfElevators());
        }
        return instance;
    }


    private ElevatorController(int numElevators) {
        setupElevators(numElevators);
    }

    private void setupElevators(int numElevators) {
        elevators = new ArrayList<>();
        for (int elevatorID = 1; elevatorID <= numElevators; elevatorID++) {
            elevators.add(new Elevator(elevatorID, 10, 1000, 2000, 1000));
        }
    }

    public int getNumberOfElevators() {
        return elevators.size();
    }

    public Elevator getElevator(int id) {
        return elevators.get(id - 1);
    }
}
