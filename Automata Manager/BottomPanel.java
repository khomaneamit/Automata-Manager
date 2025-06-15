import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class BottomPanel extends JPanel{

    //for add transition
    private final List<Transition> transitions = new ArrayList<>();
    private DFA struct = new DFA();

    BottomPanel()
    {
        System.out.println("constructor called");
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(200, 300));
        setLayout(null);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        setBorder(border);
    }

    public boolean validateDFA(List<Transition> transitions, DFA struct) {
        struct = this.struct;
        transitions = this.transitions;
        Set<String> allSymbols = new HashSet<>();              // All input symbols
        Set<Circle> allStates = new HashSet<>();               // All states (from + to)
        Map<Circle, Set<String>> stateToSymbols = new HashMap<>();  // Symbols per state
    
        // Step 1: Collect symbols and states
        for (Transition t : transitions) {
            allSymbols.add(t.symbol);
            allStates.add(t.from);
            allStates.add(t.to);
    
            // Step 2: Track used symbols per source state
            Set<String> used = stateToSymbols.getOrDefault(t.from, new HashSet<>());
            if (used.contains(t.symbol)) {
                JOptionPane.showMessageDialog(null,
                    "❌ Duplicate transition for symbol '" + t.symbol + "' from state " + t.from.name,
                    "DFA Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            used.add(t.symbol);
            stateToSymbols.put(t.from, used);
        }
    
        // Step 3: Check that all states have a transition for every symbol
        for (Circle state : allStates) {
            Set<String> usedSymbols = stateToSymbols.getOrDefault(state, new HashSet<>());
            if (!usedSymbols.containsAll(allSymbols)) {
                Set<String> missing = new HashSet<>(allSymbols);
                missing.removeAll(usedSymbols);
                JOptionPane.showMessageDialog(null,
                    "❌ State " + state.name + " is missing transitions for symbols: " + missing,
                    "DFA Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        if(struct.getInitialState() == null){
            JOptionPane.showMessageDialog(null,
                    "❌ not set initial state",
                    "DFA Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(struct.getFinalStates().isEmpty()){
            JOptionPane.showMessageDialog(null,
                    "❌ not set final state",
                    "DFA Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
