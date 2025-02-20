import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class RightPanel extends JPanel 
{
    private final Map<String, Circle> circles = new LinkedHashMap<>();
    private int counter = 0;
    private Circle selectedCircle = null;
    private int offsetX, offsetY;

    public RightPanel() 
    {
        this.setLayout(null);
        setBackground(Color.WHITE);

        addMouseListener(new MouseAdapter() 
        {
            public void mousePressed(MouseEvent e) 
            {
                for(Circle circle : circles.values()) 
                {
                    if(circle.contains(e.getX(), e.getY())) 
                    {
                        selectedCircle = circle;
                        offsetX = e.getX() - selectedCircle.x;
                        offsetY = e.getY() - selectedCircle.y;
                        repaint();
                        return;
                    }
                }
            }

            public void mouseReleased(MouseEvent e) 
            {
                selectedCircle = null;
            }
        });

        addMouseMotionListener(new MouseAdapter() 
        {
            public void mouseDragged(MouseEvent e) 
            {
                if(selectedCircle != null) 
                {
                    selectedCircle.x = e.getX() - offsetX;
                    selectedCircle.y = e.getY() - offsetY;
                    repaint();
                }
            }
        });
    }

    public void addCircle() 
    {
        int x = 50 + (counter * 20) % 300;
        int y = 50 + (counter * 20) % 300;
        String name = "q" + counter;
        circles.put(name, new Circle(name, x, y, 30));
        counter++;
        repaint();
    }

    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);

        for(Circle circle : circles.values()) 
        {
            g.setColor(Color.WHITE);
            g.fillOval(circle.x, circle.y, circle.radius * 2, circle.radius * 2);
            g.setColor(Color.BLACK);
            g.drawOval(circle.x, circle.y, circle.radius * 2, circle.radius * 2);
            g.drawString(circle.name, circle.x + circle.radius, circle.y + circle.radius);
        }
    }
}