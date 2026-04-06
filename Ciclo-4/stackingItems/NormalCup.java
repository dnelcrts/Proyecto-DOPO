import java.util.Random;

/**
 * Representa una taza normal en la torre de apilamiento.
 * Se comporta como la taza original del proyecto.
 * Extiende Cup usando el mecanismo de herencia.
 * 
 * @author Daniel Cortes
 * @version 2.0
 */
public class NormalCup extends Cup
{
    private static final String[] COLORS = {
        "red", "blue", "green", "yellow", "magenta"
    };

    /**
     * Crea una taza normal con tamanio dado.
     * El color se asigna aleatoriamente.
     * 
     * @param size tamanio de la taza
     */
    public NormalCup(int size)
    {
        super(size, COLORS[new Random().nextInt(COLORS.length)]);
    }
}