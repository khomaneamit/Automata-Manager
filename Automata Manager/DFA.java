import java.util.*;

public class DFA 
{
    private Object[] obj;
    public DFA()
    {
        obj = new Object[5];
        obj[0] = new HashSet<Circle>();
        obj[1] = new HashSet<Character>();
        obj[2] = new HashMap<Circle, HashMap<Character, Circle>>();
        obj[3] = null;
        obj[4] = new HashSet<Circle>();
    }

    public Object getObj(int index) {
        System.out.println(obj[0]);
        return obj[index];
    }
    
}
