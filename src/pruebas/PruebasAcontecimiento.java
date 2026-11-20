package pruebas;

import combatientes.Combatiente;
import combatientes.CultivadorDeLaEspada;
import combatientes.HechiceroDelVacio;
import combatientes.MonjeDelCuerpoCelestial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import acontecimientos.*;
import torneo.Pareja;

import static org.junit.jupiter.api.Assertions.*;



class PruebasAcontecimiento {

	private Combatiente aurelio;
    private Combatiente nebula;
    private Combatiente kael;
    private Combatiente darian;
    private Pareja pareja1;
    private Pareja pareja2;

    @BeforeEach
    void setUp() {
        aurelio = new CultivadorDeLaEspada(1, "Aurelio");
        nebula = new HechiceroDelVacio(2, "Nebula");
        kael = new MonjeDelCuerpoCelestial(3, "Kael");
        darian = new CultivadorDeLaEspada(4, "Darian");

        pareja1 = new Pareja(1, aurelio, nebula);
        pareja2 = new Pareja(2, kael, darian);
    }

    @Test
    void crearInicioCombateValido() {
        InicioCombate evento = new InicioCombate(1, pareja1, pareja2);

        assertEquals(1, evento.getRonda());
        assertEquals(pareja1, evento.getPareja1());
        assertEquals(pareja2, evento.getPareja2());
        assertNotNull(evento.getFechaHora());
        assertFalse(evento.getFechaHora().isBlank());
        assertTrue(evento.getDescripcion().contains("Ronda 1"));
    }

    @Test
    void crearAccionRealizadaEnSusTresVariantes() {
        AccionRealizada ofensiva = new AccionRealizada(
                2, aurelio, kael, "Corte de los Mil Amaneceres", true);
        assertEquals(2, ofensiva.getAsalto());
        assertEquals(aurelio, ofensiva.getActor());
        assertEquals(kael, ofensiva.getObjetivo());
        assertTrue(ofensiva.isEsTecnica());
        assertTrue(ofensiva.getDescripcion().contains("Corte de los Mil Amaneceres"));
        assertTrue(ofensiva.getDescripcion().contains("Kael"));

        AccionRealizada propia = new AccionRealizada(
                2, nebula, nebula, "Meditacion del Inmortal", true);
        assertTrue(propia.getDescripcion().contains("sobre si mismo"));

        AccionRealizada basico = new AccionRealizada(
                3, darian, aurelio, "Ataque basico", false);
        assertFalse(basico.isEsTecnica());
        assertTrue(basico.getDescripcion().contains("ataque basico"));
    }

    @Test
    void crearEventosDeCierre() {
        Eliminacion eliminacion = new Eliminacion(4, kael, aurelio);
        assertEquals(4, eliminacion.getAsalto());
        assertEquals(kael, eliminacion.getEliminado());
        assertEquals(aurelio, eliminacion.getCausante());

        ResultadoCombate resultado = new ResultadoCombate(1, pareja1, 120, pareja2, 0, pareja1);
        assertEquals(1, resultado.getRonda());
        assertEquals(120, resultado.getVidaTotalPareja1());
        assertEquals(0, resultado.getVidaTotalPareja2());
        assertEquals(pareja1, resultado.getGanadora());

        Proclamacion proclamacion = new Proclamacion(pareja1);
        assertEquals(pareja1, proclamacion.getCampeona());
        assertTrue(proclamacion.getDescripcion().contains("Dos Santos Supremos"));
    }

    @Test
    void eliminacionPorEfectoTemporalNombraElEfectoYNoTieneCausante() {
        Eliminacion eliminacion = new Eliminacion(5, kael, "Veneno");

        assertEquals(kael, eliminacion.getEliminado());
        assertNull(eliminacion.getCausante());
        assertTrue(eliminacion.getDescripcion().contains("por Veneno"));
        assertFalse(eliminacion.getDescripcion().contains("tras el ataque de"));
    }

    @Test
    void rechazarParametrosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new InicioCombate(1, null, pareja2));
        assertThrows(IllegalArgumentException.class, () -> new AccionRealizada(1, null, kael, "Ataque", false));
        assertThrows(IllegalArgumentException.class, () -> new AccionRealizada(1, aurelio, kael, "   ", true));
        assertThrows(IllegalArgumentException.class, () -> new Eliminacion(1, null, aurelio));
        assertThrows(IllegalArgumentException.class, () -> new ResultadoCombate(1, pareja1, 50, pareja2, 0, null));
        assertThrows(IllegalArgumentException.class, () -> new Proclamacion(null));
    }
}
