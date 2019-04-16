package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorJsonConfigurationTest {
    String configurationFilePath = "./configuration/json/20_floors_4_elevators.json";

    @Test
    void throwsExceptionIfConfigurationNotFound() {
        String filenameThatDoesNotExist = "fazbaz.record";
        assertThrows(ConfigurationException.class, () -> {
            new ElevatorJsonConfiguration(filenameThatDoesNotExist);
        });
    }

    @Test
    void getElevatorSpeedInMilliseconds() throws ConfigurationException {
        ElevatorJsonConfiguration e = new ElevatorJsonConfiguration(configurationFilePath);
        assertEquals(e.getElevatorSpeedInMilliseconds(), 1000);
    }

    @Test
    void getElevatorCapacity() throws ConfigurationException {
            ElevatorJsonConfiguration e = new ElevatorJsonConfiguration(configurationFilePath);
            assertEquals(e.getElevatorCapacity(), 15);
    }

    @Test
    void getReturnToFirstFloorAfter() throws ConfigurationException {
            ElevatorJsonConfiguration e = new ElevatorJsonConfiguration(configurationFilePath);
            assertEquals(e.getReturnToFirstFloorAfter(), 60000);
    }

    @Test
    void getDoorOpenTime() throws ConfigurationException {
        ElevatorJsonConfiguration e = new ElevatorJsonConfiguration(configurationFilePath);
        assertEquals(e.getDoorOpenTime(), 3000);
    }
}