package main;

import acontecimientos.CronistaDeLaSecta;
import acontecimientos.HeraldoImperial;
import combatientes.Combatiente;
import combatientes.CreadorCombatientes;
import combatientes.Escuela;
import equipamiento.AnilloEspiritual;
import equipamiento.ArmaduraDorada;
import equipamiento.CategoriaEquipamiento;
import equipamiento.EspadaCelestial;
import equipamiento.SelloDelFenix;
import tecnicas.AlientoVital;
import tecnicas.CorteDeLosMilAmaneceres;
import tecnicas.DominioDeLaNada;
import tecnicas.MeditacionDelInmortal;
import tecnicas.MuroDelCuerpoDorado;
import tecnicas.PalmaEnvenenada;
import tecnicas.RugidoBestiaPrimordial;
import torneo.Pareja;
import torneo.Torneo;

import java.util.List;

public class TorneoDeLosInmortales {

    public static void main(String[] args) {
        HeraldoImperial heraldo = new HeraldoImperial();

        ejecutarEscenarioCuatroParejas(heraldo);
        ejecutarEscenarioOchoParejasConBonus(heraldo);
    }

    private static void ejecutarEscenarioCuatroParejas(HeraldoImperial heraldo) {
        heraldo.anunciar("=================================================================");
        heraldo.anunciar(" ESCENARIO 1: TORNEO DE 4 PAREJAS (ESCUELAS CLÁSICAS Y EQUIPO)");
        heraldo.anunciar("=================================================================");

        Escuela escuelaDeLaEspada = new Escuela(
                "Escuela de la Espada Celestial",
                List.of(new CorteDeLosMilAmaneceres(), new RugidoBestiaPrimordial()),
                List.of(new EspadaCelestial(8)));

        Escuela palacioDelVacio = new Escuela(
                "Palacio del Vacío",
                List.of(new DominioDeLaNada(), new MeditacionDelInmortal()),
                List.of(new AnilloEspiritual(15)));

        Escuela monasterioCuerpoDorado = new Escuela(
                "Monasterio del Cuerpo Dorado",
                List.of(new MuroDelCuerpoDorado(), new MeditacionDelInmortal()),
                List.of(new ArmaduraDorada(5), new SelloDelFenix(20)));

        CreadorCombatientes creador = new CreadorCombatientes();

        // Pareja 1
        Combatiente aurelio = creador.crear("Cultivador de la Espada", "Aurelio", escuelaDeLaEspada);
        Combatiente nebula = creador.crear("Hechicero del Vacío", "Nébula", palacioDelVacio);

        // Pareja 2
        Combatiente kael = creador.crear("Monje del Cuerpo Celestial", "Kael", monasterioCuerpoDorado);
        Combatiente darian = creador.crear("Cultivador de la Espada", "Darian", escuelaDeLaEspada);

        // Pareja 3 (ajuste de equipamiento previo al torneo)
        Combatiente lyra = creador.crear("Hechicero del Vacío", "Lyra", palacioDelVacio);
        lyra.equipar(new SelloDelFenix(15));
        Combatiente thorne = creador.crear("Monje del Cuerpo Celestial", "Thorne", monasterioCuerpoDorado);
        thorne.quitarEquipamientoPorCategoria(CategoriaEquipamiento.SELLO);
        thorne.equipar(new EspadaCelestial(4));

        // Pareja 4
        Combatiente soren = creador.crear("Cultivador de la Espada", "Soren", escuelaDeLaEspada);
        Combatiente mira = creador.crear("Hechicero del Vacío", "Mira", palacioDelVacio);

        Torneo torneo = new Torneo();
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();

        torneo.agregarEscuchador(heraldo);
        torneo.agregarEscuchador(cronista);

        torneo.inscribir(new Pareja(1, aurelio, nebula));
        torneo.inscribir(new Pareja(2, kael, darian));
        torneo.inscribir(new Pareja(3, lyra, thorne));
        torneo.inscribir(new Pareja(4, soren, mira));

        torneo.iniciar();

        heraldo.presentarTablaPosiciones(cronista.generarTablaPosiciones());
    }

    private static void ejecutarEscenarioOchoParejasConBonus(HeraldoImperial heraldo) {
        heraldo.anunciar("=================================================================");
        heraldo.anunciar(" ESCENARIO 2: TORNEO DE 8 PAREJAS Y EFECTOS TEMPORALES (BONUS)");
        heraldo.anunciar("=================================================================");

        Escuela sectaVenenoSombrio = new Escuela(
                "Secta del Veneno Sombrío",
                List.of(new PalmaEnvenenada(), new CorteDeLosMilAmaneceres()),
                List.of(new EspadaCelestial(5), new AnilloEspiritual(10)));

        Escuela jardinInmortal = new Escuela(
                "Jardín de la Longevidad Inmortal",
                List.of(new AlientoVital(), new MuroDelCuerpoDorado()),
                List.of(new ArmaduraDorada(4), new SelloDelFenix(25)));

        Escuela escuelaDeLaEspada = new Escuela(
                "Escuela de la Espada Celestial",
                List.of(new CorteDeLosMilAmaneceres(), new RugidoBestiaPrimordial()),
                List.of(new EspadaCelestial(7)));

        Escuela palacioDelVacio = new Escuela(
                "Palacio del Vacío",
                List.of(new DominioDeLaNada(), new MeditacionDelInmortal()),
                List.of(new AnilloEspiritual(15)));

        CreadorCombatientes creador = new CreadorCombatientes();
        Torneo torneo = new Torneo();
        CronistaDeLaSecta cronista = new CronistaDeLaSecta();

        torneo.agregarEscuchador(heraldo);
        torneo.agregarEscuchador(cronista);

        torneo.inscribir(new Pareja(1,
                creador.crear("Cultivador de la Espada", "Shen", sectaVenenoSombrio),
                creador.crear("Monje del Cuerpo Celestial", "Bao", jardinInmortal)));

        torneo.inscribir(new Pareja(2,
                creador.crear("Hechicero del Vacío", "Zhen", palacioDelVacio),
                creador.crear("Cultivador de la Espada", "Lin", escuelaDeLaEspada)));

        torneo.inscribir(new Pareja(3,
                creador.crear("Monje del Cuerpo Celestial", "Wei", jardinInmortal),
                creador.crear("Hechicero del Vacío", "Xue", sectaVenenoSombrio)));

        torneo.inscribir(new Pareja(4,
                creador.crear("Cultivador de la Espada", "Feng", escuelaDeLaEspada),
                creador.crear("Monje del Cuerpo Celestial", "Hao", jardinInmortal)));

        torneo.inscribir(new Pareja(5,
                creador.crear("Hechicero del Vacío", "Mei", palacioDelVacio),
                creador.crear("Cultivador de la Espada", "Jun", sectaVenenoSombrio)));

        torneo.inscribir(new Pareja(6,
                creador.crear("Monje del Cuerpo Celestial", "Rui", jardinInmortal),
                creador.crear("Hechicero del Vacío", "Lan", palacioDelVacio)));

        torneo.inscribir(new Pareja(7,
                creador.crear("Cultivador de la Espada", "Tao", escuelaDeLaEspada),
                creador.crear("Hechicero del Vacío", "Yun", sectaVenenoSombrio)));

        torneo.inscribir(new Pareja(8,
                creador.crear("Monje del Cuerpo Celestial", "Long", jardinInmortal),
                creador.crear("Cultivador de la Espada", "Jin", escuelaDeLaEspada)));

        torneo.iniciar();

        heraldo.presentarTablaPosiciones(cronista.generarTablaPosiciones());
    }
}
