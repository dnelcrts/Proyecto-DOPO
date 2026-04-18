/**
 * Representa una taza tipo hierarchical en la torre de apilamiento.
 * Al entrar a la torre, desplaza todos los objetos de menor tamanio.
 * Si llega al fondo de la torre, no se puede quitar.
 * Extiende Cup usando el mecanismo de herencia.
 * 
 * @author Daniel Cortes
 * @version 2.0
 */
public class HierarchicalCup extends Cup
{
    private boolean reachedBottom;

    /**
     * Crea una taza hierarchical con tamanio dado.
     * El color es cyan para distinguirla visualmente.
     * 
     * @param size tamanio de la taza
     */
    public HierarchicalCup(int size)
    {
        super(size, "cyan");
        reachedBottom = false;
    }

    /**
     * Marca que esta taza llego al fondo de la torre.
     */
    public void setReachedBottom()
    {
        reachedBottom = true;
    }

    /**
     * Indica si esta taza llego al fondo de la torre.
     * Si es true, no puede ser quitada.
     * 
     * @return true si llego al fondo
     */
    public boolean hasReachedBottom()
    {
        return reachedBottom;
    }
}