package models;

import java.util.ArrayList;

import configuration.BuildingConfigurable;
import configuration.ConfigurationException;
import configuration.ElevatorConfigurationFactory;
import gui.ElevatorDisplay;

public class Building {
    private ArrayList<Floor> floors;
    private static Building instance;
    private static BuildingConfigurable buildingConfiguration = Building.initialize();

    private static BuildingConfigurable initialize() {
        try {
            return ElevatorConfigurationFactory.build("json", "configuration/json/20_floors_4_elevators.json");
        } catch (ConfigurationException ex) {
            System.out.println("Building created with invalid configuration, exiting program");
            ex.printStackTrace();
            System.exit(1);
        }

        return null;
    }

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
        int numberOfFloors = buildingConfiguration.getNumberOfFloors();
        floors = new ArrayList<>();
        for (int i = 0; i < numberOfFloors; i++) {
            floors.add(new Floor(i + 1));
        }
    }

    public Floor getFloor(int floorNumber) throws InvalidValueException {
        checkFloor("getFloor called with invalid floor", floorNumber);
        return floors.get(floorNumber - 1);
    }

    public int getNumberOfFloors() {
        return buildingConfiguration.getNumberOfFloors();
    }

    public int getNumberOfElevators() {
        return buildingConfiguration.getNumberOfElevators();
    }

    public int getElevatorCapacity() {
        return buildingConfiguration.getElevatorCapacity();
    }

    public int getElevatorSpeed() {
        return buildingConfiguration.getElevatorSpeed();
    }

    public int getDoorOpenTime() {
        return buildingConfiguration.getDoorOpenTime();
    }

    public int getReturnToDefaultFloorTimeout() {
        return buildingConfiguration.getReturnToDefaultFloorTimeout();
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

    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("Building Report \n");

        for (Floor f : floors) {
            output.append(f.toString());
        }

        return output.toString();
    }
}
