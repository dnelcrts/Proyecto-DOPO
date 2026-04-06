/**
 * Representa una tapa normal en la torre de apilamiento.
 * Se comporta como la tapa original del proyecto.
 * Extiende Lid usando el mecanismo de herencia.
 * 
 * @author Daniel Cortes
 * @version 2.0
 */
public class NormalLid extends Lid
{
    /**
     * Crea una tapa normal con tamanio y color dados.
     * El color es el mismo de su taza correspondiente.
     * 
     * @param size  tamanio de la tapa
     * @param color color de la tapa
     */
    public NormalLid(int size, String color)
    {
        super(size, color);
    }
}