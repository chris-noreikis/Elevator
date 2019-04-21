package SimulationConfiguration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonConfigurationTest {
    String configurationFilePath = "./configuration/json/20_floors_4_elevators.json";

    @Test
    void throwsExceptionIfConfigurationNotFound() {
        String filenameThatDoesNotExist = "fazbaz.record";
        assertThrows(ConfigurationException.class, () -> {
            new JsonConfiguration(filenameThatDoesNotExist);
        });
    }

    @Test
    void getElevatorSpeedInMilliseconds() throws ConfigurationException {
        JsonConfiguration e = new JsonConfiguration(configurationFilePath);
        assertEquals(e.getElevatorSpeedInMilliseconds(), 1000);
    }

    @Test
    void getElevatorCapacity() throws ConfigurationException {
            JsonConfiguration e = new JsonConfiguration(configurationFilePath);
            assertEquals(e.getElevatorCapacity(), 15);
    }

    @Test
    void getReturnToFirstFloorAfter() throws ConfigurationException {
            JsonConfiguration e = new JsonConfiguration(configurationFilePath);
            assertEquals(e.getReturnToFirstFloorAfter(), 60000);
    }

    @Test
    void getDoorOpenTime() throws ConfigurationException {
        JsonConfiguration e = new JsonConfiguration(configurationFilePath);
        assertEquals(e.getDoorOpenTime(), 3000);
    }
}