import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Representa una torre donde se apilan tazas y tapas.
 * Solo se encarga de agregar, quitar items y coordinar la vista.
 * Las operaciones complejas las delega a TowerOperations.
 * 
 * @author Daniel
 * @version 1.0
 */
public class Tower
{
    private int width;
    private int maxHeight;
    private boolean visible;
    private boolean lastOk;

    private ArrayList<StackingItem> items;
    private TowerView view;
    private TowerOperations operations;

    /**
     * Crea una torre con un ancho y altura maxima dados.
     * 
     * @param width ancho de la torre en cm
     * @param maxHeight altura maxima de la torre en cm
     */
    public Tower(int width, int maxHeight)
    {
        this.width = width;
        this.maxHeight = maxHeight;
        visible = false;
        lastOk = true;
        items = new ArrayList<StackingItem>();
        view = new TowerView(width, maxHeight);
        operations = new TowerOperations();
    }

    /**
     * Crea una torre con un numero dado de tazas ya dentro.
     * 
     * @param cups numero de tazas a crear
     */
    public Tower(int cups)
    {
        this.width = cups;
        this.maxHeight = cups * 2;
        visible = false;
        lastOk = true;
        items = new ArrayList<StackingItem>();
        view = new TowerView(this.width, this.maxHeight);
        operations = new TowerOperations();
        for(int i = 1; i <= cups; i++) {
            items.add(new Cup(i));
        }
    }

    // ---- AGREGAR Y QUITAR ----

    /**
     * Agrega una taza a la torre.
     * 
     * @param i tamanio de la taza
     */
    public void pushCup(int i)
    {
        if(i < 1 || i > width) {
            fail("El número de taza debe estar entre 1 y " + width);
        } else if(items.size() >= maxHeight) {
            fail("No hay espacio para agregar otra taza.");
        } else if(existsCup(i)) {
            fail("Ya existe una taza con número " + i);
        } else {
            items.add(new Cup(i));
            lastOk = true;
            refresh();
        }
    }

    /**
     * Elimina la ultima taza de la torre.
     * Si tiene tapa, la elimina tambien.
     */
    public void popCup()
    {
        int index = -1;
        for(int i = items.size() - 1; i >= 0; i--) {
            if(items.get(i) instanceof Cup && index == -1) {
                index = i;
            }
        }

        if(index == -1) {
            fail("No hay tazas para eliminar.");
        } else {
            Cup cup = (Cup) items.get(index);
            // Si tiene tapa, la eliminamos primero
            if(cup.hasLid()) {
                items.remove(cup.getLid());
                cup.removeLid();
            }
            items.remove(index);
            lastOk = true;
            refresh();
        }
    }

    /**
     * Elimina una taza en una posicion especifica.
     * Si tiene tapa, la elimina tambien.
     * 
     * @param i posicion de la taza
     */
    public void removeCup(int i)
    {
        if(i < 0 || i >= items.size() || !(items.get(i) instanceof Cup)) {
            fail("No se puede eliminar taza en esa posición.");
        } else {
            Cup cup = (Cup) items.get(i);
            if(cup.hasLid()) {
                items.remove(cup.getLid());
                cup.removeLid();
            }
            items.remove(i);
            lastOk = true;
            refresh();
        }
    }

    /**
     * Agrega una tapa a la torre.
     * La tapa toma el color de su taza correspondiente.
     * 
     * @param i tamanio de la tapa
     */
    public void pushLid(int i)
    {
        if(i < 1 || i > width) {
            fail("El número de tapa debe estar entre 1 y " + width);
        } else if(items.size() >= maxHeight) {
            fail("No hay espacio para agregar otra tapa.");
        } else if(existsLid(i)) {
            fail("Ya existe una tapa con número " + i);
        } else if(!existsCup(i)) {
            fail("No se puede agregar tapa porque no existe la taza " + i);
        } else {
            Cup cup = findCup(i);
            Lid lid = new Lid(i, cup.getColor());
            // Le avisamos a la taza que tiene tapa
            cup.setLid(lid);
            items.add(lid);
            lastOk = true;
            refresh();
        }
    }

    /**
     * Elimina la ultima tapa de la torre.
     */
    public void popLid()
    {
        int index = -1;
        for(int i = items.size() - 1; i >= 0; i--) {
            if(items.get(i) instanceof Lid && index == -1) {
                index = i;
            }
        }

        if(index == -1) {
            fail("No hay tapas para eliminar.");
        } else {
            Lid lid = (Lid) items.get(index);
            // Le avisamos a la taza que ya no tiene tapa
            Cup cup = findCup(lid.getSize());
            if(cup != null) {
                cup.removeLid();
            }
            items.remove(index);
            lastOk = true;
            refresh();
        }
    }

