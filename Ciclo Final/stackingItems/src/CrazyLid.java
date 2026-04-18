/**
 * Representa una tapa tipo crazy en la torre de apilamiento.
 * En lugar de tapar a su taza, se ubica en la base de la torre.
 * Extiende Lid usando el mecanismo de herencia.
 * 
 * @author Daniel Cortes
 * @version 2.0
 */
public class CrazyLid extends Lid
{
    /**
     * Crea una tapa crazy con tamanio y color dados.
     * El color es magenta para distinguirla visualmente.
     * 
     * @param size  tamanio de la tapa
     * @param color color de la tapa
     */
    public CrazyLid(int size, String color)
    {
        super(size, color);
    }
}