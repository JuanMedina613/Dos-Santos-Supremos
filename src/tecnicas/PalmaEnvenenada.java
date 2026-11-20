package tecnicas;

import combate.Veneno;
import combatientes.Combatiente;

public class PalmaEnvenenada extends Tecnica {
    public static final int COSTO_ENERGIA = 14;
    public static final int DANIO_INICIAL = 10;
    public static final int DANIO_VENENO = 8;
    public static final int DURACION_TURNOS = 3;

    public PalmaEnvenenada() {
        super("Palma Envenenada", COSTO_ENERGIA, TipoTecnica.OFENSIVA);
    }

    @Override
    protected void aplicarEfecto(Combatiente ejecutor, Combatiente objetivo) {
        if (objetivo == null) {
            throw new IllegalArgumentException("El objetivo no puede ser nulo.");
        }
        int danioFinal = ejecutor.calcularDanioProducido(DANIO_INICIAL);
        objetivo.recibirDanio(danioFinal);
        if (objetivo.estaEnPie()) {
            objetivo.aplicarEfectoTemporal(new Veneno(DANIO_VENENO, DURACION_TURNOS));
        }
    }
}
