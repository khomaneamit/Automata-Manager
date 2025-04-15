import javax.swing.*;
import java.awt.*;

class DrawDFA extends JPanel {
    JButton addStateButton, addTransitionButton, runDFAButton;
    LeftPanel left;
    RightPanel right;

    public DrawDFA() {
        this.setLayout(new BorderLayout());

        left = new LeftPanel();
        this.add(left, BorderLayout.WEST);

        right = new RightPanel();
        this.add(right, BorderLayout.CENTER);

        left.addStateButton.addActionListener(e -> right.addCircle());
        left.addTransitionButton.addActionListener(e -> right.addTransition());
    }
}