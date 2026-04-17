/**
 * Resuelve y simula el problema de la maraton de tazas.
 * Clase Tower se usa SOLO para simular, no para resolver.
 *
 * @author Daniel
 * @version 1.0
 */
public class TowerContest
{
    /**
     * Resuelve el problema: encuentra el orden de tazas para altura h.
     *
     * Idea clave:
     * La altura de la torre = suma de alturas de una "cadena" de tazas
     * que se apoyan unas en otras desde el suelo.
     * Hay dos casos:
     * CASO 1 (cup_n en la cadena): h = suma de alturas de las tazas en la cadena.
     * CASO 2 (cup_n como contenedor): h = 1 + suma de alturas de la cadena interior.
     *
     * @param n numero de tazas
     * @param h altura deseada
     * @return String con las alturas en orden, o "impossible"
     */
    public String solve(int n, long h)
    {
        if(!isPossible(n, h)) {
            return "impossible";
        }

        // CASO 1: cup_n en la cadena desde el suelo
        // h = (2n-1) + S1, donde S1 es suma de impares de {1,3,...,2n-3}
        long S1 = h - (2L * n - 1);
        int[] chain1 = subsetSumOdds(S1, n - 1);
        if(chain1 != null) {
            String result = buildOrder(n, chain1, false);
            return result;
        }

        // CASO 2: cup_n como contenedor exterior (va primero)
        // h = 1 + S2, donde S2 es suma de impares de {1,3,...,2n-3}
        long S2 = h - 1;
        int[] chain2 = subsetSumOdds(S2, n - 1);
        if(chain2 != null) {
            String result = buildOrder(n, chain2, true);
            return result;
        }

        return "impossible";
    }

    /**
     * Simula la solucion usando Tower para visualizar.
     * Tower se usa SOLO para mostrar, no para calcular.
     *
     * @param n numero de tazas
     * @param h altura deseada
     */
    public void simulate(int n, long h)
    {
        String solution = solve(n, h);

        if(solution.equals("impossible")) {
            System.out.println("impossible");
            return;
        }

        // crear torre con n tazas (constructor Tower(cups))
        Tower tower = new Tower(n);

        // parsear el orden de la solucion
        String[] parts = solution.split(" ");
        int[] targetOrder = new int[n];
        for(int i = 0; i < n; i++) {
            int altura = Integer.parseInt(parts[i]);
            // indice de la taza = (altura + 1) / 2
            targetOrder[i] = (altura + 1) / 2;
        }

        // reordenar la torre usando swaps
        for(int i = 0; i < n; i++) {
            String[][] items = tower.stackingItem();
            int currentAtPos = Integer.parseInt(items[i][1]);
            if(currentAtPos != targetOrder[i]) {
                String[] from = {"cup", "" + currentAtPos};
                String[] to   = {"cup", "" + targetOrder[i]};
                tower.swap(from, to);
            }
        }

        tower.makeVisible();
        System.out.println(solution);
        tower.makeInvisible();
    }

    // ---- METODOS PRIVADOS ----

    /**
     * Verifica si la altura h es posible con n tazas.
     * Valores imposibles: h < 2n-1, h > n^2, o h == n^2-2 (para n >= 3).
     */
    private boolean isPossible(int n, long h)
    {
        if(n <= 0) return false;
        if(n == 1) return h == 1;
        if(n == 2) return h == 3 || h == 4;
        return h >= (2L * n - 1) && h <= (long) n * n && h != (long) n * n - 2;
    }

    /**
     * Encuentra un subconjunto de {1, 3, 5, ..., 2k-1} que sume exactamente S.
     * Retorna los INDICES de los elementos seleccionados (1-based), o null si imposible.
     *
     * Propiedad: {1,3,...,2k-1} puede representar todos los valores de 0 a k^2
     * EXCEPTO k^2-2 (para k >= 2).
     */
    private int[] subsetSumOdds(long S, int k)
    {
        if(S == 0) return new int[0];
        if(S < 0 || k == 0) return null;
        if(k >= 2 && S == (long) k * k - 2) return null;
        if(S > (long) k * k) return null;

        // intentar tomar el elemento 2k-1 (indice k)
        long take = 2L * k - 1;
        if(S >= take) {
            int[] rest = subsetSumOdds(S - take, k - 1);
            if(rest != null) {
                int[] result = new int[rest.length + 1];
                result[0] = k;
                for(int i = 0; i < rest.length; i++) {
                    result[i + 1] = rest[i];
                }
                return result;
            }
        }

        // no tomar el elemento 2k-1, buscar con k-1 elementos
        return subsetSumOdds(S, k - 1);
    }

    /**
     * Construye el String de alturas en orden dado una cadena.
     *
     * @param n numero total de tazas
     * @param chainIndices indices de las tazas en la cadena (sin cup_n)
     * @param cupNFirst si true, cup_n va primero (CASO 2); si false, va al final de la cadena (CASO 1)
     */
    private String buildOrder(int n, int[] chainIndices, boolean cupNFirst)
    {
        // marcar cuales estan en la cadena
        boolean[] inChain = new boolean[n + 1];
        for(int idx : chainIndices) {
            inChain[idx] = true;
        }

        StringBuilder sb = new StringBuilder();

        if(cupNFirst) {
            // CASO 2: cup_n primero, luego cadena creciente, luego resto decreciente
            sb.append(2 * n - 1);

            // cadena en orden creciente
            for(int i = 1; i < n; i++) {
                if(inChain[i]) {
                    sb.append(" ").append(2 * i - 1);
                }
            }

            // resto en orden decreciente
            for(int i = n - 1; i >= 1; i--) {
                if(!inChain[i]) {
                    sb.append(" ").append(2 * i - 1);
                }
            }
        } else {
            // CASO 1: cadena (incluyendo cup_n) en orden creciente, luego resto decreciente
            // cadena en orden creciente (indices menores primero)
            for(int i = 1; i < n; i++) {
                if(inChain[i]) {
                    sb.append(2 * i - 1).append(" ");
                }
            }
            // cup_n al final de la cadena
            sb.append(2 * n - 1);

            // resto en orden decreciente
            for(int i = n - 1; i >= 1; i--) {
                if(!inChain[i]) {
                    sb.append(" ").append(2 * i - 1);
                }
            }
        }

        return sb.toString();
    }
}