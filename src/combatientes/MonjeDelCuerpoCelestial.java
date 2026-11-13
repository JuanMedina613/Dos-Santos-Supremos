package combatientes;

public class MonjeDelCuerpoCelestial extends Combatiente {
    private static final double FACTOR_REDUCCION = 0.75; // Recibe 25% menos de daño

    public MonjeDelCuerpoCelestial(int id, String nombre) {
        super(id, nombre);
    }

    @Override
    protected int aplicarReduccionPorEspecialidad(int danioEntrante) {
        return (int) Math.round(danioEntrante * FACTOR_REDUCCION);
    }
}