package combatientes;

import equipamiento.Equipamiento;
import tecnicas.Tecnica;

import java.util.Collections;
import java.util.List;

public class Escuela {
    private final String nombre;
    private final List<Tecnica> tecnicasIniciales;
    private final List<Equipamiento> equipamientosIniciales;

    public Escuela(String nombre, List<Tecnica> tecnicasIniciales, List<Equipamiento> equipamientosIniciales) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la escuela no puede estar vacio");
        }
        if (tecnicasIniciales == null) {
            throw new NullPointerException("La lista de tecnicas no puede ser nula");
        }
        if (equipamientosIniciales == null) {
            throw new NullPointerException("La lista de equipamientos no puede ser nula");
        }

        this.nombre = nombre;
        this.tecnicasIniciales = List.copyOf(tecnicasIniciales);
        this.equipamientosIniciales = List.copyOf(equipamientosIniciales);
    }

    public String getNombre() {
        return nombre;
    }

    public List<Tecnica> getTecnicasIniciales() {
        return Collections.unmodifiableList(tecnicasIniciales);
    }

    public List<Equipamiento> getEquipamientosIniciales() {
        return Collections.unmodifiableList(equipamientosIniciales);
    }
}