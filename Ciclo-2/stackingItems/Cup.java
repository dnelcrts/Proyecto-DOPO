import java.util.Random;

/**
 * Represents a cup in the stacking tower.
 * 
 * @author Daniel
 * @version 1.0
 */
public class Cup extends StackingItem
{
    private static String[] COLORS = {
        "red", "blue", "green", "yellow", "magenta"
    };

    /**
     * Creates a cup with a given size and a random color.
     * 
     * @param size the size of the cup
     */
    public Cup(int size)
    {
        super(size, COLORS[new Random().nextInt(COLORS.length)], "cup");
    }
}