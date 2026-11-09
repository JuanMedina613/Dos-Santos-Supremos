package combatientes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import equipamiento.Equipamiento;
import tecnicas.Tecnica;

public abstract class Combatiente {
    private final int id;
    private final String nombre;
    protected int vidaBase = 100;
    protected int energiaBase = 40;
    protected int vidaActual = 100;
    protected int energiaActual = 40;
    
    private List<Equipamiento> equipamientos = new ArrayList<>();
    private List<Tecnica> tecnicas = new ArrayList<>();
    private Map<String, Integer> usosTecnicas = new HashMap<>();

    public Combatiente(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public void equipar(Equipamiento nuevo) {
        if (equipamientos.size() >= 3) {
            throw new IllegalStateException("Límite de 3 equipamientos alcanzado.");
        }
        for (Equipamiento e : equipamientos) {
            if (e.getCategoria() == nuevo.getCategoria()) {
                throw new IllegalStateException("Categoría duplicada: " + nuevo.getCategoria());
            }
        }
        equipamientos.add(nuevo);
    }

    public void desequipar(Equipamiento e) {
        equipamientos.remove(e);
        if (vidaActual > getVidaMaxima()) vidaActual = getVidaMaxima();
        if (energiaActual > getEnergiaMaxima()) energiaActual = getEnergiaMaxima();
    }

    public int getVidaMaxima() {
        return vidaBase + equipamientos.stream().mapToInt(Equipamiento::getBonusVidaMaxima).sum();
    }

    public int getEnergiaMaxima() {
        return energiaBase + equipamientos.stream().mapToInt(Equipamiento::getBonusEnergiaMaxima).sum();
    }
    
    public void restarVida(int cantidad) {
        this.vidaActual -= cantidad;
        if (this.vidaActual < 0) {
            this.vidaActual = 0; // Evita que la vida quede en números negativos
        }
    }
    
    public void aprenderTecnica(Tecnica tecnica) {
        if (tecnica != null && !this.tecnicas.contains(tecnica)) {
            this.tecnicas.add(tecnica);
            this.usosTecnicas.put(tecnica.getNombre(), 5); // 5 usos por torneo según la consigna
        }
    }
    
    public abstract int calcularDanio(int danioBase);
    public abstract int recibirDanio(int danioEntrante);
    public abstract int calcularCostoEnergia(int costoBase);
    
    
    // Getters y métodos de estado omitidos por brevedad
}
   