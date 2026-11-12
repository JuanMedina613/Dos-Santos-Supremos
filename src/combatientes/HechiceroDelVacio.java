package combatientes;

public class HechiceroDelVacio extends Combatiente {
    private static final double FACTOR_COSTO_ENERGIA = 0.75; // Gasta 25% menos de energía

    public HechiceroDelVacio(int id, String nombre) {
        super(id, nombre);
    }

    public HechiceroDelVacio(int id, String nombre, int vidaBase, int energiaBase) {
        super(id, nombre, vidaBase, energiaBase);
    }

    @Override
    public int calcularCostoEnergia(int costoBase) {
        return Math.max(0, (int) Math.round(costoBase * FACTOR_COSTO_ENERGIA));
    }
}