import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas de caja negra colectivas para el Ciclo 4.
 * Verifica el comportamiento del simulador con los nuevos tipos
 * de tazas y tapas desde el punto de vista del usuario.
 *
 * @author Daniel Cortes
 * @version 1.0
 */
public class TowerCC4test
{
    private Tower tower;

    @Before
    public void setUp()
    {
        tower = new Tower(5, 20);
    }

    // ========== INTERACCION ENTRE TIPOS ==========

    @Test
    public void testQueOpenerCupEliminaMultiplesTapasBlockeantes()
    {
        tower.pushCup("normal", 3);
        tower.pushCup("normal", 4);
        tower.pushLid("normal", 3);
        tower.pushLid("normal", 4);
        // opener de tamanio 2 debe eliminar tapas de tamanio >= 2
        tower.pushCup("opener", 2);
        assertTrue(tower.ok());
        String[][] items = tower.stackingItem();
        for(String[] item : items) {
            assertFalse(item[0].equals("lid"));
        }
    }

    @Test
    public void testQueHierarchicalCupNoPuedeQuitarseSiLlegoAlFondo()
    {
        tower.pushCup("hierarchical", 1);
        int heightAntes = tower.height();
        tower.popCup();
        assertFalse(tower.ok());
        assertEquals(heightAntes, tower.height());
    }

    @Test
    public void testQueCrazyLidYNormalLidPuedenCoexistir()
    {
        tower.pushCup("normal", 1);
        tower.pushCup("normal", 2);
        tower.pushLid("normal", 2);
        tower.pushLid("crazy", 1);
        assertTrue(tower.ok());
        assertEquals(4, tower.height());
    }

    @Test
    public void testQueStickyLidNoAfectaPopDeOtrasTapas()
    {
        tower.pushCup("normal", 1);
        tower.pushCup("normal", 2);
        tower.pushLid("sticky", 1);
        tower.pushLid("normal", 2);
        // popLid debe quitar la normal, no la sticky
        tower.popLid();
        assertTrue(tower.ok());
        assertEquals(3, tower.height());
    }

    @Test
    public void testQueFearfulLidPuedeQuitarseSiNoEstaTapandoSuTaza()
    {
        tower.pushCup("normal", 2);
        tower.pushLid("fearful", 2);
        // mover la fearful lid con swap para que no este tapando su taza
        tower.pushCup("normal", 3);
        tower.swap(new String[]{"lid","2"}, new String[]{"cup","3"});
        // ahora la fearful no esta tapando su taza, deberia poder salir
        tower.popLid();
        assertTrue(tower.ok());
    }

    // ========== ALTURA DE LA TORRE ==========

    @Test
    public void testQueAlturaCreceCorrctamenteConNuevosTipos()
    {
        tower.pushCup("normal", 1);
        tower.pushCup("opener", 2);
        tower.pushCup("hierarchical", 3);
        assertEquals(3, tower.height());
    }

    @Test
    public void testQueAlturaDisminuyeAlQuitarTazaNormal()
    {
        tower.pushCup("normal", 1);
        tower.pushCup("opener", 2);
        tower.popCup();
        assertTrue(tower.ok());
        assertEquals(1, tower.height());
    }

    // ========== STACKING ITEMS ==========

    @Test
    public void testQueStackingItemRetornaTiposCorrctosParaNuevosTipos()
    {
        tower.pushCup("normal", 1);
        tower.pushCup("opener", 2);
        tower.pushLid("sticky", 1);
        String[][] items = tower.stackingItem();
        assertEquals("cup", items[0][0]);
        assertEquals("cup", items[1][0]);
        assertEquals("lid", items[2][0]);
    }

    @Test
    public void testQueStackingItemRetornaTamaniosCorrctosParaNuevosTipos()
    {
        tower.pushCup("normal", 1);
        tower.pushCup("opener", 2);
        tower.pushLid("sticky", 1);
        String[][] items = tower.stackingItem();
        assertEquals("1", items[0][1]);
        assertEquals("2", items[1][1]);
        assertEquals("1", items[2][1]);
    }

    // ========== OK ==========

    @Test
    public void testQueOkEsTrueDepuesDeOperacionesExitosasConNuevosTipos()
    {
        tower.pushCup("opener", 1);
        tower.pushCup("hierarchical", 2);
        tower.pushLid("crazy", 1);
        assertTrue(tower.ok());
    }

    @Test
    public void testQueOkEsFalseAlIntentarQuitarHierarchicalDelFondo()
    {
        tower.pushCup("hierarchical", 1);
        tower.popCup();
        assertFalse(tower.ok());
    }

    @Test
    public void testQueOkEsFalseAlIntentarPopLidStickyLid()
    {
        tower.pushCup("normal", 1);
        tower.pushLid("sticky", 1);
        tower.popLid();
        assertFalse(tower.ok());
    }

    // ========== CREADOR MASIVO SOLO USA NORMALES ==========

    @Test
    public void testQueConstructorMasivoSoloCreaTagzasNormales()
    {
        Tower t = new Tower(3);
        String[][] items = t.stackingItem();
        assertEquals(3, items.length);
        for(String[] item : items) {
            assertEquals("cup", item[0]);
        }
        assertTrue(t.ok());
    }

    // ========== LISTEDCUPS ==========

    @Test
    public void testQueLikedCupsContabilizaCorrectamenteConNuevosTipos()
    {
        tower.pushCup("normal", 1);
        tower.pushCup("opener", 2);
        tower.pushCup("hierarchical", 3);
        tower.pushLid("normal", 1);
        tower.pushLid("sticky", 2);
        // no lanza excepcion
        try {
            tower.listedCups();
            assertTrue(true);
        } catch(Exception e) {
            fail("listedCups() lanzo excepcion: " + e.getMessage());
        }
    }
}