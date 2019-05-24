package models;

import java.util.ArrayList;

import configuration.ConfigurationException;
import configuration.SimulationConfiguration;
import gui.ElevatorDisplay.Direction;
import gui.ElevatorDisplay;

public class Building {
    private ArrayList<Floor> floors;
    private static Building instance;
    private int elevatorSpeed;
    private int elevatorCapacity;
    private int returnToDefaultFloorTimeout;
    private int numberOfElevators;
    private int doorOpenTime;
    private int numberOfFloors;

    public static Building getInstance() throws InvalidValueException {
        if (instance == null) {
            instance = new Building();
        }
        return instance;
    }

    private Building() throws InvalidValueException {
        elevatorSpeed = getConfigurationFieldFromJSON("elevatorSpeed");
        elevatorCapacity = getConfigurationFieldFromJSON("elevatorPersonCapacity");
        returnToDefaultFloorTimeout = getConfigurationFieldFromJSON("returnToDefaultFloorTimeout");
        numberOfElevators = getConfigurationFieldFromJSON("elevators");
        doorOpenTime = getConfigurationFieldFromJSON("doorOpenTime");
        numberOfFloors = getConfigurationFieldFromJSON("floors");
        ElevatorDisplay.getInstance().initialize(getNumberOfFloors());
        setupFloors();
    }

    public int getElevatorSpeed() {
        return elevatorSpeed;
    }

    public int getElevatorCapacity() {
        return elevatorCapacity;
    }

    public int getReturnToDefaultFloorTimeout() {
        return returnToDefaultFloorTimeout;
    }

    public int getDoorOpenTime() {
        return doorOpenTime;
    }

    public int getNumberOfElevators() { return numberOfElevators; }

    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    private int getConfigurationFieldFromJSON(String configurationFieldName) throws InvalidValueException {
        int configurationValue;
        try {
            configurationValue = SimulationConfiguration.getInstance().getConfigurationField(configurationFieldName);
        } catch (ConfigurationException e) {
            configurationValue = -1;
            System.out.println("Building created with invalid configuration, exiting program");
            e.printStackTrace();
            System.exit(1);
        }

        return configurationValue;
    }

    private void setupFloors() throws InvalidValueException {
        int numberOfFloors = getNumberOfFloors();
        floors = new ArrayList<>();
        for (int i = 0; i < numberOfFloors; i++) {
            floors.add(new Floor(i + 1));
        }
    }

    private Floor getFloor(int floorNumber) throws InvalidValueException {
        checkFloor("getFloor called with invalid floor", floorNumber);
        return floors.get(floorNumber - 1);
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

    public Person peekNextPerson(int floorNumber, Direction direction) throws InvalidValueException {
        Building.getInstance().checkFloor("Invalid floor number", floorNumber);
        if (direction == null) {
            throw new InvalidValueException("Direction cannot be null");
        }

        return getFloor(floorNumber).peekNextPerson(direction);
    }

    public void removePerson(int floorNumber, Person p) throws InvalidValueException {
        Building.getInstance().checkFloor("Invalid floor number", floorNumber);

        getFloor(floorNumber).removePerson(p);
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
