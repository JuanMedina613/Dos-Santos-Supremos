package acontecimientos;

import combatientes.Combatiente;

public class EfectoTemporalAplicado extends Acontecimiento {
    private final int asalto;
    private final Combatiente afectado;
    private final String nombreEfecto;
    private final int turnosRestantes;

    public EfectoTemporalAplicado(
            int asalto,
            Combatiente afectado,
            String nombreEfecto,
            int turnosRestantes) {
        super(formatearDescripcion(asalto, afectado, nombreEfecto, turnosRestantes));
        this.asalto = asalto;
        this.afectado = afectado;
        this.nombreEfecto = nombreEfecto;
        this.turnosRestantes = turnosRestantes;
    }

    private static String formatearDescripcion(
            int asalto,
            Combatiente afectado,
            String nombreEfecto,
            int turnosRestantes) {
        if (afectado == null) {
            throw new IllegalArgumentException("El combatiente afectado no puede ser nulo");
        }
        if (nombreEfecto == null || nombreEfecto.isBlank()) {
            throw new IllegalArgumentException("El nombre del efecto no puede estar vacio");
        }
        return "Asalto " + asalto + ": " + afectado.getNombre()
                + " sufre el efecto de " + nombreEfecto
                + " (Vida actual: " + afectado.getVidaActual() + "/" + afectado.getVidaMaxima()
                + ", turnos restantes: " + turnosRestantes + ")";
    }

    public int getAsalto() {
        return asalto;
    }

    public Combatiente getAfectado() {
        return afectado;
    }

    public String getNombreEfecto() {
        return nombreEfecto;
    }

    public int getTurnosRestantes() {
        return turnosRestantes;
    }
}