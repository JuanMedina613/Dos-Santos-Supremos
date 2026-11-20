package pruebas;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import acontecimientos.AccionRealizada;
import acontecimientos.Acontecimiento;
import acontecimientos.CronistaDeLaSecta;
import acontecimientos.Eliminacion;
import combate.Combate;
import combatientes.Combatiente;
import combatientes.CultivadorDeLaEspada;
import combatientes.HechiceroDelVacio;
import combatientes.MonjeDelCuerpoCelestial;
import torneo.Pareja;
import tecnicas.Tecnica;
import tecnicas.CorteDeLosMilAmaneceres;
import tecnicas.MuroDelCuerpoDorado;
import tecnicas.RugidoBestiaPrimordial;
import tecnicas.MeditacionDelInmortal;
import equipamiento.SelloDelFenix;

import java.util.List;

public class PruebasCombate {

	 private static class CombatienteSinDanio
     extends Combatiente {

	 public CombatienteSinDanio(
	         int id,
	         String nombre) {
	
	     super(id, nombre);
	 }
	
	 @Override
	 public int calcularDanioAtaqueBasico() {
	     return 0;
	 }
	}
	
	private static class CombatienteLetal
	     extends Combatiente {
	
	 public CombatienteLetal(
	         int id,
	         String nombre) {
	
	     super(id, nombre);
	 }
	
	 @Override
	 public int calcularDanioAtaqueBasico() {
	     return 1000;
	 }
	}
	
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
         * Combatientes que no hacen daño: el combate llega al límite
         * de asaltos con la misma vida total en ambas parejas.
         * La pareja de mayor id se pasa primero para que el resultado
         * no dependa del orden de los parámetros.
         */
        Pareja parejaMenorId = new Pareja(1,
                new CombatienteSinDanio(1, "A"), new CombatienteSinDanio(2, "B"));
        Pareja parejaMayorId = new Pareja(2,
                new CombatienteSinDanio(3, "C"), new CombatienteSinDanio(4, "D"));

        Combate combate = new Combate(parejaMayorId, parejaMenorId);
        Pareja ganadora = combate.ejecutar();

