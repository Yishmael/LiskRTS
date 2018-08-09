package gameplay;

public class Requirement {
    private Object requirement;
    private boolean met;

    public Requirement(Object requirement) {
        this.requirement = requirement;
    }

    public boolean isMet() {
        return met;
    }

    public void setMet(boolean met) {
        this.met = met;
    }

    public Object getRequirement() {
        return requirement;
    }

}
