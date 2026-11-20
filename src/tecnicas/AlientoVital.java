package tecnicas;

import combate.Regeneracion;
import combatientes.Combatiente;

public class AlientoVital extends Tecnica {
    public static final int COSTO_ENERGIA = 14;
    public static final int CURACION_POR_TURNO = 12;
    public static final int DURACION_TURNOS = 3;

    public AlientoVital() {
        super("Aliento Vital", COSTO_ENERGIA, TipoTecnica.RECUPERACION);
    }

    @Override
    protected void aplicarEfecto(Combatiente ejecutor, Combatiente objetivo) {
        ejecutor.aplicarEfectoTemporal(new Regeneracion(CURACION_POR_TURNO, DURACION_TURNOS));
    }
}
