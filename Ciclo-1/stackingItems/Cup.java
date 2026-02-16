import java.util.Random;

/**
 * Represents a cup in the stacking tower.
 * 
 * @author Daniel
 * @version 1.0
 */
import java.util.Random;

public class Cup extends StackingItem
{
    private int size;
    private String color;

    private static String[] COLORS = {
        "red", "blue", "green", "yellow", "magenta"
    };

    public Cup(int size)
    {
        this.size = size;

        Random r = new Random();
        color = COLORS[r.nextInt(COLORS.length)];
    }

    public int getSize()
    {
        return size;
    }

    public String getType()
    {
        return "cup";
    }

    public String getColor()
    {
        return color;
    }
}
