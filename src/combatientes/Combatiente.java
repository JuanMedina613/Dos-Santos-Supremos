package combatientes;

import equipamiento.CategoriaEquipamiento;
import equipamiento.Equipamiento;
import excepciones.ReglaEquipamientoException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Combatiente {
    public static final int VIDA_BASE_POR_DEFECTO = 100;
    public static final int ENERGIA_BASE_POR_DEFECTO = 40;
    public static final int DANIO_ATAQUE_BASICO = 15;
    public static final int MAX_EQUIPAMIENTOS = 3;

    private final int id;
    private final String nombre;
    private final int vidaBaseMaxima;
    private final int energiaBaseMaxima;

    private int vidaActual;
    private int energiaActual;

    // Reducción de daño temporal durante el combate (ej. Muro del Cuerpo Dorado)
    private int reduccionDanioCombate;

    private final List<Equipamiento> equipamientos;

    public Combatiente(int id, String nombre) {
        this(id, nombre, VIDA_BASE_POR_DEFECTO, ENERGIA_BASE_POR_DEFECTO);
    }

    public Combatiente(int id, String nombre, int vidaBaseMaxima, int energiaBaseMaxima) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del combatiente no puede estar vacío.");
        }
        if (vidaBaseMaxima <= 0 || energiaBaseMaxima < 0) {
            throw new IllegalArgumentException("Valores base de vida o energía inválidos.");
        }
        this.id = id;
        this.nombre = nombre;
        this.vidaBaseMaxima = vidaBaseMaxima;
        this.energiaBaseMaxima = energiaBaseMaxima;
        this.vidaActual = vidaBaseMaxima;
        this.energiaActual = energiaBaseMaxima;
        this.reduccionDanioCombate = 0;
        this.equipamientos = new ArrayList<>();
    }

    // --- GESTIÓN DE EQUIPAMIENTO ---

    public void equipar(Equipamiento nuevoEquipamiento) {
        Objects.requireNonNull(nuevoEquipamiento, "El equipamiento no puede ser nulo.");

        if (equipamientos.size() >= MAX_EQUIPAMIENTOS) {
            throw new ReglaEquipamientoException(
                "No se pueden llevar más de " + MAX_EQUIPAMIENTOS + " equipamientos."
            );
        }

        boolean categoriaRepetida = equipamientos.stream()
                .anyMatch(e -> e.getCategoria() == nuevoEquipamiento.getCategoria());

        if (categoriaRepetida) {
            throw new ReglaEquipamientoException(
                "El combatiente ya posee un equipamiento de la categoría: " + nuevoEquipamiento.getCategoria()
            );
        }

        // Equipar un objeto que aumenta un máximo NO aumenta el valor actual
        equipamientos.add(nuevoEquipamiento);
    }

    public void quitarEquipamiento(Equipamiento equipamiento) {
        Objects.requireNonNull(equipamiento, "El equipamiento a quitar no puede ser nulo.");

        boolean eliminado = equipamientos.remove(equipamiento);
        if (!eliminado) {
            throw new ReglaEquipamientoException("El combatiente no lleva puesto ese equipamiento.");
        }

        // Al quitarlo, el valor actual se recorta al nuevo máximo si lo supera
        recortarEstadisticasAlMaximo();
    }

    public void quitarEquipamientoPorCategoria(CategoriaEquipamiento categoria) {
        Equipamiento encontrado = equipamientos.stream()
                .filter(e -> e.getCategoria() == categoria)
                .findFirst()
                .orElseThrow(() -> new ReglaEquipamientoException(
                    "No lleva equipamiento de la categoría: " + categoria
                ));
        quitarEquipamiento(encontrado);
    }

    private void recortarEstadisticasAlMaximo() {
        this.vidaActual = Math.min(this.vidaActual, getVidaMaxima());
        this.energiaActual = Math.min(this.energiaActual, getEnergiaMaxima());
    }

    public List<Equipamiento> getEquipamientos() {
        return Collections.unmodifiableList(equipamientos);
    }

    // --- CÁLCULO DINÁMICO DE ESTADÍSTICAS MÁXIMAS Y BONOS ---

    public int getVidaMaxima() {
        int bonoEquipo = equipamientos.stream().mapToInt(Equipamiento::getBonoVidaMaxima).sum();
        return vidaBaseMaxima + bonoEquipo;
    }

    public int getEnergiaMaxima() {
        int bonoEquipo = equipamientos.stream().mapToInt(Equipamiento::getBonoEnergiaMaxima).sum();
        return energiaBaseMaxima + bonoEquipo;
    }

    public int getBonoDanioEquipamiento() {
        return equipamientos.stream().mapToInt(Equipamiento::getBonoDanio).sum();
    }

    public int getReduccionDanioEquipamiento() {
        return equipamientos.stream().mapToInt(Equipamiento::getReduccionDanio).sum();
    }

    // --- GESTIÓN DE VIDA Y ENERGÍA (RECORTANDO A LOS MÁXIMOS) ---

    public void recibirDanio(int danioBruto) {
        if (danioBruto <= 0 || !estaEnPie()) {
            return;
        }
        // 1. Reducción polimórfica según el tipo de combatiente (ej. Monje del Cuerpo Celestial)
        int danioTrasEspecialidad = aplicarReduccionPorEspecialidad(danioBruto);

        // 2. Reducción acumulada por equipamiento (Armadura Dorada) + técnicas defensivas activas
        int reduccionTotal = getReduccionDanioEquipamiento() + reduccionDanioCombate;
        int danioFinal = Math.max(0, danioTrasEspecialidad - reduccionTotal);

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
        int costoReal = calcularCostoEnergia(costoBaseTecnica);
        if (this.energiaActual < costoReal) {
            throw new IllegalStateException("Energía insuficiente para ejecutar la técnica.");
        }
        this.energiaActual -= costoReal;
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

    // --- CÁLCULO DE DAÑO PRODUCIDO Y ESTADO DE COMBATE ---

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

    // --- GETTERS E IDENTIDAD ---

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