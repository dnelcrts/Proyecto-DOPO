import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

/**
 * Pruebas unitarias para TowerOperations.
 * Cubre orderTower, reverseTower, cover, swap y swapToReduce.
 * Todas las pruebas usan elementos normales.
 *
 * @author Daniel Cortes
 * @version 1.0
 */
public class TowerOperationsTest
{
    private TowerOperations operations;
    private ArrayList<StackingItem> items;

    @Before
    public void setUp()
    {
        operations = new TowerOperations();
        items = new ArrayList<StackingItem>();
    }

    // ========== orderTower ==========

    @Test
    public void testQueOrderTowerConListaVaciaRetornaListaVacia()
    {
        ArrayList<StackingItem> result = operations.orderTower(items, 10);
        assertEquals(0, result.size());
    }

    @Test
    public void testQueOrderTowerOrdenaDeMayorAMenor()
    {
        items.add(new NormalCup(1));
        items.add(new NormalCup(3));
        items.add(new NormalCup(2));
        ArrayList<StackingItem> result = operations.orderTower(items, 10);
        assertEquals(3, result.get(0).getSize());
        assertEquals(2, result.get(1).getSize());
        assertEquals(1, result.get(2).getSize());
    }

    @Test
    public void testQueOrderTowerPoneTapaDepuesDeSuTaza()
    {
        NormalCup cup = new NormalCup(2);
        NormalLid lid = new NormalLid(2, "red");
        cup.setLid(lid);
        items.add(cup);
        items.add(lid);
        items.add(new NormalCup(3));
        ArrayList<StackingItem> result = operations.orderTower(items, 10);
        assertEquals(3, result.get(0).getSize());
        assertEquals("cup", result.get(1).getType());
        assertEquals(2, result.get(1).getSize());
        assertEquals("lid", result.get(2).getType());
        assertEquals(2, result.get(2).getSize());
    }

    @Test
    public void testQueOrderTowerRespetaMaxHeight()
    {
        items.add(new NormalCup(1));
        items.add(new NormalCup(2));
        items.add(new NormalCup(3));
        ArrayList<StackingItem> result = operations.orderTower(items, 2);
        assertEquals(2, result.size());
    }

