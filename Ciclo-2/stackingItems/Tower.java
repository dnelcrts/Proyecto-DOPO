import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * Represents a tower where stacking items (cups and lids) can be managed.
 * This class controls the simulator logic and delegates drawing to TowerView.
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

    /**
     * Creates a new tower with a given width and maximum height.
     * 
     * @param width the width of the tower in cm
     * @param maxHeight the maximum height of the tower in cm
     */
    public Tower(int width, int maxHeight)
    {
        this.width = width;
        this.maxHeight = maxHeight;
        visible = false;
        lastOk = true;

        items = new ArrayList<StackingItem>();
        view = new TowerView(width, maxHeight);
    }

    public void pushCup(int i)
    {
    if(i < 1 || i > width) {
        fail("El número de taza debe estar entre 1 y " + width);
        return;
    }

    if(items.size() >= maxHeight) {
        fail("No hay espacio para agregar otra taza.");
        return;
    }

    if(existsCup(i)) {
        fail("Ya existe una taza con número " + i);
        return;
    }

    items.add(new Cup(i));
    lastOk = true;
    refresh();
    }   

    /**
     * Removes the last cup in the tower.
     */
    public void popCup()
    {
        for(int i = items.size() - 1; i >= 0; i--) {
            if(items.get(i) instanceof Cup) {
                items.remove(i);
                lastOk = true;
                refresh();
                return;
            }
        }
        fail("No hay tazas para eliminar.");
    }

    /**
     * Removes a cup from a specific position.
     * 
     * @param i position of the cup (starting at 0)
     */
    public void removeCup(int i)
    {
        if(i < 0 || i >= items.size() || !(items.get(i) instanceof Cup)) {
            fail("No se puede eliminar taza en esa posición.");
            return;
        }

        items.remove(i);
        lastOk = true;
        refresh();
    }

    /**
     * Pushes a lid into the tower.
     * 
     * @param i the size/type of lid
     */
    public void pushLid(int i)
    {
    if(i < 1 || i > width) {
        fail("El número de tapa debe estar entre 1 y " + width);
        return;
    }

    if(items.size() >= maxHeight) {
        fail("No hay espacio para agregar otra tapa.");
        return;
    }

    if(existsLid(i)) {
        fail("Ya existe una tapa con número " + i);
        return;
    }

    if(!existsCup(i)) {
        fail("No se puede agregar tapa porque no existe la taza " + i);
        return;
    }

    String cupColor = getCupColor(i);

    items.add(new Lid(i, cupColor));
    lastOk = true;
    refresh();
    }

    private String findLastCupColor()
    {
    for(int j = items.size() - 1; j >= 0; j--) {
        if(items.get(j) instanceof Cup) {
            return items.get(j).getColor();
        }
    }
    return null;
    }

    /**
     * Removes the last lid in the tower.
     */
    public void popLid()
    {
        for(int i = items.size() - 1; i >= 0; i--) {
            if(items.get(i) instanceof Lid) {
                items.remove(i);
                lastOk = true;
                refresh();
                return;
            }
        }
        fail("No hay tapas para eliminar.");
    }

    /**
     * Removes a lid from a specific position.
     * 
     * @param i position of the lid (starting at 0)
     */
    public void removeLid(int i)
    {
        if(i < 0 || i >= items.size() || !(items.get(i) instanceof Lid)) {
            fail("No se puede eliminar tapa en esa posición.");
            return;
        }

        items.remove(i);
        lastOk = true;
        refresh();
    }

    /**
     * Sorts tower items from largest to smallest.
     */
    public void orderTower()
    {
    ArrayList<StackingItem> ordered = new ArrayList<>();

    // Vamos de mayor a menor
    for(int size = width; size >= 1; size--) {

        Cup cupFound = null;
        Lid lidFound = null;

        for(StackingItem it : items) {
            if(it.getSize() == size) {
                if(it instanceof Cup) {
                    cupFound = (Cup) it;
                }
                else if(it instanceof Lid) {
                    lidFound = (Lid) it;
                }
            }
        }

        // primero cup y luego lid (si existen)
        if(cupFound != null) {
            ordered.add(cupFound);
        }
        if(lidFound != null) {
            ordered.add(lidFound);
        }
    }

    // Si no caben todos, solo incluir los que quepan
    if(ordered.size() > maxHeight) {
        ordered = new ArrayList<>(ordered.subList(0, maxHeight));
    }

    items = ordered;
    lastOk = true;
    refresh();
    }

    /**
     * Reverses the order of the tower items.
     */
    public void reverseTower()
    {
    ArrayList<StackingItem> reversed = new ArrayList<StackingItem>();

    for(int i = items.size() - 1; i >= 0; i--) {
        reversed.add(items.get(i));
    }

    // solo incluir los que quepan
    if(reversed.size() > maxHeight) {
        reversed = new ArrayList<>(reversed.subList(0, maxHeight));
    }

    items = reversed;
    lastOk = true;
    refresh();
    }

    /**
     * Returns the current height of the tower.
     * 
     * @return height of tower
     */
    public int height()
    {
        return items.size();
    }

    /**
     * Prints the number of lids and cups.
     */
    public void listedCups()
    {
        int cups = 0;
        int lids = 0;

        for(StackingItem it : items) {
            if(it instanceof Cup) cups++;
            if(it instanceof Lid) lids++;
        }

        System.out.println("Cups: " + cups);
        System.out.println("Lids: " + lids);
    }

    /**
     * Returns a matrix with information about the stacking items.
     * 
     * @return array with info of items
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
     * Makes the simulator visible.
     */
    public void makeVisible()
    {
        visible = true;
        view.makeVisible();
        refresh();
    }

    /**
     * Makes the simulator invisible.
     */
    public void makeInvisible()
    {
        visible = false;
        view.makeInvisible();
    }

    /**
     * Returns true if last operation was ok.
     * 
     * @return lastOk
     */
    public boolean ok()
    {
        return lastOk;
    }

    /**
     * Refresh tower view if visible.
     */
    private void refresh()
    {
        if(visible) {
            view.update(items);
        }
    }

    /**
     * Marks operation as failed and shows message if visible.
     */
    private void fail(String msg)
    {
        lastOk = false;

        if(visible) {
            JOptionPane.showMessageDialog(null, msg);
        }
    }
    
    private ArrayList<StackingItem> getTopBlock()
    {
        ArrayList<StackingItem> block = new ArrayList<>();

        if(items.isEmpty()) {
        return block;
        }

        StackingItem top = items.get(items.size() - 1);

        // Si arriba hay una tapa, el bloque es: tapa + taza debajo
        if(top instanceof Lid) {
            if(items.size() < 2) {
                return block; // error raro: tapa sin taza
            }

            StackingItem below = items.get(items.size() - 2);

            if(below instanceof Cup) {
                block.add(below);
                block.add(top);
            }
        } else {
        // Si arriba hay una taza, bloque es solo la taza
        block.add(top);
        }

        return block;
    }
    
    private boolean existsCup(int size)
    {
        for(StackingItem it : items) {
            if(it instanceof Cup && it.getSize() == size) {
                return true;
            }
        }
        return false;
    }

    private boolean existsLid(int size)
    {
        for(StackingItem it : items) {
            if(it instanceof Lid && it.getSize() == size) {
                return true;
            }
        }
        return false;
    }
    
    private String getCupColor(int size)
    {
        for(StackingItem it : items) {
            if(it instanceof Cup && it.getSize() == size) {
                return it.getColor();
            }
        }
        return "black";
    }
    
    /**
     * Creates a tower with a given number of cups.
     * Cups are created from 1 to cups.
     * 
     * @param cups the number of cups to create
     */
    public Tower(int cups)
    {
        this.width = cups;
        this.maxHeight = cups * 2;
        visible = false;
        lastOk = true;
        items = new ArrayList<StackingItem>();
        view = new TowerView(this.width, this.maxHeight);
        for (int i = 1; i <= cups; i++) {
            items.add(new Cup(i));
        }
    }
    
    /**
     * Swaps the position of two items in the tower.
     * Each item is identified by its type and size.
     * 
     * @param o1 first item as {type, size}
     * @param o2 second item as {type, size}
     */
    public void swap(String[] o1, String[] o2)
    {
        if (o1 == null || o2 == null || o1.length < 2 || o2.length < 2) {
            lastOk = false;
            return;
        }
        int i1 = -1;
        int i2 = -1;
        for (int i = 0; i < items.size(); i++) {
            StackingItem item = items.get(i);
            if (item.getType().equals(o1[0]) && item.getSize() == Integer.parseInt(o1[1])) {
                i1 = i;
            }
            if (item.getType().equals(o2[0]) && item.getSize() == Integer.parseInt(o2[1])) {
                i2 = i;
            }
        }
        if (i1 < 0 || i2 < 0 || i1 == i2) {
            lastOk = false;
            return;
        }
        StackingItem temp = items.get(i1);
        items.set(i1, items.get(i2));
        items.set(i2, temp);
        lastOk = true;
        refresh();
    }
    
    /**
     * Places each lid directly on top of its matching cup.
     */
    public void cover()
    {
        lastOk = false;
        for (int i = 0; i < items.size(); i++) {
            StackingItem item = items.get(i);
            if (item instanceof Lid) {
                int cupIndex = -1;
                for (int j = 0; j < items.size(); j++) {
                    if (items.get(j) instanceof Cup && items.get(j).getSize() == item.getSize()) {
                        cupIndex = j;
                        break;
                    }
                }
                if (cupIndex >= 0 && i != cupIndex + 1) {
                    StackingItem lid = items.remove(i);
                    if (cupIndex > i) cupIndex--;
                    items.add(cupIndex + 1, lid);
                    i = -1;
                }
            }
        }
        lastOk = true;
        refresh();
    }
    
    /**
     * Finds a swap that reduces the height of the tower.
     * Returns the two items to swap, or empty array if none found.
     * 
     * @return array with the two items to swap
     */
     String[][] swapToReduce()
    {
        lastOk = false;
        int alturaActual = items.size();
        for (int a = 0; a < items.size(); a++) {
            for (int b = a + 1; b < items.size(); b++) {
                ArrayList<StackingItem> copia = new ArrayList<>(items);
                StackingItem temp = copia.get(a);
                copia.set(a, copia.get(b));
                copia.set(b, temp);
                int nuevaAltura = copia.size();
                for (int k = copia.size() - 1; k >= 0; k--) {
                    if (copia.get(k) instanceof Lid) {
                        nuevaAltura--;
                    } else {
                    break;
                    }
                }
                if (nuevaAltura < alturaActual) {
                    lastOk = true;
                    return new String[][] {
                        {items.get(a).getType(), "" + items.get(a).getSize()},
                        {items.get(b).getType(), "" + items.get(b).getSize()}
                    };
                }
            }
        }
        return new String[0][0];
    }
}