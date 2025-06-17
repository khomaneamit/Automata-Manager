import java.awt.BorderLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;

public class BottomPanel extends JPanel{

    //for add transition
    private List<Transition> transitions = new ArrayList<>();
    private DFA struct = new DFA();
    private JTable table;
    private JScrollPane scrollPane;

    BottomPanel()
    {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(200, 300));
        setLayout(new BorderLayout());
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        setBorder(border);
    }

    public void showTransitionTable() {
        removeAll(); // Clear previous components like validation result or table

        String[] columns = {"From State", "Input Symbol", "To State"};
        String[][] data = new String[transitions.size()][3];

        for (int i = 0; i < transitions.size(); i++) {
            Transition t = transitions.get(i);
            data[i][0] = t.from.name;
            data[i][1] = t.symbol;
            data[i][2] = t.to.name;
        }

        table = new JTable(data, columns);
        table.setEnabled(false); // read-only

        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.EAST);

        revalidate();
        repaint();
    }


    public boolean validateDFA(List<Transition> transitions, DFA struct) {
        this.struct = struct;
        this.transitions = transitions;
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

        showTransitionTable();
        return true;
    }
}
