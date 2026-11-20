package pruebas;

import acontecimientos.Acontecimiento;
import acontecimientos.CronistaDeLaSecta;
import acontecimientos.InicioCombate;
import acontecimientos.Proclamacion;
import acontecimientos.ResultadoCombate;
import combatientes.Combatiente;
import combatientes.CultivadorDeLaEspada;
import combatientes.CreadorCombatientes;
import combatientes.Escuela;
import equipamiento.AnilloEspiritual;
import equipamiento.ArmaduraDorada;
import equipamiento.EspadaCelestial;
import equipamiento.SelloDelFenix;
import tecnicas.CorteDeLosMilAmaneceres;
import tecnicas.DominioDeLaNada;
import tecnicas.MeditacionDelInmortal;
import tecnicas.MuroDelCuerpoDorado;
import tecnicas.RugidoBestiaPrimordial;
import excepciones.InscripcionInvalidaException;
import org.junit.jupiter.api.Test;
import torneo.Cruce;
import torneo.Pareja;
import torneo.Torneo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PruebasTorneo {

    private Combatiente combatiente(int id) {
        return new CultivadorDeLaEspada(id, "Combatiente " + id);
    }

    private Pareja pareja(int idPareja, int idCombatiente1, int idCombatiente2) {
        return new Pareja(
                idPareja,
                combatiente(idCombatiente1),
                combatiente(idCombatiente2));
    }

    private Torneo torneoConCuatroParejas() {
        Torneo torneo = new Torneo();
        torneo.inscribir(pareja(1, 1, 2));
        torneo.inscribir(pareja(2, 3, 4));
        torneo.inscribir(pareja(3, 5, 6));
        torneo.inscribir(pareja(4, 7, 8));
        return torneo;
    }

    @Test
    public void rechazaInscribirDosVecesLaMismaPareja() {
        Torneo torneo = new Torneo();
        Pareja pareja = pareja(1, 1, 2);

        torneo.inscribir(pareja);

        assertThrows(
                InscripcionInvalidaException.class,
                () -> torneo.inscribir(pareja));
        assertEquals(1, torneo.getParejasInscriptas().size());
    }

    @Test
    public void rechazaParejaConCombatienteYaInscripto() {
        Torneo torneo = new Torneo();
        Combatiente compartido = combatiente(1);
        Pareja primera = new Pareja(1, compartido, combatiente(2));
        Pareja segunda = new Pareja(2, compartido, combatiente(3));

        torneo.inscribir(primera);

        assertThrows(
                InscripcionInvalidaException.class,
                () -> torneo.inscribir(segunda));
        assertEquals(List.of(primera), torneo.getParejasInscriptas());
    }

    @Test
    public void rechazaIniciarConCantidadDistintaDeCuatroUOcho() {
        Torneo torneo = new Torneo();
        torneo.inscribir(pareja(1, 1, 2));
        torneo.inscribir(pareja(2, 3, 4));

        assertThrows(InscripcionInvalidaException.class, torneo::iniciar);
        assertFalse(torneo.estaIniciado());
    }

    @Test
    public void primeraRondaRespetaElOrdenDeInscripcion() {
        Torneo torneo = torneoConCuatroParejas();
        List<Pareja> inscriptas = torneo.getParejasInscriptas();

        torneo.iniciar();

        List<Cruce> primeraRonda = torneo.getCrucesDeRonda(1);
        assertEquals(2, primeraRonda.size());

        assertEquals(inscriptas.get(0), primeraRonda.get(0).getPareja1());
        assertEquals(inscriptas.get(1), primeraRonda.get(0).getPareja2());
        assertEquals(inscriptas.get(2), primeraRonda.get(1).getPareja1());
        assertEquals(inscriptas.get(3), primeraRonda.get(1).getPareja2());
    }

    @Test
    public void siguienteRondaRespetaElOrdenDeGanadoresDeLosCrucesPrevios() {
        Torneo torneo = torneoConCuatroParejas();

        torneo.iniciar();

        List<Cruce> primeraRonda = torneo.getCrucesDeRonda(1);
        List<Cruce> finalTorneo = torneo.getCrucesDeRonda(2);

        assertEquals(1, finalTorneo.size());
        assertEquals(
                primeraRonda.get(0).getGanadora(),
                finalTorneo.get(0).getPareja1());
        assertEquals(
                primeraRonda.get(1).getGanadora(),
                finalTorneo.get(0).getPareja2());
    }

    @Test
    public void torneoDeCuatroParejasTieneDosRondasYTresCruces() {
        Torneo torneo = torneoConCuatroParejas();

        Pareja campeona = torneo.iniciar();

        assertEquals(2, torneo.getRondas().size());
        assertEquals(2, torneo.getCrucesDeRonda(1).size());
        assertEquals(1, torneo.getCrucesDeRonda(2).size());
        assertEquals(campeona, torneo.getCampeona());
        assertNotNull(campeona);
    }

    @Test
    public void admiteExactamenteOchoParejas() {
        Torneo torneo = new Torneo();
        int idCombatiente = 1;

        for (int idPareja = 1; idPareja <= 8; idPareja++) {
            torneo.inscribir(pareja(
                    idPareja,
                    idCombatiente,
                    idCombatiente + 1));
            idCombatiente += 2;
        }

        Pareja campeona = torneo.iniciar();

        assertNotNull(campeona);
        assertEquals(3, torneo.getRondas().size());
        assertEquals(4, torneo.getCrucesDeRonda(1).size());
        assertEquals(2, torneo.getCrucesDeRonda(2).size());
        assertEquals(1, torneo.getCrucesDeRonda(3).size());
    }

    @Test
    public void noPermiteInscripcionesUnaVezIniciado() {
        Torneo torneo = torneoConCuatroParejas();
        torneo.iniciar();

        assertThrows(
                IllegalStateException.class,
                () -> torneo.inscribir(pareja(5, 9, 10)));
    }

    @Test
    public void cruceSoloPuedeEjecutarseUnaVez() {
        Cruce cruce = new Cruce(
                1,
                pareja(1, 1, 2),
                pareja(2, 3, 4));

        Pareja ganadora = cruce.ejecutar();

        assertNotNull(ganadora);
        assertTrue(cruce.fueEjecutado());
        assertEquals(ganadora, cruce.getGanadora());
        assertThrows(IllegalStateException.class, cruce::ejecutar);
    }

    @Test
    public void dosTorneosIgualesProducenElMismoCampeon() {
        Torneo primero = torneoConCuatroParejas();
        Torneo segundo = torneoConCuatroParejas();

        Pareja campeonaPrimero = primero.iniciar();
        Pareja campeonaSegundo = segundo.iniciar();

        assertEquals(campeonaPrimero.getId(), campeonaSegundo.getId());
    }

    /** Arma siempre el mismo torneo con escuelas, tecnicas y equipamiento */
    private Torneo torneoConEscuelas(CronistaDeLaSecta cronista) {
        Escuela espada = new Escuela("Escuela de la Espada Celestial",
                List.of(new CorteDeLosMilAmaneceres(), new RugidoBestiaPrimordial()),
                List.of(new EspadaCelestial(8)));
        Escuela vacio = new Escuela("Palacio del Vacio",
                List.of(new DominioDeLaNada(), new MeditacionDelInmortal()),
                List.of(new AnilloEspiritual(15)));
        Escuela monasterio = new Escuela("Monasterio del Cuerpo Dorado",
                List.of(new MuroDelCuerpoDorado(), new MeditacionDelInmortal()),
                List.of(new ArmaduraDorada(3), new SelloDelFenix(20)));
        CreadorCombatientes creador = new CreadorCombatientes();

        Torneo torneo = new Torneo();
        torneo.agregarEscuchador(cronista);
        torneo.inscribir(new Pareja(1,
                creador.crear("Cultivador de la Espada", "Aurelio", espada),
                creador.crear("Hechicero del Vacio", "Nebula", vacio)));
        torneo.inscribir(new Pareja(2,
                creador.crear("Monje del Cuerpo Celestial", "Kael", monasterio),
                creador.crear("Cultivador de la Espada", "Darian", espada)));
        torneo.inscribir(new Pareja(3,
                creador.crear("Hechicero del Vacio", "Lyra", vacio),
                creador.crear("Monje del Cuerpo Celestial", "Thorne", monasterio)));
        torneo.inscribir(new Pareja(4,
                creador.crear("Cultivador de la Espada", "Soren", espada),
                creador.crear("Hechicero del Vacio", "Mira", vacio)));
        return torneo;
    }

    @Test
    public void dosTorneosIgualesProducenElMismoCampeonYElMismoHistorial() {
        CronistaDeLaSecta cronistaPrimero = new CronistaDeLaSecta();
        CronistaDeLaSecta cronistaSegundo = new CronistaDeLaSecta();

        Pareja campeonaPrimero = torneoConEscuelas(cronistaPrimero).iniciar();
        Pareja campeonaSegundo = torneoConEscuelas(cronistaSegundo).iniciar();

        assertEquals(campeonaPrimero.getId(), campeonaSegundo.getId());

        // Se comparan las descripciones: la fecha y hora cambia en cada ejecucion
        List<String> historialPrimero = cronistaPrimero.obtenerHistorial().stream()
                .map(Acontecimiento::getDescripcion).toList();
        List<String> historialSegundo = cronistaSegundo.obtenerHistorial().stream()
                .map(Acontecimiento::getDescripcion).toList();

        assertFalse(historialPrimero.isEmpty());
        assertEquals(historialPrimero, historialSegundo);
    }

    @Test
    public void torneoDeCuatroParejasRegistraTresIniciosYTresResultados() {
        Torneo torneo = torneoConCuatroParejas();
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        torneo.agregarEscuchador(cronista);

        torneo.iniciar();

        long inicios = cronista.obtenerHistorial().stream()
                .filter(InicioCombate.class::isInstance)
                .count();
        long resultados = cronista.obtenerHistorial().stream()
                .filter(ResultadoCombate.class::isInstance)
                .count();

        assertEquals(3, inicios);
        assertEquals(3, resultados);
    }

    @Test
    public void cadaInicioDeCombateApareceAntesDeSuResultado() {
        Torneo torneo = torneoConCuatroParejas();
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        torneo.agregarEscuchador(cronista);
        torneo.iniciar();

        List<Acontecimiento> historial = cronista.obtenerHistorial();
        int combatesAbiertos = 0;

        for (Acontecimiento acontecimiento : historial) {
            if (acontecimiento instanceof InicioCombate) {
                combatesAbiertos++;
            } else if (acontecimiento instanceof ResultadoCombate) {
                assertTrue(combatesAbiertos > 0,
                        "No puede existir un resultado sin inicio previo");
                combatesAbiertos--;
            }
        }

        assertEquals(0, combatesAbiertos);
    }

    @Test
    public void resultadoDeCombateConservaRondaGanadoraYVidaFinal() {
        Torneo torneo = torneoConCuatroParejas();
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        torneo.agregarEscuchador(cronista);
        torneo.iniciar();

        ResultadoCombate primerResultado = cronista.obtenerHistorial().stream()
                .filter(ResultadoCombate.class::isInstance)
                .map(ResultadoCombate.class::cast)
                .findFirst()
                .orElseThrow();

        Cruce primerCruce = torneo.getCrucesDeRonda(1).get(0);
        assertEquals(1, primerResultado.getRonda());
        assertEquals(primerCruce.getGanadora(), primerResultado.getGanadora());
        assertEquals(primerCruce.getPareja1().getVidaTotal(),
                primerResultado.getVidaTotalPareja1());
        assertEquals(primerCruce.getPareja2().getVidaTotal(),
                primerResultado.getVidaTotalPareja2());
    }

    @Test
    public void torneoFinalizaConUnaUnicaProclamacionDeLaCampeona() {
        Torneo torneo = torneoConCuatroParejas();
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        torneo.agregarEscuchador(cronista);

        Pareja campeona = torneo.iniciar();

        List<Proclamacion> proclamaciones = cronista.obtenerHistorial().stream()
                .filter(Proclamacion.class::isInstance)
                .map(Proclamacion.class::cast)
                .toList();

        assertEquals(1, proclamaciones.size());
        assertEquals(campeona, proclamaciones.get(0).getCampeona());
        assertEquals(campeona, torneo.getCampeona());
    }

    @Test
    public void proclamacionEsElUltimoAcontecimientoDelTorneo() {
        Torneo torneo = torneoConCuatroParejas();
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        torneo.agregarEscuchador(cronista);
        torneo.iniciar();

        List<Acontecimiento> historial = cronista.obtenerHistorial();

        assertFalse(historial.isEmpty());
        assertInstanceOf(Proclamacion.class, historial.get(historial.size() - 1));
    }

    @Test
    public void cronistaGeneraUnaPosicionPorCadaParejaDelTorneo() {
        Torneo torneo = torneoConCuatroParejas();
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        torneo.agregarEscuchador(cronista);
        torneo.iniciar();

        assertEquals(4, cronista.generarTablaPosiciones().size());
    }

}
