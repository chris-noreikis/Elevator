package reports;

public abstract class Action {
    private long creationTimestamp;

    Action() {
        this.creationTimestamp = System.currentTimeMillis();
    }

    abstract public String getFormattedAction();

    public long getCreationTimestamp() { return creationTimestamp; }

    public String getFormattedAction(long startTimestamp) {
        long elapsedTimeSinceFirstAction = creationTimestamp - startTimestamp;
        int millisecondsInHour = 3600000;
        int millisecondsInMinute = 60000;
        int millisecondsInSecond = 1000;
        long hours = elapsedTimeSinceFirstAction / millisecondsInHour;
        long minutes = (elapsedTimeSinceFirstAction / millisecondsInMinute) % 60;
        long seconds = (elapsedTimeSinceFirstAction / millisecondsInSecond) % 60;
        String formattedeTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return String.format("%s %s", formattedeTime, this.getFormattedAction());
    }
}
