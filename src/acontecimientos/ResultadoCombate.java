package acontecimientos;

import torneo.Pareja;

public class ResultadoCombate extends Acontecimiento {
    private final int ronda;
    private final Pareja pareja1;
    private final int vidaTotalPareja1;
    private final Pareja pareja2;
    private final int vidaTotalPareja2;
    private final Pareja ganadora;

    public ResultadoCombate(
            int ronda,
            Pareja pareja1,
            int vidaTotalPareja1,
            Pareja pareja2,
            int vidaTotalPareja2,
            Pareja ganadora) {
        super(formatearDescripcion(ronda, pareja1, vidaTotalPareja1, pareja2, vidaTotalPareja2, ganadora));
        this.ronda = ronda;
        this.pareja1 = pareja1;
        this.vidaTotalPareja1 = vidaTotalPareja1;
        this.pareja2 = pareja2;
        this.vidaTotalPareja2 = vidaTotalPareja2;
        this.ganadora = ganadora;
    }

    private static String formatearDescripcion(
            int ronda,
            Pareja pareja1,
            int vidaTotalPareja1,
            Pareja pareja2,
            int vidaTotalPareja2,
            Pareja ganadora) {
        if (pareja1 == null || pareja2 == null) {
            throw new IllegalArgumentException("Las parejas del resultado no pueden ser nulas.");
        }
        if (ganadora == null) {
            throw new IllegalArgumentException("La pareja ganadora no puede ser nula.");
        }
        return "Ronda " + ronda + " - Victoria de " + ganadora
                + " | Vida total final -> " + pareja1 + ": " + vidaTotalPareja1
                + ", " + pareja2 + ": " + vidaTotalPareja2 + ".";
    }

    public int getRonda() {
        return ronda;
    }

    public Pareja getPareja1() {
        return pareja1;
    }

    public int getVidaTotalPareja1() {
        return vidaTotalPareja1;
    }

    public Pareja getPareja2() {
        return pareja2;
    }

    public int getVidaTotalPareja2() {
        return vidaTotalPareja2;
    }

    public Pareja getGanadora() {
        return ganadora;
    }
}