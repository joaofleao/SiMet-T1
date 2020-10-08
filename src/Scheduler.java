import java.util.ArrayList;

public class Scheduler {

    private ArrayList<Action> scheduler;

    public Scheduler() {
        scheduler = new ArrayList();
    }

    public void addAction(double time, int action) {
        Action item = new Action(time, action);

        scheduler.add(item);
    }

    public Action nextAction() {
        int j = 0;
        double smaller = Double.MAX_VALUE;
        for (int i = 0; i < scheduler.size(); i++) {
            if (scheduler.get(i).getTime() < smaller) {
                smaller = scheduler.get(i).getTime();
                j = i;
            }
        }
        return scheduler.remove(j);
    }

    public String toString() {
        String text = "";

        for (int i = 0; i < scheduler.size(); i++) {
            text = text + scheduler.get(i) + ", ";
        }

        return ("[" + text + "]");
    }
}