    /**
     * Elimina una tapa en una posicion especifica.
     * 
     * @param i posicion de la tapa
     */
    public void removeLid(int i)
    {
        if(i < 0 || i >= items.size() || !(items.get(i) instanceof Lid)) {
            fail("No se puede eliminar tapa en esa posición.");
        } else {
            Lid lid = (Lid) items.get(i);
            Cup cup = findCup(lid.getSize());
            if(cup != null) {
                cup.removeLid();
            }
            items.remove(i);
            lastOk = true;
            refresh();
        }
    }

    // ---- OPERACIONES DELEGADAS A TowerOperations ----

    /**
     * Ordena los items de mayor a menor.
     */
    public void orderTower()
    {
        items = operations.orderTower(items, maxHeight);
        lastOk = true;
        refresh();
    }

    /**
     * Invierte el orden de los items.
     */
    public void reverseTower()
    {
        items = operations.reverseTower(items, maxHeight);
        lastOk = true;
        refresh();
    }

    /**
     * Coloca cada tapa encima de su taza correspondiente.
     */
    public void cover()
    {
        items = operations.cover(items);
        lastOk = true;
        refresh();
    }

    /**
     * Intercambia dos items de la torre.
     * Si una taza tiene tapa, la tapa se mueve con ella.
     * 
     * @param o1 primer item como {tipo, tamanio}
     * @param o2 segundo item como {tipo, tamanio}
     */
    public void swap(String[] o1, String[] o2)
    {
        ArrayList<StackingItem> result = operations.swap(items, o1, o2);

        if(result == null) {
            fail("No se pudo realizar el intercambio.");
        } else {
            items = result;
            lastOk = true;
            refresh();
        }
    }

    /**
     * Busca un intercambio que reduzca la altura de la torre.
     * 
     * @return los dos items a intercambiar
     */
    public String[][] swapToReduce()
    {
        String[][] result = operations.swapToReduce(items);

        if(result.length == 0) {
            lastOk = false;
        } else {
            lastOk = true;
        }

        return result;
    }

    // ---- CONSULTAS ----

    /**
     * Retorna la altura actual de la torre.
     * 
     * @return altura de la torre
     */
    public int height()
    {
        return items.size();
    }

    /**
     * Imprime cuantas tazas y tapas hay en la torre.
     */
    public void listedCups()
    {
        int cups = 0;
        int lids = 0;

        for(StackingItem it : items) {
            if(it instanceof Cup) {
                cups++;
            }
            if(it instanceof Lid) {
                lids++;
            }
        }

        System.out.println("Cups: " + cups);
        System.out.println("Lids: " + lids);
    }

    /**
     * Retorna una matriz con el tipo y tamanio de cada item.
     * 
     * @return arreglo con info de los items
     */
    public String[][] stackingItem()
    {
        String[][] info = new String[items.size()][2];

        for(int i = 0; i < items.size(); i++) {
            info[i][0] = items.get(i).getType();
            info[i][1] = "" + items.get(i).getSize();
        }

        return info;
    }

    /**
     * Retorna true si la ultima operacion fue exitosa.
     * 
     * @return lastOk
     */
    public boolean ok()
    {
        return lastOk;
    }

    // ---- VISIBILIDAD ----

    /**
     * Hace visible la torre.
     */
    public void makeVisible()
    {
        visible = true;
        view.makeVisible();
        refresh();
    }

    /**
     * Hace invisible la torre.
     */
    public void makeInvisible()
    {
        visible = false;
        view.makeInvisible();
    }

    // ---- PRIVADOS ----

    private void refresh()
    {
        if(visible) {
            view.update(items);
        }
    }

    private void fail(String msg)
    {
        lastOk = false;
        if(visible) {
            JOptionPane.showMessageDialog(null, msg);
        }
    }

    private boolean existsCup(int size)
    {
        boolean found = false;
        for(StackingItem it : items) {
            if(it instanceof Cup && it.getSize() == size) {
                found = true;
            }
        }
        return found;
    }

    private boolean existsLid(int size)
    {
        boolean found = false;
        for(StackingItem it : items) {
            if(it instanceof Lid && it.getSize() == size) {
                found = true;
            }
        }
        return found;
    }

    private String getCupColor(int size)
    {
        String color = "black";
        for(StackingItem it : items) {
            if(it instanceof Cup && it.getSize() == size) {
                color = it.getColor();
            }
        }
        return color;
    }

    // Busca y retorna una taza por su tamanio
    private Cup findCup(int size)
    {
        Cup found = null;
        for(StackingItem it : items) {
            if(it instanceof Cup && it.getSize() == size) {
                found = (Cup) it;
            }
        }
        return found;
    }
}