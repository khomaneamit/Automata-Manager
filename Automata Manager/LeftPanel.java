import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

class LeftPanel extends JPanel
{
    JButton addStateButton, addTransitionButton, runDFAButton;
    public LeftPanel()
    {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(200, 100));
        setLayout(null);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        setBorder(border);

        addStateButton = new JButton("Add State");
        add(addStateButton);
        addStateButton.setBounds(20, 50, 150, 30);

        addTransitionButton = new JButton("Add Transition");
        add(addTransitionButton);
        addTransitionButton.setBounds(20, 130, 150, 30);

        runDFAButton = new JButton("Run DFA");
        add(runDFAButton);
        runDFAButton.setBounds(20, 210, 150, 30);
    }
}