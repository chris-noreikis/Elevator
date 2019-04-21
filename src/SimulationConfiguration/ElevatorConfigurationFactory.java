package SimulationConfiguration;

public class ElevatorConfigurationFactory {
    public static BuildingConfigurable build(String type, String filepath) throws ConfigurationException {
        switch (type) {
            case "json":
                return new JsonConfiguration(filepath);
            default:
                return new NullObjectConfiguration();
        }
    }

    public static BuildingConfigurable build() {
        return new NullObjectConfiguration();
    }
}
