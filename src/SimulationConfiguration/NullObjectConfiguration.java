package SimulationConfiguration;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class NullObjectConfiguration implements BuildingConfigurable {
    public int getElevatorSpeedInMilliseconds() {
        return 1000;
    }

    public int getElevatorCapacity() {
        return 15;
    }

    public int getReturnToFirstFloorAfter() {
        return 60000;
    }

    public int getDoorOpenTime() {
        return 1000;
    }

    public int getNumberOfElevators() { return 4; }

    public int getNumberOfFloors() { return 20; }
}