package combatientes;

import equipamiento.CategoriaEquipamiento;
import equipamiento.Equipamiento;
import excepciones.ReglaEquipamientoException;
import tecnicas.Tecnica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class Combatiente {
    public static final int VIDA_BASE_POR_DEFECTO = 100;
    public static final int ENERGIA_BASE_POR_DEFECTO = 40;
    public static final int DANIO_ATAQUE_BASICO = 15;
    public static final int MAX_EQUIPAMIENTOS = 3;
    public static final int USOS_MAXIMOS_TECNICA = 5;

    private final int id;
    private final String nombre;
    private final int vidaBaseMaxima;
    private final int energiaBaseMaxima;

    private int vidaActual;
    private int energiaActual;

    // Reducción de daño temporal durante el combate (ej. Muro del Cuerpo Dorado)
    private int reduccionDanioCombate;

    private final List<Equipamiento> equipamientos;
    private final Map<Tecnica, Integer> usosPorTecnica;

    // Constructor único y limpio (el ID lo provee el CreadorCombatientes)
    public Combatiente(int id, String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del combatiente no puede estar vacio.");
        }
        this.id = id;
        this.nombre = nombre;
        this.vidaBaseMaxima = VIDA_BASE_POR_DEFECTO;
        this.energiaBaseMaxima = ENERGIA_BASE_POR_DEFECTO;
        this.vidaActual = VIDA_BASE_POR_DEFECTO;
        this.energiaActual = ENERGIA_BASE_POR_DEFECTO;
        this.reduccionDanioCombate = 0;
        this.equipamientos = new ArrayList<>();
        this.usosPorTecnica = new LinkedHashMap<>();
    }

    // ACA VAMOS A MANERJAR LAS TECNICAS GESTIÓN DE TECNICAS

    public void aprenderTecnica(Tecnica tecnica) {
        Objects.requireNonNull(tecnica, "La técnica no puede ser nula.");
        usosPorTecnica.putIfAbsent(tecnica, USOS_MAXIMOS_TECNICA);
    }

    public int getUsosRestantes(Tecnica tecnica) {
        return usosPorTecnica.getOrDefault(tecnica, 0);
    }

    public void consumirUsoTecnica(Tecnica tecnica) {
 
        if (getUsosRestantes(tecnica) <= 0) {
            throw new IllegalStateException("No quedan usos disponibles para la técnica: " + tecnica.getNombre());
        }
        usosPorTecnica.put(tecnica, getUsosRestantes(tecnica) - 1);
    }

    public List<Tecnica> getTecnicasDisponibles() {
        return new ArrayList<>(usosPorTecnica.keySet());
    }

    // ACA VAMOS A HACER LA GESTION DEL EQUIPAMIENTO

    public void equipar(Equipamiento nuevoEquipamiento) {
    	if (nuevoEquipamiento == null) {
            throw new IllegalArgumentException("El equipamiento no puede ser nulo.");
        }

        if (equipamientos.size() >= MAX_EQUIPAMIENTOS) {
            throw new ReglaEquipamientoException("No se pueden llevar mas de " + MAX_EQUIPAMIENTOS + " equipamientos.");
        }

        for (Equipamiento equipoPuesto : equipamientos) {
            if (equipoPuesto.getCategoria() == nuevoEquipamiento.getCategoria()) {
                throw new ReglaEquipamientoException("El combatiente ya posee un equipamiento de la categoria: " + nuevoEquipamiento.getCategoria());
            }
        }
        equipamientos.add(nuevoEquipamiento);
    }

    public void quitarEquipamiento(Equipamiento equipamiento) {
    	if (equipamiento == null) {
            throw new IllegalArgumentException("El equipamiento a quitar no puede ser nulo.");
        }

    	if (!equipamientos.contains(equipamiento)) {
            throw new ReglaEquipamientoException("El combatiente no lleva puesto ese equipamiento.");
        }
    	
    	equipamientos.remove(equipamiento);
    	
        recortarEstadisticasAlMaximo(); /// ESTO LO DEJO PERO HABRIA QUE VERLO A DETALLE - KEVIN!! POSIBLEMENTA TAMBIEN ARI Y TAIEL
    }

    public void quitarEquipamientoPorCategoria(CategoriaEquipamiento categoria) {
        Equipamiento encontrado = null;

        for (Equipamiento equipoPuesto : equipamientos) {
            if (equipoPuesto.getCategoria() == categoria) {
                encontrado = equipoPuesto;
            }
        }
        if (encontrado == null) {
            throw new ReglaEquipamientoException("No lleva equipamiento de la categoria: " + categoria);
        }
        quitarEquipamiento(encontrado);
    }
    
    private void recortarEstadisticasAlMaximo() {
        this.vidaActual = Math.min(this.vidaActual, getVidaMaxima());
        this.energiaActual = Math.min(this.energiaActual, getEnergiaMaxima());
    }

    public List<Equipamiento> getEquipamientos() {
        return new ArrayList<>(equipamientos);
    }

    // ACA VAMOS A HACER LOS CALCULOS 

    public int getVidaMaxima() {
        int bonoEquipo = 0;
        for (Equipamiento equipo : equipamientos) {
            bonoEquipo += equipo.getBonoVidaMaxima();
        }
        return vidaBaseMaxima + bonoEquipo;
    }

    public int getEnergiaMaxima() {
        int bonoEquipo = 0;
        for (Equipamiento equipo : equipamientos) {
            bonoEquipo += equipo.getBonoEnergiaMaxima();
        }
        return energiaBaseMaxima + bonoEquipo;
    }

    public int getBonoDanioEquipamiento() {
        int bonoTotal = 0;
        for (Equipamiento equipo : equipamientos) {
            bonoTotal += equipo.getBonoDanio();
        }
        return bonoTotal;
    }

    public int getReduccionDanioEquipamiento() {
        int reduccionTotal = 0;
        for (Equipamiento equipo : equipamientos) {
            reduccionTotal += equipo.getReduccionDanio();
        }
        return reduccionTotal;
    }

    // ACA VAMOS A GESTIONAR LA VIDA Y LA ENERGIA

    public void recibirDanio(int danioBruto) {
        if (danioBruto <= 0 || !estaEnPie()) {
            return;
        }
        
        int danioFinal = Math.max(0, aplicarReduccionPorEspecialidad(danioBruto) - getReduccionDanioEquipamiento() - reduccionDanioCombate);
        this.vidaActual = Math.max(0, this.vidaActual - danioFinal);
    }

    public void recuperarVida(int cantidad) {
        if (cantidad <= 0 || !estaEnPie()) {
            return;
        }
        this.vidaActual = Math.min(getVidaMaxima(), this.vidaActual + cantidad);
    }

    public void recuperarEnergia(int cantidad) {
        if (cantidad <= 0 || !estaEnPie()) {
            return;
        }
        this.energiaActual = Math.min(getEnergiaMaxima(), this.energiaActual + cantidad);
    }

    public void gastarEnergia(int costoBaseTecnica) {
   
        if (this.energiaActual < calcularCostoEnergia(costoBaseTecnica)) {
            throw new IllegalStateException("Energía insuficiente para ejecutar la técnica.");
        }
        this.energiaActual -= calcularCostoEnergia(costoBaseTecnica);
    }

    public void perderEnergiaPorEfecto(int cantidad) {
        if (cantidad <= 0) {
            return;
        }
        this.energiaActual = Math.max(0, this.energiaActual - cantidad);
    }

    public boolean tieneEnergiaSuficiente(int costoBaseTecnica) {
        return this.energiaActual >= calcularCostoEnergia(costoBaseTecnica);
    }

    // ACA VAMOS A CALCULAR EL DAÑO Y ESTADO DEL COMBATE

    public int calcularDanioProducido(int danioBase) {
        int danioConEquipo = danioBase + getBonoDanioEquipamiento();
        return aplicarPotenciacionPorEspecialidad(danioConEquipo);
    }

    public int calcularDanioAtaqueBasico() {
        return calcularDanioProducido(DANIO_ATAQUE_BASICO);
    }

    public void activarDefensaDeCombate(int reduccionExtra) {
        if (reduccionExtra > 0) {
            this.reduccionDanioCombate += reduccionExtra;
        }
    }

    public boolean tieneDefensaActiva() {
        return this.reduccionDanioCombate > 0;
    }

    public void restaurarParaNuevoCombate() {
        this.vidaActual = getVidaMaxima();
        this.energiaActual = getEnergiaMaxima();
        this.reduccionDanioCombate = 0;
    }

    // --- HOOKS POLIMÓRFICOS PARA LAS SUBCLASES ---

    protected int aplicarPotenciacionPorEspecialidad(int danio) {
        return danio;
    }

    protected int aplicarReduccionPorEspecialidad(int danioEntrante) {
        return danioEntrante;
    }

    public int calcularCostoEnergia(int costoBase) {
        return Math.max(0, costoBase);
    }

    // GETTERS Y SETERS

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public int getEnergiaActual() {
        return energiaActual;
    }

    public boolean estaEnPie() {
        return this.vidaActual > 0;
    }

    public boolean tieneVidaCompleta() {
        return this.vidaActual >= getVidaMaxima();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Combatiente)) return false;
        Combatiente that = (Combatiente) o;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}