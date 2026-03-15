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
    
    private Lid lid; // null si no tiene tapa

    public Cup(int size)
    {
        super(size, COLORS[new Random().nextInt(COLORS.length)], "cup");
        lid = null;
    }

    // Asigna una tapa a esta taza
    public void setLid(Lid lid)
    {
        this.lid = lid;
    }

    // Quita la tapa de esta taza
    public void removeLid()
    {
        lid = null;
    }

    // Retorna la tapa (o null si no tiene)
    public Lid getLid()
    {
        return lid;
    }

    // Dice si la taza tiene tapa
    public boolean hasLid()
    {
        return lid != null;
    }
}