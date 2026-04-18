/**
 * Representa una taza en la torre de apilamiento.
 * Es la clase base para todos los tipos de tazas.
 * Usa herencia para permitir diferentes comportamientos.
 * 
 * @author Daniel Cortes
 * @version 2.0
 */
public abstract class Cup extends StackingItem
{
    private Lid lid; // null si no tiene tapa

    /**
     * Crea una taza con tamanio y color dados.
     * 
     * @param size  tamanio de la taza
     * @param color color de la taza
     */
    public Cup(int size, String color)
    {
        super(size, color, "cup");
        lid = null;
    }

    /**
     * Asigna una tapa a esta taza.
     * 
     * @param lid tapa a asignar
     */
    public void setLid(Lid lid)
    {
        this.lid = lid;
    }

    /**
     * Quita la tapa de esta taza.
     */
    public void removeLid()
    {
        lid = null;
    }

    /**
     * Retorna la tapa de esta taza, o null si no tiene.
     * 
     * @return tapa de la taza
     */
    public Lid getLid()
    {
        return lid;
    }

    /**
     * Indica si esta taza tiene tapa.
     * 
     * @return true si tiene tapa, false si no
     */
    public boolean hasLid()
    {
        return lid != null;
    }
}