package equipamiento;

public class EspadaCelestial extends Equipamiento {
    private final int bonoDanio;

    public EspadaCelestial(int bonoDanio) {
        super("Espada Celestial", CategoriaEquipamiento.ARMA);
        if (bonoDanio <= 0) {
            throw new IllegalArgumentException("El bono de daño debe ser positivo.");
        }
        this.bonoDanio = bonoDanio;
    }

    @Override
    public int getBonoDanio() {
        return bonoDanio;
    }
}