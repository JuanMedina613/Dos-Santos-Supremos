package tecnicas;

import combatientes.Combatiente;

public class MuroDelCuerpoDorado extends Tecnica {
    public static final int COSTO_ENERGIA = 10;
    public static final int REDUCCION_DANIO = 8;

    public MuroDelCuerpoDorado() {
        super("Muro del Cuerpo Dorado", COSTO_ENERGIA, TipoTecnica.DEFENSIVA);
    }

    @Override
    protected void aplicarEfecto(Combatiente ejecutor, Combatiente objetivo) {
        ejecutor.activarDefensaDeCombate(REDUCCION_DANIO);
    }
}