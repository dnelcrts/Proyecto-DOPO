import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas unitarias para TowerContest.
 * Cubren los metodos solve() y simulate().
 *
 * @author Daniel
 * @version 1.0
 */
public class TowerContestTest
{
    private TowerContest contest;

    @Before
    public void setUp()
    {
        contest = new TowerContest();
    }

    // ---- PRUEBAS PARA solve() ----

    @Test
    public void testQuesolveConN1YH1RetornaResultadoValido()
    {
        String result = contest.solve(1, 1);
        assertNotNull(result);
        assertFalse(result.equals("impossible"));
    }

    @Test
    public void testQuesolveConN1YH2RetornaImpossible()
    {
        String result = contest.solve(1, 2);
        assertEquals("impossible", result);
    }

    @Test
    public void testQuesolveConEjemploOficialN4H9RetornaResultadoValido()
    {
        String result = contest.solve(4, 9);
        assertNotNull(result);
        assertFalse(result.equals("impossible"));
    }

    @Test
    public void testQuesolveConEjemploOficialN4H100RetornaImpossible()
    {
        String result = contest.solve(4, 100);
        assertEquals("impossible", result);
    }

    @Test
    public void testQuesolveConAlturaMinimaRetornaResultadoValido()
    {
        // Para n=5, altura minima = 2*5-1 = 9
        String result = contest.solve(5, 9);
        assertNotNull(result);
        assertFalse(result.equals("impossible"));
    }

    @Test
    public void testQuesolveConAlturaMaximaRetornaResultadoValido()
    {
        // Para n=5, altura maxima = 5^2 = 25
        String result = contest.solve(5, 25);
        assertNotNull(result);
        assertFalse(result.equals("impossible"));
    }

    @Test
    public void testQuesolveConUnicoImposibleEnRangoRetornaImpossible()
    {
        // Para n=5, el unico imposible en rango es 25-2=23
        String result = contest.solve(5, 23);
        assertEquals("impossible", result);
    }

    @Test
    public void testQuesolveConAlturaMenorQueMinimoRetornaImpossible()
    {
        // Para n=4, minimo=7. h=6 debe ser imposible.
        String result = contest.solve(4, 6);
        assertEquals("impossible", result);
    }

    @Test
    public void testQuesolveConAlturaMayorQueMaximoRetornaImpossible()
    {
        // Para n=4, maximo=16. h=17 debe ser imposible.
        String result = contest.solve(4, 17);
        assertEquals("impossible", result);
    }

    @Test
    public void testQuesolveRetornaNAlturasEnElResultado()
    {
        int n = 4;
        String result = contest.solve(n, 9);
        assertFalse(result.equals("impossible"));
        String[] parts = result.split(" ");
        assertEquals(n, parts.length);
    }

    @Test
    public void testQuesolveRetornaTodasLasTazasSinRepetir()
    {
        int n = 4;
        String result = contest.solve(n, 9);
        assertFalse(result.equals("impossible"));
        String[] parts = result.split(" ");

        boolean[] found = new boolean[n + 1];
        for(String part : parts) {
            int altura = Integer.parseInt(part);
            int indice = (altura + 1) / 2;
            assertTrue("Altura invalida: " + altura, indice >= 1 && indice <= n);
            assertFalse("Taza repetida: " + altura, found[indice]);
            found[indice] = true;
        }

        for(int i = 1; i <= n; i++) {
            assertTrue("Falta la taza " + i, found[i]);
        }
    }

    @Test
    public void testQuesolveConN2RetornaResultadoValidoParaAmbasAlturas()
    {
        assertFalse(contest.solve(2, 3).equals("impossible"));
        assertFalse(contest.solve(2, 4).equals("impossible"));
    }

    @Test
    public void testQuesolveConN2YAlturaFueraDeRangoRetornaImpossible()
    {
        assertEquals("impossible", contest.solve(2, 2));
        assertEquals("impossible", contest.solve(2, 5));
    }

    @Test
    public void testQuesolveConAlturasCeroRetornaImpossible()
    {
        assertEquals("impossible", contest.solve(3, 0));
    }

    // ---- PRUEBAS PARA simulate() ----

    @Test
    public void testQuesimulateConCasoValidoNoLanzaExcepcion()
    {
        try {
            contest.simulate(4, 9);
        } catch(Exception e) {
            fail("simulate() lanzo excepcion inesperada: " + e.getMessage());
        }
    }

    @Test
    public void testQuesimulateConCasoImposibleNoLanzaExcepcion()
    {
        try {
            contest.simulate(4, 100);
        } catch(Exception e) {
            fail("simulate() con impossible lanzo excepcion: " + e.getMessage());
        }
    }

    @Test
    public void testQuesimulateConN1NoLanzaExcepcion()
    {
        try {
            contest.simulate(1, 1);
        } catch(Exception e) {
            fail("simulate() con n=1 lanzo excepcion: " + e.getMessage());
        }
    }

    @Test
    public void testQuesimulateConAlturaMinimaNoLanzaExcepcion()
    {
        try {
            contest.simulate(3, 5);
        } catch(Exception e) {
            fail("simulate() con altura minima lanzo excepcion: " + e.getMessage());
        }
    }
}