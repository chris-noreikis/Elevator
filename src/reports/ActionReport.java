package reports;

import java.util.ArrayList;

public class ActionReport {
    private ArrayList<Action> actions;
    private boolean initalized = false;
    private static final ActionReport instance = new ActionReport();

    public static ActionReport getInstance() {
        return instance;
    }

    public ActionReport() {
        actions = new ArrayList<>();
    }

    public void addAction(Action action) {
        actions.add(action);
        Action firstAction = actions.get(0);
        System.out.println(action.getFormattedAction(firstAction.getCreationTimestamp()));
    }
}
