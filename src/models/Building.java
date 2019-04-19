package models;

import java.util.ArrayList;

import gui.ElevatorDisplay;

public class Building {
    private ArrayList<Floor> floors;
    private static Building instance;
    private BuildingConfigurable buildingConfiguration;

    public static Building getInstance()
    {
        if (instance == null) {
            String configurationFilePath = "./configuration/json/20_floors_4_elevators.json";
            instance = new Building(configurationFilePath);
        }
        return instance;
    }


    private Building(String configurationFilePath) {
        try {
            buildingConfiguration = new ElevatorJsonConfiguration(configurationFilePath);

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

    public int getNumElevators() { return buildingConfiguration.getNumberOfElevators(); }

    public void addPerson(Person p, int floorNum) {
        getFloor(floorNum - 1).addWaitingPerson(p);
    }
}
