package pruebas;

import combatientes.Combatiente;
import equipamiento.AnilloEspiritual;
import equipamiento.ArmaduraDorada;
import equipamiento.CategoriaEquipamiento;
import equipamiento.EspadaCelestial;
import equipamiento.SelloDelFenix;
import excepciones.ReglaEquipamientoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PruebasEquipamiento {

    private static final int BONO_ESPADA = 5;
    private static final int REDUCCION_ARMADURA = 4;
    private static final int BONO_ANILLO = 10;
    private static final int BONO_SELLO = 20;

    /** Combatiente sin especialidad, para medir solo el efecto del equipamiento. */
    private static class CombatienteDePrueba extends Combatiente {
        CombatienteDePrueba(int id, String nombre) {
            super(id, nombre);
        }
    }

    private Combatiente nuevoCombatiente() {
        return new CombatienteDePrueba(1, "Portador");
    }

    @Test
    public void equiparUnCuartoObjetoSeRechazaSinCambios() {
        Combatiente combatiente = nuevoCombatiente();
        combatiente.equipar(new EspadaCelestial(BONO_ESPADA));
        combatiente.equipar(new ArmaduraDorada(REDUCCION_ARMADURA));
        combatiente.equipar(new AnilloEspiritual(BONO_ANILLO));

        assertThrows(ReglaEquipamientoException.class,
                () -> combatiente.equipar(new SelloDelFenix(BONO_SELLO)));

        assertEquals(Combatiente.MAX_EQUIPAMIENTOS, combatiente.getEquipamientos().size());
        assertEquals(Combatiente.VIDA_BASE_POR_DEFECTO, combatiente.getVidaMaxima());
    }

    @Test
    public void equiparUnaCategoriaRepetidaSeRechazaSinCambios() {
        Combatiente combatiente = nuevoCombatiente();
        combatiente.equipar(new EspadaCelestial(BONO_ESPADA));

        assertThrows(ReglaEquipamientoException.class,
                () -> combatiente.equipar(new EspadaCelestial(BONO_ESPADA * 2)));

        assertEquals(1, combatiente.getEquipamientos().size());
        assertEquals(BONO_ESPADA, combatiente.getBonoDanioEquipamiento());
    }

    @Test
    public void losEfectosDeDistintasCategoriasSeAcumulan() {
        Combatiente combatiente = nuevoCombatiente();
        combatiente.equipar(new EspadaCelestial(BONO_ESPADA));
        combatiente.equipar(new SelloDelFenix(BONO_SELLO));
        combatiente.equipar(new AnilloEspiritual(BONO_ANILLO));

        assertEquals(10 + BONO_ESPADA, combatiente.calcularDanioProducido(10));
        assertEquals(Combatiente.VIDA_BASE_POR_DEFECTO + BONO_SELLO, combatiente.getVidaMaxima());
        assertEquals(Combatiente.ENERGIA_BASE_POR_DEFECTO + BONO_ANILLO, combatiente.getEnergiaMaxima());
    }

    @Test
    public void laArmaduraReduceElDanioRecibido() {
        Combatiente combatiente = nuevoCombatiente();
        combatiente.equipar(new ArmaduraDorada(REDUCCION_ARMADURA));

        combatiente.recibirDanio(20);

        assertEquals(Combatiente.VIDA_BASE_POR_DEFECTO - (20 - REDUCCION_ARMADURA),
                combatiente.getVidaActual());
    }

    @Test
    public void equiparUnObjetoQueSubeUnMaximoNoSubeElValorActual() {
        Combatiente combatiente = nuevoCombatiente();

        combatiente.equipar(new SelloDelFenix(BONO_SELLO));
        combatiente.equipar(new AnilloEspiritual(BONO_ANILLO));

        assertEquals(Combatiente.VIDA_BASE_POR_DEFECTO + BONO_SELLO, combatiente.getVidaMaxima());
        assertEquals(Combatiente.VIDA_BASE_POR_DEFECTO, combatiente.getVidaActual());
        assertEquals(Combatiente.ENERGIA_BASE_POR_DEFECTO, combatiente.getEnergiaActual());
    }

    @Test
    public void alQuitarElSelloLaVidaActualSeRecortaAlNuevoMaximo() {
        Combatiente combatiente = nuevoCombatiente();
        combatiente.equipar(new SelloDelFenix(BONO_SELLO));
        combatiente.restaurarParaNuevoCombate();
        assertEquals(Combatiente.VIDA_BASE_POR_DEFECTO + BONO_SELLO, combatiente.getVidaActual());

        combatiente.quitarEquipamientoPorCategoria(CategoriaEquipamiento.SELLO);

        assertEquals(Combatiente.VIDA_BASE_POR_DEFECTO, combatiente.getVidaMaxima());
        assertEquals(Combatiente.VIDA_BASE_POR_DEFECTO, combatiente.getVidaActual());
    }

    @Test
    public void alQuitarElSelloNoSeRecortaSiLaVidaNoSuperaElNuevoMaximo() {
        Combatiente combatiente = nuevoCombatiente();
        combatiente.equipar(new SelloDelFenix(BONO_SELLO));
        combatiente.restaurarParaNuevoCombate();
        combatiente.recibirDanio(50);

        combatiente.quitarEquipamientoPorCategoria(CategoriaEquipamiento.SELLO);

        assertEquals(Combatiente.VIDA_BASE_POR_DEFECTO + BONO_SELLO - 50, combatiente.getVidaActual());
    }

    @Test
    public void alQuitarElAnilloLaEnergiaActualSeRecorta() {
        Combatiente combatiente = nuevoCombatiente();
        combatiente.equipar(new AnilloEspiritual(BONO_ANILLO));
        combatiente.restaurarParaNuevoCombate();

        combatiente.quitarEquipamientoPorCategoria(CategoriaEquipamiento.ANILLO);

        assertEquals(Combatiente.ENERGIA_BASE_POR_DEFECTO, combatiente.getEnergiaActual());
    }

    @Test
    public void elCombatientePuedeConsultarLosObjetosQueLleva() {
        Combatiente combatiente = nuevoCombatiente();
        EspadaCelestial espada = new EspadaCelestial(BONO_ESPADA);
        ArmaduraDorada armadura = new ArmaduraDorada(REDUCCION_ARMADURA);

        combatiente.equipar(espada);
        combatiente.equipar(armadura);

        assertEquals(2, combatiente.getEquipamientos().size());
        assertTrue(combatiente.getEquipamientos().contains(espada));
        assertTrue(combatiente.getEquipamientos().contains(armadura));
    }

    @Test
    public void quitarUnObjetoQueNoLlevaSeRechaza() {
        Combatiente combatiente = nuevoCombatiente();

        assertThrows(ReglaEquipamientoException.class,
                () -> combatiente.quitarEquipamiento(new EspadaCelestial(BONO_ESPADA)));
        assertThrows(ReglaEquipamientoException.class,
                () -> combatiente.quitarEquipamientoPorCategoria(CategoriaEquipamiento.ARMA));
    }

    @Test
    public void alQuitarUnObjetoSeLiberaSuCategoria() {
        Combatiente combatiente = nuevoCombatiente();
        combatiente.equipar(new EspadaCelestial(BONO_ESPADA));

        combatiente.quitarEquipamientoPorCategoria(CategoriaEquipamiento.ARMA);
        combatiente.equipar(new EspadaCelestial(BONO_ESPADA * 2));

        assertEquals(BONO_ESPADA * 2, combatiente.getBonoDanioEquipamiento());
    }
}
