package acontecimientos;

import torneo.Pareja;

public class Proclamacion extends Acontecimiento {
    private final Pareja campeona;

    public Proclamacion(Pareja campeona) {
        super(formatearDescripcion(campeona));
        this.campeona = campeona;
    }

    private static String formatearDescripcion(Pareja campeona) {
        if (campeona == null) {
            throw new IllegalArgumentException("La pareja campeona no puede ser nula.");
        }
        return "¡Proclamación! " + campeona.getCombatiente1().getNombre()
                + " y " + campeona.getCombatiente2().getNombre()
                + " (" + campeona + ") son los Dos Santos Supremos.";
    }

    public Pareja getCampeona() {
        return campeona;
    }
}
