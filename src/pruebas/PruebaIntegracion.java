package pruebas;

import acontecimientos.Acontecimiento;
import acontecimientos.CronistaDeLaSecta;
import acontecimientos.PosicionTabla;
import acontecimientos.Proclamacion;
import combatientes.Combatiente;
import combatientes.CreadorCombatientes;
import combatientes.Escuela;
import equipamiento.AnilloEspiritual;
import equipamiento.ArmaduraDorada;
import equipamiento.EspadaCelestial;
import excepciones.InscripcionInvalidaException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tecnicas.CorteDeLosMilAmaneceres;
import tecnicas.DominioDeLaNada;
import tecnicas.MeditacionDelInmortal;
import tecnicas.MuroDelCuerpoDorado;
import torneo.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PruebaIntegracion {

    @Test
    void validarReglasDeInscripcionYCantidadDeParejas() {
        CreadorCombatientes creador = new CreadorCombatientes();
        Escuela escuela = new Escuela(
                "Escuela Base",
                List.of(new CorteDeLosMilAmaneceres()),
                List.of(new EspadaCelestial(5)));

        Combatiente c1 = creador.crear("Cultivador de la Espada", "C1", escuela);
        Combatiente c2 = creador.crear("Hechicero del Vacío", "C2", escuela);
        Combatiente c3 = creador.crear("Monje del Cuerpo Celestial", "C3", escuela);
        Combatiente c4 = creador.crear("Cultivador de la Espada", "C4", escuela);

        Pareja p1 = new Pareja(1, c1, c2);
        Pareja p2ConRepetido = new Pareja(2, c1, c3); // c1 ya está en p1
        Pareja p3 = new Pareja(3, c3, c4);

        Torneo torneo = new Torneo();
        torneo.inscribir(p1);

        // Pareja duplicada
        assertThrows(InscripcionInvalidaException.class, () -> torneo.inscribir(p1));

        // Combatiente ya inscripto en otra pareja
        assertThrows(InscripcionInvalidaException.class, () -> torneo.inscribir(p2ConRepetido));

        // Cantidad inválida de parejas (solo 2 inscriptas)
        torneo.inscribir(p3);
        assertThrows(InscripcionInvalidaException.class, torneo::iniciar);
    }

    @Test
    @DisplayName("Torneo de 4 parejas completa 2 rondas, proclama campeón y arma la tabla en el Cronista")
    void ejecutarTorneoDeCuatroParejasYVerificarCronista() {
        Torneo torneo = construirTorneoDeterministaCuatroParejas();
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        torneo.agregarEscuchador(cronista);

        Pareja campeona = torneo.iniciar();

        assertNotNull(campeona);
        assertEquals(2, torneo.getRondas().size());

        List<Acontecimiento> historial = cronista.obtenerHistorial();
        assertFalse(historial.isEmpty());
        assertTrue(historial.get(historial.size() - 1) instanceof Proclamacion);

        List<PosicionTabla> tabla = cronista.generarTablaPosiciones();
        assertEquals(4, tabla.size());
        assertEquals(campeona, tabla.get(0).getPareja());
        assertEquals(2, tabla.get(0).getUltimaRonda());
        assertEquals(2, tabla.get(1).getUltimaRonda());
        assertEquals(1, tabla.get(2).getUltimaRonda());
        assertEquals(1, tabla.get(3).getUltimaRonda());

        // No permite inscribir luego de iniciado
        CreadorCombatientes creador = new CreadorCombatientes();
        Escuela esc = new Escuela("Extra", List.of(), List.of());
        Pareja tarde = new Pareja(99,
                creador.crear("Cultivador de la Espada", "X1", esc),
                creador.crear("Cultivador de la Espada", "X2", esc));
        assertThrows(IllegalStateException.class, () -> torneo.inscribir(tarde));
    }

    @Test
    @DisplayName("Dos torneos armados igual producen el mismo campeón, la misma tabla y el mismo historial")
    void reproducibilidadDeDosTorneosIdenticos() {
        Torneo torneoA = construirTorneoDeterministaCuatroParejas();
        CronistaDeLaSecta cronistaA = new CronistaDeLaSecta();
        torneoA.agregarEscuchador(cronistaA);

        Torneo torneoB = construirTorneoDeterministaCuatroParejas();
        CronistaDeLaSecta cronistaB = new CronistaDeLaSecta();
        torneoB.agregarEscuchador(cronistaB);

        Pareja campeonaA = torneoA.iniciar();
        Pareja campeonaB = torneoB.iniciar();

        assertEquals(campeonaA, campeonaB);

        List<Acontecimiento> historialA = cronistaA.obtenerHistorial();
        List<Acontecimiento> historialB = cronistaB.obtenerHistorial();

        assertEquals(historialA.size(), historialB.size());
        for (int i = 0; i < historialA.size(); i++) {
            assertEquals(historialA.get(i).getDescripcion(), historialB.get(i).getDescripcion());
        }

        List<PosicionTabla> tablaA = cronistaA.generarTablaPosiciones();
        List<PosicionTabla> tablaB = cronistaB.generarTablaPosiciones();
        assertEquals(tablaA.size(), tablaB.size());
        for (int i = 0; i < tablaA.size(); i++) {
            assertEquals(tablaA.get(i).getPareja(), tablaB.get(i).getPareja());
            assertEquals(tablaA.get(i).getUltimaRonda(), tablaB.get(i).getUltimaRonda());
            assertEquals(tablaA.get(i).getVidaTotalFinal(), tablaB.get(i).getVidaTotalFinal());
        }
    }

    private Torneo construirTorneoDeterministaCuatroParejas() {
        Escuela espada = new Escuela(
                "Escuela de la Espada Celestial",
                List.of(new CorteDeLosMilAmaneceres()),
                List.of(new EspadaCelestial(8)));
        Escuela vacio = new Escuela(
                "Palacio del Vacío",
                List.of(new DominioDeLaNada(), new MeditacionDelInmortal()),
                List.of(new AnilloEspiritual(15)));
        Escuela monje = new Escuela(
                "Monasterio del Cuerpo Dorado",
                List.of(new MuroDelCuerpoDorado(), new MeditacionDelInmortal()),
                List.of(new ArmaduraDorada(5)));

        CreadorCombatientes creador = new CreadorCombatientes();
        Torneo torneo = new Torneo();

        torneo.inscribir(new Pareja(1,
                creador.crear("Cultivador de la Espada", "Aurelio", espada),
                creador.crear("Hechicero del Vacío", "Nébula", vacio)));
        torneo.inscribir(new Pareja(2,
                creador.crear("Monje del Cuerpo Celestial", "Kael", monje),
                creador.crear("Cultivador de la Espada", "Darian", espada)));
        torneo.inscribir(new Pareja(3,
                creador.crear("Hechicero del Vacío", "Lyra", vacio),
                creador.crear("Monje del Cuerpo Celestial", "Thorne", monje)));
        torneo.inscribir(new Pareja(4,
                creador.crear("Cultivador de la Espada", "Soren", espada),
                creador.crear("Hechicero del Vacío", "Mira", vacio)));

        return torneo;
    }
}