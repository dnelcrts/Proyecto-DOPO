/**
 * Representa un elemento que puede apilarse en la torre.
 * Las tazas y tapas son elementos apilables.
 * 
 * @author Daniel Cortes
 * @version 2.0
 */
public abstract class StackingItem
{
    private int size;
    private String color;
    private String type;

    /**
     * Crea un elemento apilable con tamanio, color y tipo dados.
     * 
     * @param size  tamanio del elemento
     * @param color color del elemento
     * @param type  tipo del elemento ("cup" o "lid")
     */
    public StackingItem(int size, String color, String type)
    {
        this.size = size;
        this.color = color;
        this.type = type;
    }

    /**
     * Retorna el tamanio del elemento.
     * 
     * @return tamanio del elemento
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Retorna el tipo del elemento.
     * 
     * @return tipo del elemento
     */
    public String getType()
    {
        return type;
    }

    /**
     * Retorna el color del elemento.
     * 
     * @return color del elemento
     */
    public String getColor()
    {
        return color;
    }
}