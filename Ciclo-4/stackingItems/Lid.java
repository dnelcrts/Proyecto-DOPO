/**
 * Representa una tapa en la torre de apilamiento.
 * Es la clase base para todos los tipos de tapas.
 * Usa herencia para permitir diferentes comportamientos.
 * 
 * @author Daniel Cortes
 * @version 2.0
 */
public abstract class Lid extends StackingItem
{
    /**
     * Crea una tapa con tamanio y color dados.
     * 
     * @param size  tamanio de la tapa
     * @param color color de la tapa
     */
    public Lid(int size, String color)
    {
        super(size, color, "lid");
    }
}