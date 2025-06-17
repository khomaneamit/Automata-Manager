import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class BottomPanel extends JPanel{

    //for add transition
    private List<Transition> transitions = new ArrayList<>();
    private DFA struct = new DFA();
    private JTable table;
    private JScrollPane scrollPane;

    private JTextField inputField;
    private JLabel currentCharLabel;
    private JTable logTable;
    private DefaultTableModel logTableModel;
    private String inputString = "";
    private int currentCharIndex = 0;

    private String currentState = "q0"; // or initial DFA state
    private char currentSymbol = '\0';

    private Transition lastExecutedTransition = null;

    BottomPanel()
    {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(200, 300));
        setLayout(new GridLayout(1,3));
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        setBorder(border);
    }

    private void updateCurrentCharLabel() 
    {
        if(inputString.isEmpty() || currentCharIndex >= inputString.length()) 
        {
            currentCharLabel.setText("Current Symbol: ␣");
        } 
        else 
        {
            currentCharLabel.setText("Current Symbol: " + inputString.charAt(currentCharIndex));
        }
    }    

    public void addInputSection() 
    {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Test DFA"));

        inputField = new JTextField(10);
        JButton submitBtn = new JButton("Submit");
        JButton nextBtn = new JButton("Next");
        JButton prevBtn = new JButton("Previous");

        inputPanel.add(new JLabel("Enter Input String:"));
        inputPanel.add(inputField);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(submitBtn);
        inputPanel.add(Box.createVerticalStrut(10));
        inputPanel.add(prevBtn);
        inputPanel.add(nextBtn);

        add(inputPanel);

        submitBtn.addActionListener(e -> {
            highlightTransitionInTable("", '\0'); // Resets highlights
            inputString = inputField.getText().trim();
            currentCharIndex = 0;
            logTableModel.setRowCount(0);
            updateCurrentCharLabel();
        });

        nextBtn.addActionListener(e -> {
            if(currentCharIndex < inputString.length()) 
            {
                currentSymbol = inputString.charAt(currentCharIndex);
                String nextState = getNextState(currentState, currentSymbol);
                logTableModel.addRow(new Object[]{currentCharIndex + 1, currentState, currentSymbol, nextState});
                currentState = nextState;
                highlightTransitionInTable(currentState, currentSymbol);
                currentCharIndex++;
                updateCurrentCharLabel();

            }
        });

        prevBtn.addActionListener(e -> {
            if(currentCharIndex > 0) 
            {
                highlightTransitionInTable("", '\0'); // Resets highlights
                currentCharIndex--;
                logTableModel.setRowCount(currentCharIndex);
                updateCurrentCharLabel();
            }
        });
    }

    private String getNextState(String fromState, char symbol) 
    {
        for(Transition t : transitions) 
        {
            if(t.from.name.equals(fromState) && t.symbol.equals(String.valueOf(symbol))) 
            {
                lastExecutedTransition = t;
                repaint(); // trigger repaint to highlight
                return t.to.name;
            }
        }
        JOptionPane.showMessageDialog(null,"No transition found from state " + fromState + " with symbol '" + symbol + "'","DFA Execution Error",JOptionPane.ERROR_MESSAGE);
        return null;
    }
    
    private void highlightTransitionInTable(String fromState, char symbol) 
    {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() 
        {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) 
            {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String from = (String) table.getValueAt(row, 0);
                String sym = (String) table.getValueAt(row, 1);
                if(from.equals(fromState) && sym.equals(String.valueOf(symbol))) 
                {
                    cell.setBackground(Color.YELLOW);
                } 
                else 
                {
                    cell.setBackground(Color.WHITE);
                }
                return cell;
            }
        });
        table.repaint();
    }

    public void addLogTableSection() 
    {
        JPanel logPanel = new JPanel(new BorderLayout());
        logPanel.setBorder(BorderFactory.createTitledBorder("Log Table"));
        currentCharLabel = new JLabel("Current Symbol: ");
        logPanel.add(currentCharLabel, BorderLayout.NORTH);
        logTableModel = new DefaultTableModel(new String[]{"Step", "Current State", "Input", "Next State"}, 0);
        logTable = new JTable(logTableModel);
        logPanel.add(new JScrollPane(logTable), BorderLayout.CENTER);
        add(logPanel);
    }    

    public void showTransitionTable() 
    {
        removeAll();
        String[] columns = {"From State", "Input Symbol", "To State"};
        String[][] data = new String[transitions.size()][3];
        for(int i = 0; i < transitions.size(); i++) 
        {
            Transition t = transitions.get(i);
            data[i][0] = t.from.name;
            data[i][1] = t.symbol;
            data[i][2] = t.to.name;
        }

        table = new JTable(data, columns) 
        {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) 
            {
                Component comp = super.prepareRenderer(renderer, row, column);
                String from = (String) getValueAt(row, 0);
                String symbol = (String) getValueAt(row, 1);
                String to = (String) getValueAt(row, 2);

                if(lastExecutedTransition != null && lastExecutedTransition.from.name.equals(from) && lastExecutedTransition.symbol.equals(symbol) && lastExecutedTransition.to.name.equals(to)) {
                    comp.setBackground(Color.YELLOW);
                } 
                else 
                {
                    comp.setBackground(Color.WHITE);
                }
                return comp;
            }
        };

        table.setEnabled(false);
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public boolean validateDFA(List<Transition> transitions, DFA struct) 
    {
        this.struct = struct;
        this.transitions = transitions;
        Set<String> allSymbols = new HashSet<>();            
        Set<Circle> allStates = new HashSet<>();               
        Map<Circle, Set<String>> stateToSymbols = new HashMap<>(); 
    
        // Step 1: Collect symbols and states
        for(Transition t : transitions) 
        {
            allSymbols.add(t.symbol);
            allStates.add(t.from);
            allStates.add(t.to);
    
            // Step 2: Track used symbols per source state
            Set<String> used = stateToSymbols.getOrDefault(t.from, new HashSet<>());
            if(used.contains(t.symbol)) 
            {
                JOptionPane.showMessageDialog(null,"Duplicate transition for symbol '" + t.symbol + "' from state " + t.from.name,"DFA Validation Error",JOptionPane.ERROR_MESSAGE);
                return false;
            }
            used.add(t.symbol);
            stateToSymbols.put(t.from, used);
        }
    
        // Step 3: Check that all states have a transition for every symbol
        for(Circle state : allStates) 
        {
            Set<String> usedSymbols = stateToSymbols.getOrDefault(state, new HashSet<>());
            if(!usedSymbols.containsAll(allSymbols)) 
            {
                Set<String> missing = new HashSet<>(allSymbols);
                missing.removeAll(usedSymbols);
                JOptionPane.showMessageDialog(null,"State " + state.name + " is missing transitions for symbols: " + missing,"DFA Validation Error",JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        if(struct.getInitialState() == null)
        {
            JOptionPane.showMessageDialog(null,"not set initial state","DFA Validation Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if(struct.getFinalStates().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"not set final state","DFA Validation Error",JOptionPane.ERROR_MESSAGE);
            return false;
        }

        currentState = struct.getInitialState().name;
        showTransitionTable();
        addInputSection();
        addLogTableSection();
        return true;
    }
}
