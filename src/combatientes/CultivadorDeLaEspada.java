package combatientes;

public class CultivadorDeLaEspada extends Combatiente {
    private static final double MULTIPLICADOR_DANIO = 1.25; // +25% al daño producido

    public CultivadorDeLaEspada(int id, String nombre) {
        super(id, nombre);
    }

    @Override
    protected int aplicarPotenciacionPorEspecialidad(int danio) {
        return (int) Math.round(danio * MULTIPLICADOR_DANIO);
    }
}