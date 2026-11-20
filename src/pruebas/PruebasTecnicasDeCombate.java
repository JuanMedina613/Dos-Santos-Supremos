package pruebas;

import combatientes.Combatiente;
import combatientes.CultivadorDeLaEspada;
import combatientes.HechiceroDelVacio;
import org.junit.jupiter.api.Test;
import tecnicas.*;

import static org.junit.jupiter.api.Assertions.*;

public class PruebasTecnicasDeCombate {

    private static final int ID_EJECUTANTE = 1;
    private static final int ID_SEGUNDO_EJECUTANTE = 2;
    private static final int ID_PRIMER_RIVAL = 3;
    private static final int ID_SEGUNDO_RIVAL = 4;

    private static final int DANIO_MENOR_A_CURACION = 10;
    private static final int ENERGIA_A_DRENAR_PARA_AGOTAR = 30;
    private static final int USOS_AGOTADOS = 0;

    private static class CombatienteDePrueba extends Combatiente {
        public CombatienteDePrueba(int id, String nombre) {
            super(id, nombre);
        }
    }

    @Test
    public void elCultivadorHaceMasDanioQueUnCombatienteBaseConLaMismaTecnica() {
        Tecnica corte = new CorteDeLosMilAmaneceres();
        Combatiente base = new CombatienteDePrueba(ID_EJECUTANTE, "Base");
        Combatiente cultivador = new CultivadorDeLaEspada(ID_SEGUNDO_EJECUTANTE, "Aurelio");

        Combatiente rivalDelBase = new CombatienteDePrueba(ID_PRIMER_RIVAL, "Rival1");
        Combatiente rivalDelCultivador = new CombatienteDePrueba(ID_SEGUNDO_RIVAL, "Rival2");

        base.aprenderTecnica(corte);
        cultivador.aprenderTecnica(corte);

        corte.aplicar(base, rivalDelBase);
        corte.aplicar(cultivador, rivalDelCultivador);

        int danioBase = Combatiente.VIDA_BASE_POR_DEFECTO - rivalDelBase.getVidaActual();
        int danioCultivador = Combatiente.VIDA_BASE_POR_DEFECTO - rivalDelCultivador.getVidaActual();

        assertEquals(CorteDeLosMilAmaneceres.DANIO_BASE, danioBase);
        assertTrue(danioCultivador > danioBase);
    }

    @Test
    public void elHechiceroDelVacioGastaMenosEnergiaAlEjecutarTecnicas() {
        Tecnica rugido = new RugidoBestiaPrimordial();
        Combatiente hechicero = new HechiceroDelVacio(ID_EJECUTANTE, "Nebula");
        Combatiente rival = new CombatienteDePrueba(ID_PRIMER_RIVAL, "Rival");

        hechicero.aprenderTecnica(rugido);
        rugido.aplicar(hechicero, rival);

        int costoConDescuento = hechicero.calcularCostoEnergia(RugidoBestiaPrimordial.COSTO_ENERGIA);
        assertEquals(Combatiente.ENERGIA_BASE_POR_DEFECTO - costoConDescuento, hechicero.getEnergiaActual());
    }

    @Test
    public void muroDelCuerpoDoradoReduceElDanioRecibidoEnElCombate() {
        Tecnica muro = new MuroDelCuerpoDorado();
        Tecnica corte = new CorteDeLosMilAmaneceres();

        Combatiente defensor = new CombatienteDePrueba(ID_EJECUTANTE, "Defensor");
        Combatiente atacante = new CombatienteDePrueba(ID_PRIMER_RIVAL, "Atacante");

        defensor.aprenderTecnica(muro);
        atacante.aprenderTecnica(corte);

        muro.aplicar(defensor, null);
        corte.aplicar(atacante, defensor);

        int danioEsperado = CorteDeLosMilAmaneceres.DANIO_BASE - MuroDelCuerpoDorado.REDUCCION_DANIO;
        assertEquals(Combatiente.VIDA_BASE_POR_DEFECTO - danioEsperado, defensor.getVidaActual());
    }

    @Test
    public void meditacionDelInmortalRecuperaVidaSinSuperarElMaximo() {
        Tecnica meditacion = new MeditacionDelInmortal();
        Combatiente ejecutante = new CombatienteDePrueba(ID_EJECUTANTE, "Monje");
        ejecutante.aprenderTecnica(meditacion);

        ejecutante.recibirDanio(DANIO_MENOR_A_CURACION);
        meditacion.aplicar(ejecutante, null);

        assertEquals(Combatiente.VIDA_BASE_POR_DEFECTO, ejecutante.getVidaActual());
    }

    @Test
    public void dominioDeLaNadaDaniaAlRivalYLeQuitaEnergia() {
        Tecnica dominio = new DominioDeLaNada();
        Combatiente ejecutante = new CombatienteDePrueba(ID_EJECUTANTE, "Ejecutante");
        Combatiente rival = new CombatienteDePrueba(ID_PRIMER_RIVAL, "Rival");

        ejecutante.aprenderTecnica(dominio);
        dominio.aplicar(ejecutante, rival);

        assertEquals(Combatiente.VIDA_BASE_POR_DEFECTO - DominioDeLaNada.DANIO_BASE, rival.getVidaActual());
        assertEquals(Combatiente.ENERGIA_BASE_POR_DEFECTO - DominioDeLaNada.DRENAJE_ENERGIA, rival.getEnergiaActual());
    }

    @Test
    public void noSePuedeEjecutarUnaTecnicaSinEnergiaSuficiente() {
        Tecnica rugido = new RugidoBestiaPrimordial();
        Combatiente ejecutante = new CombatienteDePrueba(ID_EJECUTANTE, "Agotado");
        Combatiente rival = new CombatienteDePrueba(ID_PRIMER_RIVAL, "Rival");

        ejecutante.aprenderTecnica(rugido);
        ejecutante.perderEnergiaPorEfecto(ENERGIA_A_DRENAR_PARA_AGOTAR);

        assertThrows(IllegalStateException.class, () -> rugido.aplicar(ejecutante, rival));
        assertEquals(Combatiente.USOS_MAXIMOS_TECNICA, ejecutante.getUsosRestantes(rugido));
    }

    @Test
    public void noSePuedeUsarUnaTecnicaMasDeCincoVecesEnElTorneo() {
        Tecnica muro = new MuroDelCuerpoDorado();
        Combatiente ejecutante = new CombatienteDePrueba(ID_EJECUTANTE, "Combatiente");
        ejecutante.aprenderTecnica(muro);

        for (int intento = 0; intento < Combatiente.USOS_MAXIMOS_TECNICA; intento++) {
            ejecutante.restaurarParaNuevoCombate();
            muro.aplicar(ejecutante, null);
        }

        assertEquals(USOS_AGOTADOS, ejecutante.getUsosRestantes(muro));
        ejecutante.restaurarParaNuevoCombate();

        assertThrows(IllegalStateException.class, () -> muro.aplicar(ejecutante, null));
    }
}