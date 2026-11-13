package equipamiento;

public class ArmaduraDorada extends Equipamiento {
    private final int reduccionDanio;

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