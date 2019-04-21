package models;

import java.util.ArrayList;

import SimulationConfiguration.BuildingConfigurable;
import SimulationConfiguration.ConfigurationException;
import SimulationConfiguration.ElevatorConfigurationFactory;
import gui.ElevatorDisplay;

public class Building {
    private ArrayList<Floor> floors;
    private static Building instance;
    private BuildingConfigurable buildingConfiguration;

    public static void initialize(BuildingConfigurable bc) throws ConfigurationException {
        if (instance == null) {
            instance = new Building(bc);
        } else {
            throw new ConfigurationException("Cannot call initialize on a building that has already been created");
        }
    }

    public static Building getInstance() {
        if (instance == null) {
            BuildingConfigurable bc = ElevatorConfigurationFactory.build();
            instance = new Building(bc);
        }
        return instance;
    }

    private Building(BuildingConfigurable buildingConfigurationIn) {
        try {
            buildingConfiguration = buildingConfigurationIn;

            ElevatorDisplay.getInstance().initialize(buildingConfiguration.getNumberOfFloors());

            setupFloors();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void setupFloors() {
        int numberOfFloors = buildingConfiguration.getNumberOfFloors();
        floors = new ArrayList<>();
        for (int i = 0; i < numberOfFloors; i++) {
            floors.add(new Floor(i + 1));
        }
    }

    public int getNumberOfFloors() {
        return floors.size();
    }

    public Floor getFloor(int floorNumberZeroIndexed) {
        return floors.get(floorNumberZeroIndexed);
    }

    public int getNumElevators() {
        return buildingConfiguration.getNumberOfElevators();
    }

    public void addPerson(Person p, int floorNum) {
        getFloor(floorNum - 1).addWaitingPerson(p);
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
