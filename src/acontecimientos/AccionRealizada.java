package acontecimientos;

import combatientes.Combatiente;

public class AccionRealizada extends Acontecimiento {
    private final int asalto;
    private final Combatiente actor;
    private final Combatiente objetivo;
    private final String nombreAccion;
    private final boolean esTecnica;

    public AccionRealizada(
            int asalto,
            Combatiente actor,
            Combatiente objetivo,
            String nombreAccion,
            boolean esTecnica) {
        super(formatearDescripcion(asalto, actor, objetivo, nombreAccion, esTecnica));
        this.asalto = asalto;
        this.actor = actor;
        this.objetivo = objetivo;
        this.nombreAccion = nombreAccion;
        this.esTecnica = esTecnica;
    }

    private static String formatearDescripcion(
            int asalto,
            Combatiente actor,
            Combatiente objetivo,
            String nombreAccion,
            boolean esTecnica) {
        if (actor == null) {
            throw new IllegalArgumentException("El combatiente actor no puede ser nulo.");
        }
        if (objetivo == null) {
            throw new IllegalArgumentException("El combatiente objetivo no puede ser nulo.");
        }
        if (nombreAccion == null || nombreAccion.isBlank()) {
            throw new IllegalArgumentException("El nombre de la acción no puede estar vacío.");
        }

        String tipo = esTecnica ? "la técnica '" + nombreAccion + "'" : "un ataque básico";
        if (actor.equals(objetivo)) {
            return "Asalto " + asalto + ": " + actor.getNombre()
                    + " ejecuta " + tipo + " sobre sí mismo (Vida: "
                    + actor.getVidaActual() + "/" + actor.getVidaMaxima() + ").";
        }
        return "Asalto " + asalto + ": " + actor.getNombre()
                + " ejecuta " + tipo + " sobre " + objetivo.getNombre()
                + " (Vida restante de " + objetivo.getNombre() + ": " + objetivo.getVidaActual() + ").";
    }

    public int getAsalto() {
        return asalto;
    }

    public Combatiente getActor() {
        return actor;
    }

    public Combatiente getObjetivo() {
        return objetivo;
    }

    public String getNombreAccion() {
        return nombreAccion;
    }

    public boolean isEsTecnica() {
        return esTecnica;
    }
}