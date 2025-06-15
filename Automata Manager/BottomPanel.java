import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class BottomPanel extends JPanel{
    BottomPanel()
    {
        System.out.println("constructor called");
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(200, 300));
        setLayout(null);
        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        setBorder(border);
    }
}