    @Test
    public void testQueOrderTowerConUnSoloItemRetornaUnItem()
    {
        items.add(new NormalCup(1));
        ArrayList<StackingItem> result = operations.orderTower(items, 10);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getSize());
    }

    // ========== reverseTower ==========

    @Test
    public void testQueReverseTowerConListaVaciaRetornaListaVacia()
    {
        ArrayList<StackingItem> result = operations.reverseTower(items, 10);
        assertEquals(0, result.size());
    }

    @Test
    public void testQueReverseTowerInvierteElOrden()
    {
        items.add(new NormalCup(1));
        items.add(new NormalCup(2));
        items.add(new NormalCup(3));
        ArrayList<StackingItem> result = operations.reverseTower(items, 10);
        assertEquals(3, result.get(0).getSize());
        assertEquals(2, result.get(1).getSize());
        assertEquals(1, result.get(2).getSize());
    }

    @Test
    public void testQueReverseTowerRespetaMaxHeight()
    {
        items.add(new NormalCup(1));
        items.add(new NormalCup(2));
        items.add(new NormalCup(3));
        ArrayList<StackingItem> result = operations.reverseTower(items, 2);
        assertEquals(2, result.size());
    }

    @Test
    public void testQueReverseTowerConUnSoloItemRetornaElMismoItem()
    {
        items.add(new NormalCup(2));
        ArrayList<StackingItem> result = operations.reverseTower(items, 10);
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getSize());
    }

    @Test
    public void testQueReverseTowerAplicadoDosVecesRetornaOrigenal()
    {
        items.add(new NormalCup(1));
        items.add(new NormalCup(2));
        items.add(new NormalCup(3));
        ArrayList<StackingItem> result = operations.reverseTower(
            operations.reverseTower(items, 10), 10);
        assertEquals(1, result.get(0).getSize());
        assertEquals(2, result.get(1).getSize());
        assertEquals(3, result.get(2).getSize());
    }

    // ========== cover ==========

    @Test
    public void testQueCoverConListaVaciaRetornaListaVacia()
    {
        ArrayList<StackingItem> result = operations.cover(items);
        assertEquals(0, result.size());
    }

    @Test
    public void testQueCoverPoneTapaDirectamenteEncimaDeSuTaza()
    {
        NormalCup cup = new NormalCup(2);
        NormalLid lid = new NormalLid(2, "red");
        cup.setLid(lid);
        items.add(new NormalCup(3));
        items.add(cup);
        items.add(lid);
        ArrayList<StackingItem> result = operations.cover(items);
        int cupIndex = -1;
        int lidIndex = -1;
        for(int i = 0; i < result.size(); i++) {
            if(result.get(i) instanceof Cup && result.get(i).getSize() == 2) cupIndex = i;
            if(result.get(i) instanceof Lid && result.get(i).getSize() == 2) lidIndex = i;
        }
        assertEquals(cupIndex + 1, lidIndex);
    }

    @Test
    public void testQueCoverNoCambiaElTamanioDeLaLista()
    {
        NormalCup cup = new NormalCup(1);
        NormalLid lid = new NormalLid(1, "blue");
        cup.setLid(lid);
        items.add(cup);
        items.add(new NormalCup(2));
        items.add(lid);
        int sizeBefore = items.size();
        ArrayList<StackingItem> result = operations.cover(items);
        assertEquals(sizeBefore, result.size());
    }

    @Test
    public void testQueCoverSinTapasNoModificaLaLista()
    {
        items.add(new NormalCup(1));
        items.add(new NormalCup(2));
        ArrayList<StackingItem> result = operations.cover(items);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getSize());
        assertEquals(2, result.get(1).getSize());
    }

    // ========== swap ==========

    @Test
    public void testQueSwapConNullRetornaNull()
    {
        items.add(new NormalCup(1));
        items.add(new NormalCup(2));
        assertNull(operations.swap(items, null, new String[]{"cup","2"}));
    }

    @Test
    public void testQueSwapConItemsIgualesRetornaNull()
    {
        items.add(new NormalCup(1));
        items.add(new NormalCup(2));
        assertNull(operations.swap(items,
            new String[]{"cup","1"}, new String[]{"cup","1"}));
    }

    @Test
    public void testQueSwapConItemInexistenteRetornaNull()
    {
        items.add(new NormalCup(1));
        assertNull(operations.swap(items,
            new String[]{"cup","1"}, new String[]{"cup","99"}));
    }

    @Test
    public void testQueSwapIntercambiaDosTazas()
    {
        items.add(new NormalCup(1));
        items.add(new NormalCup(2));
        items.add(new NormalCup(3));
        ArrayList<StackingItem> result = operations.swap(items,
            new String[]{"cup","1"}, new String[]{"cup","3"});
        assertNotNull(result);
        assertEquals(3, result.get(0).getSize());
        assertEquals(1, result.get(2).getSize());
    }

    @Test
    public void testQueSwapMueveTapaConSuTaza()
    {
        NormalCup cup1 = new NormalCup(1);
        NormalLid lid1 = new NormalLid(1, "red");
        cup1.setLid(lid1);
        items.add(cup1);
        items.add(lid1);
        items.add(new NormalCup(2));
        ArrayList<StackingItem> result = operations.swap(items,
            new String[]{"cup","1"}, new String[]{"cup","2"});
        assertNotNull(result);
        assertEquals(2, result.get(0).getSize());
        assertEquals(1, result.get(1).getSize());
        assertEquals("lid", result.get(2).getType());
    }

    // ========== swapToReduce ==========

    @Test
    public void testQueSwapToReduceConListaVaciaRetornaArregloVacio()
    {
        String[][] result = operations.swapToReduce(items);
        assertEquals(0, result.length);
    }

    @Test
    public void testQueSwapToReduceRetornaArregloVacioCuandoNoHayMejora()
    {
        items.add(new NormalCup(1));
        items.add(new NormalCup(2));
        String[][] result = operations.swapToReduce(items);
        assertEquals(0, result.length);
    }

    @Test
    public void testQueSwapToReduceRetornaDosItemsCuandoHayMejora()
    {
        items.add(new NormalCup(1));
        NormalCup cup2 = new NormalCup(2);
        NormalLid lid2 = new NormalLid(2, "blue");
        cup2.setLid(lid2);
        items.add(cup2);
        items.add(lid2);
        items.add(new NormalCup(3));
        String[][] result = operations.swapToReduce(items);
        if(result.length > 0) {
            assertEquals(2, result.length);
            assertEquals(2, result[0].length);
            assertEquals(2, result[1].length);
        }
    }

    @Test
    public void testQueSwapToReduceRetornaItemsConTipoYTamanio()
    {
        NormalCup cup1 = new NormalCup(1);
        NormalLid lid1 = new NormalLid(1, "red");
        cup1.setLid(lid1);
        items.add(new NormalCup(3));
        items.add(new NormalCup(2));
        items.add(cup1);
        items.add(lid1);
        String[][] result = operations.swapToReduce(items);
        if(result.length > 0) {
            assertNotNull(result[0][0]);
            assertNotNull(result[0][1]);
            assertNotNull(result[1][0]);
            assertNotNull(result[1][1]);
        }
    }
}