package pruebas;

import combatientes.Combatiente;
import combatientes.CreadorCombatientes;
import combatientes.Escuela;
import equipamiento.Equipamiento;
import org.junit.jupiter.api.Test;
import tecnicas.CorteDeLosMilAmaneceres;
import tecnicas.DominioDeLaNada;
import tecnicas.MeditacionDelInmortal;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PruebasEscuelasYCreador {

    private static final int PRIMER_ID_ESPERADO = 1;
    private static final int SEGUNDO_ID_ESPERADO = 2;
    private static final int TERCER_ID_ESPERADO = 3;
    private static final int CANTIDAD_TECNICAS_PALACIO = 2;

    @Test
    public void elCreadorAsignaIdentificadoresUnicosEnOrdenDeCreacion() {
        Escuela escuela = new Escuela(
                "Escuela de la Espada Celestial",
                List.of(new CorteDeLosMilAmaneceres()),
                List.<Equipamiento>of()
        );
        CreadorCombatientes creador = new CreadorCombatientes();

        Combatiente primero = creador.crear("Cultivador de la Espada", "Aurelio", escuela);
        Combatiente segundo = creador.crear("Monje del Cuerpo Celestial", "Kael", escuela);
        Combatiente tercero = creador.crear("Hechicero del Vacío", "Nébula", escuela);

        assertEquals(PRIMER_ID_ESPERADO, primero.getId());
        assertEquals(SEGUNDO_ID_ESPERADO, segundo.getId());
        assertEquals(TERCER_ID_ESPERADO, tercero.getId());
    }

    @Test
    public void laEscuelaPreparaAlCombatienteConSusTecnicasInicialesYCincoUsosCadaUna() {
        Escuela palacioDelVacio = new Escuela(
                "Palacio del Vacío",
                List.of(new DominioDeLaNada(), new MeditacionDelInmortal()),
                List.<Equipamiento>of()
        );
        CreadorCombatientes creador = new CreadorCombatientes();

        Combatiente nebula = creador.crear("Hechicero del Vacío", "Nébula", palacioDelVacio);

        assertEquals(CANTIDAD_TECNICAS_PALACIO, nebula.getTecnicasDisponibles().size());
        assertEquals(Combatiente.USOS_MAXIMOS_TECNICA, nebula.getUsosRestantes(new DominioDeLaNada()));
        assertEquals(Combatiente.USOS_MAXIMOS_TECNICA, nebula.getUsosRestantes(new MeditacionDelInmortal()));
    }

    @Test
    public void rechazaCrearUnCombatienteDeTipoDesconocido() {
        Escuela escuela = new Escuela("Escuela Base", List.of(), List.of());
        CreadorCombatientes creador = new CreadorCombatientes();

        assertThrows(IllegalArgumentException.class, () ->
                creador.crear("Guerrero Inventado", "SinTipo", escuela)
        );
    }
}