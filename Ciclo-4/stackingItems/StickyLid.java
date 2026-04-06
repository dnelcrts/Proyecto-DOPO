/**
 * Representa una tapa tipo sticky en la torre de apilamiento.
 * Es el tipo propuesto por el equipo.
 * Una tapa sticky no puede quitarse con popLid, solo con removeLid.
 * Simula una tapa pegada a su taza.
 * Extiende Lid usando el mecanismo de herencia.
 * 
 * @author Daniel Cortes
 * @version 2.0
 */
public class StickyLid extends Lid
{
    /**
     * Crea una tapa sticky con tamanio y color dados.
     * El color es negro para distinguirla visualmente.
     * 
     * @param size  tamanio de la tapa
     * @param color color de la tapa
     */
    public StickyLid(int size, String color)
    {
        super(size, color);
    }
}