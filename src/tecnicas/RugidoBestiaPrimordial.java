package tecnicas;

import combatientes.Combatiente;

public class RugidoBestiaPrimordial extends Tecnica {
    public static final int COSTO_ENERGIA = 20;
    public static final int DANIO_BASE = 40;

    public RugidoBestiaPrimordial() {
        super("Rugido de la Bestia Primordial", COSTO_ENERGIA, TipoTecnica.OFENSIVA);
    }

    @Override
    protected void aplicarEfecto(Combatiente ejecutor, Combatiente objetivo) {
        if (objetivo == null) {
            throw new NullPointerException("El objetivo no puede ser nulo.");
        }
        int danioFinal = ejecutor.calcularDanioProducido(DANIO_BASE);
        objetivo.recibirDanio(danioFinal);
    }
}