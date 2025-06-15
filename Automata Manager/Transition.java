import java.awt.Point;

class Transition {
    Circle from;
    String symbol;
    Circle to;
    Point controlPoint;

    public Transition(Circle from, String symbol, Circle to, Point controlPoint) {
        this.from = from;
        this.symbol = symbol;
        this.to = to;
        this.controlPoint = controlPoint;
    }
    
    public boolean isNearControlPoint(int x, int y) {
        return controlPoint != null && controlPoint.distance(x, y) < 10;
    }
}