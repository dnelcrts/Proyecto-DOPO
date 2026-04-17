import java.util.ArrayList;

/**
 * Contiene las operaciones complejas sobre la lista de items de la torre.
 * Tower delega aqui la logica que no es solo agregar o quitar items.
 * 
 * @author Daniel
 * @version 1.0
 */
public class TowerOperations
{
    /**
     * Ordena los items de mayor a menor tamanio.
     * Primero va la taza y luego la tapa de cada tamanio.
     * 
     * @param items lista de items de la torre
     * @param maxHeight altura maxima de la torre
     * @return lista ordenada
     */
    public ArrayList<StackingItem> orderTower(ArrayList<StackingItem> items, int maxHeight)
    {
        ArrayList<StackingItem> ordered = new ArrayList<>();

        int maxSize = 0;
        for(StackingItem it : items) {
            if(it.getSize() > maxSize) {
                maxSize = it.getSize();
            }
        }

        for(int size = maxSize; size >= 1; size--) {
            Cup cupFound = null;
            Lid lidFound = null;

            for(StackingItem it : items) {
                if(it.getSize() == size) {
                    if(it instanceof Cup) {
                        cupFound = (Cup) it;
                    } else if(it instanceof Lid) {
                        lidFound = (Lid) it;
                    }
                }
            }

            if(cupFound != null) {
                ordered.add(cupFound);
            }
            if(lidFound != null) {
                ordered.add(lidFound);
            }
        }

        if(ordered.size() > maxHeight) {
            ordered = new ArrayList<>(ordered.subList(0, maxHeight));
        }

        return ordered;
    }

    /**
     * Invierte el orden de los items.
     * 
     * @param items lista de items de la torre
     * @param maxHeight altura maxima de la torre
     * @return lista invertida
     */
    public ArrayList<StackingItem> reverseTower(ArrayList<StackingItem> items, int maxHeight)
    {
        ArrayList<StackingItem> reversed = new ArrayList<>();

        for(int i = items.size() - 1; i >= 0; i--) {
            reversed.add(items.get(i));
        }

        if(reversed.size() > maxHeight) {
            reversed = new ArrayList<>(reversed.subList(0, maxHeight));
        }

        return reversed;
    }

    /**
     * Coloca cada tapa directamente encima de su taza correspondiente.
     * Usa la relacion Cup-Lid para encontrar la taza correcta.
     * 
     * @param items lista de items de la torre
     * @return lista reorganizada
     */
    public ArrayList<StackingItem> cover(ArrayList<StackingItem> items)
    {
        ArrayList<StackingItem> result = new ArrayList<>(items);

        for(int i = 0; i < result.size(); i++) {
            StackingItem item = result.get(i);

            if(item instanceof Lid) {
                int cupIndex = -1;

                for(int j = 0; j < result.size(); j++) {
                    if(result.get(j) instanceof Cup && result.get(j).getSize() == item.getSize()) {
                        cupIndex = j;
                    }
                }

                if(cupIndex >= 0 && i != cupIndex + 1) {
                    Lid lid = (Lid) result.remove(i);
                    if(cupIndex > i) {
                        cupIndex--;
                    }
                    result.add(cupIndex + 1, lid);
                    i = -1;
                }
            }
        }

        return result;
    }

    /**
     * Intercambia dos items en la lista.
     * Si una taza tiene tapa, la tapa se mueve con ella.
     * 
     * @param items lista de items de la torre
     * @param o1 primer item como {tipo, tamanio}
     * @param o2 segundo item como {tipo, tamanio}
     * @return lista con los items intercambiados, o null si no se pudo
     */
    public ArrayList<StackingItem> swap(ArrayList<StackingItem> items, String[] o1, String[] o2)
    {
        ArrayList<StackingItem> result = null;

        if(o1 != null && o2 != null && o1.length >= 2 && o2.length >= 2) {
            int i1 = -1;
            int i2 = -1;

            try {
                int size1 = Integer.parseInt(o1[1]);
                int size2 = Integer.parseInt(o2[1]);

                for(int i = 0; i < items.size(); i++) {
                    StackingItem item = items.get(i);
                    if(item.getType().equals(o1[0]) && item.getSize() == size1) {
                        i1 = i;
                    }
                    if(item.getType().equals(o2[0]) && item.getSize() == size2) {
                        i2 = i;
                    }
                }
            } catch(NumberFormatException e) {
                return null;
            }

            if(i1 >= 0 && i2 >= 0 && i1 != i2) {
                result = new ArrayList<>(items);
                StackingItem temp = result.get(i1);
                result.set(i1, result.get(i2));
                result.set(i2, temp);

                // Si la taza tiene tapa, la movemos tambien
                StackingItem moved = result.get(i1);
                if(moved instanceof Cup) {
                    Cup cup = (Cup) moved;
                    if(cup.hasLid()) {
                        Lid lid = cup.getLid();
                        result.remove(lid);
                        result.add(i1 + 1, lid);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Busca un intercambio que reduzca la altura de la torre.
     * 
     * @param items lista de items de la torre
     * @return los dos items a intercambiar, o arreglo vacio si no hay ninguno
     */
    public String[][] swapToReduce(ArrayList<StackingItem> items)
    {
        String[][] found = new String[0][0];
        int alturaActual = items.size();
        boolean stop = false;

        for(int a = 0; a < items.size() && !stop; a++) {
            for(int b = a + 1; b < items.size() && !stop; b++) {
                ArrayList<StackingItem> copia = new ArrayList<>(items);
                StackingItem temp = copia.get(a);
                copia.set(a, copia.get(b));
                copia.set(b, temp);

                int nuevaAltura = copia.size();
                for(int k = copia.size() - 1; k >= 0; k--) {
                    if(copia.get(k) instanceof Lid) {
                        nuevaAltura--;
                    } else {
                        k = -1;
                    }
                }

                if(nuevaAltura < alturaActual) {
                    found = new String[][] {
                        {items.get(a).getType(), "" + items.get(a).getSize()},
                        {items.get(b).getType(), "" + items.get(b).getSize()}
                    };
                    stop = true;
                }
            }
        }

        return found;
    }
}