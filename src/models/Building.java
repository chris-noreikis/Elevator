package models;

import java.util.ArrayList;

import SimulationConfiguration.BuildingConfigurable;
import SimulationConfiguration.ConfigurationException;
import SimulationConfiguration.ElevatorConfigurationFactory;
import exceptions.InvalidValueException;
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

    public static Building getInstance() {
        if (instance == null) {
            instance = new Building();
        }
        return instance;
    }

    private Building() {
        ElevatorDisplay.getInstance().initialize(getNumberOfFloors());
        setupFloors();
    }

    private void setupFloors() {
        int numberOfFloors = buildingConfiguration.getNumberOfFloors();
        floors = new ArrayList<>();
        for (int i = 0; i < numberOfFloors; i++) {
            floors.add(new Floor(i + 1));
        }
    }

    public Floor getFloor(int floorNumber) throws InvalidValueException {
    	if(floorNumber <= 0 || floorNumber > this.getNumberOfFloors()) {
    		throw new InvalidValueException("Floor number is not a valid floor");
    	}
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
        return buildingConfiguration.getElevatorSpeedInMilliseconds();
    }

    public int getDoorOpenTime() {
        return buildingConfiguration.getDoorOpenTime();
    }

    public int getReturnToDefaultFloorTimeout() {
        return buildingConfiguration.getReturnToDefaultFloorTimeout();
    }

    public void addPerson(Person p, int floorNumber) throws InvalidValueException {
        if (p.getStartFloor() < 1 || p.getEndFloor() > getNumberOfFloors()) {
            throw new InvalidValueException("Person is trying to go to a floor that doesn\'t exist: " + p.getEndFloor());
        }
        if (p.getEndFloor() > Building.getInstance().getNumberOfFloors() || p.getEndFloor() < 1) {
            throw new InvalidValueException("Person is trying to go to a floor that doesn\'t exist: " + p.getEndFloor());
        }
        getFloor(floorNumber).addWaitingPerson(p);
    }

    public void resetState() {
        for (Floor f : floors) {
            f.resetState();
        }
    }

    public void validateFloor(String errorMessage, int floorNumber) throws InvalidValueException {
        if (floorNumber < 1 || floorNumber > getNumberOfFloors()) {
            int maxFloor = getNumberOfFloors();
            String errorMessageFormatted = String.format("%s; expected 1-%d, got %d", errorMessage, maxFloor, floorNumber);
            throw new InvalidValueException(errorMessageFormatted);
        }
    }

    public String toString() {
        String output = "";
        output += "Building Report ...\n";

        for (Floor f : floors) {
            output += f.toString();
        }

        return output;
    }
}
