package equipamiento;

public class ArmaduraDorada extends Equipamiento {
    private static final int REDUCCION_DANIO_DEFECTO = 8;
    private final int reduccionDanio;

    public ArmaduraDorada() {
        this(REDUCCION_DANIO_DEFECTO);
    }

    public ArmaduraDorada(int reduccionDanio) {
        super("Armadura Dorada", CategoriaEquipamiento.ARMADURA);
        if (reduccionDanio <= 0) {
            throw new IllegalArgumentException("La reducción de daño debe ser positiva.");
        }
        this.reduccionDanio = reduccionDanio;
    }

    @Override
    public int getReduccionDanio() {
        return reduccionDanio;
    }
}