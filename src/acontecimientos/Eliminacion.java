package acontecimientos;

import combatientes.Combatiente;

public class Eliminacion extends Acontecimiento {
	private final int asalto;
    private final Combatiente eliminado;
    private final Combatiente causante;

    public Eliminacion(int asalto, Combatiente eliminado, Combatiente causante) {
        super(formatearDescripcion(asalto, eliminado, causante));
        this.asalto = asalto;
        this.eliminado = eliminado;
        this.causante = causante;
    }

    /** Eliminación provocada por un efecto temporal (por ejemplo, Veneno): no hay causante directo. */
    public Eliminacion(int asalto, Combatiente eliminado, String nombreEfecto) {
        super(formatearDescripcionPorEfecto(asalto, eliminado, nombreEfecto));
        this.asalto = asalto;
        this.eliminado = eliminado;
        this.causante = null;
    }

    private static String formatearDescripcionPorEfecto(int asalto, Combatiente eliminado, String nombreEfecto) {
        if (eliminado == null) {
            throw new IllegalArgumentException("El combatiente eliminado no puede ser nulo.");
        }
        String causa = (nombreEfecto == null || nombreEfecto.isBlank()) ? "un efecto temporal" : nombreEfecto;
        return "Asalto " + asalto + ": " + eliminado.getNombre()
                + " queda fuera de combate por " + causa + ".";
    }

    private static String formatearDescripcion(int asalto, Combatiente eliminado, Combatiente causante) {
        if (eliminado == null) {
            throw new IllegalArgumentException("El combatiente eliminado no puede ser nulo.");
        }
        if (causante == null) {
            throw new IllegalArgumentException("El combatiente causante no puede ser nulo.");
        }
        return "Asalto " + asalto + ": "
                + eliminado.getNombre()
                + " queda fuera de combate tras el ataque de "
                + causante.getNombre() + ".";
    }

    public int getAsalto() {
        return asalto;
    }

    public Combatiente getEliminado() {
        return eliminado;
    }

    /** Devuelve null si la eliminación la provocó un efecto temporal. */
    public Combatiente getCausante() {
        return causante;
    }
}
