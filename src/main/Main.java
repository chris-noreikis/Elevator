
package main;

import SimulationConfiguration.BuildingConfigurable;
import SimulationConfiguration.ConfigurationException;
import SimulationConfiguration.ElevatorConfigurationFactory;
import models.Building;

public class Main {
    static int personCounter = 0;

    public static void main(String[] args) throws InterruptedException {
        try {
            String type = args[0];
            String filepath = args[1];
            BuildingConfigurable bc = ElevatorConfigurationFactory.build(type, filepath);
            Building.initialize(bc);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("Program requires two arguments: type [json], filepath [any valid filepath]");
            System.exit(1);
        } catch (ConfigurationException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        ElevatorTestDriver d = new ElevatorTestDriver();
        d.runTests();
    }
}
