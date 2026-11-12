package equipamiento;

import java.util.Objects;

public abstract class Equipamiento {
    private final String nombre;
    private final CategoriaEquipamiento categoria;

    public Equipamiento(String nombre, CategoriaEquipamiento categoria) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del equipamiento no puede estar vacío.");
        }
        this.nombre = nombre;
        this.categoria = Objects.requireNonNull(categoria, "La categoría no puede ser nula.");
    }

    public String getNombre() {
        return nombre;
    }

    public CategoriaEquipamiento getCategoria() {
        return categoria;
    }

    // Hooks polimórficos: cada subclase sobrescribe únicamente el bono que modifica
    public int getBonoDanio() {
        return 0;
    }

    public int getReduccionDanio() {
        return 0;
    }

    public int getBonoEnergiaMaxima() {
        return 0;
    }

    public int getBonoVidaMaxima() {
        return 0;
    }
}