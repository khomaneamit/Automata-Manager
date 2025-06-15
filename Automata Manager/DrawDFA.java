import javax.swing.*;
import java.awt.*;

class DrawDFA extends JPanel {
    JButton addStateButton, addTransitionButton, runDFAButton;
    LeftPanel left;
    RightPanel right;
    BottomPanel runDFA;

    public DrawDFA() {
        JFrame frame = new JFrame("AUTOMATA MANAGER");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        frame.setLayout(new BorderLayout());

        left = new LeftPanel();
        frame.add(left, BorderLayout.WEST);

        right = new RightPanel();
        frame.add(right, BorderLayout.CENTER);

        runDFA = new BottomPanel();
        frame.add(runDFA, BorderLayout.SOUTH);

        left.addStateButton.addActionListener(e -> right.addCircle());
        left.addTransitionButton.addActionListener(e -> right.addTransition());
        left.runDFAButton.addActionListener(e -> runDFA.validateDFA(right.getTransitions(), right.getDFA()));
    }

    public static void main(String args[])
    {
        new DrawDFA();
    }
}