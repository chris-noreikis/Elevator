package models;

import java.util.ArrayList;

import configuration.SimulationConfiguration;
import gui.ElevatorDisplay.Direction;
import gui.ElevatorDisplay;

public class Building {
    private ArrayList<Floor> floors;
    private static Building instance;

    public static Building getInstance() throws InvalidValueException {
        if (instance == null) {
            instance = new Building();
        }
        return instance;
    }

    private Building() throws InvalidValueException {
        ElevatorDisplay.getInstance().initialize(getNumberOfFloors());
        setupFloors();
    }

    private void setupFloors() throws InvalidValueException {
        int numberOfFloors = getNumberOfFloors();
        floors = new ArrayList<>();
        for (int i = 0; i < numberOfFloors; i++) {
            floors.add(new Floor(i + 1));
        }
    }

    public Floor getFloor(int floorNumber) throws InvalidValueException {
        checkFloor("getFloor called with invalid floor", floorNumber);
        return floors.get(floorNumber - 1);
    }

    public int getNumberOfFloors() throws InvalidValueException {
        return SimulationConfiguration.getInstance().getNumberOfFloors();
    }

    public void addPerson(Person person) throws InvalidValueException {
        checkFloor("Person is starting on a floor that doesn\'t exist", person.getStartFloor());
        checkFloor("Person is trying to get to a floor that doesn\'t exist", person.getEndFloor());

        getFloor(person.getStartFloor()).addWaitingPerson(person);
        person.startWaiting();
    }

    public void checkFloor(String errorMessage, int floorNumber) throws InvalidValueException {
        if (floorNumber < 1 || floorNumber > getNumberOfFloors()) {
            int maxFloor = getNumberOfFloors();
            String formattedErrorMessage = String.format("%s; expected 1-%d, got %d", errorMessage, maxFloor, floorNumber);
            throw new InvalidValueException(formattedErrorMessage);
        }
    }

    public ArrayList<Person> getPeopleOnFloorTravellingInDirection(int floorNumber, Direction direction) throws InvalidValueException {
        Floor f = getFloor(floorNumber);
        return f.getPeopleTravellingInDirection(direction);
    }

    public void addDonePeople(int floorNum, ArrayList<Person> donePeople) throws InvalidValueException {
        getFloor(floorNum).addDonePeople(donePeople);
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("Building Report \n");

        for (Floor f : floors) {
            output.append(f.toString());
        }

        return output.toString();
    }
}
