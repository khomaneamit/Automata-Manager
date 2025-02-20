import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class Main
{
    public static void main(String args[])
    {
        JFrame frame = new JFrame("AUTOMATA MANAGER");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu drawDFA = new JMenu("Draw DFA");
        Font menuFont = new Font("Arial", Font.BOLD, 16); 
        drawDFA.setFont(menuFont);
        menuBar.add(drawDFA);
        frame.setJMenuBar(menuBar);

        drawDFA.addMenuListener(new MenuListener() 
        {
            public void menuCanceled(MenuEvent arg0) {}
            public void menuDeselected(MenuEvent arg0) {}

            public void menuSelected(MenuEvent arg0) 
            {
                frame.getContentPane().removeAll();
                frame.getContentPane().add(new DrawDFA());
                frame.revalidate();
                frame.repaint();
            }
        });
    }
}