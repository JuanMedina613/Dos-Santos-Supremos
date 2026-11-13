package equipamiento;

public class AnilloEspiritual extends Equipamiento {
    private final int bonoEnergiaMaxima;

    public AnilloEspiritual(int bonoEnergiaMaxima) {
        super("Anillo Espiritual", CategoriaEquipamiento.ANILLO);
        if (bonoEnergiaMaxima <= 0) {
            throw new IllegalArgumentException("El bono de energía máxima debe ser positivo.");
        }
        this.bonoEnergiaMaxima = bonoEnergiaMaxima;
    }

    @Override
    public int getBonoEnergiaMaxima() {
        return bonoEnergiaMaxima;
    }
}