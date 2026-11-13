package equipamiento;

public class SelloDelFenix extends Equipamiento {
    private final int bonoVidaMaxima;

    public SelloDelFenix(int bonoVidaMaxima) {
        super("Sello del Fénix", CategoriaEquipamiento.SELLO);
        if (bonoVidaMaxima <= 0) {
            throw new IllegalArgumentException("El bono de vida máxima debe ser positivo.");
        }
        this.bonoVidaMaxima = bonoVidaMaxima;
    }

    @Override
    public int getBonoVidaMaxima() {
        return bonoVidaMaxima;
    }
}