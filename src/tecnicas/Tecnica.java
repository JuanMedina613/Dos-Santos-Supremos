package tecnicas;

import combatientes.Combatiente;
import java.util.Objects;

public abstract class Tecnica {
    private final String nombre;
    private final int costoEnergia;
    private final TipoTecnica tipo;

    public Tecnica(String nombre, int costoEnergia, TipoTecnica tipo) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la técnica no puede estar vacío.");
        }
        if (costoEnergia < 0) {
            throw new IllegalArgumentException("El costo de energía no puede ser negativo.");
        }
        this.nombre = nombre;
        this.costoEnergia = costoEnergia;
        this.tipo = Objects.requireNonNull(tipo, "El tipo de técnica no puede ser nulo.");
    }

    public String getNombre() {
        return nombre;
    }

    public int getCostoEnergia() {
        return costoEnergia;
    }

    public TipoTecnica getTipo() {
        return tipo;
    }

    public boolean esOfensiva() {
        return tipo == TipoTecnica.OFENSIVA;
    }

    public boolean esDefensiva() {
        return tipo == TipoTecnica.DEFENSIVA;
    }

    public boolean esDeRecuperacion() {
        return tipo == TipoTecnica.RECUPERACION;
    }

    public final void aplicar(Combatiente ejecutor, Combatiente objetivo) {
        Objects.requireNonNull(ejecutor, "El ejecutor no puede ser nulo.");
        if (!ejecutor.estaEnPie()) {
            throw new IllegalStateException("Un combatiente fuera de combate no puede ejecutar técnicas.");
        }
        if (ejecutor.getUsosRestantes(this) <= 0) {
            throw new IllegalStateException("No quedan usos disponibles para la técnica: " + nombre);
        }
        if (!ejecutor.tieneEnergiaSuficiente(costoEnergia)) {
            throw new IllegalStateException("Energía insuficiente para ejecutar la técnica: " + nombre);
        }

        ejecutor.gastarEnergia(costoEnergia);
        ejecutor.consumirUsoTecnica(this);
        aplicarEfecto(ejecutor, objetivo);
    }

    protected abstract void aplicarEfecto(Combatiente ejecutor, Combatiente objetivo);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tecnica tecnica = (Tecnica) o;
        return Objects.equals(nombre, tecnica.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), nombre);
    }

    @Override
    public String toString() {
        return nombre;
    }
}