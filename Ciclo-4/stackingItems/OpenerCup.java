/**
 * Representa una taza tipo opener en la torre de apilamiento.
 * Al entrar a la torre, elimina todas las tapas que le impiden el paso.
 * Extiende Cup usando el mecanismo de herencia.
 * 
 * @author Daniel Cortes
 * @version 2.0
 */
public class OpenerCup extends Cup
{
    /**
     * Crea una taza opener con tamanio dado.
     * El color es naranja para distinguirla visualmente.
     * 
     * @param size tamanio de la taza
     */
    public OpenerCup(int size)
    {
        super(size, "orange");
    }
}