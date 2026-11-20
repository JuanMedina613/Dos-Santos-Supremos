package pruebas;

import acontecimientos.CronistaDeLaSecta;
import acontecimientos.EfectoTemporalAplicado;
import combate.*;
import combatientes.Combatiente;
import combatientes.CultivadorDeLaEspada;
import combatientes.HechiceroDelVacio;
import org.junit.jupiter.api.Test;
import tecnicas.AlientoVital;
import tecnicas.PalmaEnvenenada;
import torneo.Pareja;

import static org.junit.jupiter.api.Assertions.*;

class PruebasEfectoTemporal {

    @Test
    void venenoAplicaDuranteTurnosYExpira() {
        Combatiente objetivo = new CultivadorDeLaEspada(1, "Objetivo");
        Veneno veneno = new Veneno(10, 2);

        objetivo.aplicarEfectoTemporal(veneno);
        assertEquals(1, objetivo.getEfectosTemporalesActivos().size());

        // Turno 1 del afectado: recibe 10 de daño, queda 1 turno restante
        objetivo.procesarEfectosDeTurno();
        assertEquals(90, objetivo.getVidaActual());
        assertEquals(1, veneno.getTurnosRestantes());
        assertEquals(1, objetivo.getEfectosTemporalesActivos().size());

        // Turno 2 del afectado: recibe 10 de daño, llega a 0 turnos y se remueve
        objetivo.procesarEfectosDeTurno();
        assertEquals(80, objetivo.getVidaActual());
        assertTrue(veneno.estaFinalizado());
        assertTrue(objetivo.getEfectosTemporalesActivos().isEmpty());

        // Turno 3: ya no recibe daño adicional
        objetivo.procesarEfectosDeTurno();
        assertEquals(80, objetivo.getVidaActual());
    }

    @Test
    void regeneracionCuraSinSuperarMaximoYExpira() {
        Combatiente combatiente = new HechiceroDelVacio(1, "Sanador");
        combatiente.recibirDanio(30); // Queda en 70/100

        Regeneracion regeneracion = new Regeneracion(20, 2);
        combatiente.aplicarEfectoTemporal(regeneracion);

        // Turno 1: 70 + 20 = 90
        combatiente.procesarEfectosDeTurno();
        assertEquals(90, combatiente.getVidaActual());

        // Turno 2: 90 + 20 -> tope 100, y el efecto finaliza
        combatiente.procesarEfectosDeTurno();
        assertEquals(100, combatiente.getVidaActual());
        assertTrue(combatiente.getEfectosTemporalesActivos().isEmpty());
    }

    @Test
    void efectosTemporalesEnCombateYLimpiezaEntreCombates() {
        Combatiente envenenador = new HechiceroDelVacio(1, "Viper");
        envenenador.aprenderTecnica(new PalmaEnvenenada());
        Combatiente companero1 = new CultivadorDeLaEspada(2, "Aliado1");

        Combatiente regenerador = new HechiceroDelVacio(3, "Loto");
        regenerador.aprenderTecnica(new AlientoVital());
        Combatiente companero2 = new CultivadorDeLaEspada(4, "Aliado2");

        Pareja p1 = new Pareja(1, envenenador, companero1);
        Pareja p2 = new Pareja(2, regenerador, companero2);

        Combate combate = new Combate(p1, p2);
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        combate.agregarEscuchador(cronista);

        combate.ejecutar();

        boolean huboEfectosTemporales = cronista.obtenerHistorial().stream()
                .anyMatch(e -> e instanceof EfectoTemporalAplicado);
        assertTrue(huboEfectosTemporales);

        // Al preparar un nuevo combate, ningún combatiente conserva efectos residuales
        p1.restaurarParaNuevoCombate();
        p2.restaurarParaNuevoCombate();
        assertTrue(envenenador.getEfectosTemporalesActivos().isEmpty());
        assertTrue(regenerador.getEfectosTemporalesActivos().isEmpty());
    }
}
