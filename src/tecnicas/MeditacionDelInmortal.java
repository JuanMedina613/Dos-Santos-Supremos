package tecnicas;

import combatientes.Combatiente;

public class MeditacionDelInmortal extends Tecnica {
    public static final int COSTO_ENERGIA = 12;
    public static final int CURACION_BASE = 25;

    public MeditacionDelInmortal() {
        super("Meditación del Inmortal", COSTO_ENERGIA, TipoTecnica.RECUPERACION);
    }

    @Override
    protected void aplicarEfecto(Combatiente ejecutor, Combatiente objetivo) {
        ejecutor.recuperarVida(CURACION_BASE);
    }
}