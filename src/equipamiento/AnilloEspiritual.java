package equipamiento;

public class AnilloEspiritual extends Equipamiento {
    private static final int BONO_ENERGIA_DEFECTO = 20;
    private final int bonoEnergiaMaxima;

    public AnilloEspiritual() {
        this(BONO_ENERGIA_DEFECTO);
    }

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