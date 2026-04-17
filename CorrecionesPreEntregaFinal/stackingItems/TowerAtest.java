import javax.swing.JOptionPane;

/**
 * Pruebas de aceptacion para el Ciclo 4.
 * Evidencian los comportamientos mas importantes del simulador.
 * Cada prueba muestra la simulacion visualmente y pregunta al usuario
 * si la acepta.
 *
 * @author Daniel Cortes
 * @version 1.0
 */
public class TowerAtest
{
    /**
     * Prueba de aceptacion 1:
     * Demuestra el comportamiento de OpenerCup y HierarchicalCup.
     * Una OpenerCup elimina tapas al entrar.
     * Una HierarchicalCup desplaza elementos menores y no puede
     * quitarse si llego al fondo.
     */
    public void testAceptacionNuevosTiposDeTazas()
    {
        Tower tower = new Tower(5, 20);
        tower.makeVisible();

        pausa(1000);

        // Agregar tazas normales
        tower.pushCup("normal", 1);
        pausa(800);
        tower.pushCup("normal", 3);
        pausa(800);
        tower.pushCup("normal", 5);
        pausa(800);

        // Agregar tapas normales
        tower.pushLid("normal", 1);
        pausa(800);
        tower.pushLid("normal", 3);
        pausa(800);

        JOptionPane.showMessageDialog(null,
            "Se agregaron tazas normales (1, 3, 5) y tapas (1, 3).\n" +
            "Ahora entra una OpenerCup de tamanio 2.\n" +
            "Debe eliminar las tapas que le bloquean el paso.");
        pausa(500);

        // OpenerCup elimina tapas bloqueantes
        tower.pushCup("opener", 2);
        pausa(1000);

        JOptionPane.showMessageDialog(null,
            "La OpenerCup elimino las tapas bloqueantes.\n" +
            "Ahora entra una HierarchicalCup de tamanio 4.\n" +
            "Debe desplazar los elementos de menor tamanio.");
        pausa(500);

        // HierarchicalCup desplaza menores
        tower.pushCup("hierarchical", 4);
        pausa(1000);

        JOptionPane.showMessageDialog(null,
            "La HierarchicalCup se ubico sobre los elementos menores.\n" +
            "Ahora intentamos quitarla con popCup.\n" +
            "Como no llego al fondo, debe poder quitarse.");
        pausa(500);

        tower.popCup();
        pausa(1000);

        // Ahora agregar hierarchical que llega al fondo
        tower.pushCup("hierarchical", 1);
        pausa(800);

        // No hay elementos debajo, llega al fondo
        JOptionPane.showMessageDialog(null,
            "La HierarchicalCup llego al fondo de la torre.\n" +
            "Ahora intentamos quitarla con popCup.\n" +
            "NO debe poder quitarse.");
        pausa(500);

        tower.popCup();
        pausa(800);

        int respuesta = JOptionPane.showConfirmDialog(null,
            "¿La simulacion de OpenerCup y HierarchicalCup\n" +
            "se comporto correctamente?",
            "Prueba de Aceptacion 1",
            JOptionPane.YES_NO_OPTION);

        if(respuesta == JOptionPane.YES_OPTION) {
            System.out.println("Prueba de aceptacion 1: ACEPTADA");
        } else {
            System.out.println("Prueba de aceptacion 1: RECHAZADA");
        }

        tower.makeInvisible();
    }

    /**
     * Prueba de aceptacion 2:
     * Demuestra el comportamiento de FearfulLid, CrazyLid y StickyLid.
     * FearfulLid no sale si esta tapando su taza.
     * CrazyLid se ubica en la base.
     * StickyLid solo puede quitarse con removeLid.
     */
    public void testAceptacionNuevosTiposDeTapas()
    {
        Tower tower = new Tower(5, 20);
        tower.makeVisible();

        pausa(1000);

        // Agregar tazas base
        tower.pushCup("normal", 1);
        pausa(800);
        tower.pushCup("normal", 2);
        pausa(800);
        tower.pushCup("normal", 3);
        pausa(800);

        JOptionPane.showMessageDialog(null,
            "Se agregaron tres tazas normales (1, 2, 3).\n" +
            "Ahora agregamos una CrazyLid para la taza 1.\n" +
            "Debe ubicarse en la BASE de la torre, no arriba.");
        pausa(500);

        // CrazyLid va a la base
        tower.pushLid("crazy", 1);
        pausa(1000);

        JOptionPane.showMessageDialog(null,
            "La CrazyLid esta en la base de la torre.\n" +
            "Ahora agregamos una FearfulLid para la taza 2.\n" +
            "Intentaremos quitarla con popLid.\n" +
            "NO debe poder salir porque esta tapando su taza.");
        pausa(500);

        // FearfulLid no puede salir si tapa su taza
        tower.pushLid("fearful", 2);
        pausa(800);
        tower.popLid();
        pausa(800);

        JOptionPane.showMessageDialog(null,
            "La FearfulLid NO pudo salir.\n" +
            "Ahora agregamos una StickyLid para la taza 3.\n" +
            "Intentaremos quitarla con popLid.\n" +
            "NO debe poder salir con popLid.");
        pausa(500);

        // StickyLid no puede salir con popLid
        tower.pushLid("sticky", 3);
        pausa(800);
        tower.popLid();
        pausa(800);

        JOptionPane.showMessageDialog(null,
            "La StickyLid NO pudo salir con popLid.\n" +
            "Ahora la quitamos con removeLid.\n" +
            "Esta vez SI debe poder salir.");
        pausa(500);

        // StickyLid si puede salir con removeLid
        String[][] items = tower.stackingItem();
        int stickyIndex = -1;
        for(int i = 0; i < items.length; i++) {
            if(items[i][0].equals("lid") && items[i][1].equals("3")) {
                stickyIndex = i;
            }
        }
        if(stickyIndex != -1) {
            tower.removeLid(stickyIndex);
        }
        pausa(1000);

        int respuesta = JOptionPane.showConfirmDialog(null,
            "¿La simulacion de FearfulLid, CrazyLid y StickyLid\n" +
            "se comporto correctamente?",
            "Prueba de Aceptacion 2",
            JOptionPane.YES_NO_OPTION);

        if(respuesta == JOptionPane.YES_OPTION) {
            System.out.println("Prueba de aceptacion 2: ACEPTADA");
        } else {
            System.out.println("Prueba de aceptacion 2: RECHAZADA");
        }

        tower.makeInvisible();
    }

    /**
     * Ejecuta ambas pruebas de aceptacion.
     */
    public void runAll()
    {
        testAceptacionNuevosTiposDeTazas();
        testAceptacionNuevosTiposDeTapas();
    }

    // Pausa en milisegundos para hacer la simulacion visible
    private void pausa(int ms)
    {
        try {
            Thread.sleep(ms);
        } catch(InterruptedException e) {
            // ignorar
        }
    }
}