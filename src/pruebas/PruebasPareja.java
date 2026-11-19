package pruebas;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import combatientes.Combatiente;
import combatientes.CultivadorDeLaEspada;
import combatientes.MonjeDelCuerpoCelestial;
import tecnicas.CorteDeLosMilAmaneceres;
import tecnicas.Tecnica;
import torneo.Pareja;

public class PruebasPareja {

    @Test
    public void unaParejaContieneSusDosCombatientes() {

        Combatiente primero =
                new CultivadorDeLaEspada(1, "Aurelio");

        Combatiente segundo =
                new MonjeDelCuerpoCelestial(2, "Kael");

        Pareja pareja = new Pareja(1, primero, segundo);

        assertTrue(pareja.contiene(primero));
        assertTrue(pareja.contiene(segundo));
        assertEquals(2, pareja.getCombatientes().size());
    }

    @Test
    public void unaParejaNoPuedeContenerDosVecesAlMismoCombatiente() {

        Combatiente combatiente =
                new CultivadorDeLaEspada(1, "Aurelio");

        assertThrows(
                IllegalArgumentException.class,
                () -> new Pareja(1, combatiente, combatiente)
        );
    }

    @Test
    public void unaParejaConservaSoloLosCombatientesEnPie() {

        Combatiente primero =
                new CultivadorDeLaEspada(1, "Aurelio");

        Combatiente segundo =
                new MonjeDelCuerpoCelestial(2, "Kael");

        Pareja pareja = new Pareja(1, primero, segundo);

        segundo.recibirDanio(1000);

        assertTrue(pareja.tieneCombatientesEnPie());
        assertEquals(1, pareja.getCombatientesEnPie().size());
        assertTrue(pareja.getCombatientesEnPie().contains(primero));
        assertFalse(pareja.getCombatientesEnPie().contains(segundo));
    }

    @Test
    public void unaParejaQuedaFueraCuandoCaenSusDosCombatientes() {

        Combatiente primero =
                new CultivadorDeLaEspada(1, "Aurelio");

        Combatiente segundo =
                new MonjeDelCuerpoCelestial(2, "Kael");

        Pareja pareja = new Pareja(1, primero, segundo);

        primero.recibirDanio(1000);
        segundo.recibirDanio(1000);

        assertFalse(pareja.tieneCombatientesEnPie());
        assertTrue(pareja.getCombatientesEnPie().isEmpty());
    }

    @Test
    public void alUsarUnaTecnicaElCompaneroQuedaBloqueado() {

        Combatiente primero =
                new CultivadorDeLaEspada(1, "Aurelio");

        Combatiente segundo =
                new MonjeDelCuerpoCelestial(2, "Kael");

        Tecnica corte = new CorteDeLosMilAmaneceres();

        primero.aprenderTecnica(corte);
        segundo.aprenderTecnica(corte);

        Pareja pareja = new Pareja(1, primero, segundo);

        assertTrue(pareja.puedeUsarTecnica(primero, corte));
        assertTrue(pareja.puedeUsarTecnica(segundo, corte));

        pareja.registrarUsoDeTecnica(primero, corte);

        assertTrue(pareja.puedeUsarTecnica(primero, corte));
        assertFalse(pareja.puedeUsarTecnica(segundo, corte));
    }

    @Test
    public void elBloqueoTerminaSiElCombatienteQueUsoLaTecnicaCae() {

        Combatiente primero =
                new CultivadorDeLaEspada(1, "Aurelio");

        Combatiente segundo =
                new MonjeDelCuerpoCelestial(2, "Kael");

        Tecnica corte = new CorteDeLosMilAmaneceres();

        primero.aprenderTecnica(corte);
        segundo.aprenderTecnica(corte);

        Pareja pareja = new Pareja(1, primero, segundo);

        pareja.registrarUsoDeTecnica(primero, corte);

        assertFalse(pareja.puedeUsarTecnica(segundo, corte));

        primero.recibirDanio(1000);

        assertTrue(pareja.puedeUsarTecnica(segundo, corte));
    }

    @Test
    public void limpiarBloqueosPermiteAlCompaneroUsarLaTecnica() {

        Combatiente primero =
                new CultivadorDeLaEspada(1, "Aurelio");

        Combatiente segundo =
                new MonjeDelCuerpoCelestial(2, "Kael");

        Tecnica corte = new CorteDeLosMilAmaneceres();

        primero.aprenderTecnica(corte);
        segundo.aprenderTecnica(corte);

        Pareja pareja = new Pareja(1, primero, segundo);

        pareja.registrarUsoDeTecnica(primero, corte);

        assertFalse(pareja.puedeUsarTecnica(segundo, corte));

        pareja.limpiarBloqueosDeCombate();

        assertTrue(pareja.puedeUsarTecnica(segundo, corte));
    }
}