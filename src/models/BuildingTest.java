package models;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BuildingTest {
    @BeforeAll
    static void setIsUnitTest() {
        Building.isUnitTest = true;
    }

    String configurationFilePath = "./configuration/json/20_floors_4_elevators.json";

    @Test
    void canInitializeFloors() {
        Building b = new Building(configurationFilePath);
        assertEquals(b.getNumberOfFloors(), 20);
    }

    @Test
    void canInitializeElevators() {
        Building b = new Building(configurationFilePath);
        assertEquals(b.getNumberOfElevators(), 4);
    }
}