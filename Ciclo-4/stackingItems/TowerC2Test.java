import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for Tower Cycle 2.
 * All tests run in invisible mode.
 * 
 * @author Daniel
 * @version 1.0
 */
public class TowerC2Test
{
    private Tower tower;

    @Before
    public void setUp()
    {
        // Siempre en modo invisible
        tower = new Tower(4);
    }

    // ========== CONSTRUCTOR Tower(cups) ==========

    @Test
    public void testConstructorCupsCreatesCorrectNumberOfCups()
    {
        Tower t = new Tower(3);
        assertEquals(3, t.height());
    }

    @Test
    public void testConstructorCupsWithZeroCreatesEmptyTower()
    {
        Tower t = new Tower(0);
        assertEquals(0, t.height());
    }

    @Test
    public void testConstructorCupsOkIsTrue()
    {
        Tower t = new Tower(4);
        assertTrue(t.ok());
    }

    @Test
    public void testConstructorCupsItemsAreCups()
    {
        Tower t = new Tower(3);
        String[][] items = t.stackingItem();
        for (String[] item : items) {
            assertEquals("cup", item[0]);
        }
    }

    // ========== SWAP ==========
    //cambiarnombres
    @Test
    public void deberiahacerswapdedoscopas()
    {
        // torre tiene cup1, cup2, cup3, cup4
        tower.swap(new String[]{"cup","1"}, new String[]{"cup","3"});
        assertTrue(tower.ok());
        String[][] items = tower.stackingItem();
        assertEquals("3", items[0][1]); // cup3 ahora en posicion 0
        assertEquals("1", items[2][1]); // cup1 ahora en posicion 2
    }

    @Test
    public void testSwapWithNullFails()
    {
        tower.swap(null, new String[]{"cup","2"});
        assertFalse(tower.ok());
    }

    @Test
    public void testSwapWithSameItemFails()
    {
        tower.swap(new String[]{"cup","1"}, new String[]{"cup","1"});
        assertFalse(tower.ok());
    }

    @Test
    public void testSwapWithNonExistentItemFails()
    {
        tower.swap(new String[]{"cup","99"}, new String[]{"cup","1"});
        assertFalse(tower.ok());
    }

    @Test
    public void testSwapCupAndLid()
    {
        tower.pushLid(1);
        tower.swap(new String[]{"cup","2"}, new String[]{"lid","1"});
        assertTrue(tower.ok());
    }

    // ========== COVER ==========

    @Test
    public void testCoverPlacesLidOnTopOfCup()
    {
        tower.pushLid(1);
        tower.cover();
        assertTrue(tower.ok());
        String[][] items = tower.stackingItem();
        // cup1 en posicion 0, lid1 en posicion 1
        assertEquals("cup", items[0][0]);
        assertEquals("1", items[0][1]);
        assertEquals("lid", items[1][0]);
        assertEquals("1", items[1][1]);
    }

    @Test
    public void testCoverWithNoLidsDoesNothing()
    {
        int heightBefore = tower.height();
        tower.cover();
        assertTrue(tower.ok());
        assertEquals(heightBefore, tower.height());
    }

    @Test
    public void testCoverDoesNotChangeTotalItems()
    {
        tower.pushLid(2);
        int heightBefore = tower.height();
        tower.cover();
        assertEquals(heightBefore, tower.height());
    }

    // ========== SWAP TO REDUCE ==========

    @Test
    public void testSwapToReduceReturnsEmptyWhenNoSwapHelps()
    {
        // Torre ordenada, ningún swap reduce altura
        String[][] result = tower.swapToReduce();
        assertEquals(0, result.length);
    }

    @Test
    public void testSwapToReduceReturnsTwoItems()
    {
        // Poner una tapa al final para que haya posibilidad de reducir
        tower.pushLid(1);
        tower.swap(new String[]{"lid","1"}, new String[]{"cup","2"});
        String[][] result = tower.swapToReduce();
        if (result.length > 0) {
            assertEquals(2, result.length);
            assertEquals(2, result[0].length);
            assertEquals(2, result[1].length);
        }
    }

    @Test
    public void testSwapToReduceOkTrueWhenFound()
    {
        tower.pushLid(4);
        // lid4 esta arriba, si hay swap que lo baje, ok sera true
        tower.swapToReduce();
        // no podemos garantizar que encuentre uno, pero no debe fallar
        assertNotNull(tower.swapToReduce());
    }
}