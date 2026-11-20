package combate;

import acontecimientos.AccionRealizada;
import acontecimientos.Acontecimiento;
import acontecimientos.EfectoTemporalAplicado;
import acontecimientos.Eliminacion;
import acontecimientos.EscuchadorAcontecimientos;
import combatientes.Combatiente;
import tecnicas.Tecnica;
import torneo.Pareja;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Combate {

    public static final int MAXIMO_ASALTOS = 20;
    public static final int ENERGIA_RECUPERADA_POR_ASALTO = 10;

    private final Pareja pareja1;
    private final Pareja pareja2;

    private int numeroAsalto;
    private Pareja ganadora;
    private final List<EscuchadorAcontecimientos> escuchadores;

    public Combate(Pareja pareja1, Pareja pareja2) {

        if (pareja1 == null || pareja2 == null) {
            throw new IllegalArgumentException(
                    "Las parejas del combate no pueden ser nulas");
        }

        if (pareja1.equals(pareja2)) {
            throw new IllegalArgumentException(
                    "Una pareja no puede combatir contra si misma");
        }

        this.pareja1 = pareja1;
        this.pareja2 = pareja2;
        this.numeroAsalto = 0;
        this.escuchadores = new ArrayList<>();
    }

    public void agregarEscuchador(EscuchadorAcontecimientos escuchador) {
        if (escuchador == null) throw new IllegalArgumentException("El escuchador no puede ser nulo");
        if (!escuchadores.contains(escuchador)) escuchadores.add(escuchador);
    }

    public void quitarEscuchador(EscuchadorAcontecimientos escuchador) {
        escuchadores.remove(escuchador);
    }

    private void notificar(Acontecimiento acontecimiento) {
        for (EscuchadorAcontecimientos escuchador : new ArrayList<>(escuchadores)) {
            escuchador.notificar(acontecimiento);
        }
    }

    public Pareja ejecutar() {

        prepararCombate();

        while (numeroAsalto < MAXIMO_ASALTOS
                && pareja1.tieneCombatientesEnPie()
                && pareja2.tieneCombatientesEnPie()) {

            numeroAsalto++;

            recuperarEnergiaInicioAsalto();

            ejecutarAsalto();
        }

        ganadora = determinarGanadora();

        pareja1.limpiarBloqueosDeCombate();
        pareja2.limpiarBloqueosDeCombate();

        return ganadora;
    }

    private void prepararCombate() {
        pareja1.restaurarParaNuevoCombate();
        pareja2.restaurarParaNuevoCombate();
    }

    private void recuperarEnergiaInicioAsalto() {

        for (Combatiente combatiente : pareja1.getCombatientesEnPie()) {
            combatiente.recuperarEnergia(ENERGIA_RECUPERADA_POR_ASALTO);
        }

        for (Combatiente combatiente : pareja2.getCombatientesEnPie()) {
            combatiente.recuperarEnergia(ENERGIA_RECUPERADA_POR_ASALTO);
        }
    }

    private void ejecutarAsalto() {

        Pareja primera;
        Pareja segunda;

        if (pareja1.getId() < pareja2.getId()) {
            primera = pareja1;
            segunda = pareja2;
        } else {
            primera = pareja2;
            segunda = pareja1;
        }

        ejecutarTurnoPareja(primera, segunda);

        if (!primera.tieneCombatientesEnPie() || !segunda.tieneCombatientesEnPie()) {
            return;
        }

        ejecutarTurnoPareja(segunda, primera);
    }

    private void ejecutarTurnoPareja(
            Pareja actuante,
            Pareja rival) {

        List<Combatiente> orden =
                new ArrayList<>(actuante.getCombatientes());

        orden.sort(Comparator.comparingInt(Combatiente::getId));

        for (Combatiente combatiente : orden) {

            if (!combatiente.estaEnPie()) {
                continue;
            }

            String ultimoEfecto = null;
            for (EfectoTemporal efecto : combatiente.procesarEfectosDeTurno()) {
                ultimoEfecto = efecto.getNombre();
                notificar(new EfectoTemporalAplicado(
                        numeroAsalto,
                        combatiente,
                        efecto.getNombre(),
                        efecto.getTurnosRestantes()));
            }

            if (!combatiente.estaEnPie()) {
                notificar(new Eliminacion(numeroAsalto, combatiente, ultimoEfecto));
                continue;
            }

            if (!rival.tieneCombatientesEnPie()) {
                return;
            }

            ejecutarAccion(combatiente, actuante, rival);
        }
    }

    private void ejecutarAccion(
            Combatiente actor,
            Pareja aliados,
            Pareja rivales) {

        Tecnica tecnica = elegirTecnica(actor, aliados);

        if (tecnica == null) {
            Combatiente objetivo = ejecutarAtaqueBasico(actor, rivales);
            notificar(new AccionRealizada(numeroAsalto, actor, objetivo, "Ataque basico", false));
            notificarEliminacionSiCorresponde(objetivo, actor);
            return;
        }

        Combatiente objetivo;

        if (tecnica.esOfensiva()) {
            objetivo = elegirObjetivo(rivales);
        } else {
            objetivo = actor;
        }

        tecnica.aplicar(actor, objetivo);
        aliados.registrarUsoDeTecnica(actor, tecnica);
        notificar(new AccionRealizada(numeroAsalto, actor, objetivo, tecnica.getNombre(), true));
        if (tecnica.esOfensiva()) notificarEliminacionSiCorresponde(objetivo, actor);
    }

    private Tecnica elegirTecnica(
            Combatiente actor,
            Pareja aliados) {

        List<Tecnica> disponibles =
                actor.getTecnicasDisponibles();

        // 1. Recuperacion si le falta vida
        if (actor.getVidaActual() < actor.getVidaMaxima()) {

            Tecnica recuperacion =
                    buscarPrimeraTecnicaPosible(
                            actor,
                            aliados,
                            disponibles,
                            TipoBusqueda.RECUPERACION);

            if (recuperacion != null) {
                return recuperacion;
            }
        }

        // 2. Defensiva si no tiene una defensa activa
        if (actor.getReduccionDanioCombate() == 0) {

            Tecnica defensiva =
                    buscarPrimeraTecnicaPosible(
                            actor,
                            aliados,
                            disponibles,
                            TipoBusqueda.DEFENSIVA);

            if (defensiva != null) {
                return defensiva;
            }
        }

        // 3. Ofensiva con mas usos restantes
        Tecnica mejorOfensiva = null;
        int mayorCantidadUsos = -1;

        for (Tecnica tecnica : disponibles) {

            if (!tecnica.esOfensiva()) {
                continue;
            }

            if (!puedeEjecutarTecnica(actor, aliados, tecnica)) {
                continue;
            }

            int usos = actor.getUsosRestantes(tecnica);

            if (usos > mayorCantidadUsos) {
                mejorOfensiva = tecnica;
                mayorCantidadUsos = usos;
            }
        }

        return mejorOfensiva;
    }

    private Tecnica buscarPrimeraTecnicaPosible(
            Combatiente actor,
            Pareja aliados,
            List<Tecnica> tecnicas,
            TipoBusqueda tipo) {

        for (Tecnica tecnica : tecnicas) {

            boolean corresponde = switch (tipo) {
                case RECUPERACION -> tecnica.esDeRecuperacion();
                case DEFENSIVA -> tecnica.esDefensiva();
            };

            if (corresponde
                    && puedeEjecutarTecnica(actor, aliados, tecnica)) {
                return tecnica;
            }
        }

        return null;
    }

    private boolean puedeEjecutarTecnica(
            Combatiente actor,
            Pareja aliados,
            Tecnica tecnica) {

        return actor.estaEnPie()
                && actor.getUsosRestantes(tecnica) > 0
                && actor.tieneEnergiaSuficiente(
                        tecnica.getCostoEnergia())
                && aliados.puedeUsarTecnica(actor, tecnica);
    }

    private Combatiente ejecutarAtaqueBasico(
            Combatiente atacante,
            Pareja rivales) {

        Combatiente objetivo =
                elegirObjetivo(rivales);

        int danio =
                atacante.calcularDanioAtaqueBasico();

        objetivo.recibirDanio(danio);
        return objetivo;
    }

    private void notificarEliminacionSiCorresponde(Combatiente objetivo, Combatiente causante) {
        if (!objetivo.estaEnPie()) notificar(new Eliminacion(numeroAsalto, objetivo, causante));
    }

    private Combatiente elegirObjetivo(Pareja rivales) {

        return rivales.getCombatientesEnPie()
                .stream()
                .min(
                        Comparator
                                .comparingInt(Combatiente::getVidaActual)
                                .thenComparingInt(Combatiente::getId)
                )
                .orElseThrow(() ->
                        new IllegalStateException(
                                "No existen rivales en pie"));
    }

    private Pareja determinarGanadora() {

        if (!pareja1.tieneCombatientesEnPie()) {
            return pareja2;
        }

        if (!pareja2.tieneCombatientesEnPie()) {
            return pareja1;
        }

        int vidaPareja1 = pareja1.getVidaTotal();
        int vidaPareja2 = pareja2.getVidaTotal();

        if (vidaPareja1 > vidaPareja2) {
            return pareja1;
        }

        if (vidaPareja2 > vidaPareja1) {
            return pareja2;
        }

        // Desempate indicado por la consigna:
        // gana la pareja de menor identificador
        return pareja1.getId() < pareja2.getId()
                ? pareja1
                : pareja2;
    }

    public int getNumeroAsalto() {
        return numeroAsalto;
    }

    public Pareja getGanadora() {
        return ganadora;
    }

    private enum TipoBusqueda {
        RECUPERACION,
        DEFENSIVA
    }
}