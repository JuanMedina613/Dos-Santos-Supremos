package combate;

import combatientes.Combatiente;

public abstract class EfectoTemporal {
    private final String nombre;
    private int turnosRestantes;

    protected EfectoTemporal(String nombre, int duracionTurnos) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del efecto temporal no puede estar vacío.");
        }
        if (duracionTurnos <= 0) {
            throw new IllegalArgumentException("La duración en turnos debe ser positiva.");
        }
        this.nombre = nombre;
        this.turnosRestantes = duracionTurnos;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTurnosRestantes() {
        return turnosRestantes;
    }

    public boolean estaFinalizado() {
        return turnosRestantes <= 0;
    }

    public final void aplicarEnTurno(Combatiente afectado) {
        if (afectado == null) {
            throw new IllegalArgumentException("El combatiente afectado no puede ser nulo.");
        }
        if (estaFinalizado() || !afectado.estaEnPie()) {
            return;
        }
        ejecutarEfecto(afectado);
        turnosRestantes--;
        if (estaFinalizado()) {
            alFinalizar(afectado);
        }
    }

    protected abstract void ejecutarEfecto(Combatiente afectado);

    protected void alFinalizar(Combatiente afectado) {
        // Hook opcional para efectos que deban revertir modificaciones al expirar.
    }
}