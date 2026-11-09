package tecnicas;

import combatientes.Combatiente;

public abstract class Tecnica {
    private final String nombre;
    private final int costoEnergia;

    public Tecnica(String nombre, int costoEnergia) {
        this.nombre = nombre;
        this.costoEnergia = costoEnergia;
    }

    public String getNombre() { return nombre; }
    public int getCostoEnergia() { return costoEnergia; }

    public abstract void aplicar(Combatiente ejecutor, Combatiente objetivo);
}