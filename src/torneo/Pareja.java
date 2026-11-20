package torneo;

import combatientes.Combatiente;
import tecnicas.Tecnica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pareja {

    private final int id;
    private final Combatiente combatiente1;
    private final Combatiente combatiente2;

    /*
     * Registra qué combatiente utilizó primero una técnica
     * durante el combate actual.
     *
     * Mientras ese combatiente siga en pie y tenga usos disponibles,
     * su compañero no podrá utilizar la misma técnica.
     */
    private final Map<Tecnica, Combatiente> usuarioPorTecnica;

    public Pareja(int id, Combatiente combatiente1, Combatiente combatiente2) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El identificador de la pareja debe ser positivo.");
        }

        if (combatiente1 == null) {
            throw new NullPointerException(
                    "El primer combatiente no puede ser nulo.");
        }

        if (combatiente2 == null) {
            throw new NullPointerException(
                    "El segundo combatiente no puede ser nulo.");
        }

        this.combatiente1 = combatiente1;
        this.combatiente2 = combatiente2;

        if (combatiente1.equals(combatiente2)) {
            throw new IllegalArgumentException(
                    "Una pareja no puede contener dos veces al mismo combatiente.");
        }

        this.id = id;
        this.usuarioPorTecnica = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public List<Combatiente> getCombatientes() {
        return List.of(combatiente1, combatiente2);
    }
    
    public Combatiente getCombatiente1() {
		return this.combatiente1;
    }
    
    public Combatiente getCombatiente2() {
		return this.combatiente2;
    }

    public List<Combatiente> getCombatientesEnPie() {
        List<Combatiente> combatientesEnPie = new ArrayList<>();

        if (combatiente1.estaEnPie()) {
            combatientesEnPie.add(combatiente1);
        }

        if (combatiente2.estaEnPie()) {
            combatientesEnPie.add(combatiente2);
        }

        return combatientesEnPie;
    }

    public boolean tieneCombatientesEnPie() {
        return combatiente1.estaEnPie() || combatiente2.estaEnPie();
    }

    public boolean contiene(Combatiente combatiente) {
        return combatiente1.equals(combatiente)
                || combatiente2.equals(combatiente);
    }

    public boolean puedeUsarTecnica(
            Combatiente combatiente,
            Tecnica tecnica) {

        if (combatiente == null) {
            throw new NullPointerException(
                    "El combatiente no puede ser nulo.");
        }

        if (tecnica == null) {
            throw new NullPointerException(
                    "La técnica no puede ser nula.");
        }

        if (!contiene(combatiente)) {
            return false;
        }

        Combatiente usuarioRegistrado = usuarioPorTecnica.get(tecnica);

        // Nadie de la pareja utilizó todavía esta técnica.
        if (usuarioRegistrado == null) {
            return true;
        }

        // El mismo combatiente que inició su uso puede seguir utilizándola.
        if (usuarioRegistrado.equals(combatiente)) {
            return true;
        }

        /*
         * Si el combatiente que bloqueaba la técnica cayó
         * o agotó sus usos, el compañero puede utilizarla.
         */
        if (!usuarioRegistrado.estaEnPie()
                || usuarioRegistrado.getUsosRestantes(tecnica) == 0) {

            usuarioPorTecnica.remove(tecnica);
            return true;
        }

        return false;
    }

    public void registrarUsoDeTecnica(
            Combatiente combatiente,
            Tecnica tecnica) {

        if (combatiente == null) {
            throw new NullPointerException(
                    "El combatiente no puede ser nulo.");
        }

        if (tecnica == null) {
            throw new NullPointerException(
                    "La técnica no puede ser nula.");
        }

        if (!contiene(combatiente)) {
            throw new IllegalArgumentException(
                    "El combatiente no pertenece a esta pareja.");
        }

        usuarioPorTecnica.putIfAbsent(tecnica, combatiente);
    }

    public void limpiarBloqueosDeCombate() {
        usuarioPorTecnica.clear();
    }

    public void restaurarParaNuevoCombate() {
        combatiente1.restaurarParaNuevoCombate();
        combatiente2.restaurarParaNuevoCombate();
        limpiarBloqueosDeCombate();
    }
    
    public int getVidaTotal() {
        int total = 0;
        if (combatiente1.estaEnPie()) {
            total += combatiente1.getVidaActual();
        }
        if (combatiente2.estaEnPie()) {
            total += combatiente2.getVidaActual();
        }
        return total;
    }

    @Override
    public boolean equals(Object objeto) {

        if (this == objeto) {
            return true;
        }

        if (!(objeto instanceof Pareja)) {
            return false;
        }

        Pareja otra = (Pareja) objeto;

        return id == otra.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pareja " + id
                + " [" + combatiente1.getNombre()
                + " - " + combatiente2.getNombre() + "]";
    }
}