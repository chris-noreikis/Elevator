package models;

public class ElevatorLogger {
    private static ElevatorLogger instance;
    private long creationTimestamp;

    public static ElevatorLogger getInstance() {
        if (instance == null) {
            instance = new ElevatorLogger();
        }
        return instance;
    }

    private ElevatorLogger() {
        creationTimestamp = System.currentTimeMillis();
    }

    public void logAction(String action) throws InvalidValueException {
        if (action == null) {
            throw new InvalidValueException("action cannot be null");
        }

        String formattedTime = getElapsedTime();
        System.out.println(String.format("%s %s", formattedTime, action));
    }

    private String getElapsedTime() {
        long elapsedTimeSinceFirstAction = System.currentTimeMillis() - creationTimestamp;
        int millisecondsInHour = 3600000;
        int millisecondsInMinute = 60000;
        int millisecondsInSecond = 1000;
        long hours = elapsedTimeSinceFirstAction / millisecondsInHour;
        long minutes = (elapsedTimeSinceFirstAction / millisecondsInMinute) % 60;
        long seconds = (elapsedTimeSinceFirstAction / millisecondsInSecond) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
