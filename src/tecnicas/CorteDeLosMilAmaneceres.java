package tecnicas;

import combatientes.Combatiente;
import java.util.Objects;

public class CorteDeLosMilAmaneceres extends Tecnica {
    public static final int COSTO_ENERGIA = 12;
    public static final int DANIO_BASE = 25;

    public CorteDeLosMilAmaneceres() {
        super("Corte de los Mil Amaneceres", COSTO_ENERGIA, TipoTecnica.OFENSIVA);
    }

    @Override
    protected void aplicarEfecto(Combatiente ejecutor, Combatiente objetivo) {
        Objects.requireNonNull(objetivo, "El objetivo no puede ser nulo.");
        int danioFinal = ejecutor.calcularDanioProducido(DANIO_BASE);
        objetivo.recibirDanio(danioFinal);
    }
}