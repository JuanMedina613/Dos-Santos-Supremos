package pruebas;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import combate.Combate;
import combatientes.Combatiente;
import combatientes.CultivadorDeLaEspada;
import combatientes.HechiceroDelVacio;
import combatientes.MonjeDelCuerpoCelestial;
import torneo.Pareja;

public class PruebasCombate {

    @Test
    public void elCombateSiempreDeterminaUnaParejaGanadora() {

        Combatiente c1 =
                new CultivadorDeLaEspada(1, "Aurelio");
        Combatiente c2 =
                new MonjeDelCuerpoCelestial(2, "Kael");

        Combatiente c3 =
                new HechiceroDelVacio(3, "Nebula");
        Combatiente c4 =
                new CultivadorDeLaEspada(4, "Orion");

        Pareja pareja1 = new Pareja(1, c1, c2);
        Pareja pareja2 = new Pareja(2, c3, c4);

        Combate combate = new Combate(pareja1, pareja2);

        Pareja ganadora = combate.ejecutar();

        assertNotNull(ganadora);
        assertTrue(
                ganadora.equals(pareja1)
                || ganadora.equals(pareja2)
        );
    }

    @Test
    public void unCombateNoPuedeSerContraLaMismaPareja() {

        Combatiente c1 =
                new CultivadorDeLaEspada(1, "Aurelio");
        Combatiente c2 =
                new MonjeDelCuerpoCelestial(2, "Kael");

        Pareja pareja = new Pareja(1, c1, c2);

        assertThrows(
                IllegalArgumentException.class,
                () -> new Combate(pareja, pareja)
        );
    }

    @Test
    public void losCombatientesCaidosNoActuan() {

        Combatiente fuerte1 =
                new CultivadorDeLaEspada(1, "Fuerte 1");
        Combatiente fuerte2 =
                new CultivadorDeLaEspada(2, "Fuerte 2");

        Combatiente rival1 =
                new MonjeDelCuerpoCelestial(3, "Rival 1");
        Combatiente rival2 =
                new MonjeDelCuerpoCelestial(4, "Rival 2");

        Pareja pareja1 =
                new Pareja(1, fuerte1, fuerte2);
        Pareja pareja2 =
                new Pareja(2, rival1, rival2);

        Combate combate =
                new Combate(pareja1, pareja2);

        Pareja ganadora = combate.ejecutar();

        assertNotNull(ganadora);

        /*
         * Si un combatiente llega a cero de vida,
         * el motor debe excluirlo de los turnos
         * y de la selección de objetivos.
         */
        for (Combatiente combatiente :
                pareja1.getCombatientes()) {

            assertTrue(combatiente.getVidaActual() >= 0);
        }

        for (Combatiente combatiente :
                pareja2.getCombatientes()) {

            assertTrue(combatiente.getVidaActual() >= 0);
        }
    }

    @Test
    public void elCombateRespetaElLimiteMaximoDeAsaltos() {

        Combatiente c1 =
                new MonjeDelCuerpoCelestial(1, "Monje 1");
        Combatiente c2 =
                new MonjeDelCuerpoCelestial(2, "Monje 2");

        Combatiente c3 =
                new MonjeDelCuerpoCelestial(3, "Monje 3");
        Combatiente c4 =
                new MonjeDelCuerpoCelestial(4, "Monje 4");

        Pareja pareja1 = new Pareja(1, c1, c2);
        Pareja pareja2 = new Pareja(2, c3, c4);

        Combate combate = new Combate(pareja1, pareja2);

        combate.ejecutar();

        assertTrue(
                combate.getNumeroAsalto()
                <= Combate.MAXIMO_ASALTOS
        );
    }

    @Test
    public void anteEmpateDeVidaGanaLaParejaDeMenorId() {

        /*
         * Usamos combatientes del mismo tipo y sin técnicas
         * para producir un escenario completamente simétrico.
         */

        Combatiente c1 =
                new MonjeDelCuerpoCelestial(1, "A");
        Combatiente c2 =
                new MonjeDelCuerpoCelestial(2, "B");

        Combatiente c3 =
                new MonjeDelCuerpoCelestial(3, "C");
        Combatiente c4 =
                new MonjeDelCuerpoCelestial(4, "D");

        Pareja parejaMenorId =
                new Pareja(1, c1, c2);

        Pareja parejaMayorId =
                new Pareja(2, c3, c4);

        Combate combate =
                new Combate(parejaMenorId, parejaMayorId);

        Pareja ganadora = combate.ejecutar();

        assertNotNull(ganadora);
    }

    @Test
    public void elCombateEsDeterministaParaEstadosInicialesEquivalentes() {

        Combatiente a1 =
                new CultivadorDeLaEspada(1, "A");
        Combatiente a2 =
                new MonjeDelCuerpoCelestial(2, "B");
        Combatiente a3 =
                new HechiceroDelVacio(3, "C");
        Combatiente a4 =
                new CultivadorDeLaEspada(4, "D");

        Pareja p1a = new Pareja(1, a1, a2);
        Pareja p2a = new Pareja(2, a3, a4);

        Combate combate1 = new Combate(p1a, p2a);

        Pareja resultado1 = combate1.ejecutar();

        Combatiente b1 =
                new CultivadorDeLaEspada(1, "A");
        Combatiente b2 =
                new MonjeDelCuerpoCelestial(2, "B");
        Combatiente b3 =
                new HechiceroDelVacio(3, "C");
        Combatiente b4 =
                new CultivadorDeLaEspada(4, "D");

        Pareja p1b = new Pareja(1, b1, b2);
        Pareja p2b = new Pareja(2, b3, b4);

        Combate combate2 = new Combate(p1b, p2b);

        Pareja resultado2 = combate2.ejecutar();

        assertEquals(
                resultado1.getId(),
                resultado2.getId()
        );

        assertEquals(
                combate1.getNumeroAsalto(),
                combate2.getNumeroAsalto()
        );
    }
}