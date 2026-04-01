import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas de caja negra para TowerContest.
 * Verifica que solve() retorna una solucion valida
 * para cada caso de entrada del problema de la maraton.
 *
 * @author Daniel
 * @version 1.0
 */
public class TowerContestCTest
{
    private TowerContest contest;

    @Before
    public void setUp()
    {
        contest = new TowerContest();
    }

    /**
     * Verifica que el orden retornado por solve() produce la altura h correcta.
     */
    private long calcularAltura(int[] orden)
    {
        int n = orden.length;
        int[] indices = new int[n];
        int[] bases   = new int[n];
        int[] topes   = new int[n];
        int placedCount = 0;
        long torreAltura = 0;

        for(int pos = 0; pos < n; pos++) {
            int alturaActual = orden[pos];
            int idxActual = (alturaActual + 1) / 2;

            int mejorIdx  = -1;
            int mejorTope = -1;
            for(int j = 0; j < placedCount; j++) {
                if(indices[j] < idxActual && indices[j] > mejorIdx) {
                    mejorIdx  = indices[j];
                    mejorTope = topes[j];
                }
            }

            int piso;
            if(mejorIdx >= 0) {
                piso = mejorTope;
            } else {
                int contIdx  = Integer.MAX_VALUE;
                int contBase = 0;
                for(int j = 0; j < placedCount; j++) {
                    if(indices[j] > idxActual && indices[j] < contIdx) {
                        contIdx  = indices[j];
                        contBase = bases[j];
                    }
                }
                piso = (contIdx < Integer.MAX_VALUE) ? contBase + 1 : 0;
            }

            int tope = piso + alturaActual;
            indices[placedCount] = idxActual;
            bases[placedCount]   = piso;
            topes[placedCount]   = tope;
            placedCount++;

            if(tope > torreAltura) torreAltura = tope;
        }
        return torreAltura;
    }

    private boolean esResultadoValido(int n, long h)
    {
        String result = contest.solve(n, h);
        if(result.equals("impossible")) return false;

        String[] parts = result.split(" ");
        if(parts.length != n) return false;

        boolean[] found = new boolean[n + 1];
        int[] orden = new int[n];
        for(int i = 0; i < n; i++) {
            int altura = Integer.parseInt(parts[i]);
            int indice = (altura + 1) / 2;
            if(indice < 1 || indice > n) return false;
            if(found[indice]) return false;
            found[indice] = true;
            orden[i] = altura;
        }

        return calcularAltura(orden) == h;
    }

    // ---- CASOS OFICIALES DEL ICPC ----

    @Test
    public void testQuesolveConN4H9ProduceOrdenQueDaAltura9()
    {
        assertTrue(esResultadoValido(4, 9));
    }

    @Test
    public void testQuesolveConN4H100RetornaImpossible()
    {
        assertEquals("impossible", contest.solve(4, 100));
    }

    // ---- CASOS DE ALTURA MINIMA ----

    @Test
    public void testQuesolveConN1YAlturaMinimaProduceOrdenCorrecto()
    {
        assertTrue(esResultadoValido(1, 1));
    }

    @Test
    public void testQuesolveConN2YAlturaMinimaProduceOrdenCorrecto()
    {
        assertTrue(esResultadoValido(2, 3));
    }

    @Test
    public void testQuesolveConN3YAlturaMinimaProduceOrdenCorrecto()
    {
        assertTrue(esResultadoValido(3, 5));
    }

    @Test
    public void testQuesolveConN4YAlturaMinimaProduceOrdenCorrecto()
    {
        assertTrue(esResultadoValido(4, 7));
    }

    @Test
    public void testQuesolveConN5YAlturaMinimaProduceOrdenCorrecto()
    {
        assertTrue(esResultadoValido(5, 9));
    }

    // ---- CASOS DE ALTURA MAXIMA ----

    @Test
    public void testQuesolveConN1YAlturaMaximaProduceOrdenCorrecto()
    {
        assertTrue(esResultadoValido(1, 1));
    }

    @Test
    public void testQuesolveConN2YAlturaMaximaProduceOrdenCorrecto()
    {
        assertTrue(esResultadoValido(2, 4));
    }

    @Test
    public void testQuesolveConN3YAlturaMaximaProduceOrdenCorrecto()
    {
        assertTrue(esResultadoValido(3, 9));
    }

    @Test
    public void testQuesolveConN4YAlturaMaximaProduceOrdenCorrecto()
    {
        assertTrue(esResultadoValido(4, 16));
    }

    @Test
    public void testQuesolveConN5YAlturaMaximaProduceOrdenCorrecto()
    {
        assertTrue(esResultadoValido(5, 25));
    }

    // ---- CASOS IMPOSIBLES ----

    @Test
    public void testQuesolveConAlturaMenorQueMinimoRetornaImpossible()
    {
        assertEquals("impossible", contest.solve(4, 6));
    }

    @Test
    public void testQuesolveConAlturaMayorQueMaximoRetornaImpossible()
    {
        assertEquals("impossible", contest.solve(4, 17));
    }

    @Test
    public void testQuesolveConUnicoImposibleEnRangoN3RetornaImpossible()
    {
        assertEquals("impossible", contest.solve(3, 7));
    }

    @Test
    public void testQuesolveConUnicoImposibleEnRangoN4RetornaImpossible()
    {
        assertEquals("impossible", contest.solve(4, 14));
    }

    @Test
    public void testQuesolveConUnicoImposibleEnRangoN5RetornaImpossible()
    {
        assertEquals("impossible", contest.solve(5, 23));
    }

    // ---- CASOS INTERMEDIOS ----

    @Test
    public void testQuesolveConN4H8ProduceOrdenQueDaAltura8()
    {
        assertTrue(esResultadoValido(4, 8));
    }

    @Test
    public void testQuesolveConN4H11ProduceOrdenQueDaAltura11()
    {
        assertTrue(esResultadoValido(4, 11));
    }

    @Test
    public void testQuesolveConN4H13ProduceOrdenQueDaAltura13()
    {
        assertTrue(esResultadoValido(4, 13));
    }

    @Test
    public void testQuesolveConN5H15ProduceOrdenQueDaAltura15()
    {
        assertTrue(esResultadoValido(5, 15));
    }

    @Test
    public void testQuesolveConN5H18ProduceOrdenQueDaAltura18()
    {
        assertTrue(esResultadoValido(5, 18));
    }

    @Test
    public void testQuesolveConN5H20ProduceOrdenQueDaAltura20()
    {
        assertTrue(esResultadoValido(5, 20));
    }

    @Test
    public void testQuesolveConN6H19ProduceOrdenQueDaAltura19()
    {
        assertTrue(esResultadoValido(6, 19));
    }

    @Test
    public void testQuesolveConN6H29ProduceOrdenQueDaAltura29()
    {
        assertTrue(esResultadoValido(6, 29));
    }
}