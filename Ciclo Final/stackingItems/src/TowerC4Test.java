import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas unitarias para el Ciclo 4.
 * Cubre los nuevos tipos de tazas y tapas.
 * Todas las pruebas corren en modo invisible.
 *
 * @author Daniel Cortes
 * @version 1.0
 */
public class TowerC4Test
{
    private Tower tower;

    @Before
    public void setUp()
    {
        tower = new Tower(5, 20);
    }

    // ========== NormalCup ==========

    @Test
    public void testQuePushCupNormalAgregaTazaCorrectamente()
    {
        tower.pushCup("normal", 1);
        assertTrue(tower.ok());
        assertEquals(1, tower.height());
    }

    @Test
    public void testQuePushCupNormalConTamanioDuplicadoFalla()
    {
        tower.pushCup("normal", 1);
        tower.pushCup("normal", 1);
        assertFalse(tower.ok());
        assertEquals(1, tower.height());
    }

    // ========== OpenerCup ==========

    @Test
    public void testQuePushCupOpenerEliminaLasTapasQueLeBloquean()
    {
        tower.pushCup("normal", 3);
        tower.pushLid("normal", 3);
        // hay una tapa de tamanio 3, opener de tamanio 2 debe eliminarla
        tower.pushCup("opener", 2);
        assertTrue(tower.ok());
        // la tapa de tamanio 3 debe haber sido eliminada
        String[][] items = tower.stackingItem();
        boolean lidFound = false;
        for(String[] item : items) {
            if(item[0].equals("lid") && item[1].equals("3")) {
                lidFound = true;
            }
        }
        assertFalse(lidFound);
    }

    @Test
    public void testQuePushCupOpenerSinTapasNoFalla()
    {
        tower.pushCup("normal", 3);
        tower.pushCup("opener", 2);
        assertTrue(tower.ok());
    }

    @Test
    public void testQuePushCupOpenerSeAgregaCorrectamente()
    {
        tower.pushCup("opener", 1);
        assertTrue(tower.ok());
        assertEquals(1, tower.height());
    }

    // ========== HierarchicalCup ==========

    @Test
    public void testQuePushCupHierarchicalSeAgregaCorrectamente()
    {
        tower.pushCup("hierarchical", 3);
        assertTrue(tower.ok());
        assertEquals(1, tower.height());
    }

    @Test
    public void testQuePushCupHierarchicalDesplazaMenores()
    {
        tower.pushCup("normal", 1);
        tower.pushCup("normal", 2);
        tower.pushCup("hierarchical", 3);
        assertTrue(tower.ok());
        // la hierarchical debe estar por encima de las menores
        String[][] items = tower.stackingItem();
        int indexH = -1;
        int indexC1 = -1;
        for(int i = 0; i < items.length; i++) {
            if(items[i][0].equals("cup") && items[i][1].equals("3")) indexH = i;
            if(items[i][0].equals("cup") && items[i][1].equals("1")) indexC1 = i;
        }
        assertTrue(indexH > indexC1);
    }

    @Test
    public void testQuePopCupHierarchicalQueNoLlegoAlFondoEsExitoso()
    {
        tower.pushCup("normal", 5);
        tower.pushCup("hierarchical", 3);
        tower.popCup();
        assertTrue(tower.ok());
    }

    @Test
    public void testQuePopCupHierarchicalQueAlcanzoElFondoFalla()
    {
        tower.pushCup("hierarchical", 1);
        // como no hay nada debajo, llega al fondo
        tower.popCup();
        assertFalse(tower.ok());
    }

    // ========== NormalLid ==========

    @Test
    public void testQuePushLidNormalAgregaTapaCorrectamente()
    {
        tower.pushCup("normal", 1);
        tower.pushLid("normal", 1);
        assertTrue(tower.ok());
        assertEquals(2, tower.height());
    }

    @Test
    public void testQuePushLidNormalSinTazaFalla()
    {
        tower.pushLid("normal", 1);
        assertFalse(tower.ok());
    }

    // ========== FearfulLid ==========

    @Test
    public void testQuePushLidFearfulConTazaEnTorreEsExitoso()
    {
        tower.pushCup("normal", 2);
        tower.pushLid("fearful", 2);
        assertTrue(tower.ok());
    }

    @Test
    public void testQuePopLidFearfulQueTapaASuTazaFalla()
    {
        tower.pushCup("normal", 2);
        tower.pushLid("fearful", 2);
        tower.popLid();
        assertFalse(tower.ok());
    }

    // ========== CrazyLid ==========

    @Test
    public void testQuePushLidCrazySeUbicaEnLaBase()
    {
        tower.pushCup("normal", 1);
        tower.pushCup("normal", 2);
        tower.pushLid("crazy", 1);
        assertTrue(tower.ok());
        // la crazy debe estar en la posicion 0
        String[][] items = tower.stackingItem();
        assertEquals("lid", items[0][0]);
        assertEquals("1", items[0][1]);
    }

    @Test
    public void testQuePushLidCrazyNoSeUbicaArriba()
    {
        tower.pushCup("normal", 1);
        tower.pushCup("normal", 2);
        tower.pushLid("crazy", 1);
        String[][] items = tower.stackingItem();
        // el ultimo elemento no debe ser la crazy
        int last = items.length - 1;
        assertFalse(items[last][0].equals("lid") && items[last][1].equals("1"));
    }

    // ========== StickyLid ==========

    @Test
    public void testQuePushLidStickySeAgregaCorrectamente()
    {
        tower.pushCup("normal", 1);
        tower.pushLid("sticky", 1);
        assertTrue(tower.ok());
        assertEquals(2, tower.height());
    }

    @Test
    public void testQuePopLidStickyFalla()
    {
        tower.pushCup("normal", 1);
        tower.pushLid("sticky", 1);
        tower.popLid();
        assertFalse(tower.ok());
    }

    @Test
    public void testQueRemoveLidStickyEsExitoso()
    {
        tower.pushCup("normal", 1);
        tower.pushLid("sticky", 1);
        // buscar el indice de la tapa
        String[][] items = tower.stackingItem();
        int lidIndex = -1;
        for(int i = 0; i < items.length; i++) {
            if(items[i][0].equals("lid") && items[i][1].equals("1")) {
                lidIndex = i;
            }
        }
        tower.removeLid(lidIndex);
        assertTrue(tower.ok());
        assertEquals(1, tower.height());
    }

    // ========== pushCup y pushLid con tipo invalido ==========

    @Test
    public void testQuePushCupConTipoInvalidoCreaTazaNormal()
    {
        tower.pushCup("desconocido", 1);
        assertTrue(tower.ok());
        assertEquals(1, tower.height());
    }

    @Test
    public void testQuePushLidConTipoInvalidoCreaTapaNormal()
    {
        tower.pushCup("normal", 1);
        tower.pushLid("desconocido", 1);
        assertTrue(tower.ok());
        assertEquals(2, tower.height());
    }
    
    @Test
    public void testQueRemoveCupHierarchicalQueAlcanzoElFondoFalla()
    {
        tower.pushCup("hierarchical", 1);
        // como no hay nada debajo, llega al fondo
        tower.removeCup(0);
        assertFalse(tower.ok());
        assertEquals(1, tower.height());
    }
}