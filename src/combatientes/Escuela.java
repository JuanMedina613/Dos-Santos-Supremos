package combatientes;

import java.util.List;

import equipamiento.Equipamiento;
import tecnicas.Tecnica;

public class Escuela {
    private final String nombre;
    private final List<Tecnica> tecnicasIniciales;
    private final List<Equipamiento> equipamientoInicial;

    public Escuela(String nombre, List<Tecnica> tecnicas, List<Equipamiento> equipamiento) {
        this.nombre = nombre;
        this.tecnicasIniciales = tecnicas;
        this.equipamientoInicial = equipamiento;
    }

    public List<Tecnica> getTecnicasIniciales() { return tecnicasIniciales; }
    public List<Equipamiento> getEquipamientoInicial() { return equipamientoInicial; }
    
   
}