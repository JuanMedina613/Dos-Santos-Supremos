package equipamiento;

public abstract class Equipamiento {
    private final String nombre;
    private final CategoriaEquipamiento categoria;

    public Equipamiento(String nombre, CategoriaEquipamiento categoria) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del equipamiento no puede estar vacío.");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría no puede ser nula.");
        }
        this.nombre = nombre;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public CategoriaEquipamiento getCategoria() {
        return categoria;
    }

    // POLIMORFISMO SOLO PARA LAS CLASES QUE LA IMPLEMENTAN
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