/**
 * Represents a lid in the stacking tower.
 * 
 * @author Daniel
 * @version 1.0
 */
public class Lid extends StackingItem
{
    private int size;
    private String color;

    /**
     * Creates a lid with a given size and color.
     */
    public Lid(int size, String color)
    {
        this.size = size;
        this.color = color;
    }

    public int getSize()
    {
        return size;
    }

    public String getType()
    {
        return "lid";
    }

    public String getColor()
    {
        return color;
    }
}