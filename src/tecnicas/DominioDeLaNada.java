package tecnicas;

import combatientes.Combatiente;

public class DominioDeLaNada extends Tecnica {
    public static final int COSTO_ENERGIA = 15;
    public static final int DANIO_BASE = 20;
    public static final int DRENAJE_ENERGIA = 10;

    public DominioDeLaNada() {
        super("Dominio de la Nada", COSTO_ENERGIA, TipoTecnica.OFENSIVA);
    }

    @Override
    protected void aplicarEfecto(Combatiente ejecutor, Combatiente objetivo) {
        if (objetivo == null) {
            throw new NullPointerException("El objetivo no puede ser nulo.");
        }
        int danioFinal = ejecutor.calcularDanioProducido(DANIO_BASE);
        objetivo.recibirDanio(danioFinal);
        objetivo.perderEnergiaPorEfecto(DRENAJE_ENERGIA);
    }
}