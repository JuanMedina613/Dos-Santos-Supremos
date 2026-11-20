package combate;

import combatientes.Combatiente;

public class Regeneracion extends EfectoTemporal {
    private final int curacionPorTurno;

    public Regeneracion(int curacionPorTurno, int duracionTurnos) {
        super("Regeneracion", duracionTurnos);
        if (curacionPorTurno <= 0) {
            throw new IllegalArgumentException("La curacion por turno debe ser positiva");
        }
        this.curacionPorTurno = curacionPorTurno;
    }

    public int getCuracionPorTurno() {
        return curacionPorTurno;
    }

    @Override
    protected void ejecutarEfecto(Combatiente afectado) {
        afectado.recuperarVida(curacionPorTurno);
    }
}