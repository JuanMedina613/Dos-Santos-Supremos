package combatientes;

import equipamiento.Equipamiento;
import tecnicas.Tecnica;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Escuela {
    private final String nombre;
    private final List<Tecnica> tecnicasIniciales;
    private final List<Equipamiento> equipamientosIniciales;

    public Escuela(String nombre, List<Tecnica> tecnicasIniciales, List<Equipamiento> equipamientosIniciales) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la escuela no puede estar vacío.");
        }
        Objects.requireNonNull(tecnicasIniciales, "La lista de técnicas no puede ser nula.");
        Objects.requireNonNull(equipamientosIniciales, "La lista de equipamientos no puede ser nula.");

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