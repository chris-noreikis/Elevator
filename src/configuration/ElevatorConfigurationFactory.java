package configuration;

public class ElevatorConfigurationFactory {
    public static SimulationDefinable build(String type, String filepath) throws ConfigurationException {
        switch (type) {
            case "json":
                return new JsonConfiguration(filepath);
            default:
                return null;
        }
    }
}
