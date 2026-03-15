/**
 * Represents an item that can be stacked in a tower.
 * Cups and lids are stacking items.
 * 
 * @author Daniel
 * @version 1.0
 */
public class StackingItem
{
    private int size;
    private String color;
    private String type;

    /**
     * Creates a stacking item with a given size, color and type.
     * 
     * @param size the size of the item
     * @param color the color of the item
     * @param type the type of the item (cup or lid)
     */
    public StackingItem(int size, String color, String type)
    {
        this.size = size;
        this.color = color;
        this.type = type;
    }

    /**
     * Returns the size of the item.
     * 
     * @return size of the item
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Returns the type of the item.
     * 
     * @return type of the item
     */
    public String getType()
    {
        return type;
    }

    /**
     * Returns the color of the item.
     * 
     * @return color of the item
     */
    public String getColor()
    {
        return color;
    }
}