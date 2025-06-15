import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.QuadCurve2D;
import java.util.*;

class RightPanel extends JPanel {
    private DFA struct = new DFA();
    private final Map<String, Circle> circles = new LinkedHashMap<>();
    private int counter = 0;
    private Circle selectedCircle = null;
    private int offsetX, offsetY;
    private JPopupMenu popupMenu;
    
    //for add transition
    HashMap<Circle, HashMap<String, Circle>> transition = new HashMap<>();

    public RightPanel() {
        setLayout(null);
        setBackground(Color.WHITE);

        // Create Popup Menu for States
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
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedCircle != null && !SwingUtilities.isRightMouseButton(e)) {
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

    public void addTransition() {
        String str = struct.getallStates();
        str = str.substring(1, str.length()-1);
        StringBuffer temp = new StringBuffer();
        for(int i=0;i<str.length();i++)
        {
            if(str.charAt(i) != 32)
            {
                temp.append(str.charAt(i));
            }
        }
        str = temp.toString();

        JComboBox<String> inputStateBox = new JComboBox<>(str.split(","));
        JComboBox<String> outputStateBox = new JComboBox<>(str.split(","));
        JTextField inputSymbolfield = new JTextField();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Select input state:"));
        panel.add(inputStateBox);
        panel.add(new JLabel("Enter input Symbol:"));
        panel.add(inputSymbolfield);
        panel.add(new JLabel("Select output state:"));
        panel.add(outputStateBox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Define Transition", JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);
        if(result == JOptionPane.OK_OPTION){
            String inputState = (String) inputStateBox.getSelectedItem();
            String inputSymbol = inputSymbolfield.getText();
            String outputState = (String) outputStateBox.getSelectedItem();

            System.out.println(outputState);

            Circle inputCircle = null;
            for (Circle circle : circles.values()) {
                if (inputState.equals(circle.name)) {
                    inputCircle = circle;
                    break;
                }
            }


            Circle outputCircle = null;
            for (Circle circle : circles.values()) {
                if (outputState.equals(circle.name)) {
                    outputCircle = circle;
                    break;
                }
            }

            HashMap<String, Circle> t1 = transition.getOrDefault(inputCircle, new HashMap<String, Circle>());
            t1.put(inputSymbol, outputCircle);
            transition.put(inputCircle, t1);

            t1 = transition.get(inputCircle);
            outputCircle = t1.get(inputSymbol);

            System.out.println(inputCircle.name+" "+inputSymbol+" "+outputCircle.name);
            repaint();
        }
        
    }

    private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2, int c1, int c2, String label) 
    {
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.black);

        //System.out.println("x1: "+x1+" x2: "+x2+" y1: "+y1+" y2: "+y2+" c1: "+c1+" c2: "+c2);
        QuadCurve2D curve = new QuadCurve2D.Float();
        curve.setCurve(x1,y1,c1,c2,x2,y2);
        g2d.draw(curve);

        double t = 0.5; // Point at middle of curve
        double x = (1 - t) * (1 - t) * x1 + 2 * (1 - t) * t * c1 + t * t * x2;
        double y = (1 - t) * (1 - t) * y1 + 2 * (1 - t) * t * c2 + t * t * y2;

        double dx = x2 - c1;
        double dy = y2 - c2;
        double angle = Math.atan2(dy, dx);

        int len = 10;
        int[] xPoints = {
            (int) (x),
            (int) (x - len * Math.cos(angle - Math.PI / 6)),
            (int) (x - len * Math.cos(angle + Math.PI / 6))
        };
        int[] yPoints = {
            (int) (y),
            (int) (y - len * Math.sin(angle - Math.PI / 6)),
            (int) (y - len * Math.sin(angle + Math.PI / 6))
        };
        g2d.fillPolygon(xPoints, yPoints, 3);

        // Draw label
        g2d.setColor(Color.BLACK);
        g2d.drawString(label, c1 - 5, c2 - 5);

        // Optional: draw control point
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillOval(c1 - 5, c2 - 5, 10, 10);
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw transitions (arrows)
        g.setColor(Color.BLACK);
        for (Circle fromCircle : transition.keySet()) {
            HashMap<String, Circle> transitionsForState = transition.get(fromCircle);
        
            for (String symbol : transitionsForState.keySet()) {
                Circle toCircle = transitionsForState.get(symbol);
            
                //System.out.println("from circle : x = "+fromCircle.x+" , y = "+fromCircle.y+" , r = "+fromCircle.radius);
                //System.out.println("toCircle circle : x = "+toCircle.x+" , y = "+toCircle.y+" , r = "+toCircle.radius);
                int fromX = fromCircle.x + fromCircle.radius;
                int fromY = fromCircle.y + fromCircle.radius;
                int toX = toCircle.x + toCircle.radius;
                int toY = toCircle.y + toCircle.radius;
            
                // Draw arrow line
                drawArrow(g2d, fromX, fromY, toX, toY, (toX+fromX)/2, (toY+fromY)/2, symbol);
                if(fromCircle.equals(toCircle))
                {
                    drawArrow(g2d, fromX, fromY, toX, toY, toX-100, toY-100, symbol);
                }
            }
        }

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