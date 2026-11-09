package combatientes;

public class CultivadorDeLaEspada extends Combatiente {
    public CultivadorDeLaEspada(int id, String nombre) { super(id, nombre); }

    @Override
    public int calcularDanio(int danioBase) {
        return (int) (danioBase * 1.2) + equipamientos.stream().mapToInt(Equipamiento::getBonusDanio).sum();
    }

    @Override
    public int recibirDanio(int danioEntrante) {
        int reduccion = equipamientos.stream().mapToInt(Equipamiento::getReduccionDanio).sum();
        return Math.max(0, danioEntrante - reduccion);
    }

    @Override
    public int calcularCostoEnergia(int costoBase) { return costoBase; }
}