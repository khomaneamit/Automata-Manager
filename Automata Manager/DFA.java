import java.util.*;

public class DFA {
    private Circle initialState = null;
    private HashSet<Circle> states = new HashSet<>();
    private ArrayList<String> statenames = new ArrayList<>();
    private HashSet<Circle> finalStates = new HashSet<>();

    public void addState(Circle state) {
        states.add(state);
        statenames.add(state.name);
    }

    public void removeState(Circle state) {
        states.remove(state);
        finalStates.remove(state);
        if (initialState == state) {
            initialState = null;
        }
        statenames.remove(state.name);
    }

    public void setInitialState(Circle state) {
        if (initialState == state) {
            initialState = null; // Toggle back to non-initial
        } else {
            initialState = state;
        }
    }

    public Circle getInitialState() {
        return initialState;
    }

    public void toggleFinalState(Circle state) {
        if (finalStates.contains(state)) {
            finalStates.remove(state);
        } else {
            finalStates.add(state);
        }
    }

    public boolean isFinalState(Circle state) {
        return finalStates.contains(state);
    }

    public String getallStates(){
        return statenames.toString();
    }
}
