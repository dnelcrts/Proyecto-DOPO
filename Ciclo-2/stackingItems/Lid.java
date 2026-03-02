/**
 * Represents a lid in the stacking tower.
 * 
 * @author Daniel
 * @version 1.0
 */
public class Lid extends StackingItem
{
    /**
     * Creates a lid with a given size and color.
     * 
     * @param size the size of the lid
     * @param color the color of the lid
     */
    public Lid(int size, String color)
    {
        super(size, color, "lid");
    }
}