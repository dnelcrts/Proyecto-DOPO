import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Representa una torre donde se apilan tazas y tapas.
 * Maneja diferentes tipos de tazas y tapas usando polimorfismo.
 * Las operaciones complejas las delega a TowerOperations.
 * 
 * @author Daniel Cortes
 * @version 2.0
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
     * @param width     ancho de la torre en cm
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
     * Crea una torre con un numero dado de tazas normales ya dentro.
     * El creador masivo solo usa elementos normales.
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
            items.add(new NormalCup(i));
        }
    }

    // ---- AGREGAR Y QUITAR TAZAS ----

    /**
     * Agrega una taza normal a la torre.
     * 
     * @param i tamanio de la taza
     */
    public void pushCup(int i)
    {
        pushCup("normal", i);
    }

    /**
     * Agrega una taza de un tipo especifico a la torre.
     * Tipos disponibles: "normal", "opener", "hierarchical".
     * Usa polimorfismo para manejar el comportamiento de cada tipo.
     * 
     * @param type tipo de taza
     * @param i    tamanio de la taza
     */
    public void pushCup(String type, int i)
    {
        if(i < 1 || i > width) {
            fail("El numero de taza debe estar entre 1 y " + width);
        } else if(items.size() >= maxHeight) {
            fail("No hay espacio para agregar otra taza.");
        } else if(existsCup(i)) {
            fail("Ya existe una taza con numero " + i);
        } else {
            Cup cup = createCup(type, i);
            if(cup instanceof OpenerCup) {
                removeBlockingLids(i);
            }
            if(cup instanceof HierarchicalCup) {
                pushHierarchical((HierarchicalCup) cup);
            } else {
                items.add(cup);
            }
            lastOk = true;
            refresh();
        }
    }

    /**
     * Elimina la ultima taza de la torre.
     * Si tiene tapa, la elimina tambien.
     * No puede eliminar una HierarchicalCup que llego al fondo.
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
            if(cup instanceof HierarchicalCup) {
                HierarchicalCup hc = (HierarchicalCup) cup;
                if(hc.hasReachedBottom()) {
                    fail("La taza hierarchical llego al fondo y no puede quitarse.");
                    return;
                }
            }
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
     * No puede eliminar una HierarchicalCup que llego al fondo.
     * 
     * @param i posicion de la taza
     */
    public void removeCup(int i)
    {
        if(i < 0 || i >= items.size() || !(items.get(i) instanceof Cup)) {
            fail("No se puede eliminar taza en esa posicion.");
        } else {
            Cup cup = (Cup) items.get(i);
            if(cup instanceof HierarchicalCup) {
                HierarchicalCup hc = (HierarchicalCup) cup;
                if(hc.hasReachedBottom()) {
                    fail("La taza hierarchical llego al fondo y no puede quitarse.");
                    return;
                }
            }
            if(cup.hasLid()) {
                items.remove(cup.getLid());
                cup.removeLid();
            }
            items.remove(i);
            lastOk = true;
            refresh();
        }
    }

    // ---- AGREGAR Y QUITAR TAPAS ----

    /**
     * Agrega una tapa normal a la torre.
     * 
     * @param i tamanio de la tapa
     */
    public void pushLid(int i)
    {
        pushLid("normal", i);
    }

    /**
     * Agrega una tapa de un tipo especifico a la torre.
     * Tipos disponibles: "normal", "fearful", "crazy", "sticky".
     * Usa polimorfismo para manejar el comportamiento de cada tipo.
     * 
     * @param type tipo de tapa
     * @param i    tamanio de la tapa
     */
    public void pushLid(String type, int i)
    {
        if(i < 1 || i > width) {
            fail("El numero de tapa debe estar entre 1 y " + width);
        } else if(items.size() >= maxHeight) {
            fail("No hay espacio para agregar otra tapa.");
        } else if(existsLid(i)) {
            fail("Ya existe una tapa con numero " + i);
        } else if(!existsCup(i)) {
            fail("No se puede agregar tapa porque no existe la taza " + i);
        } else {
            Cup cup = findCup(i);
            Lid lid = createLid(type, i, cup.getColor());

            if(lid instanceof CrazyLid) {
                cup.setLid(lid);
                items.add(0, lid);
            } else {
                cup.setLid(lid);
                items.add(lid);
            }

            lastOk = true;
            refresh();
        }
    }

    /**
     * Elimina la ultima tapa de la torre.
     * No puede eliminar una StickyLid con este metodo.
     * No puede eliminar una FearfulLid que esta tapando a su taza.
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

            if(lid instanceof StickyLid) {
                fail("La tapa sticky no puede quitarse con popLid. Use removeLid.");
                return;
            }

            if(lid instanceof FearfulLid) {
                Cup cup = findCup(lid.getSize());
                if(cup != null && cup.getLid() == lid) {
                    fail("La tapa fearful esta tapando a su taza y no puede salir.");
                    return;
                }
            }

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
            fail("No se puede eliminar tapa en esa posicion.");
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
     * Retorna los numeros de las tazas tapadas por sus tapas,
     * ordenados de menor a mayor.
     *
     * @return arreglo con los numeros de las tazas tapadas
     */
    public int[] listedCups()
    {
        ArrayList<Integer> tapadas = new ArrayList<Integer>();
        for(StackingItem it : items) {
            if(it instanceof Cup && ((Cup) it).hasLid()) {
                tapadas.add(it.getSize());
            }
        }
        int[] result = new int[tapadas.size()];
        for(int i = 0; i < tapadas.size(); i++) {
            result[i] = tapadas.get(i);
        }
        java.util.Arrays.sort(result);
        return result;
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

    /**
     * Cierra el simulador.
     */
    public void exit()
    {
        makeInvisible();
    }

    // ---- PRIVADOS ----

    /**
     * Crea una taza del tipo indicado.
     * Usa encapsulamiento para centralizar la creacion de tazas.
     */
    private Cup createCup(String type, int size)
    {
        if(type.equals("opener")) {
            return new OpenerCup(size);
        } else if(type.equals("hierarchical")) {
            return new HierarchicalCup(size);
        } else {
            return new NormalCup(size);
        }
    }

    /**
     * Crea una tapa del tipo indicado.
     * Usa encapsulamiento para centralizar la creacion de tapas.
     */
    private Lid createLid(String type, int size, String color)
    {
        if(type.equals("fearful")) {
            return new FearfulLid(size, color);
        } else if(type.equals("crazy")) {
            return new CrazyLid(size, color);
        } else if(type.equals("sticky")) {
            return new StickyLid(size, color);
        } else {
            return new NormalLid(size, color);
        }
    }

    /**
     * Elimina todas las tapas que bloquean el paso a una OpenerCup.
     * Una tapa bloquea si su tamanio es mayor o igual al de la taza.
     */
    private void removeBlockingLids(int cupSize)
    {
        ArrayList<StackingItem> toRemove = new ArrayList<StackingItem>();
        for(StackingItem it : items) {
            if(it instanceof Lid && it.getSize() <= cupSize) {
                toRemove.add(it);
            }
        }
        for(StackingItem lid : toRemove) {
            Cup cup = findCup(lid.getSize());
            if(cup != null) {
                cup.removeLid();
            }
            items.remove(lid);
        }
    }

    /**
     * Inserta una HierarchicalCup desplazando los items de menor tamanio.
     * Si no queda ningun elemento de menor tamanio debajo, marca la taza
     * como inamovible.
     */
    private void pushHierarchical(HierarchicalCup cup)
    {
        int insertIndex = 0;
        for(int i = 0; i < items.size(); i++) {
            if(items.get(i).getSize() >= cup.getSize()) {
                insertIndex = i + 1;
            }
        }
        items.add(insertIndex, cup);

        boolean hayMenoresDebajo = false;
        for(int i = 0; i < insertIndex; i++) {
            if(items.get(i).getSize() < cup.getSize()) {
                hayMenoresDebajo = true;
            }
        }
        if(!hayMenoresDebajo) {
            cup.setReachedBottom();
        }
    }

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
        for(StackingItem it : items) {
            if(it instanceof Cup && it.getSize() == size) return true;
        }
        return false;
    }

    private boolean existsLid(int size)
    {
        for(StackingItem it : items) {
            if(it instanceof Lid && it.getSize() == size) return true;
        }
        return false;
    }

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