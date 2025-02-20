public class Circle 
{
    int x, y, radius;
    String name;

    public Circle(){}
    public Circle(String name, int x, int y, int radius) 
    {
        this.name = name;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public boolean contains(int mx, int my) 
    {
        int dx = mx - (x + radius);
        int dy = my - (y + radius);
        return dx * dx + dy * dy <= radius * radius;
    }
}