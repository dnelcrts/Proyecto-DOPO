/**
 * Representa una tapa tipo fearful en la torre de apilamiento.
 * Si su taza companiera no esta en la torre, no puede entrar.
 * Si esta tapando a su taza, no puede salir.
 * Extiende Lid usando el mecanismo de herencia.
 * 
 * @author Daniel Cortes
 * @version 2.0
 */
public class FearfulLid extends Lid
{
    /**
     * Crea una tapa fearful con tamanio y color dados.
     * El color es gris oscuro para distinguirla visualmente.
     * 
     * @param size  tamanio de la tapa
     * @param color color de la tapa
     */
    public FearfulLid(int size, String color)
    {
        super(size, color);
    }
}