/**
 * Abstract class representing an item that can be stacked in a tower.
 * Cups and lids are stacking items.
 * 
 * @author Daniel
 * @version 1.0
 */
public abstract class StackingItem
{
    /**
     * Returns the size of the item (used for ordering).
     * 
     * @return size of the item
     */
    public abstract int getSize();

    /**
     * Returns the type of the item (Cup or Lid).
     * 
     * @return type name
     */
    public abstract String getType();
    
    public abstract String getColor();
}