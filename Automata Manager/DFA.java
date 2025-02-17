import java.util.*;

class DFA{
    Set<String> states;
    Set<Character> alphabet;
    Map<String, Map<Character, String>> transitions;
    String startState;
    Set<String> finalStates;
    public DFA(Set<String> states, Set<Character> alphabet, Map<String, Map<Character, String>> transitions,
            String startState, Set<String> finalStates) {
        this.states = states;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.startState = startState;
        this.finalStates = finalStates;
    }
}