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

    public Combatiente getCausante() {
        return causante;
    }
}
