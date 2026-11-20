package tecnicas;

import combatientes.Combatiente;

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
        if (tipo == null) {
            throw new NullPointerException("El tipo de técnica no puede ser nulo.");
        }
        this.tipo = tipo;
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
        if (ejecutor == null) {
            throw new NullPointerException("El ejecutor no puede ser nulo.");
        }
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
        return nombre == null
                ? tecnica.nombre == null
                : nombre.equals(tecnica.nombre);
    }

    @Override
    public int hashCode() {
        int hash = 31 + getClass().hashCode();
        hash = 31 * hash + (nombre == null ? 0 : nombre.hashCode());
        return hash;
    }

    @Override
    public String toString() {
        return nombre;
    }
}