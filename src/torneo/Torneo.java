package torneo;

import acontecimientos.Acontecimiento;
import acontecimientos.EscuchadorAcontecimientos;
import acontecimientos.Proclamacion;
import combatientes.Combatiente;
import excepciones.InscripcionInvalidaException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Torneo {

    private final List<Pareja> parejasInscriptas;
    private final Set<Pareja> parejasRegistradas;
    private final Set<Combatiente> combatientesRegistrados;

    private final List<List<Cruce>> rondas;

    // Observadores del torneo: Cronista, Heraldo, etc.
    private final List<EscuchadorAcontecimientos> escuchadores;

    private boolean iniciado;
    private Pareja campeona;

    public Torneo() {
        this.parejasInscriptas = new ArrayList<>();
        this.parejasRegistradas = new HashSet<>();
        this.combatientesRegistrados = new HashSet<>();
        this.rondas = new ArrayList<>();
        this.escuchadores = new ArrayList<>();
        this.iniciado = false;
    }

    // -------- OBSERVER --------

    public void agregarEscuchador(
            EscuchadorAcontecimientos escuchador) {

        if (escuchador == null) {
            throw new NullPointerException(
                    "El escuchador no puede ser nulo."
            );
        }

        if (!escuchadores.contains(escuchador)) {
            escuchadores.add(escuchador);
        }
    }

    public void quitarEscuchador(
            EscuchadorAcontecimientos escuchador) {

        escuchadores.remove(escuchador);
    }

    private void notificar(
            Acontecimiento acontecimiento) {

        for (EscuchadorAcontecimientos escuchador
                : new ArrayList<>(escuchadores)) {

            escuchador.notificar(acontecimiento);
        }
    }

    // -------- INSCRIPCIONES --------

    public void inscribir(Pareja pareja) {

        if (iniciado) {
            throw new IllegalStateException(
                    "No se pueden registrar parejas " +
                    "una vez iniciado el torneo."
            );
        }

        if (pareja == null) {
            throw new InscripcionInvalidaException(
                    "La pareja no puede ser nula."
            );
        }

        if (parejasInscriptas.size() >= 8) {
            throw new InscripcionInvalidaException(
                    "El torneo admite como máximo ocho parejas."
            );
        }

        if (parejasRegistradas.contains(pareja)) {
            throw new InscripcionInvalidaException(
                    "La pareja ya está inscripta."
            );
        }

        for (Combatiente combatiente
                : pareja.getCombatientes()) {

            if (combatientesRegistrados.contains(combatiente)) {

                throw new InscripcionInvalidaException(
                        "El combatiente ya está inscripto " +
                        "en otra pareja."
                );
            }
        }

        parejasInscriptas.add(pareja);
        parejasRegistradas.add(pareja);

        combatientesRegistrados.addAll(
                pareja.getCombatientes()
        );
    }

    // -------- EJECUCIÓN DEL TORNEO --------

    public Pareja iniciar() {

        if (iniciado) {
            throw new IllegalStateException(
                    "El torneo ya fue iniciado."
            );
        }

        validarCantidadParejas();

        iniciado = true;

        List<Pareja> participantes =
                new ArrayList<>(parejasInscriptas);

        int numeroRonda = 1;

        while (participantes.size() > 1) {

            List<Cruce> crucesDeLaRonda =
                    new ArrayList<>();

            List<Pareja> ganadoras =
                    new ArrayList<>();

            for (int i = 0;
                 i < participantes.size();
                 i += 2) {

                Pareja primera = participantes.get(i);
                Pareja segunda = participantes.get(i + 1);

                Cruce cruce = new Cruce(
                        numeroRonda,
                        crucesDeLaRonda.size() + 1,
                        primera,
                        segunda
                );

                /*
                 * Los observadores del torneo también
                 * deben escuchar lo que ocurre
                 * dentro de cada cruce.
                 */
                for (EscuchadorAcontecimientos escuchador
                        : escuchadores) {

                    cruce.agregarEscuchador(escuchador);
                }

                crucesDeLaRonda.add(cruce);

                Pareja ganadora =
                        cruce.ejecutar();

                ganadoras.add(ganadora);
            }

            rondas.add(
                    Collections.unmodifiableList(
                            crucesDeLaRonda
                    )
            );

            participantes = ganadoras;
            numeroRonda++;
        }

        campeona = participantes.get(0);

        // Último acontecimiento del torneo
        notificar(
                new Proclamacion(campeona)
        );

        return campeona;
    }

    private void validarCantidadParejas() {

        int cantidad = parejasInscriptas.size();

        if (cantidad != 4 && cantidad != 8) {

            throw new InscripcionInvalidaException(
                    "El torneo debe iniciarse " +
                    "con exactamente 4 u 8 parejas."
            );
        }
    }

    // -------- GETTERS --------

    public List<Pareja> getParejasInscriptas() {

        return Collections.unmodifiableList(
                parejasInscriptas
        );
    }

    public List<List<Cruce>> getRondas() {

        return Collections.unmodifiableList(
                rondas
        );
    }

    public List<Cruce> getCrucesDeRonda(
            int numeroRonda) {

        if (numeroRonda <= 0
                || numeroRonda > rondas.size()) {

            throw new IllegalArgumentException(
                    "Número de ronda inexistente."
            );
        }

        return rondas.get(numeroRonda - 1);
    }

    public Pareja getCampeona() {
        return campeona;
    }

    public boolean estaIniciado() {
        return iniciado;
    }
}