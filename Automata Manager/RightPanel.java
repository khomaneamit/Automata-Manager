import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

class RightPanel extends JPanel {
    private DFA struct = new DFA();
    private final Map<String, Circle> circles = new LinkedHashMap<>();
    private int counter = 0;
    private Circle selectedCircle = null;
    private int offsetX, offsetY;
    private JPopupMenu popupMenu;
    private boolean isDragging = false;

    public RightPanel() {
        setLayout(null);
        setBackground(Color.WHITE);

        // Create Popup Menu
        popupMenu = new JPopupMenu();
        JMenuItem setInitial = new JMenuItem("Set as Initial");
        JMenuItem setFinal = new JMenuItem("Set as Final");
        JMenuItem deleteState = new JMenuItem("Delete State");
        popupMenu.add(setInitial);
        popupMenu.add(setFinal);
        popupMenu.add(deleteState);

        // Set Initial State
        setInitial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedCircle != null) {
                    System.out.println("Setting " + selectedCircle.name + " as Initial State");
                    struct.setInitialState(selectedCircle);
                    repaint();
                }
            }
        });

        // Toggle Final State
        setFinal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedCircle != null) {
                    System.out.println("Toggling Final State for " + selectedCircle.name);
                    struct.toggleFinalState(selectedCircle);
                    repaint();
                }
            }
        });

        // Delete State
        deleteState.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedCircle != null) {
                    System.out.println("Deleting State " + selectedCircle.name);
                    Circle toDelete = selectedCircle;
                    selectedCircle = null;
                    struct.removeState(toDelete);
                    circles.remove(toDelete.name);
                    repaint();
                }
            }
        });

        // Mouse Listener for Clicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                isDragging = false;
                Circle circleAtPosition = null;
                
                // Find if a circle was clicked
                for (Circle circle : circles.values()) {
                    if (circle.contains(e.getX(), e.getY())) {
                        circleAtPosition = circle;
                        break;
                    }
                }
                
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (circleAtPosition != null) {
                        selectedCircle = circleAtPosition;
                        offsetX = e.getX() - circleAtPosition.x;
                        offsetY = e.getY() - circleAtPosition.y;
                        repaint();
                        // Show popup menu
                        popupMenu.show(RightPanel.this, e.getX(), e.getY());
                    }
                } else { // Left mouse button
                    selectedCircle = circleAtPosition;
                    if (selectedCircle != null) {
                        offsetX = e.getX() - selectedCircle.x;
                        offsetY = e.getY() - selectedCircle.y;
                        System.out.println("Selected Circle: " + selectedCircle.name);
                    }
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // Keep the selection after dragging to allow popup menu to work
                isDragging = false;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedCircle != null && !SwingUtilities.isRightMouseButton(e)) {
                    isDragging = true;
                    selectedCircle.x = e.getX() - offsetX;
                    selectedCircle.y = e.getY() - offsetY;
                    repaint();
                }
            }
        });
    }

    public void addCircle() {
        int x = 50 + (counter * 20) % 300;
        int y = 50 + (counter * 20) % 300;
        String name = "q" + counter;
        Circle newCircle = new Circle(name, x, y, 30);
        circles.put(name, newCircle);
        struct.addState(newCircle);
        counter++;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        for (Circle circle : circles.values()) {
            // Highlight selected circle
            if (circle == selectedCircle) {
                g.setColor(new Color(200, 220, 255)); // Light blue for selection
                g.fillOval(circle.x, circle.y, circle.radius * 2, circle.radius * 2);
            } else {
                g.setColor(Color.WHITE);
                g.fillOval(circle.x, circle.y, circle.radius * 2, circle.radius * 2);
            }
            
            g.setColor(Color.BLACK);
            g.drawOval(circle.x, circle.y, circle.radius * 2, circle.radius * 2);
            
            // Draw state name
            FontMetrics fm = g.getFontMetrics();
            int textWidth = fm.stringWidth(circle.name);
            g.drawString(circle.name, 
                       circle.x + circle.radius - (textWidth / 2),
                       circle.y + circle.radius + 5);

            // Highlight initial state
            if (circle == struct.getInitialState()) {
                g.setColor(Color.BLUE);
                // Draw arrow pointing to the circle
                int arrowX = circle.x - 20;
                int arrowY = circle.y + circle.radius;
                g.drawLine(arrowX, arrowY, circle.x, arrowY);
                g.drawLine(circle.x, arrowY, circle.x - 5, arrowY - 5);
                g.drawLine(circle.x, arrowY, circle.x - 5, arrowY + 5);
            }

            // Highlight final states with double circle
            if (struct.isFinalState(circle)) {
                g.setColor(Color.RED);
                g.drawOval(circle.x + 5, circle.y + 5, circle.radius * 2 - 10, circle.radius * 2 - 10);
            }
        }
    }
}