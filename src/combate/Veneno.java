package combate;

import combatientes.Combatiente;

public class Veneno extends EfectoTemporal {
    private final int danioPorTurno;

    public Veneno(int danioPorTurno, int duracionTurnos) {
        super("Veneno", duracionTurnos);
        if (danioPorTurno <= 0) {
            throw new IllegalArgumentException("El daño por turno del veneno debe ser positivo.");
        }
        this.danioPorTurno = danioPorTurno;
    }

    public int getDanioPorTurno() {
        return danioPorTurno;
    }

    @Override
    protected void ejecutarEfecto(Combatiente afectado) {
        afectado.recibirDanio(danioPorTurno);
    }
}