        assertEquals(Combate.MAXIMO_ASALTOS, combate.getNumeroAsalto());
        assertEquals(parejaMenorId.getVidaTotal(), parejaMayorId.getVidaTotal());
        assertEquals(parejaMenorId, ganadora);
    }

    @Test
    public void alLimiteDeAsaltosGanaLaParejaConMasVidaTotal() {
        Combatiente conSello = new CombatienteSinDanio(3, "Con sello");
        conSello.equipar(new SelloDelFenix(30));

        Pareja parejaMenorId = new Pareja(1,
                new CombatienteSinDanio(1, "A"), new CombatienteSinDanio(2, "B"));
        Pareja parejaConMasVida = new Pareja(2,
                conSello, new CombatienteSinDanio(4, "D"));

        Combate combate = new Combate(parejaMenorId, parejaConMasVida);
        Pareja ganadora = combate.ejecutar();

        assertEquals(Combate.MAXIMO_ASALTOS, combate.getNumeroAsalto());
        assertTrue(parejaConMasVida.getVidaTotal() > parejaMenorId.getVidaTotal());
        assertEquals(parejaConMasVida, ganadora);
    }

    @Test
    public void losUsosSeConservanEntreCombatesYVidaYEnergiaSeRecuperan() {
        Tecnica corte = new CorteDeLosMilAmaneceres();
        Combatiente atacante = new CultivadorDeLaEspada(1, "Atacante");
        atacante.aprenderTecnica(corte);
        Pareja parejaAtacante = new Pareja(1, atacante, new CombatienteSinDanio(2, "Companero"));
        Pareja parejaRival = new Pareja(2,
                new CultivadorDeLaEspada(3, "Rival 1"), new CultivadorDeLaEspada(4, "Rival 2"));

        new Combate(parejaAtacante, parejaRival).ejecutar();

        int usosTrasPrimerCombate = atacante.getUsosRestantes(corte);
        assertTrue(usosTrasPrimerCombate < Combatiente.USOS_MAXIMOS_TECNICA);
        assertTrue(atacante.getEnergiaActual() < atacante.getEnergiaMaxima()
                || atacante.getVidaActual() < atacante.getVidaMaxima());

        // Es lo que hace Combate al comenzar el siguiente combate.
        parejaAtacante.restaurarParaNuevoCombate();
        parejaRival.restaurarParaNuevoCombate();

        assertEquals(usosTrasPrimerCombate, atacante.getUsosRestantes(corte));
        for (Combatiente c : List.of(atacante, parejaRival.getCombatiente1(), parejaRival.getCombatiente2())) {
            assertEquals(c.getVidaMaxima(), c.getVidaActual());
            assertEquals(c.getEnergiaMaxima(), c.getEnergiaActual());
            assertTrue(c.estaEnPie());
        }
    }

    @Test
    public void losCaidosEnUnCombateVuelvenAActuarEnElSiguiente() {
        Pareja fuerte = new Pareja(1,
                new CombatienteLetal(1, "Letal 1"), new CombatienteLetal(2, "Letal 2"));
        Combatiente debil1 = new CultivadorDeLaEspada(3, "Debil 1");
        Combatiente debil2 = new CultivadorDeLaEspada(4, "Debil 2");
        Pareja debil = new Pareja(2, debil1, debil2);

        new Combate(fuerte, debil).ejecutar();
        assertFalse(debil.tieneCombatientesEnPie());

        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        Pareja otraFuerte = new Pareja(3,
                new CombatienteLetal(5, "Letal 3"), new CombatienteLetal(6, "Letal 4"));
        Combate segundo = new Combate(debil, otraFuerte);
        segundo.agregarEscuchador(cronista);
        segundo.ejecutar();

        boolean debilActuo = cronista.obtenerHistorial().stream()
                .filter(AccionRealizada.class::isInstance)
                .map(AccionRealizada.class::cast)
                .anyMatch(a -> a.getActor().equals(debil1));
        assertTrue(debilActuo);
    }

    @Test
    public void anteRivalesConLaMismaVidaEligeComoObjetivoAlDeMenorId() {
        Combatiente actor = new CombatienteLetal(1, "Actor");
        Pareja parejaActuante = new Pareja(
                1, actor, new CombatienteSinDanio(2, "Companero"));

        Combatiente rivalMenorId = new CombatienteSinDanio(3, "Rival menor ID");
        Combatiente rivalMayorId = new CombatienteSinDanio(4, "Rival mayor ID");
        Pareja parejaRival = new Pareja(2, rivalMenorId, rivalMayorId);

        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        Combate combate = new Combate(parejaActuante, parejaRival);
        combate.agregarEscuchador(cronista);
        combate.ejecutar();

        AccionRealizada primeraAccion = cronista.obtenerHistorial().stream()
                .filter(AccionRealizada.class::isInstance)
                .map(AccionRealizada.class::cast)
                .findFirst()
                .orElseThrow();

        assertEquals(actor, primeraAccion.getActor());
        assertEquals(rivalMenorId, primeraAccion.getObjetivo());
    }

    @Test
    public void unCombatienteCaidoNoVuelveAActuarNiAserObjetivo() {
        Combatiente atacante = new CombatienteLetal(1, "Atacante");
        Pareja parejaActuante = new Pareja(
                1, atacante, new CombatienteSinDanio(2, "Companero"));

        Combatiente primeroEnCaer = new CombatienteSinDanio(3, "Primero en caer");
        Combatiente sobreviviente = new CombatienteSinDanio(4, "Sobreviviente");
        Pareja parejaRival = new Pareja(2, primeroEnCaer, sobreviviente);

        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        Combate combate = new Combate(parejaActuante, parejaRival);
        combate.agregarEscuchador(cronista);
        combate.ejecutar();

        java.util.List<Acontecimiento> historial = cronista.obtenerHistorial();
        int indiceEliminacion = -1;
        for (int i = 0; i < historial.size(); i++) {
            Acontecimiento acontecimiento = historial.get(i);
            if (acontecimiento instanceof Eliminacion eliminacion
                    && eliminacion.getEliminado().equals(primeroEnCaer)) {
                indiceEliminacion = i;
                break;
            }
        }

        assertTrue(indiceEliminacion >= 0,
                "Debe registrarse la eliminacion del combatiente.");

        for (int i = indiceEliminacion + 1; i < historial.size(); i++) {
            if (historial.get(i) instanceof AccionRealizada accion) {
                assertNotEquals(primeroEnCaer, accion.getActor(),
                        "Un caido no debe volver a actuar.");
                assertNotEquals(primeroEnCaer, accion.getObjetivo(),
                        "Un caido no debe volver a ser objetivo.");
            }
        }
    }

    @Test
    public void dentroDeLaParejaActuaPrimeroElCombatienteDeMenorId() {
        Combatiente menorId = new CombatienteSinDanio(1, "Menor ID");
        Combatiente mayorId = new CombatienteSinDanio(2, "Mayor ID");
        Pareja primeraPareja = new Pareja(
                1, mayorId, menorId); // orden invertido intencionalmente
        Pareja segundaPareja = new Pareja(
                2,
                new CombatienteSinDanio(3, "Rival 1"),
                new CombatienteSinDanio(4, "Rival 2"));

        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        Combate combate = new Combate(primeraPareja, segundaPareja);
        combate.agregarEscuchador(cronista);
        combate.ejecutar();

        AccionRealizada primeraAccion = cronista.obtenerHistorial().stream()
                .filter(AccionRealizada.class::isInstance)
                .map(AccionRealizada.class::cast)
                .findFirst()
                .orElseThrow();

        assertEquals(menorId, primeraAccion.getActor());
    }

    @Test
    public void comienzaLaParejaDeMenorIdentificador() {
        Combatiente actorParejaMenor = new CombatienteSinDanio(10, "Pareja menor");
        Pareja parejaMenor = new Pareja(
                1, actorParejaMenor, new CombatienteSinDanio(11, "Companero menor"));
        Pareja parejaMayor = new Pareja(
                2,
                new CombatienteSinDanio(1, "Pareja mayor A"),
                new CombatienteSinDanio(2, "Pareja mayor B"));

        CronistaDeLaSecta cronista = new CronistaDeLaSecta();
        Combate combate = new Combate(parejaMayor, parejaMenor);
        combate.agregarEscuchador(cronista);
        combate.ejecutar();

        AccionRealizada primeraAccion = cronista.obtenerHistorial().stream()
                .filter(AccionRealizada.class::isInstance)
                .map(AccionRealizada.class::cast)
                .findFirst()
                .orElseThrow();

        assertTrue(parejaMenor.contiene(primeraAccion.getActor()));
    }


    private AccionRealizada primeraAccionDe(
            Combate combate,
            CronistaDeLaSecta cronista) {

        combate.agregarEscuchador(cronista);
        combate.ejecutar();

        return cronista.obtenerHistorial()
                .stream()
                .filter(AccionRealizada.class::isInstance)
                .map(AccionRealizada.class::cast)
                .findFirst()
                .orElseThrow();
    }

    @Test
    public void siNoTieneDefensaActivaPriorizaTecnicaDefensiva() {
        Combatiente actor = new CultivadorDeLaEspada(1, "Actor");
        Tecnica defensiva = new MuroDelCuerpoDorado();
        Tecnica ofensiva = new CorteDeLosMilAmaneceres();
        actor.aprenderTecnica(defensiva);
        actor.aprenderTecnica(ofensiva);

        Pareja aliados = new Pareja(
                1, actor, new CombatienteSinDanio(2, "Companero"));
        Pareja rivales = new Pareja(
                2,
                new CombatienteSinDanio(3, "Rival 1"),
                new CombatienteSinDanio(4, "Rival 2"));

        AccionRealizada accion = primeraAccionDe(
                new Combate(aliados, rivales),
                new CronistaDeLaSecta());

        assertEquals(defensiva.getNombre(), accion.getNombreAccion());
        assertTrue(accion.isEsTecnica());
        assertEquals(actor, accion.getObjetivo());
    }

    @Test
    public void entreOfensivasEligeLaDeMasUsosRestantes() {
        Combatiente actor = new CultivadorDeLaEspada(1, "Actor");
        Tecnica conMenosUsos = new CorteDeLosMilAmaneceres();
        Tecnica conMasUsos = new RugidoBestiaPrimordial();
        actor.aprenderTecnica(conMenosUsos);
        actor.aprenderTecnica(conMasUsos);
        actor.consumirUsoTecnica(conMenosUsos);
        actor.consumirUsoTecnica(conMenosUsos);

        Pareja aliados = new Pareja(
                1, actor, new CombatienteSinDanio(2, "Companero"));
        Pareja rivales = new Pareja(
                2,
                new CombatienteSinDanio(3, "Rival 1"),
                new CombatienteSinDanio(4, "Rival 2"));

        AccionRealizada accion = primeraAccionDe(
                new Combate(aliados, rivales),
                new CronistaDeLaSecta());

        assertEquals(conMasUsos.getNombre(), accion.getNombreAccion());
        assertTrue(accion.isEsTecnica());
    }

    @Test
    public void siNoPuedeUsarTecnicasRealizaAtaqueBasico() {
        Combatiente actor = new CultivadorDeLaEspada(1, "Actor");
        Tecnica ofensiva = new CorteDeLosMilAmaneceres();
        actor.aprenderTecnica(ofensiva);

        for (int i = 0; i < Combatiente.USOS_MAXIMOS_TECNICA; i++) {
            actor.consumirUsoTecnica(ofensiva);
        }

        Pareja aliados = new Pareja(
                1, actor, new CombatienteSinDanio(2, "Companero"));
        Pareja rivales = new Pareja(
                2,
                new CombatienteSinDanio(3, "Rival 1"),
                new CombatienteSinDanio(4, "Rival 2"));

        AccionRealizada accion = primeraAccionDe(
                new Combate(aliados, rivales),
                new CronistaDeLaSecta());

        assertEquals("Ataque basico", accion.getNombreAccion());
        assertFalse(accion.isEsTecnica());
    }

    @Test
    public void anteEmpateDeUsosEligeLaPrimeraTecnicaAprendida() {
        Combatiente actor = new CultivadorDeLaEspada(1, "Actor");
        Tecnica primera = new CorteDeLosMilAmaneceres();
        Tecnica segunda = new RugidoBestiaPrimordial();
        actor.aprenderTecnica(primera);
        actor.aprenderTecnica(segunda);

        Pareja aliados = new Pareja(
                1, actor, new CombatienteSinDanio(2, "Companero"));
        Pareja rivales = new Pareja(
                2,
                new CombatienteSinDanio(3, "Rival 1"),
                new CombatienteSinDanio(4, "Rival 2"));

        AccionRealizada accion = primeraAccionDe(
                new Combate(aliados, rivales),
                new CronistaDeLaSecta());

        assertEquals(primera.getNombre(), accion.getNombreAccion());
        assertTrue(accion.isEsTecnica());
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
    
    @Test
    public void siLeFaltaVidaPriorizaTecnicaDeRecuperacion() {

        /*
         * La pareja rival tiene menor ID, por lo que actúa primero
         * y daña al actor antes de que llegue su turno.
         */
        Combatiente rival =
                new CultivadorDeLaEspada(1, "Rival");

        Pareja parejaRival =
                new Pareja(
                        1,
                        rival,
                        new CombatienteSinDanio(2, "Companero rival")
                );

        Combatiente actor =
                new CultivadorDeLaEspada(3, "Actor");

        Tecnica recuperacion =
                new MeditacionDelInmortal();

        Tecnica ofensiva =
                new CorteDeLosMilAmaneceres();

        actor.aprenderTecnica(recuperacion);
        actor.aprenderTecnica(ofensiva);

        Pareja parejaActor =
                new Pareja(
                        2,
                        actor,
                        new CombatienteSinDanio(4, "Companero")
                );

        CronistaDeLaSecta cronista =
                new CronistaDeLaSecta();

        Combate combate =
                new Combate(parejaRival, parejaActor);

        combate.agregarEscuchador(cronista);
        combate.ejecutar();

        /*
         * Buscamos la primera acción realizada específicamente
         * por el actor.
         */
        AccionRealizada accionDelActor =
                cronista.obtenerHistorial()
                        .stream()
                        .filter(AccionRealizada.class::isInstance)
                        .map(AccionRealizada.class::cast)
                        .filter(accion ->
                                accion.getActor().equals(actor))
                        .findFirst()
                        .orElseThrow();

        assertEquals(
                recuperacion.getNombre(),
                accionDelActor.getNombreAccion()
        );

        assertTrue(
                accionDelActor.isEsTecnica()
        );

        assertEquals(
                actor,
                accionDelActor.getObjetivo()
        );
    }
    
    @Test
    public void eligeComoObjetivoAlRivalEnPieConMenorVida() {

        Combatiente atacante1 =
                new CultivadorDeLaEspada(1, "Atacante 1");

        Combatiente atacante2 =
                new CultivadorDeLaEspada(2, "Atacante 2");

        Pareja parejaAtacante =
                new Pareja(
                        1,
                        atacante1,
                        atacante2
                );

        Combatiente rival1 =
                new CombatienteSinDanio(3, "Rival 1");

        Combatiente rival2 =
                new CombatienteSinDanio(4, "Rival 2");

        Pareja parejaRival =
                new Pareja(
                        2,
                        rival1,
                        rival2
                );

        CronistaDeLaSecta cronista =
                new CronistaDeLaSecta();

        Combate combate =
                new Combate(
                        parejaAtacante,
                        parejaRival
                );

        combate.agregarEscuchador(cronista);
        combate.ejecutar();

        /*
         * El primer atacante encuentra a ambos rivales
         * con la misma vida.
         *
         * Por desempate ataca al de menor ID: rival1.
         *
         * Cuando actúa atacante2, rival1 ya tiene
         * menos vida que rival2.
         *
         * Por lo tanto atacante2 también debe elegir
         * a rival1.
         */
        AccionRealizada accionDelSegundoAtacante =
                cronista.obtenerHistorial()
                        .stream()
                        .filter(AccionRealizada.class::isInstance)
                        .map(AccionRealizada.class::cast)
                        .filter(accion ->
                                accion.getActor()
                                        .equals(atacante2))
                        .findFirst()
                        .orElseThrow();

        assertTrue(
                rival1.getId() < rival2.getId()
        );

        assertEquals(
                rival1,
                accionDelSegundoAtacante.getObjetivo()
        );
    }
    
    @Test
    public void recuperarEnergiaNoSuperaElMaximo() {

        Combatiente combatiente =
                new CultivadorDeLaEspada(1, "Actor");

        int energiaMaxima =
                combatiente.getEnergiaMaxima();

        combatiente.gastarEnergia(5);

        assertEquals(
                energiaMaxima - 5,
                combatiente.getEnergiaActual()
        );

        combatiente.recuperarEnergia(
                Combate.ENERGIA_RECUPERADA_POR_ASALTO
        );

        assertEquals(
                energiaMaxima,
                combatiente.getEnergiaActual()
        );
    }
}